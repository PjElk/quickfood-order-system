package quickfood;

//Driver class to store driver information
public class Driver {
  private String name;
  private String location;
  private int load;

 // Constructor to initialize a Driver object
 public Driver(String name, String location, int load) {
   this.name = name;
   this.location = location;
   this.load = load;
 }

 // Getters and setters for Driver attributes
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

 public int getLoad() {
   return load;
 }

 public void setLoad(int load) {
   this.load = load;
 }
}
