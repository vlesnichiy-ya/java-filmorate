CREATE TYPE "rating_mpa" AS ENUM (
  'G',
  'PG',
  'PG_13',
  'R',
  'NC_17'
);

CREATE TYPE "friendship" AS ENUM (
  'asked',
  'confirmed'
);

CREATE TABLE "films" (
  "id" long PRIMARY KEY,
  "description" varchar,
  "release_date" date,
  "duration" int,
  "rating" int,
  "rating_mpa" rating_mpa,
  "genres" int
);

CREATE TABLE "films_genres" (
  "link_id" int PRIMARY KEY,
  "film_id" long,
  "genre_id" int
);

CREATE TABLE "genres" (
  "id" int PRIMARY KEY,
  "genre" varchar
);

CREATE TABLE "likes" (
  "like_id" long PRIMARY KEY,
  "film_id" long,
  "user_id" long
);

CREATE TABLE "users" (
  "id" long PRIMARY KEY,
  "email" varchar,
  "login" varchar,
  "name" varchar,
  "birth_date" date
);

CREATE TABLE "friends" (
  "user_id" long,
  "friend_id" long,
  "status" friendship
);

COMMENT ON TABLE "films_genres" IS 'Connection table for store films and genres links';

COMMENT ON TABLE "likes" IS 'Connection table for store film rating';

COMMENT ON TABLE "friends" IS 'Connection table for store friendship records';

CREATE TABLE "films_likes" (
  "films_id" long NOT NULL,
  "likes_film_id" long NOT NULL,
  PRIMARY KEY ("films_id", "likes_film_id")
);

ALTER TABLE "films_likes" ADD FOREIGN KEY ("films_id") REFERENCES "films" ("id");

ALTER TABLE "films_likes" ADD FOREIGN KEY ("likes_film_id") REFERENCES "likes" ("film_id");


CREATE TABLE "users_likes" (
  "users_id" long NOT NULL,
  "likes_user_id" long NOT NULL,
  PRIMARY KEY ("users_id", "likes_user_id")
);

ALTER TABLE "users_likes" ADD FOREIGN KEY ("users_id") REFERENCES "users" ("id");

ALTER TABLE "users_likes" ADD FOREIGN KEY ("likes_user_id") REFERENCES "likes" ("user_id");


ALTER TABLE "friends" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");

CREATE TABLE "users_friends" (
  "users_id" long NOT NULL,
  "friends_friend_id" long NOT NULL,
  PRIMARY KEY ("users_id", "friends_friend_id")
);

ALTER TABLE "users_friends" ADD FOREIGN KEY ("users_id") REFERENCES "users" ("id");

ALTER TABLE "users_friends" ADD FOREIGN KEY ("friends_friend_id") REFERENCES "friends" ("friend_id");


ALTER TABLE "films_genres" ADD FOREIGN KEY ("film_id") REFERENCES "films" ("id");

ALTER TABLE "films_genres" ADD FOREIGN KEY ("genre_id") REFERENCES "genres" ("id");
