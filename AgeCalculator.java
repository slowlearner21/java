
// 1. age calculator as on any date given by user
// 2. a login page which will allow user id  and password maximum 3 times
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class AgeCalculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter your birth date (DD/MM/YYYY): ");
        String birthDateInput = scanner.next();

        System.out.println("Enter the current date (DD/MM/YYYY): ");
        String currentDateInput = scanner.next();

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate birthDate = LocalDate.parse(birthDateInput, formatter);
            LocalDate currentDate = LocalDate.parse(currentDateInput, formatter);

            int age = calculateAge(birthDate, currentDate);
            System.out.println("Your age as of " + currentDateInput + " is: " + age + " years");
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use DD/MM/YYYY.");
        }
    }

    public static int calculateAge(LocalDate birthDate, LocalDate currentDate) {
        int age = currentDate.getYear() - birthDate.getYear();
        if ((currentDate.getMonthValue() < birthDate.getMonthValue()) || 
            (currentDate.getMonthValue() == birthDate.getMonthValue() && currentDate.getDayOfMonth() < birthDate.getDayOfMonth())) {
            age--;
        }
        return age;
    }
}
