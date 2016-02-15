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

public class HeapFile{
	int RecCnt;						//Record Count
	HFPage hp;						//header page file
	String HFPName;					//Name of header File Page
	LinkedList <HFPage> FreeSpace;	//HFPages with free space
	LinkedList <HFPage> NoFreeSpace;//HFPages with no free space
	PageId HeaderPageId;		
	DiskMgr DM;
	BufMgr BM;
	
	/* If the given name already denotes a file, open it;
	otherwise, create new empty file*/
	public HeapFile(String name){
		this.DM = new DiskMgr();
		this.BM = new BufMgr(1000, 10);
		this.FreeSpace = new LinkedList <HFPage>();
		this.NoFreeSpace = new LinkedList <HFPage>();
		this.HFPName = name;
		/*if the file doesn't exist, create db */
		if(DM.get_file_entry(name) == null){
			this.HeaderPageId = BM.newPage((new HFPage()), 1); //runsize: how many pages to allocate
			DM.createDB(name, 10); 			//Check num pages 
		}
		
		/*open if it does exist */
		else{
			DM.openDB(name);
		}
	}
	
	/*Inserts a new record into the flie and returns its RID*/
	public RID insertRecord(byte[] record){
		return null;
	}
	
	/* */
	public Tuple getRecord(RID rid){
		return null;
	}
	
	/*Updates the specified record in the heap file */ 
	public boolean updateRecord(RID rid, Tuple newRecord){
		return true;
	}
	
	/* Deletes the specified record from the Heap file*/
	public boolean deleteRecord(RID rid){
		return true;
	}
	
	/* gets the number of records in the file */
	public int getRecCnt(){
		return 0;
	}
	
	/* Initiates a sequential scan of the heap file */
	public HeapScan openScan(){
		return null;
	}
}
