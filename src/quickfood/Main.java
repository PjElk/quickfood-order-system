package quickfood;

import java.io.*;
import java.util.*;

/**
 * Main class for the QuickFood application.
 * This class handles the primary operations including loading drivers, capturing customer and restaurant details,
 * finding available drivers, and generating invoices.
 */
public class Main {
  private static List<Driver> drivers = new ArrayList<>();

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    // Load drivers from file
    try {
      loadDrivers("driver-info.txt"); // Updated to use driver-info.txt
    } catch (IOException e) {
      System.err.println("Error loading drivers: " + e.getMessage());
      return;
    }

    // Loop to allow placing multiple orders
    while (true) {
      // Capture customer details
      try {
        System.out.println("Enter customer details below");
        int orderNumber = InputUtils.getIntInput(scanner, "Order number: ");
        String customerName = InputUtils.getStringInput(scanner, "Name: ");
        String customerContactNumber = InputUtils.getContactNumberInput(scanner, "Contact number: ");
        String customerAddress = InputUtils.getAlphanumericInput(scanner, "Address: ");
        String customerLocation = InputUtils.getStringInput(scanner, "Location: ");
        String customerEmail = InputUtils.getEmailInput(scanner, "Email: ");

        // Create Customer object
        Customer customer = new Customer(orderNumber, customerName, customerContactNumber, customerAddress, customerLocation, customerEmail);

        // Capture restaurant details
        System.out.println("Enter restaurant details below");
        String restaurantName = InputUtils.getStringInput(scanner, "Name: ");
        String restaurantLocation = InputUtils.getStringInput(scanner, "Location: ");
        String restaurantContactNumber = InputUtils.getContactNumberInput(scanner, "Contact number: ");

        // Create Restaurant object
        Restaurant restaurant = new Restaurant(restaurantName, restaurantLocation, restaurantContactNumber);

      // Find a driver in the same location as the restaurant
      Driver driver;
      try {
        driver = findDriver(restaurantLocation);
      } catch (DriverNotFoundException e) {
        writeUnavailableInvoice(customer);
        System.out.println(e.getMessage());
        continue; // Skip to the next iteration to allow placing another order
      }

      // Capture order details
      System.out.println("Enter order details:");
      List<Meal> meals = new ArrayList<>();
      double totalAmount = 0.0;

      // Loop to capture multiple meals
      while (true) {
        String mealName = InputUtils.getStringInput(scanner, "Enter meal name (or type 'done' to finish): ");
        if (mealName.equalsIgnoreCase("done")) break;

        int quantity = InputUtils.getIntInput(scanner, "Enter quantity: ");
        double price = InputUtils.getDoubleInput(scanner, "Enter price: ");

        Meal meal = new Meal(mealName, quantity, price);
        meals.add(meal);
        totalAmount += price * quantity;
      }

        String specialInstructions = InputUtils.getStringInput(scanner, "Enter any special instructions: ");

      // Create Order object
      Order order = new Order(customer, restaurant, meals, specialInstructions, totalAmount);

      // Generate invoice for the order
      generateInvoice(order, driver);

      // Prompt to place another order or exit
      System.out.println("Would you like to place another order? (yes/no)");
        String response = scanner.nextLine().trim();
        if (!response.equalsIgnoreCase("yes")) {
          break;
        }
      } catch (InvalidInputException e) {
        System.out.println(e.getMessage());
      }
    }
  }
}

