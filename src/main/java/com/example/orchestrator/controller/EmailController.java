package com.example.orchestrator.controller;
import com.example.orchestrator.dto.EmailSendDto;
import com.example.orchestrator.dto.EmailVerifyCodeDto;
import com.example.orchestrator.service.EmailService;
import com.example.orchestrator.util.GrpcResponseHelper;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.grpc.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
public class EmailController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final EmailService emailService;
    private final GrpcResponseHelper grpcResponseHelper;

    @Autowired
    public EmailController(EmailService emailService, GrpcResponseHelper grpcResponseHelper) {
        this.emailService = emailService;
        this.grpcResponseHelper = grpcResponseHelper;
    }

    @PostMapping(value = "/send", consumes = "application/json")
    public ResponseEntity<String> emailSend(@Valid @RequestBody EmailSendDto emailSendDto) {
        Response response = emailService.emailSend(emailSendDto);
        return grpcResponseHelper.createJsonResponse(response);
    }

    @PostMapping(value = "/verify", consumes = "application/json")
    public ResponseEntity<String> verifyEmailCode(@Valid @RequestBody EmailVerifyCodeDto emailVerifyCodeDto) {
        Response response = emailService.verifyEmailCode(emailVerifyCodeDto);
        return grpcResponseHelper.createJsonResponse(response);
    }
}
