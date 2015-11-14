@echo off
setlocal enabledelayedexpansion

:loop_args
    if "%~1" == "" goto :done_args
    
    if "%~1" == "--_print-summary" (
        echo !DESCRIPTION!
        exit /b 1
    )
    if "%~1" == "--_print-description" (
        echo !LONG_USAGE!
        exit /b 1
    )
    if "%~1" == "--_print-usage" (
        echo !USAGE!
        exit /b 1
    )
    shift
    goto :loop_args
)
:done_args
