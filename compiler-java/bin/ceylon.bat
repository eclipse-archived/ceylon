@echo off
setlocal

call %~dp0\ceylon-cp.bat %*

if "%exit%" == "true" (
    exit /b 1
)

rem set JAVA_DEBUG_OPTS="-Xrunjdwp:transport=dt_socket,address=8787,server=y,suspend=y"

if NOT "%PRESERVE_JAVA_OPTS%" == "true" (
	set PREPEND_JAVA_OPTS=%JAVA_DEBUG_OPTS%
	rem "Other java opts go here"
)
set "JAVA_OPTS=%PREPEND_JAVA_OPTS% %JAVA_OPTS%"

set "LIB=%CEYLON_HOME%\lib"
set "REPO=%CEYLON_HOME%\repo"
set "RUNTIME_REPO=%CEYLON_HOME%\runtime-repo"

set "JAVA_CP=%LIB%\ceylon-runtime-bootstrap.jar"
set "JAVA_CP=%JAVA_CP%;%MINIMAL_CP%"

"%JAVA%" ^
    -cp %JAVA_CP% ^
    "-Dceylon.home=%CEYLON_HOME%" ^
    "-Dceylon.system.repo=%CEYLON_REPO%" ^
    %JAVA_OPTS% ^
    com.redhat.ceylon.tools.CeylonTool ^
    %ARGS%

endlocal
