package com.tripas.test1.test1.safetynet.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.safetynet.SafetyNetApi;
import com.tripas.test1.test1.safetynet.SafetyNetClient;
import com.tripas.test1.test1.safetynet.exceptions.AttestationFailedException;
import com.tripas.test1.test1.safetynet.exceptions.GAPIClientFailedException;

/**
 * Created by Rui Vieira on 09/05/2017.
 * The Floow Ltd
 * rui.vieira@thefloow.com
 */
public class SafetyNetGetAttestationTask extends AsyncTask<Context, Object, SafetyNetApi.AttestationResult> {

    private Context context;
    private ISafetyNetGetAttestationInterface callback;
    protected SafetyNetClient safetyNetClient;
    private byte[] nonce;


    public void setup(SafetyNetClient safetyNetClient,
                      byte[] nonce,
                      ISafetyNetGetAttestationInterface attestationInterface) {
        this.safetyNetClient = safetyNetClient;
        this.nonce = nonce;
        this.callback = attestationInterface;
    }

    @Override
    protected SafetyNetApi.AttestationResult doInBackground(Context... params) {

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.context = params[0];

        // Advance JWS status message
        SafetyNetApi.AttestationResult jws;

        try {
            jws = safetyNetClient.getJws(context, nonce);
        } catch (GAPIClientFailedException e) {
            // "Google Play Services not available on the device or is out of date."
            Log.d("snc", "error1");
            return null;
        } catch (AttestationFailedException e) {
            // "failed connection to Google Play Services."
            Log.d("snc", "error2");
            return null;
        }

        return jws;
    }

    @Override
    protected void onPostExecute(SafetyNetApi.AttestationResult attestationResult) {
        if (callback != null
                && attestationResult != null)
            callback.onAttestation(attestationResult.getJwsResult());
    }

}
