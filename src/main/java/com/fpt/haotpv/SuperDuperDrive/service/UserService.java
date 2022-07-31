package com.fpt.haotpv.SuperDuperDrive.service;

import com.fpt.haotpv.SuperDuperDrive.entity.User;

public interface UserService {

    Boolean isUsernameAvailable(String username);

    Integer createUser(User user);

    User getUser(String username);
}
