package com.metfone.topup.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;


import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
import org.apache.commons.codec.binary.Base64;
import java.io.InputStream;

@Component
public class UtilHelper {

    @Value("${emoney.key}")
    private String EMONEY_PRIVATE_KEY;

    @Autowired
    MessageSource messageSource;

    public String convertErrorCodeToString(String errorCode) {
        String errorDescription = "";
        try {
            if(errorCode == null || errorCode.isEmpty()){
                errorCode = "99";
            }
            errorDescription = messageSource.getMessage(errorCode, null,
                    LocaleContextHolder.getLocaleContext().getLocale());
            if(errorDescription == null || errorDescription.isEmpty()){
                messageSource.getMessage("error.isdn.system", null,
                        LocaleContextHolder.getLocaleContext().getLocale());
            }
            return errorDescription;
        }catch (Exception e){
            return messageSource.getMessage("system.busy", null,
                    LocaleContextHolder.getLocaleContext().getLocale());
        }

    }

    public String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }

    public String decryptCallbackData(String callbackData) {
        String decrypt = decryptRSA(callbackData);
        return decrypt;
    }

    public String decryptRSA(String encryptedData) {
        try {
            Resource resource = new ClassPathResource(EMONEY_PRIVATE_KEY);

            InputStream fis = resource.getInputStream();
//            FileInputStream fis = new FileInputStream(privateKeyFilePath);
            byte[] byteKeyFromFile = new byte[fis.available()];
            fis.read(byteKeyFromFile);
            fis.close();
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(byteKeyFromFile);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            PrivateKey priKey = factory.generatePrivate(keySpec);
            // Giải mã dữ liệu
            Cipher c2 = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            c2.init(Cipher.DECRYPT_MODE, priKey);
            String decrypted = new String(c2.doFinal(Base64.decodeBase64(encryptedData)));
            return decrypted;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


}
