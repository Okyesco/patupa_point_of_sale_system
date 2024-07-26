package com.posapp.pos.common;


import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.*;

import com.posapp.pos.model.Cashier;
//import io.github.cdimascio.dotenv.Dotenv;

public class Common {
    public static double totalAmount ;
	public static double change;
	public static double payAmount;
	public static int purchaseId;
    public static String transactionId;
	public static String productID;
	public static String productqtys;
	public static String slipno;

//    static Dotenv dotenv = Dotenv.load();
    public static String appName = "PaTuPa POS SYSTEM";
    public static String storeName = "Asantewaa's Supermarket";

    public static Cashier cashierRecord = new Cashier();

    public static String generateCashierID(){
        String uuid = UUID.randomUUID().toString();
        return "CA" + uuid.substring(0, 8).toUpperCase(Locale.ROOT);
    }

    public static String generateTransactionID(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String timestamp = sdf.format(new Date());
        int randomNum = new Random().nextInt(10000);
        return "TNX" + timestamp + String.format("%04d", randomNum);
    }


    public static String encrypt(String s) {
        try {
            byte[] input = s.getBytes(StandardCharsets.UTF_8);
//            String keyString = dotenv.get("SECRET_KEY");
            String keyString = "40674244454045cb9a70040a30e1c007";
            byte[] keyBytes = keyString.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            // generate IV
            SecureRandom secureRandom = SecureRandom.getInstanceStrong();
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            byte[] ivBytes = new byte[cipher.getBlockSize()];
            secureRandom.nextBytes(ivBytes);
            GCMParameterSpec gcmSpec = new GCMParameterSpec(96, ivBytes);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmSpec);

            byte[] encrypted = cipher.doFinal(input);
            byte[] returnBytes = new byte[ivBytes.length + encrypted.length];
            System.arraycopy(ivBytes, 0, returnBytes, 0, ivBytes.length);
            System.arraycopy(encrypted, 0, returnBytes, ivBytes.length, encrypted.length);
            return Base64.getEncoder().encodeToString(returnBytes);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static String decrypt(String s) {
        if (s == null || s.isEmpty()) return "";
        try {
            byte[] encrypted = Base64.getDecoder().decode(s);
//            String keyString = dotenv.get("SECRET_KEY");
            String keyString = "40674244454045cb9a70040a30e1c007";
            byte[] keyBytes = keyString.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            byte[] ivBytes = new byte[cipher.getBlockSize()];
            System.arraycopy(encrypted, 0, ivBytes, 0, ivBytes.length);
            GCMParameterSpec gcmSpec = new GCMParameterSpec(96, ivBytes);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmSpec);
            byte[] decrypted = cipher.doFinal(encrypted, cipher.getBlockSize(), encrypted.length - cipher.getBlockSize());
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

}



