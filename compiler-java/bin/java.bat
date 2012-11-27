::
:: Only check the registry if JAVA_HOME is not already set
::
IF NOT "%JAVA_HOME%" == "" (
    exit /b 0
)

::
:: Find Java in the registry
::
set "KEY_NAME=HKLM\SOFTWARE\JavaSoft\Java Runtime Environment"
set "KEY_NAME2=HKLM\SOFTWARE\Wow6432Node\JavaSoft\Java Runtime Environment"


::
:: get the current version
::
FOR /F "usebackq skip=2 tokens=3" %%A IN (`REG QUERY "%KEY_NAME%" /v CurrentVersion 2^>nul`) DO (
    set "ValueValue=%%A"
)

if "%ValueValue%" NEQ "" (
    set "JAVA_CURRENT=%KEY_NAME%\%ValueValue%"
) else (
    rem Try again for 64bit systems
    
    FOR /F "usebackq skip=2 tokens=3" %%A IN (`REG QUERY "%KEY_NAME2%" /v CurrentVersion 2^>nul`) DO (
        set "JAVA_CURRENT=%KEY_NAME2%\%%A"
    )
)

if "%JAVA_CURRENT%" == "" (
    @echo Java not found, you must install Java in order to compile and run Ceylon programs
    @echo Go to http://www.java.com/getjava/ to download the latest version of Java
    exit /b 0
)

::
:: get the javahome
::
FOR /F "usebackq skip=2 tokens=3*" %%A IN (`REG QUERY "%JAVA_CURRENT%" /v JavaHome 2^>nul`) DO (
    set "JAVA_HOME=%%A %%B"
)
