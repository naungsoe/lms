package com.hsystems.lms.common.util;

import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Created by naungsoe on 25/8/16.
 */
public final class SecurityUtils {

  private static final String MD5_ALGORITHM = "MD5";
  private static final String SALT_ALGORITHM = "SHA1PRNG";
  private static final String KEY_ALGORITHM = "PBKDF2WithHmacSHA1";
  private static final int ITERATIONS = 1000;
  private static final int KEY_LENGTH = 256;
  private static final int SALT_LENGTH = 16;

  public static String getRandomSalt() {
    try {
      byte[] salt = new byte[SALT_LENGTH];
      SecureRandom secureRandom = SecureRandom.getInstance(SALT_ALGORITHM);
      secureRandom.nextBytes(salt);
      return CommonUtils.getHex(salt);

    } catch (NoSuchAlgorithmException e) {
      throw new IllegalArgumentException(
          "error generating random salt", e);
    }
  }

  public static String getMD5Hash(String input, String salt) {
    return getHash(input, salt, MD5_ALGORITHM);
  }

  public static String getHash(String input, String salt, String algorithm) {
    try {
      MessageDigest messageDigest = MessageDigest.getInstance(algorithm);

      if (StringUtils.isNotEmpty(salt)) {
        messageDigest.update(salt.getBytes());
      }

      byte[] hash = messageDigest.digest(input.getBytes());
      return CommonUtils.getHex(hash);

    } catch (NoSuchAlgorithmException e) {
      throw new IllegalArgumentException(
          "error generating hash", e);
    }
  }

  public static String getPassword(String input, String salt) {

    try {
      PBEKeySpec keySpec = new PBEKeySpec(
          input.toCharArray(), salt.getBytes(), ITERATIONS, KEY_LENGTH);
      SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
      byte[] key = keyFactory.generateSecret(keySpec).getEncoded();
      return CommonUtils.getHex(key);

    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      throw new IllegalArgumentException(
          "error generating hashed password", e);
    }
  }
}
