package com.lin_chen.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Formatter;


/**
 * 加密解密链式简易操作类
 * 加密：Encryptor.input(&quot;str...&quot;).aes(&quot;password&quot;).hex();
 * 解密：Encryptor.inputHex(&quot;3faf1f...&quot;).unaes(&quot;password&quot;).string();
 */
public final class Encryptor {
    private static final String NO_SALT = null;
    private static final String UTF_8 = "UTF-8";
    private byte[] data;
    private static final Integer NUM2 = 2;
    private static final Integer NUM4 = 4;
    private static final Integer NUM16 = 16;
    private static final Integer NUM128 = 128;
    private static final Integer HEX16 = 0xff;

    private Encryptor(byte[] input) {
        this.data = input;
    }

    /**
     * 从字节数组创建
     *
     * @param input input
     */
    public static Encryptor input(byte[] input) {
        return new Encryptor(input);
    }

    /**
     * 从字符串创建
     *
     * @param input input
     */
    public static Encryptor input(String input) {
        return new Encryptor(getUTF8Bytes(input));
    }

    /**
     * 从hex字符串创建
     *
     * @param hex 十六进制字符串
     */
    public static Encryptor inputHex(String hex) {
        if (hex == null || hex.length() % NUM2 != 0) {
            throw new IllegalArgumentException("参数不是有效的hex字符串");
        }
        return new Encryptor(unhex(hex));
    }

    /**
     * 从base64字符串创建
     *
     * @param base64 base64
     */
    public static Encryptor inputBase64(String base64) {
        return new Encryptor(Base64.getDecoder().decode(base64));
    }

    /**
     * 返回十六进制字符串结果
     *
     * @return 十六进制字符串
     */
    public String hex() {
        return hex(data);
    }

    /**
     * 返回base64字符串结果
     *
     * @return base64字符串
     */
    public String base64() {
        return Base64.getEncoder().encodeToString(data);
    }

    /**
     * 返回字节数组
     *
     * @return byte[]
     */
    public byte[] byteArray() {
        return Arrays.copyOf(data, data.length);
    }

    /**
     * 返回字符串
     *
     * @return utf-8编码字符串
     */
    public String string() {
        return string(UTF_8);
    }

    /**
     * 返回字符串
     *
     * @param charsetName 编码
     * @return utf-8编码字符串
     */
    public String string(String charsetName) {
        try {
            return new String(data, charsetName);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return
     */
    public Encryptor md5() {
        return md5(NO_SALT);
    }

    /**
     * @param salt salt
     * @return
     */
    public Encryptor md5(String salt) {
        data = hash(data, "MD5", getUTF8Bytes(salt));
        return this;
    }

    /**
     * @return
     */
    public Encryptor sha1() {
        return sha1(NO_SALT);
    }

    /**
     * @param salt salt
     * @return
     */
    public Encryptor sha1(String salt) {
        data = hash(data, "SHA-1", getUTF8Bytes(salt));
        return this;
    }

    /**
     * @return
     */
    public Encryptor sha256() {
        return sha256(NO_SALT);
    }

    /**
     * @param salt salt
     * @return
     */
    public Encryptor sha256(String salt) {
        data = hash(data, "SHA-256", getUTF8Bytes(salt));
        return this;
    }

    /**
     * AES加密
     *
     * @param password 密码
     */
    public Encryptor aes(String password) {
        data = execAES(data, password, Cipher.ENCRYPT_MODE);
        return this;
    }

    /**
     * AES解密
     *
     * @param password 密码
     */
    public Encryptor unaes(String password) {
        data = execAES(data, password, Cipher.DECRYPT_MODE);
        return this;
    }

    /**
     * DES加密
     *
     * @param password 密码
     */
    public Encryptor des(String password) {
        data = execDES(data, password, Cipher.ENCRYPT_MODE);
        return this;
    }

    /**
     * DES解密
     *
     * @param password 密码
     */
    public Encryptor undes(String password) {
        data = execDES(data, password, Cipher.DECRYPT_MODE);
        return this;
    }

    private byte[] execAES(byte[] input, String password, int mode) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(getUTF8Bytes(password));
            kgen.init(NUM128, secureRandom);
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(mode, key);
            return cipher.doFinal(input);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] execDES(byte[] input, String password, int mode) {
        try {
            DESKeySpec dks = new DESKeySpec(getUTF8Bytes(password));
            SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
            SecretKey key = skf.generateSecret(dks);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(mode, key);
            return cipher.doFinal(input);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] getUTF8Bytes(String str) {
        if (str == null) {
            return new byte[0];
        }
        try {
            return str.getBytes(UTF_8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private static String hex(byte[] input) {
        Formatter formatter = new Formatter(new StringBuilder(input.length * NUM2));
        for (byte b : input) {
            formatter.format("%02x", b & HEX16);
        }
        try {
            return formatter.toString();
        } finally {
            formatter.close();
        }
    }

    private static byte[] unhex(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / NUM2];
        for (int i = 0; i < len; i += NUM2) {
            data[i / NUM2] = (byte) ((Character.digit(hex.charAt(i), NUM16) << NUM4)
                    + Character.digit(hex.charAt(i + 1), NUM16));
        }
        return data;
    }

    private byte[] hash(byte[] input, String algorithm, byte[] salt) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance(algorithm);
            if (salt != null) {
                messageDigest.update(salt);
            }
            return messageDigest.digest(input);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
