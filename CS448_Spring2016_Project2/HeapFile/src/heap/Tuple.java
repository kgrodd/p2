/*tuple.java */
package heap;

import global.Convert;
import global.GlobalConst;
import global.Minibase;
import global.RID;

public class Tuple{
	byte [] data;
	int useless;
	int length;	

	
	public Tuple(byte [] data, int useless, int Length){
		this.data = data;
		this.useless = useless;
		this.length = length;
	}
	
	public Tuple(){
		this(new byte [1004], 0, 1004);
	}
	
	public int getLength(){
		return this.length;
	}
	
	public byte [] getTupleByteArray(){
		return this.data;
	}
}
