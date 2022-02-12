CREATE TABLE IF NOT EXISTS customers  (
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    name varchar(255) NOT NULL,
    surname varchar(255) NOT NULL,
    photo_url varchar,
    created_by  uuid NOT NULL DEFAULT uuid_generate_v4(),
    updated_by  uuid DEFAULT uuid_generate_v4(),
    deleted_at timestamp,
    PRIMARY KEY(id)
    );

ALTER TABLE customers
    ADD CONSTRAINT customer_created_by_id
        FOREIGN KEY(created_by)
            REFERENCES users(id);

ALTER TABLE customers
    ADD CONSTRAINT customer_updated_by_id
        FOREIGN KEY(updated_by)
            REFERENCES users(id);

