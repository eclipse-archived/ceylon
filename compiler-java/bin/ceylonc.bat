@echo off
setlocal

rem Find CEYLON_HOME
if "%CEYLON_HOME%" == "" set CEYLON_HOME=%~dp0..

rem Find Java
if "%JAVA_HOME%" == "" (
	set JAVA=java
) else (
	set JAVA="%JAVA_HOME%\bin\java.exe"
)

rem Process -cp and other args
set USER_CP=
set ARGS=

:loop_args
if "%~1" == "" goto :done_args

if "%~1" == "-cp" (
	set USER_CP=%~2
	shift
) else if "%~1" == "-classpath" (
	set USER_CP=%~2
	shift
) else (
	set ARGS=%ARGS% %1
)

shift
goto loop_args

:done_args

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
