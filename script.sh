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
insert into emp values (4,45,Jeet,230000)
select * from emp
EOF