package com.project.btoproject.enums;

public enum Hour {
    NINE,
    ELEVEN,
    THIRTEEN_THIRTY,
    SIXTEEN;

    public String toFormattedTime() {
        if (this == NINE) {
            return "9:00";
        } else if (this == ELEVEN) {
            return "11:00";
        } else if (this == THIRTEEN_THIRTY) {
            return "13:30";
        } else if (this == SIXTEEN) {
            return "16:00";
        } else {
            throw new IllegalArgumentException("Unknown Hour: " + this);
        }
    }
}