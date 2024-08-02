package quickfood;

//Customer class to store customer information
public class Customer {
  private int orderNumber;
  private String name;
  private String contactNumber;
  private String address;
  private String location;
  private String email;

 // Constructor to initialize a Customer object
  public Customer(int orderNumber, String name, String contactNumber, String address, String location, String email) throws InvalidInputException {
    if (!name.matches("[a-zA-Z ]+")) {
      throw new InvalidInputException("Invalid name. Please enter letters only.");
    }
    if (!contactNumber.matches("\\d+")) {
      throw new InvalidInputException("Invalid contact number. Please enter numbers only.");
    }
    if (!address.matches("[a-zA-Z0-9 ]+")) {
      throw new InvalidInputException("Invalid address. Please enter letters and numbers only.");
    }
    if (!location.matches("[a-zA-Z ]+")) {
      throw new InvalidInputException("Invalid location. Please enter letters only.");
    }
    if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
      throw new InvalidInputException("Invalid email. Please enter a valid email address.");
    }
    this.orderNumber = orderNumber;
    this.name = name;
    this.contactNumber = contactNumber;
    this.address = address;
    this.location = location;
    this.email = email;
  }

 // Getters and setters for Customer attributes
 public int getOrderNumber() {
   return orderNumber;
 }

 public void setOrderNumber(int orderNumber) {
   this.orderNumber = orderNumber;
 }

 public String getName() {
   return name;
 }

 public void setName(String name) {
   this.name = name;
 }

 public String getContactNumber() {
   return contactNumber;
 }

 public void setContactNumber(String contactNumber) {
   this.contactNumber = contactNumber;
 }

 public String getAddress() {
   return address;
 }

 public void setAddress(String address) {
   this.address = address;
 }

 public String getLocation() {
   return location;
 }

 public void setLocation(String location) {
   this.location = location;
 }

 public String getEmail() {
   return email;
 }

 public void setEmail(String email) {
   this.email = email;
 }
}