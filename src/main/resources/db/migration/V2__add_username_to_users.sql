alter table users
add username varchar(32) not null default 'user' || nextval(pg_get_serial_sequence('users', 'id'))
    constraint uk_users_username unique;