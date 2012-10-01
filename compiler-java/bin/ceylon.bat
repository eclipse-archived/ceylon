@echo off
setlocal

call %~dp0\ceylon-cp.bat %*

if "%exit%" == "true" (
    exit /b 1
)

set "LIB=%CEYLON_HOME%\lib"
set "RUNTIME_REPO=%CEYLON_HOME%\runtime-repo"

JAVA_CP="%LIB%\jboss-modules.jar"
JAVA_CP="%JAVA_CP%;%LIB%\ceylon-runtime-bootstrap.jar"
JAVA_CP="%JAVA_CP%;%MINIMAL_CP%"

"%JAVA%" ^
    -cp $JAVA_CP ^
    "-Dceylon.home=%CEYLON_HOME%" ^
    "-Dceylon.system.repo=%CEYLON_REPO%" ^
    com.redhat.ceylon.tools.CeylonTool ^
    %*

endlocal
