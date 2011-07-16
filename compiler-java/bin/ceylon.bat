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

%JAVA% ^
	-enableassertions ^
	-classpath ^
	"%CEYLON_HOME%\build\lib\runtime.jar;%CEYLON_HOME%\build\classes;%USER_CP%" ^
	ceylon.language.Launcher ^
	%ARGS%

endlocal
