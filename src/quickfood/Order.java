package quickfood;

import java.util.List;

//Order class to store order details
public class Order {
  private Customer customer;
  private Restaurant restaurant;
  private List<Meal> meals;
  private String specialInstructions;
  private double totalAmount;

 // Constructor to initialize an Order object
 public Order(Customer customer, Restaurant restaurant, List<Meal> meals, String specialInstructions, double totalAmount) {
   this.customer = customer;
   this.restaurant = restaurant;
   this.meals = meals;
   this.specialInstructions = specialInstructions;
   this.totalAmount = totalAmount;
 }

 // Getters and setters
 public Customer getCustomer() {
   return customer;
 }

 public void setCustomer(Customer customer) {
   this.customer = customer;
 }

 public Restaurant getRestaurant() {
   return restaurant;
 }

 public void setRestaurant(Restaurant restaurant) {
   this.restaurant = restaurant;
 }

 public List<Meal> getMeals() {
   return meals;
 }

 public void setMeals(List<Meal> meals) {
   this.meals = meals;
 }

 public String getSpecialInstructions() {
   return specialInstructions;
 }

 public void setSpecialInstructions(String specialInstructions) {
   this.specialInstructions = specialInstructions;
 }

 public double getTotalAmount() {
   return totalAmount;
 }

 public void setTotalAmount(double totalAmount) {
   this.totalAmount = totalAmount;
 }
}