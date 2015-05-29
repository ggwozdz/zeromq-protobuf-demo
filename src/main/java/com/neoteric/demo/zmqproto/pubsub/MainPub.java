package com.neoteric.demo.zmqproto.pubsub;

import org.zeromq.ZMQ;

import com.neoteric.demo.zmqproto.DemoProtos;
import com.neoteric.demo.zmqproto.DemoProtos.Alert;

public class MainPub {
	public static void main (String[] args) throws Exception {
        //  Prepare our context and publisher
        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket publisher = context.socket(ZMQ.PUB);
        publisher.bind("tcp://*:5556");

        int counter = 0;
        while (!Thread.currentThread ().isInterrupted ()) {
        	counter = ++counter%5;
        	Alert alert = DemoProtos.Alert.newBuilder()
        		.setSeverity(counter)
        		.addMessage("message 1")
        		.addMessage("message 2")
        		.addMessage("message 3")
        		.build();
        	publisher.sendMore("alerts");
            publisher.send(alert.toByteArray());
            
        }

        publisher.close ();
        context.term ();
    }
}
