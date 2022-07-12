FROM mysql:5.7.35

LABEL maintainer="Debezium Community"
LABEL database="Inventory Database"

COPY mysql.cnf /etc/mysql/conf.d/
COPY data/inventory.sql /docker-entrypoint-initdb.d/
