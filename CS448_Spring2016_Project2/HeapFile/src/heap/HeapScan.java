/*HeapScan.java*/
package heap;

import global.Convert;
import global.GlobalConst;
import global.Minibase;
import global.Page;
import global.PageId;
import global.RID;

import java.util.LinkedList;

import chainexception.ChainException;

public class HeapScan{
	private HeapFile hf;
	private HFPage currPage;
	private RID currRID;
	private LinkedList <PageId> pinned;

	/*Constructs a file scan by pinning the directoy 
	 * header page and initializing iterator fields.   */
	protected HeapScan (HeapFile hf){
		this.hf = hf;
		this.currPage = hf.hp;
		this.pinned = new LinkedList <PageId> ();
		Minibase.BufferManager.pinPage(hf.hpId, hf.hp, false);
		currPage.setCurPage(hf.hpId);
		currRID = currPage.firstRecord();
		this.pinned.add(hf.hpId);
	}

	/* Called by the garbage collector when there are 
	 * no more references to the object; closes the 
	 * scan if it's still open.  */
	protected void finalize () throws Throwable {
		return;
	}
	/* Closes the file scan, releasing any pinned pages. */ 
	public void close () throws ChainException{
		return;
	}

	/* Returns true if there are more records to scan, false otherwise.  */
	public boolean hasNext () throws ChainException{
		
		return false;
	}	

	/* Gets the next record in the file scan.  */
	public Tuple getNext (RID rid) throws ChainException{
		byte [] data;
		if(currPage.nextRecord(currRID) == null){
		//illegal argument exception
			return null;
		}
		rid = currPage.nextRecord(currRID);
		data = currPage.selectRecord(rid);
		Tuple t = new Tuple(data, 0, data.length);
		return t;
	}
}
