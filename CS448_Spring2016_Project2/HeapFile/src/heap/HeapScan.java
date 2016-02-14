/*HeapScan.java*/
package heap;

import global.Convert;
import global.GlobalConst;
import global.Minibase;
import global.RID;

public class HeapScan{
	protected HeapScan (HeapFile hf){}
	protected void finalize () throws Throwable {}
	public void close () {}
	public boolean hasNext () {}	
	public Tuple getNext (RID rid) {}
}
