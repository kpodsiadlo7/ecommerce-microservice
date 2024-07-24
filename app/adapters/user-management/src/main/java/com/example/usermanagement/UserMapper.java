package com.example.usermanagement;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserMapper {

    UserRecord mapToDto(User user){
        return new UserRecord(
                user.getUniqueUserId(),
                user.getRoles().toString()
        );
    }

    List<UserRecord> mapToDtoList(List<User> users){
        return users.stream().map(this::mapToDto).toList();
    }
}
