package com.example.orchestrator.service;

import com.example.grpc.*;
import com.example.orchestrator.dto.EmailSendDto;
import com.example.orchestrator.dto.EmailVerifyCodeDto;
import com.example.orchestrator.service.grpc.EmailGrpcService;
import com.example.orchestrator.service.grpc.UserGrpcService;
import com.example.orchestrator.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final EmailGrpcService emailGrpcService;

    public Response emailSend(EmailSendDto emailSendDto) {
        EmailSendRequest request = EmailSendRequest.newBuilder()
                .setEmail(emailSendDto.getEmail())
                .build();

        return emailGrpcService.emailSend(request);
    }

    public Response verifyEmailCode(EmailVerifyCodeDto emailVerifyCodeDto) {
        VerifyEmailCodeRequest request = VerifyEmailCodeRequest.newBuilder()
                .setEmail(emailVerifyCodeDto.getEmail())
                .setCode(emailVerifyCodeDto.getCode())
                .build();

        return emailGrpcService.verifyEmailCode(request);
    }
}
