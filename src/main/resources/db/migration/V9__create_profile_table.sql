alter table users drop column if exists rating;

create table user_profile(
    id bigint primary key,
    user_id bigint references users(id) on delete cascade not null,
    avatar_url text,
    rating numeric(3,2),
    bio text
);