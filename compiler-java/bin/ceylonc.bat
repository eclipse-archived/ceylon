@echo off
setlocal

call %~dp0\args.bat %*

rem JAVA_CP are classes required by the compiler to run
set JAVA_CP=%CEYLON_HOME%\build\lib\compiler.jar
set JAVA_CP=%JAVA_CP%;%HOMEDRIVE%%HOMEPATH%\.ceylon\repo\com\redhat\ceylon\typechecker\0.1\com.redhat.ceylon.typechecker-0.1.jar
set JAVA_CP=%JAVA_CP%;%CEYLON_HOME%\lib\antlrworks-1.3.1.jar

rem COMPILE_CP are classes required by the code being compiled
set COMPILE_CP=%CEYLON_HOME%\build\lib\runtime.jar;%USER_CP%

%JAVA% ^
	-enableassertions ^
	-classpath "%JAVA_CP%" ^
	com.redhat.ceylon.compiler.Main ^
	-classpath "%COMPILE_CP%" ^
	%ARGS%

endlocal
