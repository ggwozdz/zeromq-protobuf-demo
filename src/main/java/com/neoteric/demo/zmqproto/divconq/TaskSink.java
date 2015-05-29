package com.neoteric.demo.zmqproto.divconq;

import org.zeromq.ZMQ;

import com.neoteric.demo.zmqproto.DemoProtos;
import com.neoteric.demo.zmqproto.DemoProtos.Task;

public class TaskSink {
	public static void main (String[] args) throws Exception {

        //  Prepare our context and socket
        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket receiver = context.socket(ZMQ.PULL);
        receiver.bind("tcp://*:5558");

        //  Wait for start of batch
        String string = new String(receiver.recv(0));

        //  Start our clock now
        long tstart = System.currentTimeMillis();

        //  Process 100 confirmations
        int taskCount;
        for (taskCount = 0; taskCount < 100; taskCount++) {
        	byte[] finishedTaskBytes = receiver.recv();
        	Task task = DemoProtos.Task.parseFrom(finishedTaskBytes);
        	System.out.println("finished task "+task.getTaskName());
            //if ((taskCount / 10) * 10 == taskCount) {
            //    System.out.print(":");
            //} else {
            //    System.out.print(".");
            //}
        }
        //  Calculate and report duration of batch
        long tend = System.currentTimeMillis();

        System.out.println("\nTotal elapsed time: " + (tend - tstart) + " msec");
        receiver.close();
        context.term();
    }
}
