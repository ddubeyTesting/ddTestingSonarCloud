package com.zemoso.figma.growthcapital.util;

public enum StatusType {
    PENDING("Pending"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    UPCOMING("Upcoming"),
    DONE("Done");

    private final String name;

    StatusType(String name) {
        this.name = name;
    }
}
