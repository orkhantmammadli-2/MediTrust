package com.ltc.appointmentservice.configuration;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class HmacUtil {

    private static final String HMAC_SHA256 = "HmacSHA256";

    public static String generateHmac(
            String data,
            String secret
    ) throws Exception {

        Mac mac = Mac.getInstance(HMAC_SHA256);

        SecretKeySpec secretKey =
                new SecretKeySpec(
                        secret.getBytes(),
                        HMAC_SHA256
                );

        mac.init(secretKey);

        byte[] hash =
                mac.doFinal(data.getBytes());

        return Base64.getEncoder()
                .encodeToString(hash);
    }
}
