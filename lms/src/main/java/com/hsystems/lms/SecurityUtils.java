package com.hsystems.lms;

import org.apache.hadoop.hbase.util.Bytes;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Created by administrator on 25/8/16.
 */
public final class SecurityUtils {

  private static final int iterations = 1000;

  private static final int keyLength = 256;

  public static String getRandomSalt() throws NoSuchAlgorithmException {
    byte[] salt = new byte[16];
    SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
    secureRandom.nextBytes(salt);
    return Bytes.toHex(salt);
  }

  public static String getMD5Hash(String input, String salt) {
    try {
      return getHash(input, salt, "MD5");
    } catch (NoSuchAlgorithmException e) {
      return "";
    }
  }

  public static String getHash(String input, String salt, String algorithm)
      throws NoSuchAlgorithmException {

    MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
    messageDigest.update(salt.getBytes());

    byte[] hash = messageDigest.digest(input.getBytes());
    return Bytes.toHex(hash);
  }

  public static String getPassword(String input, String salt)
      throws NoSuchAlgorithmException, InvalidKeySpecException {

    PBEKeySpec keySpec = new PBEKeySpec(
        input.toCharArray(), salt.getBytes(), iterations, keyLength);
    SecretKeyFactory keyFactory = SecretKeyFactory
        .getInstance("PBKDF2WithHmacSHA1");
    byte[] key = keyFactory.generateSecret(keySpec).getEncoded();
    return Bytes.toHex(key);
  }
}
