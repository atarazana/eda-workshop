FROM mysql:5.7.35

LABEL maintainer="Debezium Community"
LABEL database="Enterprise Database"

COPY mysql.cnf /etc/mysql/conf.d/
COPY data/enterprise.sql /docker-entrypoint-initdb.d/
#COPY data/enterprise-data.sql /docker-entrypoint-initdb.d/
