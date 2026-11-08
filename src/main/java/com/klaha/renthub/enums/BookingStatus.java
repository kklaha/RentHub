package com.klaha.renthub.enums;

public enum BookingStatus {
    CREATED,
    CONFIRMED,
    PAID,
    REJECTED,
    CANCELED,
    ENDED;
    public boolean canMoveTo(BookingStatus next) {
        return switch (this) {
            case CREATED -> next == CANCELED || next == CONFIRMED || next == REJECTED;
            case CONFIRMED -> next == PAID || next == CANCELED || next == ENDED;
            case PAID -> next == ENDED;
            default -> false;
        };
    }
}
