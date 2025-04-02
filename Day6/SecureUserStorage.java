package Day7;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.util.Base64;
import java.util.Scanner;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class SecureUserStorage {
    private static final String FILE_NAME = "users.txt";
    private static final String SECRET_KEY = "1234567890123456"; // 16-byte key for AES

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1. Add User\n2. Search User\n3. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    System.out.print("Enter Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Enter Password: ");
                    String password = scanner.nextLine();
                    addUser(name, email, password);
                    break;
                case 2:
                    System.out.print("Enter Email to search: ");
                    email = scanner.nextLine();
                    searchUser(email);
                    break;
                case 3:
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private static void addUser(String name, String email, String password) throws Exception {
        if (userExists(email)) {
            System.out.println("User already exists!");
            return;
        }
        String encryptedPassword = encrypt(password);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(name + "," + email + "," + encryptedPassword);
            writer.newLine();
        }
        System.out.println("User added successfully.");
    }

    private static boolean userExists(String email) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[1].equals(email)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void searchUser(String email) throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3 && parts[1].equals(email)) {
                    String decryptedPassword = decrypt(parts[2]);
                    System.out.println("User Found: Name: " + parts[0] + ", Email: " + parts[1] + ", Password: " + decryptedPassword);
                    return;
                }
            }
        }
        System.out.println("User not found.");
    }

    private static String encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, getKey());
        byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    private static String decrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, getKey());
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(data));
        return new String(decrypted, StandardCharsets.UTF_8);
    }

    private static Key getKey() {
        return new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES");
    }
}
