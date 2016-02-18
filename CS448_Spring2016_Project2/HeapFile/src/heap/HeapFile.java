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
	private PageId hpId;
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
			System.out.println("Doesn't Exists!!!");
			this.hpId = Minibase.BufferManager.newPage(this.hp, 1); //newPage() does pinning
			System.out.println("head id : " + this.hpId.pid);
			Minibase.BufferManager.unpinPage(this.hpId, true);
		}
		
	}
	
		/*Inserts a new record into the flie and returns its RID*/
	public RID insertRecord(byte[] record)throws ChainException{
		RID newRid = null;
		HFPage tempHFP = new HFPage();
		boolean flag = true;
		PageId tempId = this.hpId;

		do{
			Minibase.BufferManager.pinPage(tempId, tempHFP, false);
			if(tempHFP.getFreeSpace() >= record.length) {
				tempHFP.insertRecord(record);
				flag = false;
			}
			Minibase.BufferManager.unpinPage(tempId, false);
			tempId = tempHFP.getNextPage();
		}while(tempHFP.getNextPage().pid != 0 && flag);
		this.RecCnt++;
		return newRid;
	}

	/*Updates the specified record in the heap file */ 
	public boolean updateRecord(RID rid, Tuple newRecord)throws ChainException{

		return true;
	}
	
	/* Deletes the specified record from the Heap file*/
	public boolean deleteRecord(RID rid) throws ChainException{

		return true;
	}	

	/* */
	public Tuple getRecord(RID rid)throws ChainException{

		return null;
	}
	

	
	/* gets the number of records in the file */
	public int getRecCnt() throws ChainException{
		return this.RecCnt;
	}
	
	/* Initiates a sequential scan of the heap file */
	public HeapScan openScan()throws ChainException{
		return null;
	}


	public void printHF() {
		RID newRid = null;
		PageId pid = new PageId();
		HFPage tempHFP = this.hp;
		PageId tempId = this.hpId;

		 do{
			System.out.println("id in  : " + tempHFP.getNextPage().pid + "tempId" + tempId.pid);
			Minibase.BufferManager.pinPage(tempId, tempHFP, false);
			tempHFP.print();
			Minibase.BufferManager.unpinPage(tempId, false);
			tempId= tempHFP.getNextPage();
		}while(tempHFP.getNextPage().pid != -1);
	}

}
