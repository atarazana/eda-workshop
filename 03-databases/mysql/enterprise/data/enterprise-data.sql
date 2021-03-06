
# Switch to this database
USE enterprise;

#
# Sample Data to
#

# Customer with some accounts

INSERT INTO clients
VALUES (default,"George","Bailey","gbailey@acme.com");

INSERT INTO accounts
VALUES (default,2,(select id from regions where code = 'es'),'es','0000000001','ACTIVE'),
       (default,2,(select id from regions where code = 'es'),'es','0000000002','ACTIVE'),
       (default,2,(select id from regions where code = 'pt'),'pt','0000000001','ACTIVE'),
       (default,2,(select id from regions where code = 'pt'),'pt','0000000002','INACTIVE');

# Customer with some accounts and movements

INSERT INTO clients
VALUES (default,"Edward","Walker","edward.walker@acme.com");

INSERT INTO accounts
VALUES (default,3,(select id from regions where code = 'uk'),'uk','0000000001','CLOSED'),
       (default,3,(select id from regions where code = 'uk'),'uk','0000000002','ACTIVE'),
       (default,3,(select id from regions where code = 'uk'),'it','0000000001','ACTIVE');
      
INSERT INTO movements
VALUES (5, default, '2018-06-20 06:37:03', 'Incoming Transfer', 1000),
       (5, default, '2018-06-20 06:37:03', 'Outgoing Transfer', -100),
       (5, default, '2018-06-20 06:37:03', 'Outgoing Transfer', -100),
       (5, default, '2018-06-20 06:37:03', 'Outgoing Transfer', 100),
       (6, default, '2018-06-20 06:37:03', 'Incoming Transfer', 1000),
       (6, default, '2018-06-20 06:37:03', 'Outgoing Transfer', -100),
       (6, default, '2018-06-20 06:37:03', 'Outgoing Transfer', -100),
       (6, default, '2018-06-20 06:37:03', 'Outgoing Transfer', 100),
       (7, default, '2018-06-20 06:37:03', 'Incoming Transfer', 1000),
       (7, default, '2018-06-20 06:37:03', 'Outgoing Transfer', -100),
       (7, default, '2018-06-20 06:37:03', 'Outgoing Transfer', -100),
       (7, default, '2018-06-20 06:37:03', 'Outgoing Transfer', 100),
       (7, default, '2018-06-20 06:37:03', 'Outgoing Transfer', -75),
       (7, default, '2018-06-20 06:37:03', 'Outgoing Transfer', -300),
       (7, default, '2018-06-20 06:37:03', 'Incoming Transfer', 1000),
       (7, default, '2018-06-20 06:37:03', 'Outgoing Transfer', -100),
       (7, default, '2018-06-20 06:37:03', 'Outgoing Transfer', -100),
       (7, default, '2018-06-20 06:37:03', 'Outgoing Transfer', 100),
       (7, default, '2018-06-20 06:37:03', 'Outgoing Transfer', -75),
       (7, default, '2018-06-20 06:37:03', 'Outgoing Transfer', -300);

# Updating client information

INSERT INTO accounts
VALUES (default,3,(select id from regions where code = 'it'),'it','0000000002','ACTIVE');

# More samples

INSERT INTO clients
VALUES (default,"Anne","Kretchmar","annek@acme.com");

INSERT INTO accounts
VALUES (default,4,(select id from regions where code = 'us'),'us','0000000001','ACTIVE');

INSERT INTO accounts
VALUES (default,4,(select id from regions where code = 'us'),'us','0000000002','ACTIVE');

INSERT INTO accounts
VALUES (default,4,(select id from regions where code = 'es'),'es','0000000001','ACTIVE');

# More samples

INSERT INTO clients
VALUES (default,"Roman","Martin","roman.martin@acme.com");

INSERT INTO accounts
VALUES (default,5,(select id from regions where code = 'es'),'es','0000000001','CLOSED');

INSERT INTO accounts
VALUES (default,5,(select id from regions where code = 'es'),'es','0000000002','ACTIVE');

INSERT INTO accounts
VALUES (default,5,(select id from regions where code = 'es'),'es','0000000003','CLOSED');

INSERT INTO accounts
VALUES (default,5,(select id from regions where code = 'es'),'es','0000000004','INACTIVE');

# UPDATE accounts SET status = 'CLOSED' WHERE client_id = 5 and sequence = '0000000002';

# Client with Accounts Closed

INSERT INTO clients
VALUES (default,"Jorge","Bailies","jorge.vailies@acme.com");

INSERT INTO accounts
VALUES (default,6,(select id from regions where code = 'es'),'es','0000000001','CLOSED');
INSERT INTO accounts
VALUES (default,6,(select id from regions where code = 'es'),'es','0000000003','CLOSED');
INSERT INTO accounts
VALUES (default,6,(select id from regions where code = 'es'),'es','0000000002','CLOSED');
INSERT INTO accounts
VALUES (default,6,(select id from regions where code = 'uk'),'uk','0000000001','CLOSED');
INSERT INTO accounts
VALUES (default,6,(select id from regions where code = 'uk'),'uk','0000000002','CLOSED');
INSERT INTO accounts
VALUES (default,6,(select id from regions where code = 'uk'),'uk','0000000003','CLOSED');

#UPDATE accounts SET status = 'ACTIVE' WHERE client_id = 6;
#UPDATE accounts SET status = 'INACTIVE' WHERE client_id = 6;
#UPDATE accounts SET status = 'CLOSED' WHERE client_id = 6;

# Client with Accounts Inactivated

INSERT INTO clients
VALUES (default,"Eduardo","Andante","eduardo.andante@acme.com");

INSERT INTO accounts
VALUES (default,7,(select id from regions where code = 'es'),'es','0000000001','INACTIVE');
INSERT INTO accounts
VALUES (default,7,(select id from regions where code = 'es'),'es','0000000002','INACTIVE');
INSERT INTO accounts
VALUES (default,7,(select id from regions where code = 'pt'),'pt','0000000001','INACTIVE');
INSERT INTO accounts
VALUES (default,7,(select id from regions where code = 'pt'),'pt','0000000002','INACTIVE');
INSERT INTO accounts
VALUES (default,7,(select id from regions where code = 'pt'),'pt','0000000003','INACTIVE');

# Client with Accounts VIP

INSERT INTO clients
VALUES (default,"Carles","Vicens","carles.vicens@acme.com");

INSERT INTO accounts
VALUES (default,8,(select id from regions where code = 'es'),'es','0000000001','ACTIVE');

INSERT INTO movements
VALUES (27, default, '2021-01-20 06:37:03', 'Incoming Transfer', 900000),
       (27, default, '2021-02-20 06:37:03', 'Outgoing Transfer',-100000),
       (27, default, '2021-03-20 06:37:03', 'Incoming Transfer', 150000),
       (27, default, '2021-03-20 06:37:03', 'Incoming Transfer', 150000);

# Client with Accounts NEG

INSERT INTO clients
VALUES (default,"Jose Antonio","Prada","joseantonio.prada@acme.com");

INSERT INTO accounts
VALUES (default,9,(select id from regions where code = 'pt'),'pt','0000000001','ACTIVE');

INSERT INTO movements
VALUES (29, default, '2021-01-20 06:37:03', 'Incoming Transfer', 5000),
       (29, default, '2021-02-20 06:37:03', 'Outgoing Transfer',-10000),
       (29, default, '2021-03-20 06:37:03', 'Incoming Transfer',-25000);

# Client with Accounts LOW

INSERT INTO clients
VALUES (default,"Miguel Angel","Diaz","mad@acme.com");

INSERT INTO accounts
VALUES (default,9,(select id from regions where code = 'us'),'us','0000000001','ACTIVE');

INSERT INTO movements
VALUES (31, default, '2021-01-20 06:37:03', 'Incoming Transfer', 50000),
       (31, default, '2021-02-20 06:37:03', 'Outgoing Transfer',-10000),
       (31, default, '2021-03-20 06:37:03', 'Incoming Transfer',-25000);

# Client with Accounts NEG

INSERT INTO clients
VALUES (default,"Antonio","Sinpasta","antonio.sinpasta@acme.com");

INSERT INTO accounts
VALUES (default,11,(select id from regions where code = 'es'),'uk','0000000001','ACTIVE');

INSERT INTO movements
VALUES (32, default, '2021-01-20 06:37:03', 'Incoming Transfer', -5000),
       (32, default, '2021-02-20 06:37:03', 'Outgoing Transfer',-10000),
       (32, default, '2021-03-20 06:37:03', 'Incoming Transfer',-25000),
       (32, default, '2021-01-20 06:37:03', 'Incoming Transfer', -1000);
