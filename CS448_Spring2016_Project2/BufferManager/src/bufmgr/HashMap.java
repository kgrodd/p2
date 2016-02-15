package bufmgr;

import global.Convert;
import global.GlobalConst;
import global.Minibase;
import global.Page;
import global.PageId;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import java.util.LinkedList;
import java.util.ListIterator;

import chainexception.ChainException;

public class HashMap {
	private static final int HTSIZE = 53;
	private LinkedList<Node>[] hm;

	public HashMap() {
		hm = new LinkedList[HTSIZE];

		for (int i = 0; i < HTSIZE; i++) {
			hm[i] = new LinkedList<Node>();
		}
	}

	public int hash(int pn) {
		int a = 4;
		int b = 2;

		return (pn * a + b) % HTSIZE;
	}

	public int size() {
		return this.HTSIZE;
	}

	public void add(Node n) {
		hm[hash(n.getPageNumber())].add(n);
	}
	
	public Node remove(PageId pn) {
		if(pn == null) 
			return null;
		ListIterator<Node> li = hm[hash(pn.pid)].listIterator();
		Node n  = null;

		while(li.hasNext()) {
			if(pn.pid == li.next().getPageNumber()) {
				n = li.next();
				break;
			}
		}
		hm[hash(pn.pid)].remove(n);
		return n;
	}
}
