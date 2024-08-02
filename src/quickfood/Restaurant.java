package quickfood;

//Restaurant class to store restaurant information
public class Restaurant {
  private String name;
  private String location;
  private String contactNumber;

 // Constructor to initialize a Restaurant object
 public Restaurant(String name, String location, String contactNumber) {
   this.name = name;
   this.location = location;
   this.contactNumber = contactNumber;
 }

 // Getters and setters for Restaurant attributes
 public String getName() {
   return name;
 }

 public void setName(String name) {
   this.name = name;
 }

 public String getLocation() {
   return location;
 }

 public void setLocation(String location) {
   this.location = location;
 }

 public String getContactNumber() {
   return contactNumber;
 }

 public void setContactNumber(String contactNumber) {
   this.contactNumber = contactNumber;
 }
}