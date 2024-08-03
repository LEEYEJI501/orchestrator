package com.example.orchestrator.service.grpc;

import com.example.grpc.UserServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.example.grpc.*;

@Service
public class UserGrpcServiceImpl implements UserGrpcService {
    private final UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub;
    private final ManagedChannel channel;

    public UserGrpcServiceImpl(@Value("${user.grpc.host}") String grpcHost, @Value("${user.grpc.port}") int grpcPort) {
        this.channel = ManagedChannelBuilder.forAddress(grpcHost, grpcPort)
                .usePlaintext()
                .build();
        this.userServiceBlockingStub = UserServiceGrpc.newBlockingStub(channel);
    }

    @Override
    public Response registerUser(RegisterUserRequest registerUserRequest) {
        return userServiceBlockingStub.registerUser(registerUserRequest);
    }

    @Override
    public Response checkUsername(CheckUsernameRequest checkUsernameRequest) {
        return userServiceBlockingStub.checkUsername(checkUsernameRequest);
    }

    @Override
    public Response updatePassword(UpdatePasswordRequest updatePasswordRequest) {
        return userServiceBlockingStub.updatePassword(updatePasswordRequest);
    }

    @Override
    public Response findUser(FindUserRequest findUserRequest) {
        return userServiceBlockingStub.findUser(findUserRequest);
    }

    @Override
    public Response authenticateUser(AuthenticateUserRequest request) {
        return userServiceBlockingStub.authenticateUser(request);
    }

    @Override
    public Response updateProfile(UpdateProfileRequest updateProfileRequest) {
        return userServiceBlockingStub.updateProfile(updateProfileRequest);
    }
}
