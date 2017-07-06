package com.tripas.test1.test1.safetynet;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * SafetyNet API payload Response (once unencoded from JSON Web token)
 * <p>
 * test on emulator:
 * {
 * nonce='AAAAAAAAAAAAAAAAAAAAAA==',
 * timestampMs=1494434203060,
 * apkPackageName='pt.fidelidade.drive',
 * apkCertificateDigestSha256=[p8sq2zz4TP6Qbj8gEnskqnPHjxWM7UUW0nHJQx124/U=],
 * apkDigestSha256='9ebXccz7WWd8DG53VjP/b7iIs9U3oz9d5Zj0BOwgDFQ=',
 * ctsProfileMatch=false,
 * basicIntegrity=false
 * }
 * <p>
 */
public class SafetyNetResponse {

    private static final String TAG = SafetyNetResponse.class.getSimpleName();
    private String nonce;
    private long timestampMs;
    private String apkPackageName;
    private String[] apkCertificateDigestSha256;
    private String apkDigestSha256;
    private boolean ctsProfileMatch;
    private boolean basicIntegrity;

    //forces the parse()
    private SafetyNetResponse() {
    }

    /**
     * @return BASE64 encoded
     */
    public String getNonce() {
        return nonce;
    }

    public long getTimestampMs() {
        return timestampMs;
    }

    /**
     * @return com.package.name.of.requesting.app
     */
    public String getApkPackageName() {
        return apkPackageName;
    }

    /**
     * SHA-256 hash of the certificate used to sign requesting app
     *
     * @return BASE64 encoded
     */
    public String[] getApkCertificateDigestSha256() {
        return apkCertificateDigestSha256;
    }

    /**
     * SHA-256 hash of the app's APK
     *
     * @return BASE64 encoded
     */
    public String getApkDigestSha256() {
        return apkDigestSha256;
    }


    /**
     * If the value of "ctsProfileMatch" is true, then the profile of the device running your app matches the profile of a device that has passed Android compatibility testing.
     *
     * @return
     */
    public boolean isCtsProfileMatch() {
        return ctsProfileMatch;
    }

    /**
     * If the value of "basicIntegrity" is true, then the device running your app likely wasn't tampered with, but the device has not necessarily passed Android compatibility testing.
     *
     * @return
     */
    public boolean isBasicIntegrity() {
        return basicIntegrity;
    }

    /**
     * Parse the JSON string into populated SafetyNetResponse object
     *
     * @param decodedJWTPayload JSON String (always a json string according to JWT spec)
     * @return populated SafetyNetResponse
     */
    public static SafetyNetResponse parse(String decodedJWTPayload) {

        Log.d(TAG, "decodedJWTPayload json:" + decodedJWTPayload);

        SafetyNetResponse response = new SafetyNetResponse();
        try {
            JSONObject root = new JSONObject(decodedJWTPayload);
            if (root.has("nonce")) {
                response.nonce = root.getString("nonce");
            }

            if (root.has("apkCertificateDigestSha256")) {
                JSONArray jsonArray = root.getJSONArray("apkCertificateDigestSha256");
                if (jsonArray != null) {
                    String[] certDigests = new String[jsonArray.length()];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        certDigests[i] = jsonArray.getString(i);
                    }
                    response.apkCertificateDigestSha256 = certDigests;
                }
            }

            if (root.has("apkDigestSha256")) {
                response.apkDigestSha256 = root.getString("apkDigestSha256");
            }

            if (root.has("apkPackageName")) {
                response.apkPackageName = root.getString("apkPackageName");
            }

            if (root.has("basicIntegrity")) {
                response.basicIntegrity = root.getBoolean("basicIntegrity");
            }

            if (root.has("ctsProfileMatch")) {
                response.ctsProfileMatch = root.getBoolean("ctsProfileMatch");
            }

            if (root.has("timestampMs")) {
                response.timestampMs = root.getLong("timestampMs");
            }

            return response;
        } catch (JSONException e) {
            Log.e(TAG, "problem parsing decodedJWTPayload:" + e.getMessage(), e);
        }
        return null;
    }


    @Override
    public String toString() {
        return "SafetyNetResponse{" +
                "nonce='" + nonce + '\'' +
                ", timestampMs=" + timestampMs +
                ", apkPackageName='" + apkPackageName + '\'' +
                ", apkCertificateDigestSha256=" + Arrays.toString(apkCertificateDigestSha256) +
                ", apkDigestSha256='" + apkDigestSha256 + '\'' +
                ", ctsProfileMatch=" + ctsProfileMatch +
                ", basicIntegrity=" + basicIntegrity +
                '}';
    }
}
