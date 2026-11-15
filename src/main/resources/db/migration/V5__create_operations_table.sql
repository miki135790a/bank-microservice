CREATE SEQUENCE operations_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


CREATE TABLE operations (
    id BIGINT PRIMARY KEY DEFAULT nextval('operations_id_seq'),
    from_account_id BIGINT,
    to_account_id BIGINT,
    amount NUMERIC(19, 2) NOT NULL,
    type VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    failure_reason VARCHAR(255),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,

    CONSTRAINT fk_op_from FOREIGN KEY (from_account_id) REFERENCES accounts(id) ON DELETE RESTRICT,
    CONSTRAINT fk_op_to FOREIGN KEY (to_account_id) REFERENCES accounts(id) ON DELETE RESTRICT,


    CONSTRAINT chk_op_at_least_one_account CHECK (from_account_id IS NOT NULL OR to_account_id IS NOT NULL),
    CONSTRAINT chk_op_amount_positive CHECK (amount > 0)
);

ALTER SEQUENCE operations_id_seq OWNED BY operations.id;


CREATE INDEX idx_operations_from_account ON operations(from_account_id);
CREATE INDEX idx_operations_to_account ON operations(to_account_id);
CREATE INDEX idx_operations_status ON operations(status);