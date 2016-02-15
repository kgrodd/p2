package bufmgr;
import chainexception.*;

public class PagePinnedException extends ChainException {
  
  
  	PagePinnedException(Exception ex, String name) 
    { 
      super(ex, name); 
    }
}
