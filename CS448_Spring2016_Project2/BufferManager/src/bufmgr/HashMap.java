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

public class HashMap implements GlobalConst {
	private static final int HTSIZE = 17137;
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

	public void add(Node n) {
		hm[hash(n.getPageNumber())].add(n);
	}
	
	public Node remove(PageId pn) {
		if(pn == null) 
			return null;
		ListIterator<Node> li = hm[hash(pn.pid)].listIterator();
		Node n  = null;

		while(li.hasNext()) {
			if(pn.pid == (n = li.next()).getPageNumber()) {
				break;
			}
		}

		hm[hash(pn.pid)].remove(n);
		return n;
	}

	public int getFrameNumber(PageId pn) {
		if(pn == null) 
			return -1;

		ListIterator<Node> li = hm[hash(pn.pid)].listIterator();
		Node n  = null;

		while(li.hasNext()) {
			n = li.next();
			if(pn.pid == n.getPageNumber()) {
				return n.getFrameNumber();
			}
		}
	
	
		return -1;
	}

	public int size() {
		return this.HTSIZE;
	}


	public void printHM() {
		ListIterator<Node> li = null;
		System.out.println("****************************HASHMAP*************************");
		for (int i = 0; i < HTSIZE; i++) {
			System.out.print("[row " + i + "] ");
			li = hm[i].listIterator();

			while(li.hasNext()) {
					System.out.print("[" + li.next().getPageNumber() + "] ");
			}
			System.out.println();
		}
	}
}
