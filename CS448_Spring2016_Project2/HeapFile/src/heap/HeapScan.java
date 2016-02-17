/*HeapScan.java*/
package heap;

import global.Convert;
import global.GlobalConst;
import global.Minibase;
import global.Page;
import global.PageId;
import global.RID;

import chainexception.ChainException;

public class HeapScan{
	protected HeapScan (HeapFile hf){

	}
	protected void finalize () throws Throwable {
		return;
	}
	public void close () throws ChainException{
		return;
	}
	public boolean hasNext () throws ChainException{
		return true;
	}	
	public Tuple getNext (RID rid) throws ChainException{
		return null;
	}
}
