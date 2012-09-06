@echo off
setlocal

call %~dp0\args.bat %*

if "%exit%" == "true" (
    exit /b 1
)

set "LIB=%CEYLON_HOME%\lib"

"%JAVA%" ^
    -cp "%LIB%\jboss-modules.jar;%LIB%\ceylon-runtime-bootstrap.jar" ^
    "-Dceylon.home=%CEYLON_HOME%" ^
    ceylon.modules.bootstrap.Main5 ^
    -mp "%CEYLON_REPO%" ceylon.runtime:%CEYLON_VERSION% ^
    +executable ceylon.modules.jboss.runtime.JBossRuntime ^
    %ARGS%

endlocal
