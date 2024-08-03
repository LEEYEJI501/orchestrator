package com.example.orchestrator.service.grpc;
import com.example.grpc.*;

public interface EmailGrpcService {
    Response emailSend(EmailSendRequest emailSendRequest);
    Response verifyEmailCode(VerifyEmailCodeRequest verifyEmailCodeRequest);
}
