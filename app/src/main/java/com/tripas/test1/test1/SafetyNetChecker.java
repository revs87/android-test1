package com.tripas.test1.test1;

import android.app.Activity;
import android.util.Base64;
import android.util.Log;

import com.tripas.test1.test1.safetynet.SafetyNetClient;
import com.tripas.test1.test1.safetynet.tasks.ISafetyNetGetAttestationInterface;
import com.tripas.test1.test1.safetynet.tasks.ISafetyNetVerifyAttestationInterface;


/**
 * Created by Rui Vieira on 09/05/2017.
 * The Floow Ltd
 * rui.vieira@thefloow.com
 */
public class SafetyNetChecker {

    private final String LOG_TAG = "snc";
    private final String IS_VALID_SIGNATURE = "isValidSignature";
    private final String NONCE = "znc";
    private final String ATTESTATION = "att";

    private final int SIZE_NONCE = 16; // bytes
    private Activity activity;

    public String getDeviceVerificationApiKey() {
        return activity.getResources().getString(R.string.api_key_device_verification);
    }

    private SafetyNetResult callback = new SafetyNetResult() {
        @Override
        public void onSafetyNetResult(boolean valid) {
            // != null
        }
    };
    private SafetyNetClient client;

    public void setCallback(SafetyNetResult callback) {
        this.callback = callback;
    }


    public interface SafetyNetResult {
        void onSafetyNetResult(boolean valid);
    }

    public SafetyNetChecker(Activity activity) {
        this.activity = activity;
        init();
    }

    private void init() {
        client = new SafetyNetClient();
    }


    /**
     * Main checking method
     **/
    public void check() {
        getNonce();
    }

    // 1. GetNonce from PLAT
    private void getNonce() {
        byte[] nonce = new byte[SIZE_NONCE];

        //TODO: THRIFT transaction
        for (int i = 0; i < SIZE_NONCE; i++) {
            nonce[i] = 0;
        }

        getAttestation(nonce);
    }


    // 2. GetAttestation from gms
    private void getAttestation(final byte[] nonce) {
        Log.d(LOG_TAG, NONCE + ": " + Base64.encodeToString(nonce, Base64.DEFAULT));

        client.run(
                activity,
                nonce,
                new ISafetyNetGetAttestationInterface() {
                    @Override
                    public void onAttestation(String attestation) {
                        Log.d(LOG_TAG, ATTESTATION + ": " + client.parseJsonWebSignature(attestation).toString());

                        client.validateAttestation(attestation); // currently just being used as verbose debugging logs

                        validateWithGoogle(attestation);


                        //TODO uncomment with PLAT integration
//                        if (!client.validateAttestation(attestation)) {
//                            return;
//                        }
//                        validateWithPLAT(nonce, attestation);
                    }
                });
    }


    // 3. Validate with Google
    private void validateWithGoogle(final String attestation) {
        // Client verification
        client.verify(
                getDeviceVerificationApiKey(),
                attestation,
                new ISafetyNetVerifyAttestationInterface() {
                    @Override
                    public void onVerify(boolean valid) {
                        Log.d(LOG_TAG, IS_VALID_SIGNATURE + ": " + valid);
                        if (valid) {
                            callback.onSafetyNetResult(
                                    // Final check if the apk corresponds to the original
                                    client.validateAttestation(attestation)
                            );
                            return;
                        }
                        callback.onSafetyNetResult(false);
                    }
                });
    }


    // 3. or Validate with PLAT
    private void validateWithPLAT(byte[] nonce, final String attestation) {

        //TODO: THRIFT transaction for server verification

    }
}
