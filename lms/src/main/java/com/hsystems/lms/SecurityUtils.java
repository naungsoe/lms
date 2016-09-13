package com.hsystems.lms;

import java.nio.charset.Charset;
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

  private static final Charset UTF8_CHARSET = Charset.forName("UTF-8");

  private static final int iterations = 1000;

  private static final int keyLength = 256;

  public static String getSalt() throws NoSuchAlgorithmException {
    SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
    byte[] salt = new byte[16];
    secureRandom.nextBytes(salt);
    return new String(salt, UTF8_CHARSET);
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

    byte[] bytes = messageDigest.digest(input.getBytes());
    return new String(bytes, UTF8_CHARSET);
  }

  public static String getPasswordHash(String input, String salt)
      throws NoSuchAlgorithmException, InvalidKeySpecException {

    PBEKeySpec keySpec = new PBEKeySpec(
        input.toCharArray(), salt.getBytes(), iterations, keyLength);
    SecretKeyFactory keyFactory = SecretKeyFactory
        .getInstance("PBKDF2WithHmacSHA1");
    byte[] key = keyFactory.generateSecret(keySpec).getEncoded();
    return new String(key, UTF8_CHARSET);
  }
}
