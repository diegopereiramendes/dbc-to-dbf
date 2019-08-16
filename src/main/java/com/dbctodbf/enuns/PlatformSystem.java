package com.dbctodbf.enuns;

public enum PlatformSystem {

    WINDOWS("windows"), LINUX("linux");

    private final String name;

    PlatformSystem(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }

    public String getName() {
        return name;
    }
}
