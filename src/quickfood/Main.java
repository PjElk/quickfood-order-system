package quickfood;

import java.io.*;
import java.util.*;

/*
 * The Main class manages the ordering system for QuickFood.
 * It loads driver data, processes customer and restaurant orders,
 * finds available drivers, and generates invoices.
 */
public class Main {
  private static List<Driver> drivers = new ArrayList<>();

  /*
   * The entry point of the program. It handles the main workflow including
   * loading drivers, capturing customer and restaurant details, finding a driver,
   * capturing order details, and generating invoices.
   */
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    // Load drivers from the file
    try {
      loadDrivers("drivers.txt");
    } catch (IOException e) {
      System.err.println("Error loading drivers: " + e.getMessage());
      return;
    }

    while (true) {
      try {
        // Capture customer details
        System.out.println("Enter customer details below");
        int orderNumber = getIntInput(scanner, "Order number: ");
        String customerName = getStringInput(scanner, "Name: ");
        String customerContactNumber = getContactNumberInput(scanner, "Contact number: ");
        String customerAddress = getAlphanumericInput(scanner, "Address: ");
        String customerLocation = getStringInput(scanner, "Location: ");
        String customerEmail = getEmailInput(scanner, "Email: ");

        Customer customer = new Customer(orderNumber, customerName, customerContactNumber, customerAddress, customerLocation, customerEmail);

        // Capture restaurant details
        System.out.println("Enter restaurant details below");
        String restaurantName = getStringInput(scanner, "Name: ");
        String restaurantLocation = getStringInput(scanner, "Location: ");
        String restaurantContactNumber = getContactNumberInput(scanner, "Contact number: ");

        Restaurant restaurant = new Restaurant(restaurantName, restaurantLocation, restaurantContactNumber);

        // Find a driver in the same location as the restaurant
        Driver driver;
        try {
          driver = findDriver(restaurantLocation);
        } catch (DriverNotFoundException e) {
          writeUnavailableInvoice(customer);
          System.out.println(e.getMessage());
          continue;
        }

        // Capture order details
        System.out.println("Enter order details:");
        List<Meal> meals = new ArrayList<>();
        double totalAmount = 0.0;

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

        Order order = new Order(customer, restaurant, meals, specialInstructions, totalAmount);

        // Generate invoice for the order
        generateInvoice(order, driver);

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

  /*
   * Loads drivers from a specified file.
   * @param fileName the name of the file containing driver data
   * @throws IOException if an I/O error occurs
   */
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

  /*
   * Finds the driver with the smallest load in the same location as the restaurant.
   * @param location the location to match the driver with
   * @return the driver with the smallest load in the specified location
   * @throws DriverNotFoundException if no driver is found in the specified location
   */
  private static Driver findDriver(String location) throws DriverNotFoundException {
    return drivers.stream()
        .filter(driver -> driver.getLocation().equalsIgnoreCase(location))
        .min(Comparator.comparingInt(Driver::getLoad))
        .orElseThrow(() -> new DriverNotFoundException("Sorry! Our drivers are too far away from you to be able to deliver to your location."));
  }

  /*
   * Generates an invoice and writes it to a text file.
   * @param order  the order details
   * @param driver the driver assigned to the order
   */
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
      writer.println(customer.getAddress());
      writer.println();
      writer.println("If you need to contact the restaurant, their number is " + restaurant.getContactNumber() + ".");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  /*
   * Writes an unavailable invoice if no driver is available.
   * @param customer the customer details
   */
  private static void writeUnavailableInvoice(Customer customer) {
    try (PrintWriter writer = new PrintWriter(new File("invoice.txt"))) {
      writer.println("Sorry! Our drivers are too far away from you to be able to deliver to your location.");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  /*
   * Gets a string input consisting of letters only.
   * @param scanner the Scanner object for input
   * @param prompt  the prompt message
   * @return the validated string input
   * @throws InvalidInputException if the input is invalid
   */
  private static String getStringInput(Scanner scanner, String prompt) throws InvalidInputException {
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

  /*
   * Gets a contact number input consisting of digits only.
   * @param scanner the Scanner object for input
   * @param prompt  the prompt message
   * @return the validated contact number input
   * @throws InvalidInputException if the input is invalid
   */
  private static String getContactNumberInput(Scanner scanner, String prompt) throws InvalidInputException {
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

  /*
   * Gets an alphanumeric input for address.
   * @param scanner the Scanner object for input
   * @param prompt  the prompt message
   * @return the validated alphanumeric input
   * @throws InvalidInputException if the input is invalid
   */
  private static String getAlphanumericInput(Scanner scanner, String prompt) throws InvalidInputException {
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

  /*
   * Gets an integer input.
   * @param scanner the Scanner object for input
   * @param prompt  the prompt message
   * @return the validated integer input
   * @throws InvalidInputException if the input is invalid
   */
  private static int getIntInput(Scanner scanner, String prompt) throws InvalidInputException {
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

  /*
   * Gets a double input.
   * @param scanner the Scanner object for input
   * @param prompt  the prompt message
   * @return the validated double input
   * @throws InvalidInputException if the input is invalid
   */
  private static double getDoubleInput(Scanner scanner, String prompt) throws InvalidInputException {
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

  /*
   * Gets a valid email input.
   * @param scanner the Scanner object for input
   * @param prompt  the prompt message
   * @return the validated email input
   * @throws InvalidInputException if the input is invalid
   */
  private static String getEmailInput(Scanner scanner, String prompt) throws InvalidInputException {
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