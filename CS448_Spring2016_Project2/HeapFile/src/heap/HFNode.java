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
	public PageId pid;
	public HFPage hp;
	short space;
	public HFNode(PageId pid, HFPage hp, short space){
		this.pid = pid;
		this.hp = hp;
		this.space = space;
	}

}
