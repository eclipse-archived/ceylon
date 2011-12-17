@echo off
setlocal

call %~dp0\args.bat %*

if "%exit%" == "true" (
    exit /b 1
)

set "LIB=%CEYLON_HOME%\lib"
set "RUNTIME_REPO=%CEYLON_HOME%\runtime-repo"

"%JAVA%" ^
    -cp "%LIB%\jboss-modules.jar;%LIB%\ceylon-runtime-bootstrap.jar" ^
    "-Dceylon.home=%CEYLON_HOME%" ^
    org.jboss.modules.Main ^
    -mp "%RUNTIME_REPO%" ceylon.runtime ^
    +executable ceylon.modules.jboss.runtime.JBossRuntime ^
    %ARGS%

endlocal
