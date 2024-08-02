package quickfood;

//Exception thrown when an invalid input is encountered.
public class InvalidInputException extends Exception {
  
  /*serialVersionUID is used to ensure compatibility during the serialization process.
    It's an identifier to verify that the sender and receiver of a serialized object have loaded classes
    for that object that are compatible with respect to serialization.
  */
  private static final long serialVersionUID = 1L; // This ensures version compatibility during deserialization.

  //Constructs a new InvalidInputException with the specified detail message.
  public InvalidInputException(String message) {
    super(message);
  }
}