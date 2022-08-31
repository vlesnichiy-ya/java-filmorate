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

CREATE TABLE "Film" (
  "id" long PRIMARY KEY,
  "description" varchar,
  "release_date" date,
  "duration" int,
  "rating" int,
  "rating_mpa" rating_mpa,
  "genre" int
);

CREATE TABLE "genre" (
  "id" int PRIMARY KEY,
  "genre" string
);

CREATE TABLE "Likes" (
  "film_id" long,
  "user_id" long
);

CREATE TABLE "User" (
  "id" long PRIMARY KEY,
  "email" string,
  "login" string,
  "name" string,
  "birth_date" date
);

CREATE TABLE "Friends" (
  "user_id" long,
  "friend_id" long,
  "status" friendship
);

CREATE TABLE "next_id" (
  "user_next_id" long,
  "film_next_id" long
);

COMMENT ON TABLE "Likes" IS 'Connection table for store film rating';

COMMENT ON TABLE "Friends" IS 'Connection table for store friendship records';

COMMENT ON TABLE "next_id" IS 'Stores next IDs of film and user ';

ALTER TABLE "Film" ADD FOREIGN KEY ("genre") REFERENCES "genre" ("id");

CREATE TABLE "Film_Likes" (
  "Film_id" long NOT NULL,
  "Likes_film_id" long NOT NULL,
  PRIMARY KEY ("Film_id", "Likes_film_id")
);

ALTER TABLE "Film_Likes" ADD FOREIGN KEY ("Film_id") REFERENCES "Film" ("id");

ALTER TABLE "Film_Likes" ADD FOREIGN KEY ("Likes_film_id") REFERENCES "Likes" ("film_id");


CREATE TABLE "User_Likes" (
  "User_id" long NOT NULL,
  "Likes_user_id" long NOT NULL,
  PRIMARY KEY ("User_id", "Likes_user_id")
);

ALTER TABLE "User_Likes" ADD FOREIGN KEY ("User_id") REFERENCES "User" ("id");

ALTER TABLE "User_Likes" ADD FOREIGN KEY ("Likes_user_id") REFERENCES "Likes" ("user_id");


ALTER TABLE "Friends" ADD FOREIGN KEY ("user_id") REFERENCES "User" ("id");

CREATE TABLE "User_Friends" (
  "User_id" long NOT NULL,
  "Friends_friend_id" long NOT NULL,
  PRIMARY KEY ("User_id", "Friends_friend_id")
);

ALTER TABLE "User_Friends" ADD FOREIGN KEY ("User_id") REFERENCES "User" ("id");

ALTER TABLE "User_Friends" ADD FOREIGN KEY ("Friends_friend_id") REFERENCES "Friends" ("friend_id");

