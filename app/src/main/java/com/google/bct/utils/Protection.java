package com.google.bct.utils;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import static com.google.bct.utils.AesCbcWithIntegrity.decryptString;
import static com.google.bct.utils.AesCbcWithIntegrity.encrypt;
import static com.google.bct.utils.AesCbcWithIntegrity.keys;

public class Protection {
    //    private static String SIGNATURE = "bSFXg7FLPQp38hiRs7GBZbNixqc=";
    private boolean debug = true;
    //    public static String KEY = "tBoQWvoHZZMkrYd9wt1wHQ==:SQJB4Ltsl12/+Nyz9tJcBS6rwzhiZA7CiNQ2JuuJur4=";
    public static String KEY = "8de7a11da526739759a36941f1617e3a";

    public static String decrypt(String txt){
        AesCbcWithIntegrity.SecretKeys key;

        try {
            key = keys(KEY);
            String decrypted = decryptString(new AesCbcWithIntegrity.CipherTextIvMac(txt), key);
            return decrypted;
        } catch (Exception e) {
            return null;
        }
    }

    public static String encryptIt(String txt){
        AesCbcWithIntegrity.SecretKeys key;
        try {
            key = keys(KEY); // alternately, regenerate the key from password/salt.
            // Enable Local Datastore.

            AesCbcWithIntegrity.CipherTextIvMac civ = encrypt(txt, key);
            return civ.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public static String getCertificateSHA1Fingerprint(Context context) {
        Context mContext =  context;
        PackageManager pm = mContext.getPackageManager();
        String packageName = mContext.getPackageName();
        int flags = PackageManager.GET_SIGNATURES;
        PackageInfo packageInfo = null;
        try {
            packageInfo = pm.getPackageInfo(packageName, flags);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Signature[] signatures = packageInfo.signatures;
        byte[] cert = signatures[0].toByteArray();
        InputStream input = new ByteArrayInputStream(cert);
        CertificateFactory cf = null;
        try {
            cf = CertificateFactory.getInstance("X509");
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        X509Certificate c = null;
        try {
            c = (X509Certificate) cf.generateCertificate(input);
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        String hexString = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(c.getEncoded());
            hexString = byte2HexFormatted(publicKey);
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        }
        return hexString;
    }

    public static String byte2HexFormatted(byte[] arr) {
        StringBuilder str = new StringBuilder(arr.length * 2);
        for (int i = 0; i < arr.length; i++) {
            String h = Integer.toHexString(arr[i]);
            int l = h.length();
            if (l == 1) h = "0" + h;
            if (l > 2) h = h.substring(l - 2, l);
            str.append(h.toUpperCase());
            if (i < (arr.length - 1)) str.append(':');
        }
        return str.toString();
    }

    public static final String md5(final String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getSIGNATURE(Context context) {
        try {

            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(),

                            PackageManager.GET_SIGNATURES);

            for (Signature signature : packageInfo.signatures) {

                byte[] signatureBytes = signature.toByteArray();

                MessageDigest md = MessageDigest.getInstance("SHA");

                md.update(signatureBytes);

                final String currentSignature = Base64.encodeToString(md.digest(), Base64.DEFAULT).replaceAll("[\\p{C}\\p{Z}]", "");


                return currentSignature;

            }

        } catch (Exception e) {
            //assumes an issue in checking signature., but we let the caller decide on what to do.
        }

        return null;

    }
}
