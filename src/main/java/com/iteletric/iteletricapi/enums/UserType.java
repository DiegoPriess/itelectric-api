package com.iteletric.iteletricapi.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.*;

@Getter
public enum UserType {

    ADM("Administrador"),
    CLIENT("Cliente");

    private final String displayName;

    UserType(String displayName) {
        this.displayName = displayName;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static synchronized UserType create(final HashMap<?, ?> value) {
        return UserType.getByName(value.get("name").toString());
    }

    public static synchronized UserType getByName(final String name) {
        return Arrays.stream(UserType.values()).filter(filter -> filter.name().equals(name)).findFirst().orElse(null);
    }
}