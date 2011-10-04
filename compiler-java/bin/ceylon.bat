@echo off
setlocal

call %~dp0\args.bat %*

rem Classes required by the launcher and by the code being launched
set JAVA_CP=$HOME\.ceylon\repo\ceylon\language\0.1\ceylon.language-0.1.car
set JAVA_CP=%JAVA_CP%;%USER_CP%

%JAVA% ^
	-enableassertions ^
	-classpath "%JAVA_CP%" ^
	ceylon.language.Launcher ^
	%ARGS%

endlocal
