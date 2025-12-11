
package com.sass.sportsbet.playerservice.exception;

public class PlayerAlreadyExistsException extends RuntimeException {
    public PlayerAlreadyExistsException(String name) {
        super("Player with name '" + name + "' already exists");
    }
}
