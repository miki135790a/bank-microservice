CREATE SEQUENCE outbox_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE outbox (
    id BIGINT PRIMARY KEY DEFAULT nextval('outbox_id_seq'),
    aggregate_type VARCHAR(50) NOT NULL,
    aggregate_id VARCHAR(255) NOT NULL,
    event_type VARCHAR(100) NOT NULL,
    payload TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    processed BOOLEAN NOT NULL DEFAULT FALSE,
    processed_at TIMESTAMP
);

ALTER SEQUENCE outbox_id_seq OWNED BY outbox.id;


CREATE INDEX idx_outbox_unprocessed ON outbox(created_at) WHERE processed = FALSE;