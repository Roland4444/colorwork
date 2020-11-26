package ru.com.avs.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javafx.scene.image.Image;
import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserUtils {

    private static final Logger log = LoggerFactory.getLogger(UserUtils.class);

    /**
     * get MD5 hash from string.
     *
     * @param code encoding string
     * @return md5 string
     */
    public static String getHash(String code) {
        byte[] digest = new byte[0];
        try {
            byte[] bytesOfMessage = code.getBytes(StandardCharsets.UTF_8);
            MessageDigest md = MessageDigest.getInstance("MD5");
            digest = md.digest(bytesOfMessage);
        } catch (NoSuchAlgorithmException e) {
            log.error("NoSuchAlgorithmException: ", e);
        }
        return DatatypeConverter.printHexBinary(digest).toLowerCase();
    }

    /**
     * Converting string to byte array.
     *
     * @param encoded string
     * @return byte array
     */
    public static byte[] toByte(String encoded) {
        encoded = removeSpaces(encoded);
        if (encoded.length() == 0) {
            return new byte[0];
        }

        if ((encoded.length() % 2) != 0) {
            throw new IllegalArgumentException("request bytes not even>> " + encoded);
        }
        final byte[] result = new byte[encoded.length() / 2];
        final char[] enc = encoded.toCharArray();
        for (int i = 0; i < enc.length; i += 2) {
            StringBuilder curr = new StringBuilder(2);
            curr.append(enc[i]).append(enc[i + 1]);
            result[i / 2] = (byte) Integer.parseInt(curr.toString(), 16);
        }
        return result;
    }

    public static String removeSpaces(String s) {
        return s.replaceAll(" ", "");
    }

    /**
     * Thread pause.
     *
     * @param ms Integer interval of pause
     */
    public static void waiting(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            log.error("InterruptedException: ", e);
        }
    }

    /**
     * Converting file to base64.
     *
     * @param path Path to file
     * @return String base64
     */
    public static String fileToBase64(String path) {
        File file = new File(path);
        try {
            byte[] fileContent = Files.readAllBytes(file.toPath());
            return Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            log.error("IOException: ", e);
            return null;
        }
    }

    public static boolean fileExists(String path) {
        return new File(path).exists();
    }

    /**
     * Get image by path.
     * @param path Path of image
     * @return weighing photo
     */
    public static Image getImage(String path) {
        File file = new File(path);

        if (!file.exists()) {
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            File file3 = new File(classLoader.getResource("images/no_photo-preview.png").getFile());
            return new Image(file3.toURI().toString());
        }

        return new Image(file.toURI().toString());
    }
}
