#FROM postgres:10.4
FROM registry.gitlab.com/armdev/dockers/postgres
MAINTAINER Armen Arzumanyan
WORKDIR /etc/postgresql/
COPY postgresql.conf postgresql.conf
CMD ["postgres", "-c", "config_file=/etc/postgresql/postgresql.conf"]
