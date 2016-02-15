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

public class Node {
	private int pn;
	private int fn;

	public Node(int pn, int fn) {
		this.pn = pn;
		this.fn = fn;
	}
	
	public int getPageNumber() {
		return this.pn;
	}

	public int getFrameNumber() {
		return this.fn;
	}
}
