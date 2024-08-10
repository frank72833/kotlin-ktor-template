CREATE TABLE IF NOT EXISTS accounts (
    id VARCHAR(50) NOT NULL,
    owner_name VARCHAR(100) NOT NULL,
    balance DECIMAL(65, 8) NOT NULL,
    currency_code VARCHAR(3) NOT NULL,
    created_date_time DATETIME(6) NOT NULL,
    updated_date_time DATETIME(6) NOT NULL,
    version BIGINT NOT NULL,
    CONSTRAINT pk_accounts_id PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;