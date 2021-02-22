@echo off
set type=%1
set class=%2
@echo on

mvn compile && mvn exec:java -Dexec.mainClass="graphy.main.%type%.%class%"
