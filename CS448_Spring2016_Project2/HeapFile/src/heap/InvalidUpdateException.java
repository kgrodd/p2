package heap;
import chainexception.*;

public class InvalidUpdateException extends ChainException {
  
  
  InvalidUpdateException(Exception ex, String name) 
    { 
      super(ex, name); 
    }
}
