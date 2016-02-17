/* HeapFile.java */
/* A heap File is an unordered set of records, stored on a set of pages.
 * This class provides basic support for inserting, updating, and deleting
 * records.
 * Temporary heap files are used for external sorting and in other 
 * relational operators.  A sequential scan of a heap file (via the
 * scan class) is the most basic access method */ 
package heap;

import global.Convert;
import global.GlobalConst;
import global.Minibase;
import global.Page;
import global.PageId;
import global.RID;

import diskmgr.DiskMgr;
import bufmgr.BufMgr;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;

import chainexception.ChainException;

public class HeapFile implements GlobalConst{
	private int RecCnt;						//Record Count
	private HFPage hp;						//header page file
	private String HFPName;					//Name of header File Page
	private PageId HeaderPageId;		
	private LinkedList <HFNode> haveSpace;	
	private LinkedList <HFNode> dontHaveSpace;	

	//Minibase.DiskManager.method()
	/* If the given name already denotes a file, open it;
	otherwise, create new empty file*/
	public HeapFile(String name){
		this.HFPName = name;
		this.hp = new HFPage();
		this.RecCnt = 0;
		/*if the file doesn't exist, create db */
		if(Minibase.DiskManager.get_file_entry(name) == null){
			this.HeaderPageId = Minibase.BufferManager.newPage(this.hp, 1); //newPage() does pinning
			System.out.println(;
			Minibase.BufferManager.unpinPage(this.HeaderPageId, true);
		}
		
		/*open if it does exist */
		else{
			
			this.HeaderPageId = Minibase.DiskManager.get_file_entry(name);
			Minibase.BufferManager.pinPage(this.HeaderPageId, this.hp, true);
			int counter = 0;
			RID itter = this.hp.firstRecord();
			while(itter != null){
				counter++;
				itter = this.hp.nextRecord(itter);
			}
			this.RecCnt = 0;
			Minibase.BufferManager.unpinPage(this.HeaderPageId, true);
		}
	}
	
	/*Inserts a new record into the flie and returns its RID*/
	public RID insertRecord(byte[] record)throws ChainException{
		RID newRid = null;
		PageId pid = new PageId();
		HFPage tempHFP = new HFPage();
		//System.out.println(this.hp.getNextPage() + " "+ " " + this.hp.getCurPage());
		tempHFP.setCurPage(this.hp.getNextPage());
		while(newRid == null){
			if(tempHFP.getFreeSpace() >= record.length){
				newRid = tempHFP.insertRecord(record);
				break;
			}
			tempHFP.setCurPage(tempHFP.getNextPage());
		}
		this.RecCnt++;
		return newRid;
	}
	
	/* */
	public Tuple getRecord(RID rid)throws ChainException{

		Tuple record = new Tuple();
		return null;
	}
	
	/*Updates the specified record in the heap file */ 
	public boolean updateRecord(RID rid, Tuple newRecord)throws ChainException{

		return true;
	}
	
	/* Deletes the specified record from the Heap file*/
	public boolean deleteRecord(RID rid) throws ChainException{
		this.RecCnt--;
		return true;
	}
	
	/* gets the number of records in the file */
	public int getRecCnt() throws ChainException{
		return this.RecCnt;
	}
	
	/* Initiates a sequential scan of the heap file */
	public HeapScan openScan()throws ChainException{
		return null;
	}
}
