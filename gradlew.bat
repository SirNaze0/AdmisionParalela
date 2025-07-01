@echo off

REM ------------------------------------------------------------------------------
REM Gradle startup script for Windows
REM ------------------------------------------------------------------------------

setlocal

set DIRNAME=%~dp0
if "%DIRNAME%"=="" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%

set DEFAULT_JVM_OPTS=-Xmx64m -Xms64m

set CLASSPATH=""

REM Find java.exe
if defined JAVA_HOME (
    set JAVA_EXE=%JAVA_HOME%\bin\java.exe
    if not exist "%JAVA_EXE%" (
        echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
        echo Please set the JAVA_HOME variable in your environment to match the location of your Java installation.
        exit /b 1
    )
) else (
    set JAVA_EXE=java
)

set GRADLE_WRAPPER_JAR=%APP_HOME%\gradle\wrapper\gradle-wrapper.jar

if not exist "%GRADLE_WRAPPER_JAR%" (
    echo ERROR: Could not find gradle-wrapper.jar in %GRADLE_WRAPPER_JAR%
    exit /b 1
)

set CMD_LINE_ARGS=
:parse_args
if "%1"=="" goto execute
set CMD_LINE_ARGS=%CMD_LINE_ARGS% "%1"
shift
goto parse_args

:execute
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% -Dorg.gradle.appname=%APP_BASE_NAME% -classpath %CLASSPATH% -jar "%GRADLE_WRAPPER_JAR%" %CMD_LINE_ARGS%

endlocal
