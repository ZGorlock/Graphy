@echo off

echo Graphy

set mainClass=graphy.main.%1.%2
set noCompile=false
set loggerLvl=warn
set waitAfter=false

mvn compile exec:exec ^
	-Dorg.slf4j.simpleLogger.defaultLogLevel=%loggerLvl% ^
	-Dmaven.main.skip="%noCompile%" ^
	-Dexec.executable="java" ^
	-Dexec.args="-Dfile.encoding=UTF8 -classpath %%classpath %mainClass%" && ^
if "%waitAfter%"=="true" (pause)
