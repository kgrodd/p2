/* HeapListNode.java*/
/* doubly Linked list nodes for implementing heap file */
package heap;

import global.Convert;
import global.GlobalConst;
import global.Minibase;
import global.RID;

public class HeapListNode{
	public HeapListNode(Page page, HeapListNode last, HeapListNode next){
		this.last = null;
		this.next = null;
		this.page = page;
	}
	public void insertAfter(HeapListNode lastNode, HeapListNode newNode){
		lastNode.next = newNode;
		newNode.next = null;
		newNode.last = lastNode;
	}
}
