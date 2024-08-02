package quickfood;

//Exception thrown when no driver is found in the specified location.
public class DriverNotFoundException extends Exception {
  
  /*serialVersionUID is used to ensure compatibility during the serialization process.
    It's an identifier to verify that the sender and receiver of a serialized object have loaded classes
    for that object that are compatible with respect to serialization.
  */
  private static final long serialVersionUID = 1L; // This ensures version compatibility during deserialization.

  //Constructs a new DriverNotFoundException with the specified detail message.
  public DriverNotFoundException(String message) {
    super(message);
  }
}