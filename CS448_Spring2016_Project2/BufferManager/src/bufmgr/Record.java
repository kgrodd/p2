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
	private PageId pn;
	private int pc;
	private int db;

	public Record(PageId pn, int pc, int db) {
		this.pn = pn;
		this.pc = pc;
		this.db = db;
	}

	public PageId getPageId() {
		return this.pn;
	}

	public int getPinCount() {
		return this.pc;
	}

	public int getDirtyBit() {
		return this.db;
	}

	public void incPinCount() {
		this.pc++;
	}
	
	public void decPinCount() {
		this.pc--;
	}
}
