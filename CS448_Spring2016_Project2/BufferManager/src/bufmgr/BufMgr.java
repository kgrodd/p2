package bufmgr;

import global.Convert;
import global.GlobalConst;
import global.Minibase;
import global.Page;
import global.PageId;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;
import chainexception.ChainException;



public class BufMgr implements GlobalConst{
	private String rP;
	private int lAS;
	private int numBufs;
	private Page[] bufPool;
	private Record[] bufDescr;
	private HashMap hm;


	/**
	* Create the BufMgr object.
	* Allocate pages (frames) for the buffer pool in main memory and* make the buffer manage aware that the replacement policy is
	* specified by replacerArg (e.g., LH, Clock, LRU, MRU, LFU, etc.).
	*
	* @param numbufs number of buffers in the buffer pool
	* @param lookAheadSize: Please ignore this parameter
	* @param replacementPolicy Name of the replacement policy, that parameter will be set to "LFU" (you
	can safely ignore this parameter as you will implement only one policy)
	*/
	public BufMgr(int numbufs, int lookAheadSize, String replacementPolicy) {
		this.bufPool = new Page [numbufs];
		for(int i = 0; i < numbufs; i++) {
			this.bufPool[i] = new Page();
		}
		
		this.rP = replacementPolicy;
		this.lAS = lookAheadSize;
		this.numBufs = numbufs;
		
		this.bufDescr = new Record[numbufs];
		for(int i = 0; i < numbufs; i++) {
			this.bufDescr[i] = new Record(-1, 0, 0);
		}
		
		this.hm = new HashMap();
	};

	/**
	* Pin a page.
	* First check if this page is already in the buffer pool.
	* If it is, increment the pin_count and return a pointer to this
	* page.
	* If the pin_count was 0 before the call, the page was a
	* replacement candidate, but is no longer a candidate.
	* If the page is not in the pool, choose a frame (from the
	* set of replacement candidates) to hold this page, read the
	* page (using the appropriate method from {\em diskmgr} package) and pin it.
	* Also, must write out the old page in chosen frame if it is dirty
	* before reading new page.__ (You can assume that emptyPage==false for
	* this assignment.)
	*
	* @param pageno page number in the Minibase.
	* @param page the pointer point to the page.
	* @param emptyPage true (empty page); false (non-empty page)
	*/
	public void pinPage(PageId pageno, Page page, boolean emptyPage) throws BufferPoolExceededException{
			//hm.printHM();
			int loc = -1; //replacement location for when candidate is dirty
									//printBufPool();
			for (int i = 0; i < this.numBufs; i++) {
//System.out.println("page no is " + pageno.pid + " other is " + bufDescr[i].getPageId());
				if(pageno.pid == this.bufDescr[i].getPageId()) {
					this.bufDescr[i].incPinCount();
					this.bufPool[i] = page;
					return;
				} else if (this.bufDescr[i].getPinCount() == 0){
					if(loc == -1)
						loc = i;
				}
			}
//System.out.println("location is " + loc);
			if(loc == -1) {
				throw new BufferPoolExceededException(null, "No Empty Frames to pin too");
			}

			if(bufDescr[loc].getDirtyBit() == 1) {
				try{
					Minibase.DiskManager.write_page(bufDescr[loc].getPID(), bufPool[loc]);
				} 
				catch(ChainException e) {}
				catch(IOException i) {}
			}

			try{
				Minibase.DiskManager.read_page(pageno, page);
			} 
			catch(ChainException e) {}
			catch(IOException i) {}
						//printBufPool();
			if(this.bufDescr[loc].getPageId() != -1) {
				try{
					//hm.printHM();
					//System.out.println("pid to remove is " + bufDescr[loc].getPID());
					this.hm.remove(bufDescr[loc].getPID());	
				} catch(ChainException c) {
				
				}
			}

		
			this.hm.add(new Node(pageno.pid, loc));	
			this.bufPool[loc] = page;
			this.bufDescr[loc] = new Record(pageno.pid, 1, 0);
			//printBufPool();

			//hm.printHM();
	}
	

	/**
	* Unpin a page specified by a pageId.
	* This method should be called with dirty==true if the client has
	* modified the page.
	* If so, this call should set the dirty bit
	* for this frame.
	* Further, if pin_count>0, this method should
	* decrement it.
	*If pin_count=0 before this call, throw an exception
	* to report error.
	*(For testing purposes, we ask you to throw
	* an exception named PageUnpinnedException in case of error.)
	*
	* @param pageno page number in the Minibase.
	* @param dirty the dirty bit of the frame
	*/
	public void unpinPage(PageId pageno, boolean dirty) throws PageUnpinnedException {

			//hm.printHM();
			//System.out.println("this is pageno : " + pageno);
			int frameNo = 0;
			try{
				frameNo = this.hm.getFrameNumber(pageno);
			}
			catch(ChainException e) {}

			if(this.bufDescr[frameNo].getPinCount() == 0) {
				throw new PageUnpinnedException(null, "Page not pinned");
			}			
					
			this.bufDescr[frameNo].decPinCount();
			this.bufDescr[frameNo].setDirtyBit(dirty);
			//hm.printHM();
	}


	/**
	* Allocate new pages.
	* Call DB object to allocate a run of new pages and
	* find a frame in the buffer pool for the first page
	* and pin it. (This call allows a client of the Buffer Manager
	* to allocate pages on disk.) If buffer is full, i.e., you
	* can't find a frame for the first page, ask DB to deallocate
	* all these pages, and return null.
	*
	* @param firstpage the address of the first page.
	* @param howmany total number of allocated new pages.
	*
	* @return the first page id of the new pages.__ null, if error.
	*/
	public PageId newPage(Page firstpage, int howmany) {
		PageId pageno = null;
		try {
			pageno = Minibase.DiskManager.allocate_page(howmany);
		}
		catch(ChainException e) {}
		catch(IOException i) {}
		
		int loc = -1; //replacement location for when candidate is dirty
		
		for (int i = 0; i < this.numBufs; i++) {
			if (this.bufDescr[i].getPinCount() == 0){
				loc = i;
				break;
			} 
		}	

		if(loc == -1) {
			try {
				Minibase.DiskManager.deallocate_page(pageno);
			}
			catch(ChainException e) {}

			return null;	
		}

		//this.bufDescr[loc] = new Record(pageno.pid, 1, 0);
		//this.bufPool[loc] = firstpage;

		//this.hm.add(new Node(pageno.pid, loc));
		try{
			pinPage(pageno, firstpage, false);
		} catch (ChainException c) {

		}
		return pageno;
	}
	

	/**
	* This method should be called to delete a page that is on disk.
	* This routine must call the method in diskmgr package to
	* deallocate the page.
	*
	* @param globalPageId the page number in the data base.
	*/
	public void freePage(PageId globalPageId) throws ChainException{
		
			Minibase.DiskManager.deallocate_page(globalPageId);
		
	}
	

	/**
	* Used to flush a particular page of the buffer pool to disk.
	* This method calls the write_page method of the diskmgr package.
	*
	* @param pageid the page number in the database.
	*/
	public void flushPage(PageId pageid) {
		int frameNo = 0;
		try {
			this.hm.getFrameNumber(pageid);
			Minibase.DiskManager.write_page(pageid, bufPool[frameNo]);
		} 
		catch(ChainException e) {}
		catch(IOException i) {}

		this.bufPool[frameNo] = new Page();
		this.bufDescr[frameNo] = new Record(-1, 0, 0);

		try {
			this.hm.remove(pageid);
		}
		catch(ChainException e) {}
	}
	

	/**
	* Used to flush all dirty pages in the buffer pool to disk
	*
	*/
	public void flushAllPages() {
		for(int i = 0; i < this.numBufs; i++) {
			if(this.bufDescr[i].getDirtyBit() == 1){
				this.flushPage(this.bufDescr[i].getPID());
			}
		}
	};
	

	/**
	* Returns the total number of buffer frames.
	*/
	public int getNumBuffers() {
		return this.numBufs;
	}
	
	/**
	* Returns the total number of unpinned buffer frames.
	*/
	public int getNumUnpinned() {	
		int ct = 0;
		Record r = null;
		for(int i = 0; i < this.numBufs; i++) {
			r = bufDescr[i];
			if(r.getPinCount() == 0) 
				ct++;
			else {
			//System.out.println("this is pinned : " + i + " page id is " + bufDescr[i].getPageId()) ;
			}	
		}
		return ct;	
	}

	public void printBufPool() {
		System.out.println("*******************Print BUFFPOOL***********************");
		for (int i = 0; i < numBufs; i++) {
			System.out.println("Page id : " + bufDescr[i].getPageId() + " Pin Count : "  + bufDescr[i].getPinCount() + " dirty bit : " + bufDescr[i].getDirtyBit());
		}
	}
}
