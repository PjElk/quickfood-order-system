package quickfood;

//Meal class to store meal information
public class Meal {
  private String name;
  private int quantity;
  private double price;

 // Constructor to initialize a Meal object
 public Meal(String name, int quantity, double price) {
   this.name = name;
   this.quantity = quantity;
   this.price = price;
 }

 // Getters and setters
 public String getName() {
   return name;
 }

 public void setName(String name) {
   this.name = name;
 }

 public int getQuantity() {
   return quantity;
 }

 public void setQuantity(int quantity) {
   this.quantity = quantity;
 }

 public double getPrice() {
   return price;
 }

 public void setPrice(double price) {
   this.price = price;
 }
}
