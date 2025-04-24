#!/usr/bin/env bash

cd src/main/java || exit
mkdir -p ../../../build
javac -d ../../../build $(find . -name "*.java")

cd ../../../build || exit
java org.example.Main <<EOF
create table emp (id,age)
insert into emp values (1,10)
insert into emp values (2,10)
insert into emp values (3,10)
insert into emp values (4,10)
insert into emp values (5,10)
insert into emp values (5,20)
insert into emp values (5,30)
delete from emp where id=5
select * from emp
EOF