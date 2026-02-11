CREATE TABLE users (
  id CHAR(36) NOT NULL,
  email VARCHAR(255) NOT NULL,
  created_at TIMESTAMP NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_users_email (email)
);

CREATE TABLE accounts (
  id CHAR(36) NOT NULL,
  user_id CHAR(36) NOT NULL,
  name VARCHAR(100) NOT NULL,
  currency CHAR(3) NOT NULL,
  created_at TIMESTAMP NOT NULL,
  PRIMARY KEY (id),
  KEY idx_accounts_user_id (user_id),
  CONSTRAINT fk_accounts_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE categories (
  id CHAR(36) NOT NULL,
  user_id CHAR(36) NOT NULL,
  name VARCHAR(100) NOT NULL,
  type VARCHAR(20) NOT NULL,
  PRIMARY KEY (id),
  KEY idx_categories_user_id (user_id),
  CONSTRAINT fk_categories_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE transactions (
  id CHAR(36) NOT NULL,
  account_id CHAR(36) NOT NULL,
  category_id CHAR(36) NULL,
  amount DECIMAL(19,2) NOT NULL,
  occurred_at TIMESTAMP NOT NULL,
  note VARCHAR(255),
  PRIMARY KEY (id),
  KEY idx_transactions_account_id (account_id),
  KEY idx_transactions_category_id (category_id),
  CONSTRAINT fk_transactions_account FOREIGN KEY (account_id) REFERENCES accounts(id),
  CONSTRAINT fk_transactions_category FOREIGN KEY (category_id) REFERENCES categories(id)
);
