#!/usr/bin/env bash

cd src/main/java || exit
mkdir -p ../../../build
javac -d ../../../build $(find . -name "*.java")

cd ../../../build || exit
java org.example.Main <<EOF
create database football
use football
create table teams (id int,name string)
insert into teams values (1,manutd)
select * from teams
EOF