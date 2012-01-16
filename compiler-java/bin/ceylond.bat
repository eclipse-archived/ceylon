@echo off
setlocal

call %~dp0\args.bat %*

if "%exit%" == "true" (
    exit /b 1
)

rem JAVA_CP are classes required by the tool to run
set "JAVA_CP=%CEYLON_REPO%\com\redhat\ceylon\compiler\java\%CEYLON_VERSION%\com.redhat.ceylon.compiler.java-%CEYLON_VERSION%.jar"
set "JAVA_CP=%JAVA_CP%;%CEYLON_REPO%\com\redhat\ceylon\typechecker\%CEYLON_VERSION%\com.redhat.ceylon.typechecker-%CEYLON_VERSION%.jar"
set "JAVA_CP=%JAVA_CP%;%CEYLON_REPO%\ceylon\language\%CEYLON_VERSION%\ceylon.language-%CEYLON_VERSION%.car"
rem FIXME: https://github.com/ceylon/ceylon-module-resolver/issues/7
set "JAVA_CP=%JAVA_CP%;%HOME%\.m2\repository\com\redhat\ceylon\cmr\cmr-api\1.0.0-SNAPSHOT\cmr-api-1.0.0-SNAPSHOT.jar"
set "JAVA_CP=%JAVA_CP%;%HOME%\.m2\repository\com\redhat\ceylon\cmr\cmr-spi\1.0.0-SNAPSHOT\cmr-spi-1.0.0-SNAPSHOT.jar"
set "JAVA_CP=%JAVA_CP%;%HOME%\.m2\repository\com\redhat\ceylon\cmr\cmr-impl\1.0.0-SNAPSHOT\cmr-impl-1.0.0-SNAPSHOT.jar"
set "JAVA_CP=%JAVA_CP%;%HOME%\.m2\repository\com\redhat\ceylon\cmr\cmr-webdav\1.0.0-SNAPSHOT\cmr-webdav-1.0.0-SNAPSHOT.jar"
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
