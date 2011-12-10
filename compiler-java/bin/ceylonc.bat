@echo off
setlocal

call %~dp0\args.bat %*

rem JAVA_CP are classes required by the compiler to run
set JAVA_CP=%HOME%\.ceylon\repo\com\redhat\ceylon\compiler\%CEYLON_VERSION%\com.redhat.ceylon.compiler-%CEYLON_VERSION%.jar
set JAVA_CP=%JAVA_CP%;%HOME%\.ceylon\repo\com\redhat\ceylon\typechecker\%CEYLON_VERSION%\com.redhat.ceylon.typechecker-%CEYLON_VERSION%.jar
set JAVA_CP=%JAVA_CP%;%CEYLON_HOME%\lib\antlr-3.4-complete.jar

rem COMPILE_CP are classes required by the code being compiled
set COMPILE_CP=%HOME%\.ceylon\repo\ceylon\language\%CEYLON_VERSION%\ceylon.language-%CEYLON_VERSION%.car;%USER_CP%

%JAVA% ^
	-enableassertions ^
	-classpath "%JAVA_CP%" ^
	com.redhat.ceylon.compiler.Main ^
	-classpath "%COMPILE_CP%" ^
	%ARGS%

endlocal
