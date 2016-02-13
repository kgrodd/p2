public HeapFile(String name)
public RID insertRecord(byte[] record)public Tuple getRecord(RID rid)
public void updateRecord(RID rid, Tuple newRecord)
public void deleteRecord(RID rid)
public int getRecCnt() //get number of records in the file
public HeapScan openScan()
