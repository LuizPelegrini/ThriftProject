import org.apache.thrift.TException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class LameDBHandler implements LameDB.Iface{

	private ConcurrentHashMap<Long, KeyValue> keyValueHashMap;

	public LameDBHandler(){
		keyValueHashMap = new ConcurrentHashMap<Long, KeyValue>();
	}

	public KeyValue get(long key) throws TException{
		if(keyValueHashMap.get(key) == null){
			return new KeyValue(-1L, null, -1);
		}

		return keyValueHashMap.get(key);
		//return new KeyValue(1L, ByteBuffer.allocate(1024), 0);
	}

    public List<KeyValue> getRange(long keyBegin, long keyEnd) throws TException{
    	List<KeyValue> newList = new ArrayList<KeyValue>();

    	while(keyBegin<=keyEnd){
    		if((keyValueHashMap.get(keyBegin)) == null)
    		{
    			keyBegin++;
    			continue;
			}
			else
			{
				newList.add(keyValueHashMap.get(keyBegin));
				keyBegin++;
			}	
    	}

    	return newList;
   	}

    public int put(long key, ByteBuffer value) throws ValueIsTooLongException, TException{

    	if(keyValueHashMap.get(key) == null){
	    	KeyValue newKeyValue = new KeyValue();

	    	newKeyValue.key = key;
	    	newKeyValue.value = value;
	    	newKeyValue.version = 0;

	    	keyValueHashMap.put(key, newKeyValue);
	    	
	    	return 0;
	    }
	    else{
	    	return keyValueHashMap.get(key).version;
	    }
    }

    public int update(long key, ByteBuffer value) throws ValueIsTooLongException, TException{return 0;}

    public int updateWithVersion(long key, ByteBuffer value, int version) throws ValueIsTooLongException, TException{return 0;}

    public KeyValue remove(long key) throws TException{return null;}

    public KeyValue removeWithVersion(long key, int version) throws TException{return null;}

}