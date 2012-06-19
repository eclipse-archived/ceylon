@echo off
setlocal

call %~dp0\args.bat %*

if "%exit%" == "true" (
    exit /b 1
)

rem JAVA_CP are classes required by the tool to run
set "JAVA_CP=%CEYLON_REPO%\com\redhat\ceylon\compiler\java\%CEYLON_VERSION%\com.redhat.ceylon.compiler.java-%CEYLON_VERSION%.jar"
set "JAVA_CP=%JAVA_CP%;%CEYLON_REPO%\com\redhat\ceylon\typechecker\%CEYLON_VERSION%\com.redhat.ceylon.typechecker-%CEYLON_VERSION%.jar"
set "JAVA_CP=%JAVA_CP%;%CEYLON_REPO%\com\redhat\ceylon\module-resolver\%CEYLON_VERSION%\com.redhat.ceylon.module-resolver-%CEYLON_VERSION%.jar"
set "JAVA_CP=%JAVA_CP%;%CEYLON_REPO%\ceylon\language\%CEYLON_VERSION%\ceylon.language-%CEYLON_VERSION%.car"
set "JAVA_CP=%JAVA_CP%;%CEYLON_HOME%\lib\antlr-3.4-complete.jar"
set "JAVA_CP=%JAVA_CP%;%CEYLON_HOME%\lib\sardine-314.jar"
set "JAVA_CP=%JAVA_CP%;%CEYLON_HOME%\lib\slf4j-api-1.6.1.jar"
set "JAVA_CP=%JAVA_CP%;%CEYLON_HOME%\lib\slf4j-simple-1.6.1.jar"
set "JAVA_CP=%JAVA_CP%;%CEYLON_HOME%\lib\httpclient-4.1.1.jar"
set "JAVA_CP=%JAVA_CP%;%CEYLON_HOME%\lib\httpcore-4.1.1.jar"
set "JAVA_CP=%JAVA_CP%;%CEYLON_HOME%\lib\commons-logging-1.1.1.jar"
set "JAVA_CP=%JAVA_CP%;%CEYLON_HOME%\lib\commons-codec-1.4.jar"
set "JAVA_CP=%JAVA_CP%;%CEYLON_HOME%\lib\jandex-1.0.3.Final.jar"

if "%USER_CP%" NEQ "" (
    set "JAVA_CP=%JAVA_CP%;%USER_CP%"
)

"%JAVA%" ^
    -enableassertions ^
    -classpath "%JAVA_CP%" ^
    "-Dceylon.home=%CEYLON_HOME%" ^
    com.redhat.ceylon.tools.ImportJarMain ^
    %ARGS%

endlocal
