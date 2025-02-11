@echo off
javac -d out src\*.java src\handlers\*.java
java -cp out App