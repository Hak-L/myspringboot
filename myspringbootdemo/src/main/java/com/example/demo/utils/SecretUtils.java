package com.example.demo.utils;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import java.security.Key;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecretUtils {
    private static final Logger log = LoggerFactory.getLogger(SecretUtils.class);
    public static final String DES = "DES";
    public static final String AES = "AES";
    private static Map<String, Integer> keySize = new HashMap();

    public SecretUtils() {
    }

    public static String encrypt(String data, Key key) {
        try {
            if(!StringUtils.isBlank(data) && key != null) {
                byte[] content = data.getBytes("UTF-8");
                Cipher cipher = Cipher.getInstance(key.getAlgorithm());
                cipher.init(1, key);
                byte[] result = cipher.doFinal(content);
                return parseByte2HexStr(result);
            } else {
                return null;
            }
        } catch (Exception var5) {
            log.error("error", var5);
            var5.printStackTrace();
            return null;
        }
    }

    public static String decrypt(String data, Key key) {
        try {
            if(!StringUtils.isBlank(data) && key != null) {
                byte[] content = parseHexStr2Byte(data);
                Cipher cipher = Cipher.getInstance(key.getAlgorithm());
                cipher.init(2, key);
                byte[] result = cipher.doFinal(content);
                return new String(result, "UTF-8");
            } else {
                return null;
            }
        } catch (Exception var5) {
            log.error("error", var5);
            var5.printStackTrace();
            return null;
        }
    }

    public static String encrypt(String data, String key, String algorithm) {
        try {
            if(!StringUtils.isBlank(data) && !StringUtils.isBlank(key)) {
                byte[] content = data.getBytes("UTF-8");
                KeyGenerator kgen = KeyGenerator.getInstance(algorithm);
                SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
                secureRandom.setSeed(key.getBytes());
                kgen.init(((Integer)keySize.get(algorithm)).intValue(), secureRandom);
                SecretKey secretKey = kgen.generateKey();
                byte[] enCodeFormat = secretKey.getEncoded();
                SecretKeySpec keySpec = new SecretKeySpec(enCodeFormat, algorithm);
                Cipher cipher = Cipher.getInstance(algorithm);
                cipher.init(1, keySpec);
                byte[] result = cipher.doFinal(content);
                return parseByte2HexStr(result);
            } else {
                return null;
            }
        } catch (Exception var11) {
            log.error("error", var11);
            var11.printStackTrace();
            return null;
        }
    }

    public static String decrypt(String data, String key, String algorithm) {
        try {
            if(!StringUtils.isBlank(data) && !StringUtils.isBlank(key)) {
                byte[] content = parseHexStr2Byte(data);
                KeyGenerator kgen = KeyGenerator.getInstance(algorithm);
                SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
                secureRandom.setSeed(key.getBytes());
                kgen.init(((Integer)keySize.get(algorithm)).intValue(), secureRandom);
                SecretKey secretKey = kgen.generateKey();
                byte[] enCodeFormat = secretKey.getEncoded();
                SecretKeySpec keySpec = new SecretKeySpec(enCodeFormat, algorithm);
                Cipher cipher = Cipher.getInstance(algorithm);
                cipher.init(2, keySpec);
                byte[] result = cipher.doFinal(content);
                return new String(result, "UTF-8");
            } else {
                return null;
            }
        } catch (Exception var11) {
            log.error("error", var11);
            var11.printStackTrace();
            return null;
        }
    }

    public static String parseByte2HexStr(byte[] buf) {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < buf.length; ++i) {
            String hex = Integer.toHexString(buf[i] & 255);
            if(hex.length() == 1) {
                hex = '0' + hex;
            }

            sb.append(hex.toUpperCase());
        }

        return sb.toString();
    }

    public static byte[] parseHexStr2Byte(String hexStr) {
        int b = 2;
        int h = 16;
        if(hexStr == null) {
            return null;
        } else if(hexStr.length() < 1) {
            return null;
        } else {
            byte[] result = new byte[hexStr.length() / b];

            for(int i = 0; i < hexStr.length() / b; ++i) {
                int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), h);
                int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), h);
                result[i] = (byte)(high * h + low);
            }

            return result;
        }
    }

    public static void main(String[] args) {
        String str = "4CD25D2BAF961156901778E600A283558745A48CB278034FD57AE4A1A8F65A052321EC7F488F46C048F061477C4D96D5223A47F6416A5521A53E8752414498A6EAE4C8F86ECFC912182DB28A56FDD895ACC7153DCCCC48872241C72A1CF59E24EDB1D9776AE014760B577C7235571233A0AEE7EB599AFA39EC28E891032C9C404652800BAC111CF0366358B0CD97A9F2A3B5A8F831303F50B5073F435600B58B2E2A9006766D8D9344B4C476DC81E6FCEC8A3D86E249E98033C4D567A809E9CEDCD6299BF52A079CE3D8072B3C758A5DB857ADD7B991AD045CE66892D65401F315CE0FD4F8F697B321F4D5192C10BD9C84C594B934146E56A4840B9790103897CCDAE6D29912BAA22E1FB876D1FB019AC3BD23F639AB1A2A6A0BADA664F23FA3B69D76DB788F5E8081ABCB15BCFF0F4A4F3E8724B40E05768F56383A5C76757DC01C3F9EB92EE05B071323097E9458DDC5DFC68A2DF42FF80B9887B35BACA11B3D8A7F15BB4D2766C1348423FB335371667F824AB592CCA596165E82621CFA506E4E381169BBE6107E2B35C9B31347E24A1697EC11CF4396C959A9505185CA8FB12A386375DEF8FFA7E8BB9986A4404B5FBB8DEC9960754A84A3A1D8B989AF26B954680A5B15430705CEFA1494539483B4D214B79FC08D9B2DD16EAA965CF63C";
        String key = "7EIyMgpPLZ9jJGtiGS6tF98dKR8I3i";
        System.out.println(decrypt(str, key, "AES"));
    }

    static {
        keySize.put("DES", Integer.valueOf(56));
        keySize.put("AES", Integer.valueOf(128));
    }
}
