package com.ttalkak.user.user.application.port.out;


import com.ttalkak.user.user.domain.UserCreateEvent;

public interface UserCreatePort {
    void createUser(UserCreateEvent userCreateEvent);
}
