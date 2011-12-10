@echo off
setlocal

call %~dp0\args.bat %*

rem Classes required by the launcher and by the code being launched
set JAVA_CP=%HOME%\.ceylon\repo\ceylon\language\%CEYLON_VERSION%\ceylon.language-%CEYLON_VERSION%.car
set JAVA_CP=%JAVA_CP%;%USER_CP%

%JAVA% ^
	-enableassertions ^
	-classpath "%JAVA_CP%" ^
	%ARGS%

endlocal
