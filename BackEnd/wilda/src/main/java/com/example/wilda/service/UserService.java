package com.example.wilda.service;

import com.example.wilda.entity.User;
import com.example.wilda.model.request.ChangePasswordRequest;
import com.example.wilda.model.request.CreateUserRequest;
import com.example.wilda.model.request.UpdateProfileRequest;

public interface UserService {
    
    void register(CreateUserRequest request);


    User getUserByUsername(String username);

    User updateUser(UpdateProfileRequest request);

    void changePassword(ChangePasswordRequest request);

}
