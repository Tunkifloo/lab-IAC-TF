package com.makethediference.mtdapi.service;

import com.makethediference.mtdapi.domain.entity.*;

public class UserFactory {
    public static User createUser(Role role) {
        return switch (role) {
            case ADMIN -> Admin.builder().build();
            case MAKER -> Maker.builder().build();
            case COUNCIL -> Council.builder().build();
            case COORDINATOR -> Coordinator.builder().build();
            case DIRECTOR -> Director.builder().build();
        };
    }
}
