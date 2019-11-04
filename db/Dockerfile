FROM postgres:10.3-alpine
MAINTAINER your.email@example.de

ENV POSTGRES_USER postgres
ENV POSTGRES_PASSWORD emerio@123
ENV POSTGRES_DB survey_db

# Create the table on start-up
ADD schemas/createQuery.sql /docker-entrypoint-initdb.d/

