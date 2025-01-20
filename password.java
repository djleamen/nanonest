//

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class password{
/*
 Password file is txt formatted like:
    Line1: "username"
    Line2: "password"
 */
    
    public static boolean evalPass(){
        try{
            //Create scanners
            boolean isRight = true;
            File pass = new File("passcode.txt");
            Scanner myReader = new Scanner(pass);
            Scanner userInput = new Scanner(System.in);
        
            System.out.println("Please enter your username: ");
            String usernameInput = userInput.nextLine();
            String login = myReader.nextLine();
            
            //Compare first line of file with usernameInput
            if (!usernameInput.equals(login)){
                isRight = false;
                System.out.println("Invalid Username");
                myReader.close();
                userInput.close();
                return isRight;
            }
            
            System.out.println("Please enter your password: ");
            
            String passwordInput = userInput.nextLine();
            login = myReader.nextLine();
            
            //Compare second line of file with passwordInput
            if (!passwordInput.equals(login)){
                isRight = false;
                System.out.println("Invalid Password");
                myReader.close();
                userInput.close();
                return isRight;
            }
            System.out.println(isRight);
            myReader.close();
            userInput.close();
            return isRight;
            
        } catch (FileNotFoundException e){
            System.out.println("An error occurred.");
            return false;
        }   
    }
}