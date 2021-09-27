FROM postgres:13.1-alpine
COPY /Dockerfiles/init.sql /docker-entrypoint-initdb.d/

