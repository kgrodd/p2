package bufmgr;
import chainexception.*;

public class BufferPoolExceededException extends ChainException {
  
  
  BufferPoolExceededException(Exception ex, String name) 
    { 
      super(ex, name); 
    }
}
