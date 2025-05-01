#!/usr/bin/env bash

cd src/main/java || exit
mkdir -p ../../../build
javac -d ../../../build $(find . -name "*.java")

cd ../../../build || exit
java org.example.Main <<EOF
create database company
use company
create table departments (id int,name string)
insert into departments values (1,HR)
create table interns (id int,name string,salary int)
insert into interns values (1,Varun,40)
insert into interns values (2,Harditya,40)
insert into interns values (1,Chandan,40)
insert into interns values (1,Sushil,40)
select * from interns
create database city
use city
create table capital (id int,name string)
insert into capital values (1,DC)
insert into capital values (2,Delhi)
insert into capital values (3,Tokyo)
select * from capital
EOF