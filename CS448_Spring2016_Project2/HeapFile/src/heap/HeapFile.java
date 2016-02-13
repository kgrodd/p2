package heapfile;

import global.Convert;
import global.GlobalConst;
import global.Minibase;
import global.Page;
import global.PageId;
import heap.HeapFile;
import heap.HeapScan;
import heap.Tuple;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import chainexception.ChainException;
public class HeapFile{

	/* If the given name already denotes a file, open it;
	otherwise, create new empty file*/
	public HeapFile(String name){}
	
	/*Inserts a new record into the flie and returns its RID*/
	public RID insertRecord(byte[] record){}
	
	/* */
	public Tuple getRecord(RID rid){}
	
	/*Updates the specified record in the heap file */
	public void updateRecord(RID rid, Tuple newRecord){}
	
	/* Deletes the specified record from the Heap file*/
	public void deleteRecord(RID rid){}
	
	/* gets the number of records in the file */
	public int getRecCnt(){}
	
	/* Initiates a sequential scan of the heap file */
	public HeapScan openScan(){}
}
