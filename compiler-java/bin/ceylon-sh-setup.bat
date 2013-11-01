
:loop_args
    if "%~1" == "" goto :done_args
    
    if "%~1" == "--_print-summary" (
        echo "%DESCRIPTION%"
        exit
    )
    if "%~1" == "--_print-description" (
        echo "%LONG_USAGE%"
        exit
    )
    if "%~1" == "--_print-usage" (
        echo "%USAGE%"
        exit
    )
    shift
    goto :loop_args
)
:done_args

