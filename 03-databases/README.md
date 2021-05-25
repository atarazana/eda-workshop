# Deploying Enterprise Databases

## MySQL

### Enterprise Database

Sample database with a model of clients, accounts and movements.

To build this image:

```shell script
oc new-build ./mysql/enterprise --name=mysql-enterprise
oc start-build mysql-enterprise --from-dir=./mysql/enterprise --follow
oc new-app mysql-enterprise:latest -e MYSQL_ROOT_PASSWORD=debezium -e MYSQL_USER=mysqluser -e MYSQL_PASSWORD=mysqlpw
```

To verify the new database:

```shell script
$ oc rsh mysql-enterprise-5488f4997d-8ppkm
$ mysql -u $MYSQL_USER -p$MYSQL_PASSWORD enterprise
mysql> show databases;
+--------------------+
| Database           |
+--------------------+
| information_schema |
| enterprise         |
+--------------------+
2 rows in set (0.01 sec)

mysql> show tables;
+----------------------+
| Tables_in_enterprise |
+----------------------+
| accounts             |
| clients              |
| movements            |
| regions              |
+----------------------+
4 rows in set (0.00 sec)
```

### Inventory Database

Sample database with a model of customers, products and orders.

```shell script
oc new-build ./mysql/inventory --name=mysql-inventory
oc start-build mysql-inventory --from-dir=./mysql/inventory --follow
oc new-app mysql-inventory:latest -e MYSQL_ROOT_PASSWORD=debezium -e MYSQL_USER=mysqluser -e MYSQL_PASSWORD=mysqlpw
```

```shell script
❯ oc rsh mysql-inventory-95f6fc6b4-bpvlz 
$ mysql -u $MYSQL_USER -p$MYSQL_PASSWORD inventory
mysql> show databases;
+--------------------+
| Database           |
+--------------------+
| information_schema |
| inventory          |
+--------------------+
2 rows in set (0.00 sec)

mysql> show tables;
+---------------------+
| Tables_in_inventory |
+---------------------+
| addresses           |
| customers           |
| geom                |
| orders              |
| products            |
| products_on_hand    |
+---------------------+
6 rows in set (0.00 sec)
```
