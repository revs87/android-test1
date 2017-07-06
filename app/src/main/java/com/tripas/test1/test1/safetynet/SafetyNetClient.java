package com.tripas.test1.test1.safetynet;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.tripas.test1.test1.safetynet.exceptions.AttestationFailedException;
import com.tripas.test1.test1.safetynet.exceptions.GAPIClientFailedException;
import com.tripas.test1.test1.safetynet.tasks.ISafetyNetGetAttestationInterface;
import com.tripas.test1.test1.safetynet.tasks.ISafetyNetVerifyAttestationInterface;
import com.tripas.test1.test1.safetynet.tasks.SafetyNetGetAttestationTask;
import com.tripas.test1.test1.safetynet.tasks.SafetyNetVerifyAttestationTask;

/**
 * Created by Rui Vieira on 09/05/2017.
 * The Floow Ltd
 * rui.vieira@thefloow.com
 * <p>
 * https://developer.android.com/training/safetynet/attestation.html
 */
public class SafetyNetClient {

    private final String LOG_TAG = "snc";
    private final String IS_BASIC_INTEGRITY = "ibt";
    private final String IS_CTS_PROFILE_MATCH = "icpm";

    public synchronized GoogleApiClient prepareGoogleApiClient(Context ctx) {
        // https://developers.google.com/android/guides/api-client#Starting
        // .addOnConnectionFailedListener for catching the connection fail result
        return new GoogleApiClient.Builder(ctx)
                .addApi(SafetyNet.API)
                .build();
    }

    public SafetyNetApi.AttestationResult getJws(Context ctx, byte[] nonce)
            throws GAPIClientFailedException, AttestationFailedException {
        GoogleApiClient gApiClient = this.prepareGoogleApiClient(ctx);
        ConnectionResult cr = gApiClient.blockingConnect();

        // TODO tests are needed!
        // That does not necessarily work on every version, most common error is outdated
        // version of google play services

        if (!cr.isSuccess()) {
            Log.d(LOG_TAG, "error0 " + String.valueOf(cr.getErrorCode()));
            throw new GAPIClientFailedException();
        }

        SafetyNetApi.AttestationResult attestationResult =
                SafetyNet.SafetyNetApi
                        .attest(gApiClient, nonce)
                        .await();

        Status status = attestationResult.getStatus();

        if (status.isSuccess()) {
            return attestationResult;
        } else {
            throw new AttestationFailedException();
        }
    }

    public void run(Context context, byte[] nonce, ISafetyNetGetAttestationInterface attestationInterface) {
        SafetyNetGetAttestationTask task = new SafetyNetGetAttestationTask();
        task.setup(this, nonce, attestationInterface);
        task.execute(context);
    }

    public void verify(String apiKey, String attestation, ISafetyNetVerifyAttestationInterface attestationInterface) {
        SafetyNetVerifyAttestationTask task = new SafetyNetVerifyAttestationTask();
        task.setup(apiKey, attestation, attestationInterface);
        task.execute();
    }


    public SafetyNetResponse parseJsonWebSignature(String jwsResult) {
        if (jwsResult == null) {
            return null;
        }
        //the JWT (JSON WEB TOKEN) is just a 3 base64 encoded parts concatenated by a . character
        final String[] jwtParts = jwsResult.split("\\.");

        if (jwtParts.length == 3) {
            //we're only really interested in the body/payload
            String decodedPayload = new String(Base64.decode(jwtParts[1], Base64.DEFAULT));

            return SafetyNetResponse.parse(decodedPayload);
        } else {
            return null;
        }
    }

    public boolean validateAttestation(String attestation) {
        boolean valid = true;
        SafetyNetResponse attestationObj = parseJsonWebSignature(attestation);

        Log.d(LOG_TAG, IS_BASIC_INTEGRITY + ": " + attestationObj.isBasicIntegrity());
        if (!attestationObj.isBasicIntegrity()) {
            valid = false;
        }

        Log.d(LOG_TAG, IS_CTS_PROFILE_MATCH + ": " + attestationObj.isCtsProfileMatch());
        if (!attestationObj.isCtsProfileMatch()) {
            valid = false;
        }

        return valid;
    }

}
