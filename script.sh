#!/usr/bin/env bash

# Go to source directory
cd src/main/java || exit

# Make sure build directory exists
mkdir -p ../../../build

# Compile with logger.jar in classpath
javac -cp "/Users/varungole/Desktop/Interactive Brokers/logger.jar" -d ../../../build $(find . -name "*.java")

# Go to build directory
cd ../../../build || exit

# Run with local classes first, logger jar second
java -cp ".:/Users/varungole/Desktop/Interactive Brokers/logger.jar" org.example.Main <<EOF
create database football
use football
create table teams (id int,name string)
insert into teams values (1,manutd)
insert into teams values (2,psg)
insert into teams values (3,arsenal)
insert into teams values (4,barcelona)
select * from teams
EOF
