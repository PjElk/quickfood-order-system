package quickfood;

import java.io.*;
import java.util.*;

public class Main {
  // Constants for file names and command options
  private static final String DRIVER_FILE = "driver-info.txt";  
  private static final String INVOICE_FILE = "invoice.txt";
  private static final String DONE = "done";
  private static final String YES = "yes";

  // List to store driver information
  private static List<Driver> drivers = new ArrayList<>();

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    // Load drivers from file
    try {
      loadDrivers(DRIVER_FILE);
    } catch (IOException e) {
      System.err.println("Error loading drivers: " + e.getMessage());
      return;
    }

    // Main loop to handle multiple orders
    while (true) {
      try {
        System.out.println("Enter customer details below");
        int orderNumber = InputUtils.getIntInput(scanner, "Order number: ");
        String customerName = InputUtils.getStringInput(scanner, "Name: ");
        String customerContactNumber = InputUtils.getContactNumberInput(scanner, "Contact number: ");
        String customerAddress = InputUtils.getAlphanumericInput(scanner, "Address: ");
        String customerLocation = InputUtils.getStringInput(scanner, "Location: ");
        String customerEmail = InputUtils.getEmailInput(scanner, "Email: ");

        // Create a Customer object with provided details
        Customer customer = new Customer(orderNumber, customerName, customerContactNumber, customerAddress, customerLocation, customerEmail);

        System.out.println("Enter restaurant details below");
        String restaurantName = InputUtils.getStringInput(scanner, "Name: ");
        String restaurantLocation = InputUtils.getStringInput(scanner, "Location: ");
        String restaurantContactNumber = InputUtils.getContactNumberInput(scanner, "Contact number: ");

        // Create a Restaurant object with provided details
        Restaurant restaurant = new Restaurant(restaurantName, restaurantLocation, restaurantContactNumber);

        // Find a driver for the order
        Driver driver;
        try {
          driver = findDriver(restaurantLocation);
        } catch (DriverNotFoundException e) {
          // Write an unavailable invoice if no driver is found
          writeUnavailableInvoice(customer);
          System.out.println(e.getMessage());
          continue;
        }

        System.out.println("Enter order details:");
        List<Meal> meals = new ArrayList<>();
        double totalAmount = 0.0;

        // Loop to collect meal details
        while (true) {
          String mealName = InputUtils.getStringInput(scanner, "Enter meal name (or type 'done' to finish): ");
          if (mealName.equalsIgnoreCase(DONE)) break;

          int quantity = InputUtils.getIntInput(scanner, "Enter quantity: ");
          double price = InputUtils.getDoubleInput(scanner, "Enter price: ");

          // Create a Meal object and add to the meal list
          Meal meal = new Meal(mealName, quantity, price);
          meals.add(meal);
          totalAmount += price * quantity;
        }

        // Collect special instructions for the order
        String specialInstructions = InputUtils.getStringInput(scanner, "Enter any special instructions: ");

        // Create an Order object with collected details
        Order order = new Order(customer, restaurant, meals, specialInstructions, totalAmount);

        // Generate an invoice for the order
        generateInvoice(order, driver);

        System.out.println("Would you like to place another order? (yes/no)");
        String response = scanner.nextLine().trim();
        if (!response.equalsIgnoreCase(YES)) {
          break;
        }
      } catch (InvalidInputException e) {
        // Handle invalid input exceptions
        System.out.println(e.getMessage());
      }
    }
  }

  /**
   * Loads driver information from the specified file.
   * @param filename the name of the file containing driver information.
   * @throws IOException if an error occurs while reading the file.
   */
  private static void loadDrivers(String filename) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(filename));
    String line;
    while ((line = reader.readLine()) != null) {
      String[] parts = line.split(",");
      if (parts.length == 3) {
        String name = parts[0].trim();
        String location = parts[1].trim();
        int load = Integer.parseInt(parts[2].trim());
        drivers.add(new Driver(name, location, load));
      }
    }
    reader.close();
  }

  /**
   * Finds a driver located at the specified location with the least load.
   * @param location the location to search for a driver.
   * @return the driver with the least load at the specified location.
   * @throws DriverNotFoundException if no driver is found at the specified location.
   */
  private static Driver findDriver(String location) throws DriverNotFoundException {
    Driver bestDriver = null;
    for (Driver driver : drivers) {
      if (driver.getLocation().equalsIgnoreCase(location)) {
        if (bestDriver == null || driver.getLoad() < bestDriver.getLoad()) {
          bestDriver = driver;
        }
      }
    }
    if (bestDriver == null) {
      throw new DriverNotFoundException("Sorry! Our drivers are too far away from you to be able to deliver to your location.");
    }
    bestDriver.setLoad(bestDriver.getLoad() + 1); // Increment the load of the chosen driver
    return bestDriver;
  }

  /**
   * Generates an invoice for the order and writes it to the invoice file.
   * @param order the order to generate an invoice for.
   * @param driver the driver assigned to the order.
   * @throws IOException if an error occurs while writing the invoice file.
   */
  private static void generateInvoice(Order order, Driver driver) throws IOException {
    BufferedWriter writer = new BufferedWriter(new FileWriter(INVOICE_FILE, true)); // Append mode
    writer.write("Invoice:");
    writer.newLine();
    writer.write("Customer Name: " + order.getCustomer().getName());
    writer.newLine();
    writer.write("Order Number: " + order.getCustomer().getOrderNumber());
    writer.newLine();
    writer.write("Customer Address: " + order.getCustomer().getAddress());
    writer.newLine();
    writer.write("Customer Location: " + order.getCustomer().getLocation());
    writer.newLine();
    writer.write("Customer Email: " + order.getCustomer().getEmail());
    writer.newLine();
    writer.write("Restaurant Name: " + order.getRestaurant().getName());
    writer.newLine();
    writer.write("Restaurant Location: " + order.getRestaurant().getLocation());
    writer.newLine();
    writer.write("Driver Name: " + driver.getName());
    writer.newLine();
    writer.write("Order Details:");
    writer.newLine();
    for (Meal meal : order.getMeals()) {
      writer.write("Meal: " + meal.getName() + ", Quantity: " + meal.getQuantity() + ", Price: $" + meal.getPrice());
      writer.newLine();
    }
    writer.write("Total Amount: $" + order.getTotalAmount());
    writer.newLine();
    writer.write("Special Instructions: " + order.getSpecialInstructions());
    writer.newLine();
    writer.write("--------------------------------------------------------");
    writer.newLine();
    writer.close();
  }

  /**
   * Writes an invoice indicating that delivery is not available for the customer.
   * @param customer the customer to generate the invoice for.
   * @throws IOException if an error occurs while writing the invoice file.
   */
  private static void writeUnavailableInvoice(Customer customer) throws IOException {
    BufferedWriter writer = new BufferedWriter(new FileWriter(INVOICE_FILE, true)); // Append mode
    writer.write("Invoice:");
    writer.newLine();
    writer.write("Customer Name: " + customer.getName());
    writer.newLine();
    writer.write("Order Number: " + customer.getOrderNumber());
    writer.newLine();
    writer.write("Customer Address: " + customer.getAddress());
    writer.newLine();
    writer.write("Customer Location: " + customer.getLocation());
    writer.newLine();
    writer.write("Customer Email: " + customer.getEmail());
    writer.newLine();
    writer.write("Sorry! Our drivers are too far away from you to be able to deliver to your location.");
    writer.newLine();
    writer.write("--------------------------------------------------------");
    writer.newLine();
    writer.close();
  }
}
