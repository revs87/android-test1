package com.tripas.test1.test1.safetynet.tasks;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Rui Vieira on 09/05/2017.
 * The Floow Ltd
 * rui.vieira@thefloow.com
 */
public class SafetyNetVerifyAttestationTask extends AsyncTask<Void, Void, Boolean> {

    private String attestation;
    private ISafetyNetVerifyAttestationInterface callback;

    //used to verify the safety net response - 10,000 requests/day free
    private static final String GOOGLE_VERIFICATION_URL = "https://www.googleapis.com/androidcheck/v1/attestations/verify?key=";
    private String apiKey;
    private Exception error;

    public void setup(String apiKey,
                      String attestation,
                      ISafetyNetVerifyAttestationInterface attestationInterface) {
        this.apiKey = apiKey;
        this.attestation = attestation;
        this.callback = attestationInterface;
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        try {
            URL verifyApiUrl = new URL(GOOGLE_VERIFICATION_URL + apiKey);

            HttpURLConnection urlConnection = (HttpsURLConnection) verifyApiUrl.openConnection();
            //TODO: Integrate with FloHTTP

            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");

            //build post body { "signedAttestation": "<output of getJwsResult()>" }
            String requestJsonBody = "{ \"signedAttestation\": \"" + attestation + "\"}";
            byte[] outputInBytes = requestJsonBody.getBytes("UTF-8");
            OutputStream os = urlConnection.getOutputStream();
            os.write(outputInBytes);
            os.close();

            urlConnection.connect();

            //resp ={ “isValidSignature”: true }
            InputStream is = urlConnection.getInputStream();
            StringBuilder sb = new StringBuilder();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            String response = sb.toString();
            JSONObject responseRoot = new JSONObject(response);
            if (responseRoot.has("isValidSignature")) {
                return responseRoot.getBoolean("isValidSignature");
            }
        } catch (Exception e) {
            // something went wrong requesting validation of the JWS Message
            // problem validating JWS Message
            error = e;
            Log.e("snc", "p00 :" + e.getMessage(), e);
            return false;
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean isValidSignature) {
        if (error != null) {
            callback.onVerify(false);
            return;
        }
        callback.onVerify(isValidSignature.booleanValue());
    }
}
