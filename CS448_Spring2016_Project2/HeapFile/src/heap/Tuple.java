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
	
	public Tuple(byte [] data, int useless, int length){
		this.data = data;
		this.useless = useless;
		this.length = length;
	}
	
	public Tuple(){
		this(new byte [GlobalConst.MAX_TUPSIZE], 0, GlobalConst.MAX_TUPSIZE);
	}
	
	public int getLength(){
		return this.length;
	}
	
	public byte [] getTupleByteArray(){
		return this.data;
	}
}
