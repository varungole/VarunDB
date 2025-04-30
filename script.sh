#!/usr/bin/env bash

cd src/main/java || exit
mkdir -p ../../../build
javac -d ../../../build $(find . -name "*.java")

cd ../../../build || exit
java org.example.Main <<EOF
create database company
create database family
create database country
use family;
create table employees (id int,name string,salary int)
insert into employees values (1,Varun,150000)
insert into employees values (2,Ashish,175000)
insert into employees values (3,Dmytro,200000)
insert into employees values (4,Pavel,350000)
insert into employees values (5,Jeetendra,250000)
SeLeCt * FrOm employees OrDeR bY salary
use country;
select * from employees
EOF