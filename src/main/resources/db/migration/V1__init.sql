CREATE SEQUENCE clients_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;






CREATE TABLE clients (
    id BIGINT PRIMARY KEY DEFAULT NEXTVAL('clients_id_seq') ,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL
);

ALTER SEQUENCE clients_id_seq OWNED BY clients.id;