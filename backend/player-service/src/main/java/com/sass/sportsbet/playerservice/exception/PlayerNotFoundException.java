package com.sass.sportsbet.playerservice.exception;

public class PlayerNotFoundException extends RuntimeException {
    public PlayerNotFoundException(String name) {
        super("Player with name '" + name + "' not found");
    }
}
