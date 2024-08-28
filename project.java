package bajaj_240343020095;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Random;
public class project {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("java -jar test.jar 240343020095");
            return;
        }
        String prnNumber = args[0].toLowerCase();
        String filePath = args[1];

        try {
            FileReader reader = new FileReader(filePath);
            JSONTokener tokener = new JSONTokener(reader);
            JSONObject jsonObject = new JSONObject(tokener);

            String destinationValue = findDestinationValue(jsonObject);
            if (destinationValue != null) {
                String randomString = generateRandomString(8);
                String concatenatedString = prnNumber + destinationValue + randomString;
                String hashValue = generateMD5Hash(concatenatedString);
                System.out.println(hashValue + ";" + randomString);
            } else {
                System.out.println("not found");
            }

            reader.close();
        } catch (IOException e) {
            System.out.println("error-json file " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error-generating MD5 hash: " + e.getMessage());
        }
    }

    private static String findDestinationValue(JSONObject jsonObject) {
        Iterator<String> keys = jsonObject.keys();

        while (keys.hasNext()) {
            String key = keys.next();
            Object value = jsonObject.get(key);

            if (key.equals("destination")) {
                return value.toString();
            }

            if (value instanceof JSONObject) {
                String result = findDestinationValue((JSONObject) value);
                if (result != null) return result;
            } else if (value instanceof JSONArray) {
                JSONArray array = (JSONArray) value;
                for (int i = 0; i < array.length(); i++) {
                    Object arrayElement = array.get(i);
                    if (arrayElement instanceof JSONObject) {
                        String result = findDestinationValue((JSONObject) arrayElement);
                        if (result != null) return result;
                    }
                }
            }
        }

        return null;
    }

    private static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }

    private static String generateMD5Hash(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hashInBytes = md.digest(input.getBytes());

        StringBuilder sb = new StringBuilder();
        for (byte b : hashInBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

}
