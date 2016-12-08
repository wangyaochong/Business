package util;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;


public class AESEncrypt {
    private static final Logger logger = Logger.getLogger(AESEncrypt.class.getName());
    private static  String password;

    public static String encryptString(String content){
        byte[] encrypt = encrypt(content);
        return parseByte2HexStr(encrypt);
    }
    public static String decryptString(String content){
        logger.info("decryptString.content--->【"+content+"】");
        String result=null;
        try {
            result = new String(decrypt(parseHexStr2Byte(new String(content.getBytes(),"utf-8"))), "utf-8");
        } catch (UnsupportedEncodingException e) {
            logger.info("encrypt.UnsupportedEncodingException");
            logger.log(Level.SEVERE,"[AESEncrypt]UnsupportedEncodingException",e);
        }
        return result;
    }
    private static byte[] encrypt(String content) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");// 创建AES的Key生产者
            SecureRandom secureRandom=SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(password.getBytes("utf-8"));
            kgen.init(128, secureRandom);
            // 128位的key生产者
            //加密没关系，SecureRandom是生成安全随机数序列，password.getBytes()是种子，只要种子相同，序列就一样，所以解密只要有password就行

            SecretKey secretKey = kgen.generateKey();// 根据密码，生成一个密钥

            byte[] enCodeFormat = secretKey.getEncoded();// 返回基本编码格式的密钥，如果此密钥不支持编码，则返回null。

            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");// 转换为AES专用密钥

            Cipher cipher = Cipher.getInstance("AES");// 创建密码器

            byte[] byteContent = content.getBytes("utf-8");

            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化为加密模式的密码器

            byte[] result = cipher.doFinal(byteContent);// 加密
            return result;

        } catch (NoSuchAlgorithmException e) {
            logger.info("encrypt.NoSuchAlgorithmException");
            logger.log(Level.SEVERE,"[AESEncrypt]NoSuchAlgorithmException",e);
        } catch (InvalidKeyException e) {
            logger.info("encrypt.InvalidKeyException");
            logger.log(Level.SEVERE,"[AESEncrypt]InvalidKeyException",e);
        } catch (NoSuchPaddingException e) {
            logger.info("encrypt.NoSuchPaddingException");
            logger.log(Level.SEVERE,"[AESEncrypt]NoSuchPaddingException",e);
        } catch (BadPaddingException e) {
            logger.info("encrypt.BadPaddingException");
            logger.log(Level.SEVERE,"[AESEncrypt]BadPaddingException",e);
        } catch (UnsupportedEncodingException e) {
            logger.info("encrypt.UnsupportedEncodingException");
            logger.log(Level.SEVERE,"[AESEncrypt]UnsupportedEncodingException",e);
        } catch (IllegalBlockSizeException e) {
            logger.info("encrypt.IllegalBlockSizeException");
            logger.log(Level.SEVERE,"[AESEncrypt]IllegalBlockSizeException",e);
        }
        return null;
    }
    private static byte[] decrypt(byte[] content) {
        logger.info("decrypt.content--->【"+content+"】");
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");// 创建AES的Key生产者
            SecureRandom secureRandom=SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(password.getBytes("utf-8"));
//            kgen.init(128, new SecureRandom(password.getBytes("utf-8")));
            kgen.init(128, secureRandom);
            SecretKey secretKey = kgen.generateKey();// 根据用户密码，生成一个密钥
            byte[] enCodeFormat = secretKey.getEncoded();// 返回基本编码格式的密钥
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");// 转换为AES专用密钥
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化为解密模式的密码器
            byte[] result = cipher.doFinal(content);
            return result; // 明文
        } catch (NoSuchAlgorithmException e) {
            logger.info("encrypt.NoSuchAlgorithmException");
            logger.log(Level.SEVERE,"[AESEncrypt]NoSuchAlgorithmException",e);
        } catch (InvalidKeyException e) {
            logger.info("encrypt.InvalidKeyException");
            logger.log(Level.SEVERE,"[AESEncrypt]InvalidKeyException",e);
        } catch (NoSuchPaddingException e) {
            logger.info("encrypt.NoSuchPaddingException");
            logger.log(Level.SEVERE,"[AESEncrypt]NoSuchPaddingException",e);
        } catch (BadPaddingException e) {
            logger.info("encrypt.BadPaddingException");
            logger.log(Level.SEVERE,"[AESEncrypt]BadPaddingException",e);
        } catch (IllegalBlockSizeException e) {
            logger.info("encrypt.IllegalBlockSizeException");
            logger.log(Level.SEVERE,"[AESEncrypt]IllegalBlockSizeException",e);
        } catch (UnsupportedEncodingException e) {
            logger.info("encrypt.UnsupportedEncodingException");
            logger.log(Level.SEVERE,"[AESEncrypt]UnsupportedEncodingException",e);
        }
        return null;
    }
    private static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        String result=null;
        try {
            result= new String( sb.toString().getBytes("utf-8"),"utf-8");//都使用utf-8编码
        } catch (UnsupportedEncodingException e) {
            logger.info("encrypt.UnsupportedEncodingException");
            logger.log(Level.SEVERE,"[AESEncrypt]UnsupportedEncodingException",e);
        }
        return result;
    }
    private static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length()/2];
        for (int i = 0;i< hexStr.length()/2; i++) {
            int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);
            int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    public static void setPassword(String password) {
        AESEncrypt.password = password;
    }
}
