package com.klaha.renthub.enums;

public enum OrderStatus {
    CREATED,
    CONFIRMED,
    PAID,
    REJECTED,
    COMPLETED,
    CANCELED;

    public boolean canMoveTo(OrderStatus next) {
        return switch (this) {
            case CREATED -> next == CANCELED || next == CONFIRMED || next == REJECTED;
            case CONFIRMED -> next == PAID || next == CANCELED || next == COMPLETED;
            case PAID -> next == COMPLETED;
            default -> false;
        };
    }

}
