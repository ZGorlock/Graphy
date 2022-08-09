#!/bin/bash

echo Graphy

mainClass="graphy.main.$1.$2"
noCompile="false"
loggerLvl="warn"
waitAfter="false"

mvn compile exec:exec \
	-Dorg.slf4j.simpleLogger.defaultLogLevel=$loggerLvl \
	-Dmaven.main.skip=$noCompile \
	-Dexec.executable="java" \
	-Dexec.args="-Dfile.encoding=UTF8 -classpath %classpath $mainClass" && \
if $waitAfter; then read -p "Press Enter to continue . . . " < /dev/tty; fi
