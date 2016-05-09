import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import java.nio.ByteBuffer;
import java.util.*;

import lamedb.*;

public class LameDBSimpleClient {
  
  public static final int PORT = 9090;

  public static void main(String [] args) {
    try {
      int op;

      TTransport transport;
     
      transport = new TSocket("localhost", PORT);
      transport.open();

      TProtocol protocol = new  TBinaryProtocol(transport);
      LameDB.Client client = new LameDB.Client(protocol);
      do{
        op = askForOperation();
        perform(client, op);
      }while(op!=8);

      transport.close();
    } catch (TException x) {
      System.out.println("Erro na parada!");
      x.printStackTrace();
    }
  }

  private static void perform(LameDB.Client client, int option) throws TException
  {
      switch(option){
        case 1:
          get(client);
        break;

        case 2:
          getRange(client);
        break;
        
        case 3:
          put(client);
        break;

        case 4:
	  update(client);
        break;

        case 5:
	  updateWithVersion(client);
        break;

        case 6:
	  remove(client);
        break;
        
        case 7:
	  removeWithVersion(client);
        break;

        case 8:
          System.out.println("Closing connection...");
        break;

        default:
        break;
      }
  }

  private static void get(LameDB.Client client) throws TException{
    Scanner in = new Scanner(System.in);
    System.out.print("Provide the corresponding key: ");
    long key = in.nextLong();

    KeyValue kv = client.get(key);

    if(kv.getVersion() != -1){
      System.out.println("KEY: " + kv.getKey());
      System.out.println("VALUE: " + new String(kv.getValue()));
      System.out.println("VERSION: " + kv.getVersion());
    }
    else
    {
      System.out.println("Element not found!");
    }
  }

  private static void getRange(LameDB.Client client) throws TException{
    Scanner in = new Scanner(System.in);
    System.out.print("Provide the low range: ");
    long start = in.nextLong();
    System.out.print("Provide the high range: ");
    long end = in.nextLong();

    List<KeyValue> list = client.getRange(start, end);

    if(!list.isEmpty())
    {
      for(KeyValue kv : list){
        System.out.println("KEY: " + kv.getKey());
        System.out.println("VALUE: " + new String(kv.getValue()));
        System.out.println("VERSION: " + kv.getVersion());
        System.out.println("***************************");
      } 
    }
    else
    {
      System.out.println("No elements within the provided range!");
    }
  }


  private static void put(LameDB.Client client) throws TException{
    Scanner in = new Scanner(System.in);
    System.out.print("Provide the key: ");
    long key = in.nextLong();
    System.out.print("Provide the value: ");
    in.nextLine();
    String value = in.nextLine();

    client.put(key, ByteBuffer.wrap(value.getBytes()));
  }

  private static void update(LameDB.Client client) throws TException{
    Scanner in = new Scanner(System.in);
    System.out.print("Provide the key: ");
    long key = in.nextLong();
    System.out.print("Provide the value to update: ");
    in.nextLine();
    String value = in.nextLine();

    int versionReturn = client.update(key, ByteBuffer.wrap(value.getBytes()));
    
    if(versionReturn!=-1){
	System.out.println("VERSION: " + versionReturn);
    }else{
	System.out.println("Element not found!");
    }
  }
  private static void updateWithVersion(LameDB.Client client) throws TException{
    Scanner in = new Scanner(System.in);
    System.out.print("Provide the key: ");
    long key = in.nextLong();
    System.out.print("Provide the value to update: ");
    in.nextLine();
    String value = in.nextLine();
    System.out.print("Provide the version: ");
    int version = in.nextInt();

    int versionReturn = client.updateWithVersion(key, ByteBuffer.wrap(value.getBytes()),version);

    if(versionReturn!=-1){
	System.out.println("VERSION: " + versionReturn);
    }else{
	System.out.println("Element or version not found!");
    }
  }
  private static void remove(LameDB.Client client) throws TException{
    Scanner in = new Scanner(System.in);
    System.out.print("Provide the key: ");
    long key = in.nextLong();

    KeyValue kv = client.remove(key);
    
    if(kv.getVersion() != -1){

      System.out.println("KEY: " + kv.getKey());
      System.out.println("VALUE: " + new String(kv.getValue()));
      System.out.println("VERSION: " + kv.getVersion());
    }
    else
    {
      System.out.println("Element or not found!");
    }

  }
  private static void removeWithVersion(LameDB.Client client) throws TException{
    Scanner in = new Scanner(System.in);
    System.out.print("Provide the key: ");
    long key = in.nextLong();
    System.out.print("Provide the version: ");
    int version = in.nextInt();
   
    KeyValue kv = client.removeWithVersion(key,version);

    if(kv.getVersion() != -1){
     
      System.out.println("KEY: " + kv.getKey());
      System.out.println("VALUE: " + new String(kv.getValue()));
      System.out.println("VERSION: " + kv.getVersion());
    }
    else
    {
      System.out.println("Element or version not found!");
    }
  }
 

  private static int askForOperation(){
    System.out.println("1. GET");
    System.out.println("2. GET_RANGE");
    System.out.println("3. PUT");
    System.out.println("4. UPDATE");
    System.out.println("5. UPDATE_WITH_VERSION");
    System.out.println("6. REMOVE");
    System.out.println("7. REMOVE_WITH_VERSION");
    System.out.println("8. QUIT");
    System.out.print("Option: ");

    Scanner in = new Scanner(System.in);
    return in.nextInt();
  }
}
