@echo off
cls

setlocal
for /f "delims=" %%f in ('dir /b /a:-d /o-d "SitemapParser-v*.jar" 2^>nul') do (
    set "JAR=%%f"
    goto :run
)

echo No SitemapParser jar found in "%cd%".
exit /b 1

:run
java -Dlogback.configurationFile=logback.xml -jar "%JAR%" %1
