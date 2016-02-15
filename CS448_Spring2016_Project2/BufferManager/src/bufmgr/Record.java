package bufmgr;

import global.Convert;
import global.GlobalConst;
import global.Minibase;
import global.Page;
import global.PageId;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import chainexception.ChainException;

public class Record {
	private int pn;
	private int pc;
	private int db;

	public Record(int pn, int pc, int db) {
		this.pn = pn;
		this.pc = pc;
		this.db = db;
	}

	public int getPageId() {
		return this.pn;
	}

	public PageId getPID() {
		return new PageId(this.pn);
	}
	public int getPinCount() {
		return this.pc;
	}

	public int getDirtyBit() {
		return this.db;
	}

	public void setDirtyBit(boolean b) {
		if(b) {
			this.db = 1;
		}	
		else {
			this.db = 0;
		}
	}

	public void incPinCount() {
		this.pc++;
	}
	
	public void decPinCount() {
		this.pc--;
	}
}
