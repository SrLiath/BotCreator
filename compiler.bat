@echo off
setlocal EnableDelayedExpansion

set "file=Bot.java"

:: contar o número de linhas do arquivo
set "lines=0"
for /f %%a in ('type "%file%" ^| find /v /c ""') do set /a lines=%%a

:: calcular a linha em que deve parar de ler o arquivo
set /a stopline=lines-8

:: ler as linhas do arquivo e escrevê-las em um novo arquivo temporário
set "tempfile=%temp%\tempfile.txt"
set "count=0"
(for /f "usebackq delims=" %%a in ("%file%") do (
    set /a count+=1
    if !count! leq !stopline! echo %%a
)) > "%tempfile%"

:: adicionar "}}" ao final do arquivo temporário
echo.>> "%tempfile%"
echo ^}^}>> "%tempfile%"

:: substituir o arquivo original pelo arquivo temporário
move /y "%tempfile%" "%file%" > nul

set "MAIN_CLASS=Bot"
set "JAR_FILE=Bot.jar"

rem Compile todos os arquivos .java no diretório atual
javac *.java

rem Crie um arquivo Manifest que especifica a classe principal
echo Main-Class: %MAIN_CLASS% > MANIFEST.MF

rem Crie o arquivo JAR
jar cvmf MANIFEST.MF %JAR_FILE% *.class

:: remover o .java
:: del *.java