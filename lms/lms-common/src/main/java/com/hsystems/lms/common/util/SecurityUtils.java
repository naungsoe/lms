package com.hsystems.lms.common.util;

import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.UUID;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.imageio.ImageIO;

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

  public static String genCaptcha() {
    return new StringTokenizer(UUID.randomUUID().toString(), "-").nextToken();
  }

  public static byte[] getCaptchaPng(String captcha, int width, int height) {
    BufferedImage image = new BufferedImage(width, height,
        BufferedImage.TYPE_INT_RGB);
    Graphics2D graphics = image.createGraphics();
    graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
        RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

    graphics.setColor(Color.WHITE);
    graphics.fillRect(0, 0, width, height);

    int start = 10;
    byte[] bytes = captcha.getBytes();
    graphics.setFont(new Font("Serif", Font.PLAIN, 26));

    for (int i = 0; i < bytes.length; i++) {
      graphics.setColor(Color.BLACK);
      graphics.drawString(new String(new byte[]{bytes[i]}),
          start + (i * 20), (int) (Math.random() * 20 + 20));
    }

    graphics.dispose();

    ByteArrayOutputStream stream = new ByteArrayOutputStream();

    try {
      ImageIO.write(image, "png", stream);

    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }

    return stream.toByteArray();
  }
}
