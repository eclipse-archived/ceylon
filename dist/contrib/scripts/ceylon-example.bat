@echo off
setlocal

rem This is a sample script that can be used as a template
rem for creating your own sub-commands for the "ceylon"
rem command. If you create a copy, and rename it to for
rem example "ceylon-mycommand.bat" and place it anywhere
rem in your $PATH, in ~/.ceylon/bin or in $CEYLON_HOME/bin
rem it will be found and automatically be added to the
rem list of existing commands (try typing "ceylon help").

set "USAGE=<arg1> <arg2>"
set "DESCRIPTION=Example command that does nothing"
set "LONG_USAGE=This script just shows how to create"
set "LONG_USAGE=%LONG_USAGE% custom 'ceylon' sub-commands."

call %CEYLON_HOME%\bin\ceylon-sh-setup.bat %*

if "%errorlevel%" == "1" (
    exit /b 0
)

rem REPLACE THE CODE BELOW THIS LINE WITH YOUR OWN CODE

rem Just showing some variables that the script can use
echo Ceylon home: %CEYLON_HOME%
echo Ceylon executable: %CEYLON%
echo Ceylon version major: %CEYLON_VERSION_MAJOR%
echo Ceylon version minor: %CEYLON_VERSION_MINOR%
echo Ceylon version release: %CEYLON_VERSION_RELEASE%
echo Ceylon version: %CEYLON_VERSION%
echo Ceylon version name: %CEYLON_VERSION_NAME%
echo Java home: %JAVA_HOME%
echo Invoked with arguments: %*

