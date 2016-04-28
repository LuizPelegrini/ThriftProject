import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TServerSocket;

public class LameDBServer{
	
	public static LameDBHandler handler;

	public static LameDB.Processor processor;

	public static void main(String[] args) {
		try{

			handler = new LameDBHandler();
			processor = new LameDB.Processor(handler);

			Runnable runnable = new Runnable(){
				public void run(){
					haha(processor);
				}
			};

			new Thread(runnable).start();

		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void haha(LameDB.Processor proc){
		try{
			TServerTransport serverTransport = new TServerSocket(8080);
			TServer server = new TSimpleServer(new Args(serverTransport).processor(proc));

			System.out.println("Starting Simple Server...");
			server.serve();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}