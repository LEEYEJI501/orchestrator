package com.example.orchestrator.service.grpc;

import com.example.grpc.AuthServiceGrpc;
import com.example.grpc.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthGrpcServiceImpl implements AuthGrpcService {
    private final AuthServiceGrpc.AuthServiceBlockingStub authServiceBlockingStub;
    private final ManagedChannel channel;

    public AuthGrpcServiceImpl(@Value("${auth.grpc.host}") String grpcHost, @Value("${auth.grpc.port}") int grpcPort) {
        this.channel = ManagedChannelBuilder.forAddress(grpcHost, grpcPort)
                .usePlaintext()
                .build();
        this.authServiceBlockingStub = AuthServiceGrpc.newBlockingStub(channel);
    }

    @Override
    public Response generateAccessToken(GenerateAccessTokenRequest generateAccessTokenRequest) {
        return authServiceBlockingStub.generateAccessToken(generateAccessTokenRequest);
    }

    @Override
    public Response logout(LogoutRequest logoutRequest) {
        return authServiceBlockingStub.logout(logoutRequest);
    }

    @Override
    public Response refreshToken(RefreshTokenRequest refreshTokenRequest) {
        return authServiceBlockingStub.refreshToken(refreshTokenRequest);
    }
}
