package com.neoteric.demo.zmqproto.reqrepbroker;

import java.lang.management.ManagementFactory;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

import com.google.protobuf.InvalidProtocolBufferException;
import com.neoteric.demo.zmqproto.DemoProtos;
import com.neoteric.demo.zmqproto.DemoProtos.HelloMessage;

public class ReqClient {
	public static void main(String[] args) throws InvalidProtocolBufferException {
		String processName = ManagementFactory.getRuntimeMXBean().getName();
		
        Context context = ZMQ.context(1);

        //  Socket to talk to server
        Socket requester = context.socket(ZMQ.REQ);
        requester.connect("tcp://localhost:5559");
        
        System.out.println("launch and connect client.");
        
        HelloMessage helloMessage = DemoProtos.HelloMessage.newBuilder()
    		.setMessageText("Hello from Client "+processName)
    		.build();

        for (int request_nbr = 0; request_nbr < 10; request_nbr++) {
            requester.send(helloMessage.toByteArray());
            byte[] reply = requester.recv();
            
            HelloMessage helloReply = DemoProtos.HelloMessage.parseFrom(reply);
            System.out.println("Received reply " + request_nbr + " [" + helloReply + "]");
        }
        
        //  We never get here but clean up anyhow
        requester.close();
        context.term();
    }
}
