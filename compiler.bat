@echo off
setlocal EnableDelayedExpansion
echo "setando variavel"
set nomeBot=%1

:set "file=Bot.java"


echo "2"
:: contar o número de linhas do arquivo
set "lines=0"
for /f %%a in ('type "%nomeBot%.java" ^| find /v /c ""') do set /a lines=%%a

:: calcular a linha em que deve parar de ler o arquivo
set /a stopline=lines-8

:: ler as linhas do arquivo e escrevê-las em um novo arquivo temporário
set "tempfile=%temp%\tempfile.txt"
set "count=0"
(for /f "usebackq delims=" %%a in ("%nomeBot%.java") do (
    set /a count+=1
    if !count! leq !stopline! echo %%a
)) > "%tempfile%"

:: adicionar "}}" ao final do arquivo temporário para finalizar o bot
echo.>> "%tempfile%"
echo ^}^}>> "%tempfile%"

:: substituir o arquivo original pelo arquivo temporário
move /y "%tempfile%" "%nomeBot%.java" > nul


set "MAIN_CLASS=%nomeBot%"
set "JAR_FILE=%nomeBot%.jar"

::Compile todos os arquivos .java no diretório atual
javac %nomeBot%.java

::Cria um arquivo Manifest que especifica a classe principal
echo Main-Class: %MAIN_CLASS% > MANIFEST.MF

::Cria o arquivo JAR
jar cvmf MANIFEST.MF %JAR_FILE% *.class

:: remover o .java
:: del *.java

::mover os bot .jar para pasta bot
if EXIST ".\bot" (
	move .\%nomeBot%.jar .\bot 
) ELSE (
	mkdir bot
	move .\%nomeBot%.jar .\bot
)
