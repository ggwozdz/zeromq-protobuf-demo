package com.neoteric.demo.zmqproto.reqrepbroker;

import java.lang.management.ManagementFactory;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

import com.neoteric.demo.zmqproto.DemoProtos;
import com.neoteric.demo.zmqproto.DemoProtos.HelloMessage;

public class RepServer {
	public static void main(String[] args) throws Exception {
		String processName = ManagementFactory.getRuntimeMXBean().getName();
		
		System.out.println("Starting server "+processName);
		
        Context context = ZMQ.context(1);

        //  Socket to talk to server
        Socket responder = context.socket(ZMQ.REP);
        responder.connect("tcp://localhost:5560");
        

		HelloMessage helloMessage = DemoProtos.HelloMessage.newBuilder()
			.setMessageText("Hello from Server "+processName)
			.build();

        while (!Thread.currentThread().isInterrupted()) {
        	byte[] req = responder.recv();
        	
        	HelloMessage helloMessageFromClient = DemoProtos.HelloMessage.parseFrom(req);
 
            System.out.println("Received request:\n"+helloMessageFromClient);

            //  Do some 'work'
            Thread.sleep(1000);

            //  Send reply back to client
            responder.send(helloMessage.toByteArray());
        }

        //  We never get here but clean up anyhow
        responder.close();
        context.term();
    }
}
