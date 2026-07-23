package com.myvamsnet.monpa.mapper;

import com.myvamsnet.monpa.dto.user.UserResponse;
import com.myvamsnet.monpa.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toUserResponse(User user) {
        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setCountryName(user.getCountryName());

        return response;
    }
}
