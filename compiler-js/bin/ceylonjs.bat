@echo off
setlocal

call %~dp0\args.bat %*

if "%exit%" == "true" (
    exit /b 1
)

rem JAVA_CP are classes required by the compiler to run
set "JAVA_CP=%CEYLON_REPO%\com\redhat\ceylon\compiler\java\%CEYLON_VERSION%\com.redhat.ceylon.compiler.java-%CEYLON_VERSION%.jar"
set "JAVA_CP=%JAVA_CP%;%CEYLON_REPO%\com\redhat\ceylon\typechecker\%CEYLON_VERSION%\com.redhat.ceylon.typechecker-%CEYLON_VERSION%.jar"
set "JAVA_CP=%JAVA_CP%;%CEYLON_REPO%\com\redhat\ceylon\module-resolver\%CEYLON_VERSION%\com.redhat.ceylon.module-resolver-%CEYLON_VERSION%.jar"
set "JAVA_CP=%JAVA_CP%;%CEYLON_REPO%\com\redhat\ceylon\ant\%CEYLON_VERSION%\com.redhat.ceylon.ant-%CEYLON_VERSION%.jar"
set "JAVA_CP=%JAVA_CP%;%CEYLON_HOME%\lib\antlr-3.4-complete.jar"
set "JAVA_CP=%JAVA_CP%;%CEYLON_REPO%\com\redhat\ceylon\compiler\js\%CEYLON_VERSION%\com.redhat.ceylon.compiler.js-%CEYLON_VERSION%.jar"

"%JAVA%" ^
    -enableassertions ^
    -classpath "%JAVA_CP%" ^
    "-Dceylon.home=%CEYLON_HOME%" ^
    com.redhat.ceylon.compiler.MainForJs ^
    %ARGS%

endlocal
