package com.mine.java.transport.entity;

public class Dog {
    private String name;

    public Dog(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getObjectAddress() {
        return super.toString();
    }
}
