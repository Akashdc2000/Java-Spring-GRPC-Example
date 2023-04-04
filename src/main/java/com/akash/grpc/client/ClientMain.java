package com.akash.grpc.client;

import com.akash.grpc.StudentServiceGrpc;
import com.akash.grpc.StudentRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import java.util.Scanner;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class ClientMain {
    static Logger logger = LoggerFactory.getLogger(ClientMain.class);

    public static void main(String[] args) throws InterruptedException {

        SpringApplication.run(ClientMain.class, args);
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 6565)
                .usePlaintext()
                .build();

        StudentServiceGrpc.StudentServiceStub nonBlockingStub  = StudentServiceGrpc.newStub(managedChannel);


        StudentServiceGrpc.StudentServiceBlockingStub blockingStub  = StudentServiceGrpc.newBlockingStub(managedChannel);

        StudentRequest studentRequest = StudentRequest.newBuilder()
                .setId(1)
                .build();

        logger.info("\nselect a choice: \n1 for no streaming \n2 for server side streaming \n3 for client side streaming\n4 for bidirectional streaming");
        int choice = new Scanner(System.in).nextInt();
        switch (choice){
            case 1:
                Client.getStudents(blockingStub, studentRequest);
                break;
            case 2:
                Client.getStudentsStream(nonBlockingStub, studentRequest);
                break;
            case 3:
                Client.sendUserStream(nonBlockingStub,studentRequest);
                break;
            case 4:
                Client.GetAndSendStudent(nonBlockingStub, studentRequest);
            default:
                logger.error("invalid choice enter again");
                main(args);
        }
        managedChannel.shutdown().awaitTermination(50, TimeUnit.SECONDS);
    }
}
