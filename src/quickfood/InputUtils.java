package quickfood;

import java.util.Scanner;

public class InputUtils {

  /**
   * Prompts the user to enter a string consisting of letters and spaces only.
   * @param scanner the Scanner object to read input from the user.
   * @param prompt the prompt message to display to the user.
   * @return a valid string input consisting of letters and spaces only.
   * @throws InvalidInputException if the input does not match the expected pattern.
   */
  public static String getStringInput(Scanner scanner, String prompt) throws InvalidInputException {
    while (true) {
      System.out.print(prompt);
      String input = scanner.nextLine();
      if (input.matches("[a-zA-Z ]+")) {
        return input;
      } else {
        System.out.println("Invalid input. Please enter letters only.");
      }
    }
  }

  /**
   * Prompts the user to enter a contact number consisting of digits only.
   * @param scanner the Scanner object to read input from the user.
   * @param prompt the prompt message to display to the user.
   * @return a valid contact number input consisting of digits only.
   * @throws InvalidInputException if the input does not match the expected pattern.
   */
  public static String getContactNumberInput(Scanner scanner, String prompt) throws InvalidInputException {
    while (true) {
      System.out.print(prompt);
      String input = scanner.nextLine();
      if (input.matches("\\d+")) {
        return input;
      } else {
        System.out.println("Invalid input. Please enter numbers only.");
      }
    }
  }

  /**
   * Prompts the user to enter an alphanumeric string consisting of letters, digits, and spaces.
   * @param scanner the Scanner object to read input from the user.
   * @param prompt the prompt message to display to the user.
   * @return a valid alphanumeric input consisting of letters, digits, and spaces.
   * @throws InvalidInputException if the input does not match the expected pattern.
   */
  public static String getAlphanumericInput(Scanner scanner, String prompt) throws InvalidInputException {
    while (true) {
      System.out.print(prompt);
      String input = scanner.nextLine();
      if (input.matches("[a-zA-Z0-9 ]+")) {
        return input;
      } else {
        System.out.println("Invalid input. Please enter letters and numbers only.");
      }
    }
  }

  /**
   * Prompts the user to enter an integer.
   * @param scanner the Scanner object to read input from the user.
   * @param prompt the prompt message to display to the user.
   * @return a valid integer input.
   * @throws InvalidInputException if the input is not a valid integer.
   */
  public static int getIntInput(Scanner scanner, String prompt) throws InvalidInputException {
    while (true) {
      System.out.print(prompt);
      String input = scanner.nextLine();
      try {
        return Integer.parseInt(input);
      } catch (NumberFormatException e) {
        System.out.println("Invalid input. Please enter a valid integer.");
      }
    }
  }

  /**
   * Prompts the user to enter a double.
   * @param scanner the Scanner object to read input from the user.
   * @param prompt the prompt message to display to the user.
   * @return a valid double input.
   * @throws InvalidInputException if the input is not a valid double.
   */
  public static double getDoubleInput(Scanner scanner, String prompt) throws InvalidInputException {
    while (true) {
      System.out.print(prompt);
      String input = scanner.nextLine();
      try {
        return Double.parseDouble(input);
      } catch (NumberFormatException e) {
        System.out.println("Invalid input. Please enter a valid number.");
      }
    }
  }

  /**
   * Prompts the user to enter an email address.
   * @param scanner the Scanner object to read input from the user.
   * @param prompt the prompt message to display to the user.
   * @return a valid email input.
   * @throws InvalidInputException if the input does not match the expected email pattern.
   */
  public static String getEmailInput(Scanner scanner, String prompt) throws InvalidInputException {
    while (true) {
      System.out.print(prompt);
      String input = scanner.nextLine();
      if (input.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
        return input;
      } else {
        System.out.println("Invalid input. Please enter a valid email address.");
      }
    }
  }
}
