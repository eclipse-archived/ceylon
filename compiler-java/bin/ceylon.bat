@echo off
setlocal

call %~dp0\ceylon-cp.bat %*

if "%exit%" == "true" (
    exit /b 1
)

set "LIB=%CEYLON_HOME%\lib"
set "REPO=%CEYLON_HOME%\repo"
set "RUNTIME_REPO=%CEYLON_HOME%\runtime-repo"

set "JAVA_CP=%REPO%\org\jboss\modules\main\org.jboss.modules-main.jar"
set "JAVA_CP=%JAVA_CP%;%LIB%\ceylon-runtime-bootstrap.jar"
set "JAVA_CP=%JAVA_CP%;%MINIMAL_CP%"

"%JAVA%" ^
    -cp %JAVA_CP% ^
    "-Dceylon.home=%CEYLON_HOME%" ^
    "-Dceylon.system.repo=%CEYLON_REPO%" ^
    com.redhat.ceylon.tools.CeylonTool ^
    %ARGS%

endlocal
