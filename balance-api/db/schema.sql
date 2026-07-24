CREATE TABLE balance (
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    account_id  BIGINT        NOT NULL,
    type        VARCHAR(20)   NOT NULL,
    amount      NUMERIC(19,4) NOT NULL DEFAULT 0,
    updated_at  TIMESTAMP     NOT NULL DEFAULT now(),
    CONSTRAINT uq_balance_account_type UNIQUE (account_id, type)
);

-- Conta de exemplo com saldo em conta e saldo do limite especial
INSERT INTO balance (account_id, type, amount) VALUES
    (1, 'CONTA', 1000.00),
    (1, 'LIMITE_ESPECIAL', 500.00);
