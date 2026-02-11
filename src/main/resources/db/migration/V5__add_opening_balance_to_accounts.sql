ALTER TABLE accounts
  ADD COLUMN opening_balance DECIMAL(19,2) NOT NULL DEFAULT 0.00
  AFTER currency;