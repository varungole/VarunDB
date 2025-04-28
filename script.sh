#!/usr/bin/env bash

cd src/main/java || exit
mkdir -p ../../../build
javac -d ../../../build $(find . -name "*.java")

cd ../../../build || exit
java org.example.Main <<EOF
create table emp (id int,age int,name string,salary int)
insert into emp values (1,25,Varun,150000)
insert into emp values (2,40,Pavel,250000)
insert into emp values (3,32,Dmytro,200000)
insert into emp values (4,22,Jeet,230000)
insert into emp values (5,44,Joy,210000)
insert into emp values (6,56,Sam,175000)
insert into emp values (7,50,George,167598)
insert into emp values (8,34,Rich,150000)
insert into emp values (9,18,Cash,149999)
insert into emp values (10,19,Taylor,130000)
insert into emp values (11,27,Swift,150700)
insert into emp values (12,37,Austin,290000)
insert into emp values (13,48,Tyler,530000)
insert into emp values (14,78,Ashish,4000000)
insert into emp values (15,84,Madhavi,62000)
select * from emp where salary > 200000 order by age
EOF