package com.neoteric.demo.zmqproto.divconq;

import java.util.Random;

import org.zeromq.ZMQ;

import com.neoteric.demo.zmqproto.DemoProtos;
import com.neoteric.demo.zmqproto.DemoProtos.Task;

public class TaskGenerator {
	public static void main (String[] args) throws Exception {
        ZMQ.Context context = ZMQ.context(1);

        //  Socket to send messages on
        ZMQ.Socket sender = context.socket(ZMQ.PUSH);
        sender.bind("tcp://*:5557");

        //  Socket to send messages on
        ZMQ.Socket sink = context.socket(ZMQ.PUSH);
        sink.connect("tcp://localhost:5558");

        System.out.println("Press Enter when the workers are ready: ");
        System.in.read();
        System.out.println("Sending tasks to workers\n");

        //  The first message is "0" and signals start of batch
        sink.send("0", 0);

        //  Initialize random number generator
        Random srandom = new Random(System.currentTimeMillis());

        //  Send 100 tasks
        int taskCount;
        int total_msec = 0;     //  Total expected cost in msecs
        for (taskCount = 0; taskCount < 100; taskCount++) {
            int workload = srandom.nextInt(100) + 1;
            total_msec += workload;
            System.out.print(workload + ".");
            
            Task task = DemoProtos.Task.newBuilder()
            	.setTaskName("task"+taskCount)
            	.setWorkload(workload)
            	.build();
            
            System.out.print(task);
            
            sender.send(task.toByteArray(), 0);
        }
        System.out.println("Total expected cost: " + total_msec + " msec");
        Thread.sleep(1000);              //  Give 0MQ time to deliver

        sink.close();
        sender.close();
        context.term();
    }
}
