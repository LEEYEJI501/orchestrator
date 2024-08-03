package com.example.orchestrator.service;

import com.example.grpc.*;
import com.example.orchestrator.dto.*;
import com.example.orchestrator.service.grpc.AuthGrpcService;
import com.example.orchestrator.service.grpc.UserGrpcService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserGrpcService userGrpcService;
    private final AuthGrpcService authGrpcService;
    private final FileUploadService fileUploadService;

    public Response registerUser(UserDto userDto, MultipartFile profileImage) {
        ImageDto imageDto = fileUploadService.storeFile(profileImage);

        Image image = Image.newBuilder()
            .setPath(imageDto.getUploadPath())
            .setOriginalFilename(imageDto.getOriginalFilename())
            .setFileExtension(imageDto.getFileExtension())
            .setDescription("Profile image for user " + userDto.getUsername())
            .build();

        Profile profile = Profile.newBuilder()
            .setGreeting(userDto.getProfile().getGreeting())
            .setImage(image)
            .build();

        RegisterUserRequest request = RegisterUserRequest.newBuilder()
            .setUsername(userDto.getUsername())
            .setPassword(userDto.getPassword())
            .setEmail(userDto.getEmail())
            .setProfile(profile)
            .build();

        return userGrpcService.registerUser(request);
    }

    public Response checkUsername(String username) {
        CheckUsernameRequest request = CheckUsernameRequest.newBuilder()
                .setUsername(username)
                .build();
        return userGrpcService.checkUsername(request);
    }

    public Response updatePassword(PasswordUpdateDto passwordUpdateDto) {
        UpdatePasswordRequest request = UpdatePasswordRequest.newBuilder()
                .setUserId(passwordUpdateDto.getUserId())
                .setCurrentPassword(passwordUpdateDto.getCurrentPassword())
                .setNewPassword(passwordUpdateDto.getNewPassword())
                .build();

        LogoutRequest logoutRequest = LogoutRequest.newBuilder()
                .setUserId(passwordUpdateDto.getUserId())
                .build();

        Response response = userGrpcService.updatePassword(request);
        authGrpcService.logout(logoutRequest);

        return response;
    }

    public Response findUser(FindUserDto findUserDto) {
        FindUserRequest request = FindUserRequest.newBuilder()
                .setUserId(findUserDto.getUserId())
                .build();

        return userGrpcService.findUser(request);
    }

    public Response authenticateUser(String username, String password) {
        AuthenticateUserRequest request = AuthenticateUserRequest.newBuilder()
                .setUsername(username)
                .setPassword(password)
                .build();
        return userGrpcService.authenticateUser(request);
    }

    public Response updateProfile(UpdateProfileDto updateProfileDto, MultipartFile profileImage) {
        ImageDto imageDto = fileUploadService.storeFile(profileImage);

        Image image = Image.newBuilder()
            .setPath(imageDto.getUploadPath())
            .setOriginalFilename(imageDto.getOriginalFilename())
            .setFileExtension(imageDto.getFileExtension())
            .setDescription("Updated profile image for user " + updateProfileDto.getUserId())
            .build();

        Profile profile = Profile.newBuilder()
            .setGreeting(updateProfileDto.getGreeting())
            .setImage(image)
            .build();

        UpdateProfileRequest request = UpdateProfileRequest.newBuilder()
            .setUserId(updateProfileDto.getUserId())
            .setProfile(profile)
            .build();

        return userGrpcService.updateProfile(request);
    }
}
