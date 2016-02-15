package bufmgr;
import chainexception.*;

public class PageUnpinnedException extends ChainException {
  
  
  	PageUnpinnedException(Exception ex, String name) 
    { 
      super(ex, name); 
    }
}
