
-- Switch to this database
USE enterprise;

-- Populate database with initial data

-- Create Regions

INSERT INTO regions
VALUES (default,"es","Spain","Spain"),
       (default,"pt","Portugal","Portugal"),
       (default,"uk","United Kingdom","United Kingdom"),
       (default,"it","Italy","Italy"),
       (default,"fr","France","France"),
       (default,"ge","Germany","Germany"),
       (default,"us","United States of America","United States of America");

/*
-- Update Regions

UPDATE regions SET description = 'ES' WHERE code = 'es';
UPDATE regions SET description = 'PT' WHERE code = 'pt';
UPDATE regions SET description = 'UK' WHERE code = 'uk';
UPDATE regions SET description = 'IT' WHERE code = 'it';
UPDATE regions SET description = 'FR' WHERE code = 'fr';
UPDATE regions SET description = 'GE' WHERE code = 'ge';
UPDATE regions SET description = 'US' WHERE code = 'us';

-- Update Regions (again)

UPDATE regions SET description = 'Spain' WHERE code = 'es';
UPDATE regions SET description = 'Portugal' WHERE code = 'pt';
UPDATE regions SET description = 'United Kingdom' WHERE code = 'uk';
UPDATE regions SET description = 'Italy' WHERE code = 'it';
UPDATE regions SET description = 'France' WHERE code = 'fr';
UPDATE regions SET description = 'Germany' WHERE code = 'ge';
UPDATE regions SET description = 'United States of America' WHERE code = 'us';
*/

--
-- Sample Data
--

-- Customers with some accounts and movements

INSERT INTO clients
VALUES (default,"George","Bailey","george.bailey@acme.com");

INSERT INTO accounts
VALUES (default,(select id from clients where email = 'george.bailey@acme.com'),(select id from regions where code = 'es'),'es','0000000001','ACTIVE'),
       (default,(select id from clients where email = 'george.bailey@acme.com'),(select id from regions where code = 'es'),'es','0000000002','ACTIVE'),
       (default,(select id from clients where email = 'george.bailey@acme.com'),(select id from regions where code = 'pt'),'pt','0000000001','ACTIVE'),
       (default,(select id from clients where email = 'george.bailey@acme.com'),(select id from regions where code = 'pt'),'pt','0000000002','INACTIVE');

INSERT INTO clients
VALUES (default,"Edward","Walker","edward.walker@acme.com");

INSERT INTO accounts
VALUES (default,(select id from clients where email = 'edward.walker@acme.com'),(select id from regions where code = 'uk'),'uk','0000000001','CLOSED'),
       (default,(select id from clients where email = 'edward.walker@acme.com'),(select id from regions where code = 'uk'),'uk','0000000002','ACTIVE'),
       (default,(select id from clients where email = 'edward.walker@acme.com'),(select id from regions where code = 'it'),'it','0000000001','ACTIVE');
      
INSERT INTO movements
VALUES ((select a.id from accounts a, clients c where a.client_id = c.id and email = 'edward.walker@acme.com' and region_code = 'uk' and sequence = '0000000001'), default, '2018-06-20 06:37:03', 'Incoming Transfer', 1000),
       ((select a.id from accounts a, clients c where a.client_id = c.id and email = 'edward.walker@acme.com' and region_code = 'uk' and sequence = '0000000001'), default, '2018-06-20 06:37:03', 'Outgoing Transfer', -100),
       ((select a.id from accounts a, clients c where a.client_id = c.id and email = 'edward.walker@acme.com' and region_code = 'uk' and sequence = '0000000001'), default, '2018-06-20 06:37:03', 'Outgoing Transfer', -100),
       ((select a.id from accounts a, clients c where a.client_id = c.id and email = 'edward.walker@acme.com' and region_code = 'uk' and sequence = '0000000001'), default, '2018-06-20 06:37:03', 'Outgoing Transfer', 100),
       ((select a.id from accounts a, clients c where a.client_id = c.id and email = 'edward.walker@acme.com' and region_code = 'uk' and sequence = '0000000001'), default, '2018-06-20 06:37:03', 'Incoming Transfer', 1000),
       ((select a.id from accounts a, clients c where a.client_id = c.id and email = 'edward.walker@acme.com' and region_code = 'uk' and sequence = '0000000001'), default, '2018-06-20 06:37:03', 'Outgoing Transfer', -100),
       ((select a.id from accounts a, clients c where a.client_id = c.id and email = 'edward.walker@acme.com' and region_code = 'uk' and sequence = '0000000001'), default, '2018-06-20 06:37:03', 'Outgoing Transfer', -100),
       ((select a.id from accounts a, clients c where a.client_id = c.id and email = 'edward.walker@acme.com' and region_code = 'uk' and sequence = '0000000001'), default, '2018-06-20 06:37:03', 'Outgoing Transfer', 100),
       ((select a.id from accounts a, clients c where a.client_id = c.id and email = 'edward.walker@acme.com' and region_code = 'it' and sequence = '0000000001'), default, '2018-06-20 06:37:03', 'Incoming Transfer', 1000),
       ((select a.id from accounts a, clients c where a.client_id = c.id and email = 'edward.walker@acme.com' and region_code = 'it' and sequence = '0000000001'), default, '2018-06-20 06:37:03', 'Outgoing Transfer', -100),
       ((select a.id from accounts a, clients c where a.client_id = c.id and email = 'edward.walker@acme.com' and region_code = 'it' and sequence = '0000000001'), default, '2018-06-20 06:37:03', 'Outgoing Transfer', -100),
       ((select a.id from accounts a, clients c where a.client_id = c.id and email = 'edward.walker@acme.com' and region_code = 'it' and sequence = '0000000001'), default, '2018-06-20 06:37:03', 'Outgoing Transfer', 100),
       ((select a.id from accounts a, clients c where a.client_id = c.id and email = 'edward.walker@acme.com' and region_code = 'it' and sequence = '0000000001'), default, '2018-06-20 06:37:03', 'Outgoing Transfer', -75),
       ((select a.id from accounts a, clients c where a.client_id = c.id and email = 'edward.walker@acme.com' and region_code = 'it' and sequence = '0000000001'), default, '2018-06-20 06:37:03', 'Outgoing Transfer', -300),
       ((select a.id from accounts a, clients c where a.client_id = c.id and email = 'edward.walker@acme.com' and region_code = 'it' and sequence = '0000000001'), default, '2018-06-20 06:37:03', 'Incoming Transfer', 1000),
       ((select a.id from accounts a, clients c where a.client_id = c.id and email = 'edward.walker@acme.com' and region_code = 'it' and sequence = '0000000001'), default, '2018-06-20 06:37:03', 'Outgoing Transfer', -100),
       ((select a.id from accounts a, clients c where a.client_id = c.id and email = 'edward.walker@acme.com' and region_code = 'it' and sequence = '0000000001'), default, '2018-06-20 06:37:03', 'Outgoing Transfer', -100),
       ((select a.id from accounts a, clients c where a.client_id = c.id and email = 'edward.walker@acme.com' and region_code = 'it' and sequence = '0000000001'), default, '2018-06-20 06:37:03', 'Outgoing Transfer', 100),
       ((select a.id from accounts a, clients c where a.client_id = c.id and email = 'edward.walker@acme.com' and region_code = 'it' and sequence = '0000000001'), default, '2018-06-20 06:37:03', 'Outgoing Transfer', -75),
       ((select a.id from accounts a, clients c where a.client_id = c.id and email = 'edward.walker@acme.com' and region_code = 'it' and sequence = '0000000001'), default, '2018-06-20 06:37:03', 'Outgoing Transfer', -300);

-- Updating client information

INSERT INTO accounts
VALUES (default,(select id from clients where email = 'edward.walker@acme.com'),(select id from regions where code = 'it'),'it','0000000002','ACTIVE');

-- Client with Accounts Closed

INSERT INTO clients
VALUES (default,"Jorge","Bailies","jorge.bailies@acme.com");

INSERT INTO accounts
VALUES (default,(select id from clients where email = 'jorge.bailies@acme.com'),(select id from regions where code = 'es'),'es','0000000001','CLOSED');
INSERT INTO accounts
VALUES (default,(select id from clients where email = 'jorge.bailies@acme.com'),(select id from regions where code = 'es'),'es','0000000003','CLOSED');
INSERT INTO accounts
VALUES (default,(select id from clients where email = 'jorge.bailies@acme.com'),(select id from regions where code = 'es'),'es','0000000002','CLOSED');
INSERT INTO accounts
VALUES (default,(select id from clients where email = 'jorge.bailies@acme.com'),(select id from regions where code = 'uk'),'uk','0000000001','CLOSED');
INSERT INTO accounts
VALUES (default,(select id from clients where email = 'jorge.bailies@acme.com'),(select id from regions where code = 'uk'),'uk','0000000002','CLOSED');
INSERT INTO accounts
VALUES (default,(select id from clients where email = 'jorge.bailies@acme.com'),(select id from regions where code = 'uk'),'uk','0000000003','CLOSED');

-- Client with Accounts Inactivated

INSERT INTO clients
VALUES (default,"Eduardo","Andante","eduardo.andante@acme.com");

INSERT INTO accounts
VALUES (default,(select id from clients where email = 'eduardo.andante@acme.com'),(select id from regions where code = 'es'),'es','0000000001','INACTIVE');
INSERT INTO accounts
VALUES (default,(select id from clients where email = 'eduardo.andante@acme.com'),(select id from regions where code = 'es'),'es','0000000002','INACTIVE');
INSERT INTO accounts
VALUES (default,(select id from clients where email = 'eduardo.andante@acme.com'),(select id from regions where code = 'pt'),'pt','0000000001','INACTIVE');
INSERT INTO accounts
VALUES (default,(select id from clients where email = 'eduardo.andante@acme.com'),(select id from regions where code = 'pt'),'pt','0000000002','INACTIVE');
INSERT INTO accounts
VALUES (default,(select id from clients where email = 'eduardo.andante@acme.com'),(select id from regions where code = 'pt'),'pt','0000000003','INACTIVE');

-- Client with Accounts VIP

INSERT INTO clients
VALUES (default,"Charles","Vicensa","charles.vicensa@acme.com");

INSERT INTO accounts
VALUES (default,(select id from clients where email = 'charles.vicensa@acme.com'),(select id from regions where code = 'es'),'es','0000000001','ACTIVE');

INSERT INTO movements
VALUES ((select a.id from accounts a, clients c where a.client_id = c.id and email = 'charles.vicensa@acme.com' and region_code = 'es' and sequence = '0000000001'), default, '2021-01-20 06:37:03', 'Incoming Transfer', 900),
       ((select a.id from accounts a, clients c where a.client_id = c.id and email = 'charles.vicensa@acme.com' and region_code = 'es' and sequence = '0000000001'), default, '2021-02-20 06:37:03', 'Outgoing Transfer',-100),
       ((select a.id from accounts a, clients c where a.client_id = c.id and email = 'charles.vicensa@acme.com' and region_code = 'es' and sequence = '0000000001'), default, '2021-03-20 06:37:03', 'Incoming Transfer', 150),
       ((select a.id from accounts a, clients c where a.client_id = c.id and email = 'charles.vicensa@acme.com' and region_code = 'es' and sequence = '0000000001'), default, '2021-03-20 06:37:03', 'Incoming Transfer', 150);

-- Client with Accounts NEG

INSERT INTO clients
VALUES (default,"Jose Tony","Pradas","josetony.pradas@acme.com");

INSERT INTO accounts
VALUES (default,(select id from clients where email = 'josetony.pradas@acme.com'),(select id from regions where code = 'pt'),'pt','0000000001','ACTIVE');

INSERT INTO movements
VALUES ((select a.id from accounts a, clients c where a.client_id = c.id and email = 'josetony.pradas@acme.com' and region_code = 'pt' and sequence = '0000000001'), default, '2021-01-20 06:37:03', 'Incoming Transfer', 500),
       ((select a.id from accounts a, clients c where a.client_id = c.id and email = 'josetony.pradas@acme.com' and region_code = 'pt' and sequence = '0000000001'), default, '2021-02-20 06:37:03', 'Outgoing Transfer',-100),
       ((select a.id from accounts a, clients c where a.client_id = c.id and email = 'josetony.pradas@acme.com' and region_code = 'pt' and sequence = '0000000001'), default, '2021-03-20 06:37:03', 'Incoming Transfer',-250);

-- Client with Accounts LOW

INSERT INTO clients
VALUES (default,"Miguel Angel","Dias","mad@acme.com");

INSERT INTO accounts
VALUES (default,(select id from clients where email = 'mad@acme.com'),(select id from regions where code = 'us'),'us','0000000001','ACTIVE');

INSERT INTO movements
VALUES ((select a.id from accounts a, clients c where a.client_id = c.id and email = 'mad@acme.com' and region_code = 'us' and sequence = '0000000001'), default, '2021-01-20 06:37:03', 'Incoming Transfer', 500),
       ((select a.id from accounts a, clients c where a.client_id = c.id and email = 'mad@acme.com' and region_code = 'us' and sequence = '0000000001'), default, '2021-02-20 06:37:03', 'Outgoing Transfer',-100),
       ((select a.id from accounts a, clients c where a.client_id = c.id and email = 'mad@acme.com' and region_code = 'us' and sequence = '0000000001'), default, '2021-03-20 06:37:03', 'Incoming Transfer',-250);

-- Client with Accounts NEG

INSERT INTO clients
VALUES (default,"Antonio","Sinpasta","antonio.sinpasta@acme.com");

INSERT INTO accounts
VALUES (default,(select id from clients where email = 'antonio.sinpasta@acme.com'),(select id from regions where code = 'uk'),'uk','0000000001','ACTIVE');

INSERT INTO movements
VALUES ((select a.id from accounts a, clients c where a.client_id = c.id and email = 'antonio.sinpasta@acme.com' and region_code = 'uk' and sequence = '0000000001'), default, '2021-01-20 06:37:03', 'Incoming Transfer', -500),
       ((select a.id from accounts a, clients c where a.client_id = c.id and email = 'antonio.sinpasta@acme.com' and region_code = 'uk' and sequence = '0000000001'), default, '2021-02-20 06:37:03', 'Outgoing Transfer',-1000),
       ((select a.id from accounts a, clients c where a.client_id = c.id and email = 'antonio.sinpasta@acme.com' and region_code = 'uk' and sequence = '0000000001'), default, '2021-03-20 06:37:03', 'Incoming Transfer',-2500),
       ((select a.id from accounts a, clients c where a.client_id = c.id and email = 'antonio.sinpasta@acme.com' and region_code = 'uk' and sequence = '0000000001'), default, '2021-01-20 06:37:03', 'Incoming Transfer', -100);
