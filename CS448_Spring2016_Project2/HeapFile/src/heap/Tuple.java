/*tuple.java */
package heap;

import global.Convert;
import global.GlobalConst;
import global.Minibase;
import global.RID;

public class Tuple{
	private byte [] data;
	private int useless;
	private int length;	

	
	public Tuple(byte [] data, int useless, int Length){
		this.data = data;
		this.useless = useless;
		this.length = length;
	}
	
	public Tuple(){
		this(new byte [GlobalConst.MAX_TUPSIZE], 0, GlobalConst.MAX_TUPSIZE);
	}
	
	public int getLength(){
		int len = this.length;
		return len;
	}
	
	public byte [] getTupleByteArray(){
		return this.data;
	}
		
	
}
