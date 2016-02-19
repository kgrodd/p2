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
	private PageId currPID;
	private RID currRID;
	private LinkedList <PageId> pinned;

	/*Constructs a file scan by pinning the directoy 
	 * header page and initializing iterator fields.   */
	protected HeapScan (HeapFile hf){
		this.hf = hf;
		this.currPage = hf.getHeapPage();
		this.pinned = new LinkedList <PageId> ();
		this.currPID = hf.getHeapId();
		Minibase.BufferManager.pinPage(this.currPID, this.currPage, false);
		currPage.setCurPage(hf.getHeapId());
		currRID = currPage.firstRecord();
		this.pinned.push(hf.getHeapId());
	}

	/* Called by the garbage collector when there are 
	 * no more references to the object; closes the 
	 * scan if it's still open.  */
	protected void finalize () throws Throwable {
		return;
	}
	/* Closes the file scan, releasing any pinned pages. */ 
	public void close () throws ChainException{
		while(!this.pinned.isEmpty()){
			Minibase.BufferManager.unpinPage(this.pinned.pop(), false);
		}
	}

	/* Returns true if there are more records to scan, false otherwise.  */
	public boolean hasNext () throws ChainException{
		//return currPage.hasNext(currRID);
		if(currPage.hasNext(currRID) == false){
			if(currPage.getNextPage().pid == -1){
				return false;
			}
			else{
				Minibase.BufferManager.unpinPage(currPID, false);
				this.pinned.remove(this.currPID);
				PageId pid = currPage.getNextPage();
				HFPage p = new HFPage();
				Minibase.BufferManager.pinPage(pid, p, false);
				this.pinned.add(pid);
				this.currPage = p;
				this.currRID = currPage.firstRecord();
				this.currPID = pid;
			}
		}
		return true;
	}	

	/* Gets the next record in the file scan.  */
	public Tuple getNext (RID rid) throws ChainException{
		byte [] data;
		if(currPage.hasNext(currRID) == false){
			if(currPage.getNextPage().pid == -1){
				//throw exception
				return null;
			}
			else{
				Minibase.BufferManager.unpinPage(currPID, false);
				this.pinned.remove(currPID);
				PageId pid = currPage.getNextPage();
				HFPage p = new HFPage();
				Minibase.BufferManager.pinPage(pid, p, false);
				this.pinned.add(pid);
				this.currPage = p;
				this.currRID = currPage.firstRecord();
				this.currPID = pid;
				rid = this.currRID;
				data = currPage.selectRecord(rid);
				Tuple t = new Tuple(data, 0, data.length);
				currRID = rid;
				return t;
			}
		}
		rid = currPage.nextRecord(currRID);
		data = currPage.selectRecord(rid);
		Tuple t = new Tuple(data, 0, data.length);
		currRID = rid;
		return t;
	}
}
