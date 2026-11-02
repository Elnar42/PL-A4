@echo off
setlocal
cd /d "%~dp0"

where javac >nul 2>&1
if errorlevel 1 (
    echo JDK 17 or newer is required. Install a JDK and add its bin folder to PATH.
    exit /b 1
)

if "%~3"=="" (
    echo Usage: run.bat ^<file.jpg^> ^<square size^> ^<S or M^>
    echo Example: run.bat image.jpg 20 S
    exit /b 1
)

if not exist out mkdir out
dir /s /b src\*.java > sources.txt
javac --release 17 -d out @sources.txt
set COMPILE_STATUS=%ERRORLEVEL%
del sources.txt
if not "%COMPILE_STATUS%"=="0" exit /b %COMPILE_STATUS%

java -cp out Main "%~1" "%~2" "%~3"
exit /b %ERRORLEVEL%
