CREATE TABLE IF NOT EXISTS transactions (
    id VARCHAR(50) NOT NULL,
    from_account_id VARCHAR(50) NOT NULL,
    to_account_id VARCHAR(50) NOT NULL,
    amount DECIMAL(65, 8) NOT NULL,
    currency_code VARCHAR(3) NOT NULL,
    business_date_time DATETIME(6) NOT NULL,
    created_date_time DATETIME(6) NOT NULL,
    CONSTRAINT pk_transactions_id PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

ALTER TABLE transactions
  ADD INDEX transactions_from_account_time_index (from_account_id, business_date_time);

ALTER TABLE transactions
  ADD INDEX transactions_to_account_time_index (to_account_id, business_date_time);