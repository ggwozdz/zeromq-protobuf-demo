package com.neoteric.demo.zmqproto.reqrep;

import org.zeromq.ZMQ;

import com.neoteric.demo.zmqproto.DemoProtos;
import com.neoteric.demo.zmqproto.DemoProtos.HelloMessage;

public class MainRep {
	public static void main(String[] args) throws Exception {

		
		ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket socket = context.socket(ZMQ.REP);

        socket.bind ("tcp://*:5555");

        while (!Thread.currentThread ().isInterrupted ()) {

            byte[] request = socket.recv(0);
            HelloMessage helloMessage = DemoProtos.HelloMessage.parseFrom(request);
            System.out.println("Received " + ": [" + helloMessage.getMessageText() + "]");

    		HelloMessage helloMessageResponse = DemoProtos.HelloMessage.newBuilder()
    			.setMessageText("Hello from Server")
    			.build();
            socket.send(helloMessageResponse.toByteArray(), 0);

            Thread.sleep(1000); //  Do some 'work'
        }
        
        socket.close();
        context.term();
    }
}
