package com.example.orchestrator.service.grpc;

import com.example.grpc.*;

public interface AuthGrpcService {
    Response generateAccessToken(GenerateAccessTokenRequest generateAccessTokenRequest);
    Response logout(LogoutRequest logoutRequest);
    Response refreshToken(RefreshTokenRequest refreshTokenRequest);
}
