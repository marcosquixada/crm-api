CREATE TABLE IF NOT EXISTS refreshtoken  (
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    expiry_date	timestamp NOT NULL,
    token      	varchar(255) NOT NULL,
    user_id  uuid NOT NULL DEFAULT uuid_generate_v4(),
    PRIMARY KEY(id)
    );


CREATE TABLE IF NOT EXISTS roles  (
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    name	varchar(20) NULL,
    PRIMARY KEY(id)
    );

ALTER TABLE users
    ADD CONSTRAINT users_username
        UNIQUE (username);

ALTER TABLE users
    ADD CONSTRAINT users_email
        UNIQUE (email);

CREATE TABLE IF NOT EXISTS user_roles  (
    user_id uuid NOT NULL DEFAULT uuid_generate_v4(),
    role_id uuid NOT NULL DEFAULT uuid_generate_v4(),
    PRIMARY KEY(user_id,role_id)
    );

ALTER TABLE user_roles
    ADD CONSTRAINT user_roles_user_id
        FOREIGN KEY(user_id)
            REFERENCES users(id);

ALTER TABLE user_roles
    ADD CONSTRAINT user_roles_role_id
        FOREIGN KEY(role_id)
            REFERENCES roles(id);

ALTER TABLE refreshtoken
    ADD CONSTRAINT refreshtoken_token
        UNIQUE (token);

ALTER TABLE refreshtoken
    ADD CONSTRAINT refreshtoken_user
        FOREIGN KEY(user_id)
            REFERENCES users(id);

INSERT INTO roles (id, name) VALUES ('0d12a4a3-ad64-4662-b367-92f93cce8a40', 'ROLE_ADMIN');
INSERT INTO roles (id, name) VALUES ('7515ce9e-a8ff-43e8-9902-4e47be82c980', 'ROLE_USER');
INSERT INTO roles (id, name) VALUES ('b1c178f1-919f-4f00-b84f-2186b22b28c3', 'ROLE_MODERATOR');

