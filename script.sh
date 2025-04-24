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
describe emp
EOF