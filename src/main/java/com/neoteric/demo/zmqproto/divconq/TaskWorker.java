package com.neoteric.demo.zmqproto.divconq;

import org.zeromq.ZMQ;

import com.neoteric.demo.zmqproto.DemoProtos;
import com.neoteric.demo.zmqproto.DemoProtos.Task;

public class TaskWorker {
	 public static void main (String[] args) throws Exception {
	        ZMQ.Context context = ZMQ.context(1);

	        //  Socket to receive messages on
	        ZMQ.Socket receiver = context.socket(ZMQ.PULL);
	        receiver.connect("tcp://localhost:5557");

	        //  Socket to send messages to
	        ZMQ.Socket sender = context.socket(ZMQ.PUSH);
	        sender.connect("tcp://localhost:5558");

	        //  Process tasks forever
	        while (!Thread.currentThread ().isInterrupted ()) {
	        	byte[] taskBytes = receiver.recv();
	        	Task task = DemoProtos.Task.parseFrom(taskBytes);
	        	
	            System.out.println("starting task: "+task);

	            //  Do the work
	            Thread.sleep(task.getWorkload());

	            //  Send finished to sink
	            sender.send(task.toByteArray());
	        }
	        sender.close();
	        receiver.close();
	        context.term();
	    }
}
