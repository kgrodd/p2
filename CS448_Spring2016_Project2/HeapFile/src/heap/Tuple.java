/*tuple.java */
package heap;

import global.Convert;
import global.GlobalConst;
import global.Minibase;
import global.RID;
import global.GlobalConst;

public class Tuple{
	byte [] data;	
	
	public Tuple(){
		this.data = new byte [MAX_TUPSIZE];
	}
	
	public int getLength(){
		return this.length;
	}
	
	public byte [] getTupleByteArray(){
		return this;
	}
}
