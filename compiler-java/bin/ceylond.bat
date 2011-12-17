@echo off
setlocal

call %~dp0\args.bat %*

if "%exit%" == "true" (
    exit /b 1
)

rem JAVA_CP are classes required by the tool to run
set "JAVA_CP=%CEYLON_REPO%\com\redhat\ceylon\compiler\%CEYLON_VERSION%\com.redhat.ceylon.compiler-%CEYLON_VERSION%.jar"
set "JAVA_CP=%JAVA_CP%;%CEYLON_REPO%\com\redhat\ceylon\typechecker\%CEYLON_VERSION%\com.redhat.ceylon.typechecker-%CEYLON_VERSION%.jar"
set "JAVA_CP=%JAVA_CP%;%CEYLON_HOME%\lib\antlr-3.4-complete.jar"
set "JAVA_CP=%JAVA_CP%;%CEYLON_HOME%\lib\markdownj-core-faf3bf83000.jar"

if "%USER_CP%" NEQ "" (
    set "JAVA_CP=%JAVA_CP%;%USER_CP%"
)

"%JAVA%" ^
    -enableassertions ^
    -classpath "%JAVA_CP%" ^
    "-Dceylon.home=%CEYLON_HOME%" ^
    com.redhat.ceylon.ceylondoc.Main ^
    %ARGS%

endlocal
