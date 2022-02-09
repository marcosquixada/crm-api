SET search_path TO public;
DROP EXTENSION IF EXISTS "uuid-ossp";

CREATE EXTENSION "uuid-ossp" SCHEMA public;

CREATE TABLE IF NOT EXISTS  users  (
    id uuid     NOT NULL DEFAULT uuid_generate_v4(),
    email   	varchar(50) NULL,
    password	varchar(120) NULL,
    username	varchar(20) NULL,
    PRIMARY KEY(id)
);