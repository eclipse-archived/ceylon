@echo off
setlocal

call %~dp0\args.bat %*

rem JAVA_CP are classes required by the tool to run
set JAVA_CP=%HOME%\.ceylon\repo\com\redhat\ceylon\compiler\%CEYLON_VERSION%\com.redhat.ceylon.compiler-%CEYLON_VERSION%.jar
set JAVA_CP=%JAVA_CP%;%HOME%\.ceylon\repo\com\redhat\ceylon\typechecker\%CEYLON_VERSION%\com.redhat.ceylon.typechecker-%CEYLON_VERSION%.jar
set JAVA_CP=%JAVA_CP%;%CEYLON_HOME%\lib\antlr-3.4-complete.jar
set JAVA_CP=%JAVA_CP%;%CEYLON_HOME%\lib\markdownj-core-faf3bf83000.jar

%JAVA% ^
	-enableassertions ^
	-classpath "%JAVA_CP%" ^
	com.redhat.ceylon.ceylondoc.Main ^
	-classpath "%USER_CP%" ^
	%ARGS%

endlocal
