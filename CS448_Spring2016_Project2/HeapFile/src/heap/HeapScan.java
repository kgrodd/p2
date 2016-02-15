/*HeapScan.java*/
package heap;

import global.Convert;
import global.GlobalConst;
import global.Minibase;
import global.Page;
import global.PageId;
import global.RID;

public class HeapScan{
	protected HeapScan (HeapFile hf){

	}
	protected void finalize () throws Throwable {
		return;
	}
	public void close () {
		return;
	}
	public boolean hasNext () {
		return true;
	}	
	public Tuple getNext (RID rid) {
		return null;
	}
}
