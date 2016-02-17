/*HFNode.java */
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

public class HFNode{
	private PageId pid;
	private HFPage hp;
	public HFNode(PageId pid, HFPage hp){
		this.pid = pid;
		this.hp = hp;
	}

}
