package bufmgr;
import chainexception.*;

public class HashEntryNotFoundException extends ChainException {
  
  
  HashEntryNotFoundException(Exception ex, String name) 
    { 
      super(ex, name); 
    }
}
