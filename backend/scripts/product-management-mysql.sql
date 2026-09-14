-- Run against the existing StylePin database BEFORE starting the updated backend.
-- MySQL 8+. Each statement is safe to repeat. No rows or tables are removed.
-- Keep the application stopped while applying schema changes.
USE stylepin;

SET @stylepin_ddl = IF(EXISTS(SELECT 1 FROM information_schema.columns
  WHERE table_schema=DATABASE() AND table_name='users' AND column_name='role'),
  'SELECT 1', 'ALTER TABLE users ADD COLUMN role VARCHAR(20) NOT NULL DEFAULT ''USER''');
PREPARE stylepin_stmt FROM @stylepin_ddl;
EXECUTE stylepin_stmt;
DEALLOCATE PREPARE stylepin_stmt;

SET @stylepin_ddl = IF(EXISTS(SELECT 1 FROM information_schema.columns
  WHERE table_schema=DATABASE() AND table_name='products' AND column_name='retailer'),
  'SELECT 1', 'ALTER TABLE products ADD COLUMN retailer VARCHAR(100) NULL');
PREPARE stylepin_stmt FROM @stylepin_ddl;
EXECUTE stylepin_stmt;
DEALLOCATE PREPARE stylepin_stmt;

SET @stylepin_ddl = IF(EXISTS(SELECT 1 FROM information_schema.columns
  WHERE table_schema=DATABASE() AND table_name='products' AND column_name='available'),
  'SELECT 1', 'ALTER TABLE products ADD COLUMN available BOOLEAN NOT NULL DEFAULT TRUE');
PREPARE stylepin_stmt FROM @stylepin_ddl;
EXECUTE stylepin_stmt;
DEALLOCATE PREPARE stylepin_stmt;

SET @stylepin_ddl = IF(EXISTS(SELECT 1 FROM information_schema.columns
  WHERE table_schema=DATABASE() AND table_name='products' AND column_name='outfit_id' AND is_nullable='NO'),
  'ALTER TABLE products MODIFY COLUMN outfit_id BIGINT NULL', 'SELECT 1');
PREPARE stylepin_stmt FROM @stylepin_ddl;
EXECUTE stylepin_stmt;
DEALLOCATE PREPARE stylepin_stmt;

CREATE TABLE IF NOT EXISTS product_tags (
  product_id BIGINT NOT NULL,
  tag VARCHAR(50) NOT NULL,
  PRIMARY KEY (product_id, tag),
  CONSTRAINT fk_product_tags_product FOREIGN KEY (product_id) REFERENCES products(id)
);
