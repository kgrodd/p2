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
	private boolean flag;
	/*Constructs a file scan by pinning the directoy 
	 * header page and initializing iterator fields.   */
	protected HeapScan (HeapFile hf){
		this.hf = hf;
		this.currPage = hf.getHeapPage();
		this.pinned = new LinkedList <PageId> ();
		this.currPID = hf.getHeapId();
		Minibase.BufferManager.pinPage(this.currPID, this.currPage, false);
		this.currPage.setCurPage(hf.getHeapId());
System.out.println("first record ; " + currPage.firstRecord());
		this.currRID = currPage.firstRecord();
		this.pinned.push(hf.getHeapId());
		flag = true;
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
			System.out.println("currp id : " + currPID.pid + "nextid : " + currPage.getNextPage().pid);
			if(currPage.getNextPage().pid == -1){
				return false;
			}
		}
					System.out.println("asdfcurrp id : " + currPID.pid + "nextid : " + currPage.getNextPage().pid);
		return true;
	}	

	/* Gets the next record in the file scan.  */
	public Tuple getNext (RID rid) throws ChainException{
		byte [] data;

		if(this.flag) {
		try {
			data = currPage.selectRecord(this.currRID);
			Tuple t = new Tuple(data, 0, data.length);
			this.flag = false;
return t;
} catch (NullPointerException e) { e.printStackTrace(); throw new InvalidUpdateException(null,"jd;skf");}
			
			
		}
			System.out.println("has next rid : " + currRID);
		if(currPage.hasNext(currRID) == false){
			if(currPage.getNextPage().pid == -1){
				Minibase.BufferManager.unpinPage(currPID, false);
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
				System.out.println("data is : "  + data);
				currRID = rid;
				return t;
			}
		}
		rid = currPage.nextRecord(currRID);
		data = currPage.selectRecord(rid);
				//System.out.println("outdata is : "  + data.length + "    slot no: " + rid.slotno + "   rid.pageno : " + rid.pageno);
		Tuple t = new Tuple(data, 0, data.length);
		System.out.println("tuplelen " +  t.getLength() );
		this.currRID = rid;
		return t;
	}
}
