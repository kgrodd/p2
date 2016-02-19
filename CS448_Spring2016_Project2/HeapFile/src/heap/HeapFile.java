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

import java.lang.System;
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

	//Minibase.DiskManager.method()
	/* If the given name already denotes a file, open it;
	otherwise, create new empty file*/
	public HeapFile(String name){
		this.HFPName = name;
		this.hp = new HFPage();
		this.RecCnt = 0;

		/*if the file doesn't exist, create db */
		PageId fi = Minibase.DiskManager.get_file_entry(name);
		RID rid = new RID();
		HFPage tp = this.hp;

		if(fi == null){
			System.out.println("Doesn't Exists!!!");
			this.hpId = Minibase.BufferManager.newPage(this.hp, 1); //newPage() does pinning
			System.out.println("head id : " + this.hpId.pid);
			Minibase.BufferManager.unpinPage(this.hpId, true);
		} else {
			this.hpId = fi;

			do{
				Minibase.BufferManager.pinPage(fi, tp, false);			
				tp.setCurPage(fi);
				rid = tp.firstRecord();

				while(rid != null) {
					this.RecCnt++;
					rid = tp.nextRecord(rid);
				}

				Minibase.BufferManager.unpinPage(fi, false);
				fi = tp.getNextPage();
			}while(fi.pid != -1);
		}
	}
	

	public String getName () {
		return this.HFPname;
	} 	

	public HFPage getHeapPage() {
		return this.hp;
	}

	public PageId getHeapId() {
		return this.hpId;
	}

		/*Inserts a new record into the flie and returns its RID*/
	public RID insertRecord(byte[] record)throws ChainException{
		RID newRid = null;
		HFPage tempHFP = new HFPage();
		PageId tempId = this.hpId;
		PageId t2 = new PageId();

		do{
			Minibase.BufferManager.pinPage(tempId, tempHFP, false);
			tempHFP.setCurPage(tempId);
			if(tempHFP.getFreeSpace() >= record.length) {
				newRid = tempHFP.insertRecord(record);
				Minibase.BufferManager.unpinPage(tempId, true);
				this.RecCnt++;
				return newRid;
			}
			Minibase.BufferManager.unpinPage(tempId, false);
			tempId = tempHFP.getNextPage();
		}while(tempId.pid != -1);

        HFPage tempHFPTwo = new HFPage();
		t2 = Minibase.BufferManager.newPage(tempHFPTwo, 1);
		Minibase.BufferManager.pinPage(tempHFP.getCurPage(), tempHFP, false);
		tempHFP.setNextPage(t2);
		Minibase.BufferManager.unpinPage(tempHFP.getCurPage(), false);
		tempHFPTwo.setPrevPage(tempId);
		newRid = tempHFPTwo.insertRecord(record);
		Minibase.BufferManager.unpinPage(t2, true);
		this.RecCnt++;

		return newRid;
	}

	/*Updates the specified record in the heap file */ 
	public boolean updateRecord(RID rid, Tuple newRecord)throws ChainException{
		HFPage tempHFP = new HFPage();
		PageId tempId = this.hpId;
		boolean flag = true;

		do{
			Minibase.BufferManager.pinPage(tempId, tempHFP, false);
			tempHFP.setCurPage(tempId);
			if(tempId.pid == rid.pageno.pid) {
				if(tempHFP.selectRecord(rid).length == newRecord.getLength()) {
					System.arraycopy(newRecord.getTupleByteArray(), 0, tempHFP.selectRecord(rid), 0, tempHFP.selectRecord(rid).length);
				} else {
					this.deleteRecord(rid);
					this.insertRecord(newRecord.getTupleByteArray());
				}
				Minibase.BufferManager.unpinPage(tempId, true);
			}
			Minibase.BufferManager.unpinPage(tempId, false);
			tempId = tempHFP.getNextPage();
		}while(tempId.pid != -1);
		
		if(flag == false)
			throw new InvalidUpdateException(null, "illegal argument in update");

		return flag;
	}
	
	/* Deletes the specified record from the Heap file*/
	public boolean deleteRecord(RID rid) throws ChainException{
		HFPage tempHFP = new HFPage();
		PageId tempId = this.hpId;

		do{
			Minibase.BufferManager.pinPage(tempId, tempHFP, false);
			tempHFP.setCurPage(tempId);
			if(tempId.pid == rid.pageno.pid) {
				tempHFP.deleteRecord(rid);
				Minibase.BufferManager.unpinPage(tempId, true);
				this.RecCnt--;
				return true;
			}
			Minibase.BufferManager.unpinPage(tempId, false);
			tempId = tempHFP.getNextPage();
		}while(tempId.pid != -1);
		

		throw new InvalidUpdateException(null, "illegal argument in delete");
	}	

	/* */
	public Tuple getRecord(RID rid)throws ChainException{
		HFPage tempHFP = new HFPage();
		PageId tempId = this.hpId;

		do{
			Minibase.BufferManager.pinPage(tempId, tempHFP, false);
			tempHFP.setCurPage(tempId);
			if(tempId.pid == rid.pageno.pid) {
				byte [] data = tempHFP.selectRecord(rid);
				Tuple T = new Tuple(data, 0, data.length);
				Minibase.BufferManager.unpinPage(tempId, true);
				return T;
			}
			Minibase.BufferManager.unpinPage(tempId, false);
			tempId = tempHFP.getNextPage();
		}while(tempId.pid != -1);

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
			System.out.println("next id  : " + tempHFP.getNextPage().pid + "  tempId :" + tempId.pid);
			Minibase.BufferManager.pinPage(tempId, tempHFP, false);
			System.out.println("next id  : " + tempHFP.getNextPage().pid + "  tempId :" + tempId.pid);
			tempHFP.print();
			System.out.println("next id  : " + tempHFP.getNextPage().pid + "  tempId :" + tempId.pid);
			Minibase.BufferManager.unpinPage(tempId, false);
			tempId = tempHFP.getNextPage();
		}while(tempHFP.getNextPage().pid != -1);
	}

}
