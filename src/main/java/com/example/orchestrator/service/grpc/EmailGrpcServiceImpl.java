package com.example.orchestrator.service.grpc;

import com.example.grpc.EmailServiceGrpc;
import com.example.grpc.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailGrpcServiceImpl implements EmailGrpcService {
    private final EmailServiceGrpc.EmailServiceBlockingStub emailServiceBlockingStub;
    private final ManagedChannel channel;

    public EmailGrpcServiceImpl(@Value("${email.grpc.host}") String grpcHost, @Value("${email.grpc.port}") int grpcPort) {
        this.channel = ManagedChannelBuilder.forAddress(grpcHost, grpcPort)
                .usePlaintext()
                .build();
        this.emailServiceBlockingStub = EmailServiceGrpc.newBlockingStub(channel);
    }

    @Override
    public Response emailSend(EmailSendRequest emailSendRequest) {
        return emailServiceBlockingStub.emailSend(emailSendRequest);
    }

    @Override
    public Response verifyEmailCode(VerifyEmailCodeRequest verifyEmailCodeRequest) {
        return emailServiceBlockingStub.verifyEmailCode(verifyEmailCodeRequest);
    }
}
