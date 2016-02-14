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


import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import chainexception.ChainException;

public class HeapFile{
	HeapListNode FreeSpace; 	//Linked list of pages that have free space
	HeapListNode noFreeSpace; 	//Linked List of pages that do not have free space


	/* If the given name already denotes a file, open it;
	otherwise, create new empty file*/
	public HeapFile(String name){}
	
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
