package com.example.orchestrator.service;

import com.example.grpc.*;
import com.example.orchestrator.dto.LoginDto;
import com.example.orchestrator.dto.LogoutDto;
import com.example.orchestrator.service.grpc.AuthGrpcService;
import com.example.orchestrator.service.grpc.UserGrpcService;
import com.example.orchestrator.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserGrpcService userGrpcService;
    private final AuthGrpcService authGrpcService;
    private final JsonUtil jsonUtil;

    public Response login(LoginDto loginDto) {
        AuthenticateUserRequest request = AuthenticateUserRequest.newBuilder()
                .setUsername(loginDto.getUsername())
                .setPassword(loginDto.getPassword())
                .build();

        Response response = userGrpcService.authenticateUser(request);
        Map<String, Object> result = jsonUtil.getMapByKey(response.getResult(), "result");
        boolean authenticated = (boolean) result.get("authenticated");
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("authenticated", authenticated);

        if (authenticated) {
            long userId = Long.parseLong(result.get("userId").toString());
            GenerateAccessTokenRequest authRequest = GenerateAccessTokenRequest.newBuilder()
                .setUserId(userId)
                .build();

            Response authResponse = authGrpcService.generateAccessToken(authRequest);
            String accessToken = jsonUtil.getValueByKey(authResponse.getResult(), "accessToken");
            responseMap.put("accessToken", accessToken);
        }

        String jsonResponse = jsonUtil.toJson(responseMap);

        return Response.newBuilder()
                .setResult(jsonResponse)
                .build();
    }

    public Response logout(LogoutDto logoutDto) {
        LogoutRequest request = LogoutRequest.newBuilder()
                .setUserId(logoutDto.getUserId())
                .build();

        return authGrpcService.logout(request);
    }

    public Response refreshToken(Long userId) {
        RefreshTokenRequest request = RefreshTokenRequest.newBuilder()
                .setUserId(userId)
                .build();

        return authGrpcService.refreshToken(request);
    }
}
