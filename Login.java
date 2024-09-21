import java.util.Scanner;

public class Login {
    public static void main(String[] args) {
        String password = "admin";
        int maxAttempts = 3;
        int attempts = 0;

        Scanner scanner = new Scanner(System.in);

        while (attempts < maxAttempts) {
            System.out.print("Enter password: ");
            String input = scanner.nextLine();

            if (input.equals(password)) {
                System.out.println("Login successful!");
                return;
            } else {
                attempts++;
                System.out.println("Incorrect password. Attempts remaining: " + (maxAttempts - attempts));
            }
        }

        System.out.println("Failed. Maximum number of attempts reached.");
    }
}