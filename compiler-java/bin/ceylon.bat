@echo off
setlocal

call %~dp0\args.bat %*

rem Classes required by the launcher and by the code being launched
set JAVA_CP=%CEYLON_HOME%\build\lib\runtime.jar
set JAVA_CP=%JAVA_CP%;%USER_CP%

%JAVA% ^
	-enableassertions ^
	-classpath "%JAVA_CP%" ^
	ceylon.language.Launcher ^
	%ARGS%

endlocal
