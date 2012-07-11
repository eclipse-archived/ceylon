@echo off
setlocal

call %~dp0\args.bat %*

if "%exit%" == "true" (
    exit /b 1
)

rem JAVA_CP are classes required by the tool to run
set "JAVA_CP=%MINIMAL_CP%;
set "JAVA_CP=%JAVA_CP%;%CEYLON_REPO%\ceylon\language\%CEYLON_VERSION%\ceylon.language-%CEYLON_VERSION%.car"

if "%USER_CP%" NEQ "" (
    set "JAVA_CP=%JAVA_CP%;%USER_CP%"
)

"%JAVA%" ^
    -enableassertions ^
    -classpath "%JAVA_CP%" ^
    "-Dceylon.home=%CEYLON_HOME%" ^
    com.redhat.ceylon.tools.importjar.ImportJarMain5 ^
    %ARGS%

endlocal
