package com.fpt.haotpv.SuperDuperDrive.service.impl;

import com.fpt.haotpv.SuperDuperDrive.entity.User;
import com.fpt.haotpv.SuperDuperDrive.mapper.UserMapper;
import com.fpt.haotpv.SuperDuperDrive.service.HashService;
import com.fpt.haotpv.SuperDuperDrive.service.UserService;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final HashService hashService;

    public UserServiceImpl(UserMapper userMapper,
                           HashService hashService) {
        this.userMapper = userMapper;
        this.hashService = hashService;
    }

    @Override
    public Boolean isUsernameAvailable(String username) {

        return userMapper.getUser(username) == null;
    }

    @Override
    public Integer createUser(User user) {

        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String hashedPassword = hashService.getHashedValue(user.getPassword(), encodedSalt);
        user = new User(user.getUsername(), encodedSalt, hashedPassword, user.getFirstName(), user.getLastName());

        return userMapper.insert(user);
    }

    @Override
    public User getUser(String username) {

        return userMapper.getUser(username);
    }
}
