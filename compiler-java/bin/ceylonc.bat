@echo off
setlocal

call %~dp0\args.bat %*

if "%exit%" == "true" (
    exit /b 1
)

rem JAVA_CP are classes required by the compiler to run
set "JAVA_CP=%MINIMAL_CP%;

rem COMPILE_CP are classes required by the code being compiled
set "COMPILE_CP=%CEYLON_REPO%\ceylon\language\%CEYLON_VERSION%\ceylon.language-%CEYLON_VERSION%.car;%USER_CP%"

"%JAVA%" ^
    -enableassertions ^
    -classpath "%JAVA_CP%;%COMPILE_CP%" ^
    "-Dceylon.home=%CEYLON_HOME%" ^
    com.redhat.ceylon.compiler.java.Main5 ^
    %ARGS%

endlocal
