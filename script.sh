#!/usr/bin/env bash

cd src/main/java || exit
mkdir -p ../../../build
javac -d ../../../build $(find . -name "*.java")

cd ../../../build || exit
java org.example.Main <<EOF
create database football
use football
create table teams (country string,name string,rank int)
insert into teams values (uk,manutd,13),(uk,liverpool,1),(uk,aston villa,5)
select * from teams
EOF