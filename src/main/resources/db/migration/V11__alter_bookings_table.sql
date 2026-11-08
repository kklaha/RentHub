CREATE EXTENSION IF NOT EXISTS btree_gist;

ALTER TABLE bookings ADD PRIMARY KEY (id);

ALTER TABLE bookings DROP CONSTRAINT check_order_dates;

ALTER TABLE bookings
    ALTER COLUMN item_id SET NOT NULL,
    ALTER COLUMN renter_id SET NOT NULL,
    ADD CONSTRAINT check_booking_dates CHECK (end_time > start_time),
    ADD CONSTRAINT check_booking_status
        CHECK (status IN ('CREATED', 'CONFIRMED', 'CANCELED', 'REJECTED')),
    ADD CONSTRAINT no_overlapping_confirmed_bookings
        EXCLUDE USING gist (
        item_id WITH =,
        tstzrange(start_time, end_time) WITH &&
        ) WHERE (status = 'CONFIRMED');

CREATE INDEX idx_bookings_renter_id ON bookings(renter_id);

alter table bookings rename column rentprovider_id to provider_id;