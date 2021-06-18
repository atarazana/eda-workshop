# In production you would almost certainly limit the replication user must be on the follower (slave) machine,
# to prevent other clients accessing the log from other machines. For example, 'replicator'@'follower.acme.com'.
#
# However, this grant is equivalent to specifying *any* hosts, which makes this easier since the docker host
# is not easily known to the Docker container. But don't do this in production.
#
GRANT REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO 'replicator' IDENTIFIED BY 'replpass';
GRANT SELECT, RELOAD, SHOW DATABASES, REPLICATION SLAVE, REPLICATION CLIENT  ON *.* TO 'debezium' IDENTIFIED BY 'dbz';

# Create the database that we'll use to populate data and watch the effect in the binlog
CREATE DATABASE enterprise;
GRANT ALL PRIVILEGES ON enterprise.* TO 'mysqluser'@'%';

# Switch to this database
USE enterprise;

# Create and populate our regions using a single insert with many rows
CREATE TABLE regions (
  id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
  code VARCHAR(2) NOT NULL UNIQUE KEY,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(512)
);
ALTER TABLE regions AUTO_INCREMENT = 101;

# Create some clients
CREATE TABLE clients (
  id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
  first_name VARCHAR(255) NOT NULL,
  last_name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE KEY
);

# Create some fake accounts
CREATE TABLE accounts (
  id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
  client_id INTEGER NOT NULL,
  region_id INTEGER NOT NULL,
  region_code VARCHAR(2) NOT NULL,
  sequence VARCHAR(10) NOT NULL,
  status enum('ACTIVE','INACTIVE','CLOSED') NOT NULL,
  FOREIGN KEY account_client (client_id) REFERENCES clients(id),
  FOREIGN KEY account_region_id (region_id) REFERENCES regions(id),
  FOREIGN KEY account_region_code (region_code) REFERENCES regions(code)
) AUTO_INCREMENT = 10;

# Create some very simple movements
CREATE TABLE movements (
  account_id INTEGER NOT NULL,
  id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,  
  movement_date DATE NOT NULL,
  description VARCHAR(255) NOT NULL,
  quantity INTEGER NOT NULL,
  FOREIGN KEY movement_account (account_id) REFERENCES accounts(id)
) AUTO_INCREMENT = 10001;

# Populate database with initial data

# Create Regions

INSERT INTO regions
VALUES (default,"es","Spain","Spain"),
       (default,"pt","Portugal","Portugal"),
       (default,"uk","United Kingdom","United Kingdom"),
       (default,"it","Italy","Italy"),
       (default,"fr","France","France"),
       (default,"ge","Germany","Germany"),
       (default,"us","United States of America","United States of America");

# Update Regions

UPDATE regions SET description = 'ES' WHERE code = 'es';
UPDATE regions SET description = 'PT' WHERE code = 'pt';
UPDATE regions SET description = 'UK' WHERE code = 'uk';
UPDATE regions SET description = 'IT' WHERE code = 'it';
UPDATE regions SET description = 'FR' WHERE code = 'fr';
UPDATE regions SET description = 'GE' WHERE code = 'ge';
UPDATE regions SET description = 'US' WHERE code = 'us';

# Update Regions (again)

UPDATE regions SET description = 'Spain' WHERE code = 'es';
UPDATE regions SET description = 'Portugal' WHERE code = 'pt';
UPDATE regions SET description = 'United Kingdom' WHERE code = 'uk';
UPDATE regions SET description = 'Italy' WHERE code = 'it';
UPDATE regions SET description = 'France' WHERE code = 'fr';
UPDATE regions SET description = 'Germany' WHERE code = 'ge';
UPDATE regions SET description = 'United States of America' WHERE code = 'us';
