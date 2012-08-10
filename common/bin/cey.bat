@echo off
setlocal

call %~dp0\cey-cp.bat %*

if "%exit%" == "true" (
    exit /b 1
)

set "LIB=%CEYLON_HOME%\lib"
set "RUNTIME_REPO=%CEYLON_HOME%\runtime-repo"

"%JAVA%" ^
    -cp "%LIB%\jboss-modules.jar;%LIB%\ceylon-runtime-bootstrap.jar" ^
    "-Dceylon.home=%CEYLON_HOME%" ^
    ceylon.modules.bootstrap.Main5 ^
    -mp "%RUNTIME_REPO%" ceylon.runtime ^
    +executable ceylon.modules.jboss.runtime.JBossRuntime ^
    %*

endlocal
