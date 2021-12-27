package com.lolzdev.fabriccomputers.computer;

public class Event {
    public String name;
    public Object[] args;

    public Event(String name, Object[] args) {
        this.name = name;
        this.args = args;
    }
}
