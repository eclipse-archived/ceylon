@echo off
setlocal

rem Find Java
call %CEYLON_HOME%\bin\java.bat

if "%JAVA_HOME%" == "" (
    exit /b 1
)

set "JAVA=%JAVA_HOME%\bin\java.exe"

rem Check that Java executable actually exists
if not exist "%JAVA%" (
    @echo "Cannot find java.exe at %JAVA%, check that your JAVA_HOME variable is pointing to the right place"
    exit /b 1
)

rem set JAVA_DEBUG_OPTS="-Xrunjdwp:transport=dt_socket,address=8787,server=y,suspend=y"

if NOT "%PRESERVE_JAVA_OPTS%" == "true" (
	set PREPEND_JAVA_OPTS=%JAVA_DEBUG_OPTS%
	rem "Other java opts go here"
)
set "JAVA_OPTS=%PREPEND_JAVA_OPTS% %JAVA_OPTS%"

set "LIB=%CEYLON_HOME%\lib"

"%JAVA%" ^
    %JAVA_OPTS% ^
    -jar "%LIB%\ceylon-bootstrap.jar"
    %*

endlocal
