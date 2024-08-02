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
      System.out.println("Enter customer details below");
      int orderNumber = getIntInput(scanner, "Order number: ");
      String customerName = getStringInput(scanner, "Name: ");
      String customerContactNumber = getContactNumberInput(scanner, "Contact number: ");
      String customerAddress = getAlphanumericInput(scanner, "Address: ");
      String customerLocation = getStringInput(scanner, "Location: ");
      String customerEmail = getEmailInput(scanner, "Email: ");

      // Create Customer object
      Customer customer = new Customer(orderNumber, customerName, customerContactNumber, customerAddress, customerLocation, customerEmail);

      // Capture restaurant details
      System.out.println("Enter restaurant details below");
      String restaurantName = getStringInput(scanner, "Name: ");
      String restaurantLocation = getStringInput(scanner, "Location: ");
      String restaurantContactNumber = getContactNumberInput(scanner, "Contact number: ");

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
        String mealName = getStringInput(scanner, "Enter meal name (or type 'done' to finish): ");
        if (mealName.equalsIgnoreCase("done")) break;

        int quantity = getIntInput(scanner, "Enter quantity: ");
        double price = getDoubleInput(scanner, "Enter price: ");

        Meal meal = new Meal(mealName, quantity, price);
        meals.add(meal);
        totalAmount += price * quantity;
      }

      String specialInstructions = getStringInput(scanner, "Enter any special instructions: ");

      // Create Order object
      Order order = new Order(customer, restaurant, meals, specialInstructions, totalAmount);

      // Generate invoice for the order
      generateInvoice(order, driver);

      // Prompt to place another order or exit
      System.out.print("Would you like to place another order? (yes/no): ");
      if (!scanner.nextLine().equalsIgnoreCase("yes")) {
        break; // Exit the loop if the user does not want to place another order
      }
    }
  }

  // Method to load drivers from a file
  private static void loadDrivers(String fileName) throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
      String line;
      while ((line = br.readLine()) != null) {
        String[] parts = line.split(",");
        if (parts.length == 3) {
          String name = parts[0].trim();
          String location = parts[1].trim();
          int load = Integer.parseInt(parts[2].trim());
          drivers.add(new Driver(name, location, load));
        }
      }
    }
  }

  // Method to find the driver with the smallest load in the same location as the restaurant
  private static Driver findDriver(String location) throws DriverNotFoundException {
    return drivers.stream()
      .filter(driver -> driver.getLocation().equalsIgnoreCase(location))
      .min(Comparator.comparingInt(Driver::getLoad))
      .orElseThrow(() -> new DriverNotFoundException("Sorry! Our drivers are too far away from you to be able to deliver to your location."));
  }

  // Method to generate an invoice and write it to a text file
  private static void generateInvoice(Order order, Driver driver) {
    Customer customer = order.getCustomer();
    Restaurant restaurant = order.getRestaurant();
    List<Meal> meals = order.getMeals();

    try (PrintWriter writer = new PrintWriter(new File("invoice.txt"))) {
      writer.println("Order number: " + customer.getOrderNumber());
      writer.println();
      writer.println("Customer: " + customer.getName());
      writer.println();
      writer.println("Email: " + customer.getEmail());
      writer.println();
      writer.println("Phone number: " + customer.getContactNumber());
      writer.println();
      writer.println("Location: " + customer.getLocation());
      writer.println();
      writer.println("You have ordered the following from " + restaurant.getName() + " in " + restaurant.getLocation() + ":");
      writer.println();

      for (Meal meal : meals) {
        writer.printf("%d x %s (R%.2f)%n", meal.getQuantity(), meal.getName(), meal.getPrice());
        writer.println();
      }

      writer.println("Special instructions: " + order.getSpecialInstructions());
      writer.println();
      writer.printf("Total: R%.2f%n", order.getTotalAmount());
      writer.println();
      writer.println(driver.getName() + " is nearest to the restaurant and so he will be delivering your order to you at:");
      writer.println();
      writer.println(customer.getAddress());
      writer.println();
      writer.println("If you need to contact the restaurant, their number is " + restaurant.getContactNumber() + ".");
    } catch (FileNotFoundException e) {
      System.err.println("Error writing invoice: " + e.getMessage());
    }
  }

  // Method to write an unavailable invoice if no driver is available
  private static void writeUnavailableInvoice(Customer customer) {
    try (PrintWriter writer = new PrintWriter(new File("invoice.txt"))) {
      writer.println("Sorry! Our drivers are too far away from you to be able to deliver to your location.");
    } catch (FileNotFoundException e) {
      System.err.println("Error writing unavailable invoice: " + e.getMessage());
    }
  }

  // Method to get a string input consisting of letters only
  private static String getStringInput(Scanner scanner, String prompt) {
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

  // Method to get a contact number input consisting of digits only
  private static String getContactNumberInput(Scanner scanner, String prompt) {
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

  // Method to get an alphanumeric input for address
  private static String getAlphanumericInput(Scanner scanner, String prompt) {
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

  // Method to get an integer input
  private static int getIntInput(Scanner scanner, String prompt) {
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

  // Method to get a double input
  private static double getDoubleInput(Scanner scanner, String prompt) {
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

  // Method to get an email input
  private static String getEmailInput(Scanner scanner, String prompt) {
    while (true) {
      System.out.print(prompt);
      String input = scanner.nextLine();
      if (input.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}")) {
        return input;
      } else {
        System.out.println("Invalid input. Please enter a valid email address.");
      }
    }
  }
}
