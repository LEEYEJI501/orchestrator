package com.example.orchestrator.service;

import com.example.orchestrator.dto.FindUserDto;
import com.example.orchestrator.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.grpc.*;

import java.util.Collections;
import java.util.Map;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private JsonUtil jsonUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        throw new UnsupportedOperationException("loadUserByUsername is not supported. Use loadUserByUserId instead.");
    }

    public UserDetails loadUserByUserId(Long userId) throws UsernameNotFoundException {
        FindUserDto findUserDto = new FindUserDto();
        findUserDto.setUserId(userId);

        Response response = userService.findUser(findUserDto);
        String result = response.getResult();

        Map<String, Object> resultMap = jsonUtil.getMapByKey(result, "user");
        if (resultMap == null) {
            throw new UsernameNotFoundException("User not found with id: " + userId);
        }

        String username = (String) resultMap.get("username");
        String password = (String) resultMap.get("password");

        return new User(username, password, Collections.emptyList());
    }
}
