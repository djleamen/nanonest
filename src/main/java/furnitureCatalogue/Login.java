/*
 * This class is responsible for handling the login functionality of the catalogue.
 */

package furnitureCatalogue;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import java.io.*;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;
import java.awt.Component;

public class Login {
    protected HashMap<String, String> users;
    protected final HashMap<String, String> roles;
    protected Scanner scanner;

    public Login() {
        users = new HashMap<>();
        roles = new HashMap<>();
        scanner = new Scanner(System.in);
        // Default Passwords:
        // User: user123
        // Admin: admin123
        readCSV("src/main/resources/Users.csv");
    }

    public String authenticate() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        String password = hashString(readPassword("Enter password: "));
        if (users.containsKey(username) && users.get(username).equals(password)) {
            return roles.get(username);
        } else {
            System.out.println("Invalid credentials.");
            return null;
        }
    }

    protected String readPassword(String prompt) {
//        Console console = System.console();
        if (System.console() == null) {
            System.out.print(prompt);
            return scanner.nextLine();
        } else {
            char[] passwordArray = System.console().readPassword(prompt);
            return new String(passwordArray);
        }
    }

    public String hashString(String input) {
        try {
            String password = input;
            byte[] salt = new byte[16];
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHMacSha1");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return String.format("%x", new BigInteger(hash));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    private void readCSV(String fileName){
        try{
            File userCSV = new File(fileName);
            Scanner userReader = new Scanner(userCSV);
            while(userReader.hasNextLine()) {
                String[] splitLine = userReader.nextLine().split(",");
                users.put(splitLine[0], splitLine[1]);
                if (Objects.equals(splitLine[2], "0")){
                    roles.put(splitLine[0], "user");
                } else{
                    roles.put(splitLine[0], "admin");
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void makeUserSwing(Component parent) {
        String username = JOptionPane.showInputDialog(parent, "Enter new username:");
        if (username == null || username.isEmpty()) {
            System.out.println("Cancelled or blank. Aborting user creation.");
            return;
        }
        if (users.containsKey(username)) {
            JOptionPane.showMessageDialog(parent, "That username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JPasswordField pwdField = new JPasswordField(10);
        int result = JOptionPane.showConfirmDialog(parent, pwdField, "Enter Password", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) {
            System.out.println("Cancelled. Aborting user creation.");
            return;
        }
        String rawPassword = new String(pwdField.getPassword());
        String hashed = hashString(rawPassword);
        String[] options = {"Admin", "User"};
        int choice = JOptionPane.showOptionDialog(parent, "Is this user an admin?", "User Type",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        String adminFlag = (choice == 0) ? "1" : "0";
        try (FileWriter fw = new FileWriter("src/main/resources/Users.csv", true)) {
            fw.write("\n" + username + "," + hashed + "," + adminFlag);
        } catch (IOException e) {
            e.printStackTrace();
        }
        users.put(username, hashed);
        if (adminFlag.equals("1")) {
            roles.put(username, "admin");
        } else {
            roles.put(username, "user");
        }
        JOptionPane.showMessageDialog(parent, "Created user: " + username, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public void makeUser() {
        writeCSV("src/main/resources/Users.csv");
    }

    private void writeCSV(String fileName) {
        boolean userLoop = true;
        String username = "";
        while (userLoop){
            System.out.println("Enter the Username: ");
            username = scanner.nextLine();
            if(users.containsKey(username)){
                System.out.println("Error: Invalid input");
            } else{
                userLoop = false;
            }
        }
        System.out.println("Enter the Password: ");
        String password = hashString(scanner.nextLine());
        boolean adminLoop = true;
        String admin = "";
        while (adminLoop){
            System.out.println("Is this an admin? 1: Yes, 0: No");
            admin = scanner.nextLine();
            if(!Objects.equals(admin, "1") && !Objects.equals(admin, "0")){
                System.out.println("Error: Invalid input");
            } else{
                adminLoop = false;
            }
        }
        try {
            File userCSV = new File(fileName);
            FileWriter userWriter = new FileWriter(userCSV, true);
            userWriter.write("\n" + username + "," + password + "," + admin);
            userWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}