CREATE SEQUENCE accounts_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


CREATE TABLE accounts (
    id BIGINT PRIMARY KEY DEFAULT NEXTVAL('accounts_id_seq') ,
    client_id BIGINT NOT NULL,
    number VARCHAR(20) NOT NULL UNIQUE,
    balance NUMERIC(19, 2) NOT NULL DEFAULT 0.00,
    currency VARCHAR(3) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL,


    CONSTRAINT fk_account_client
        FOREIGN KEY (client_id)
        REFERENCES clients(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

ALTER SEQUENCE accounts_id_seq OWNED BY clients.id;
