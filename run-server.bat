@echo off
setlocal
set "JAVA_HOME=C:\Program Files\Java\jdk-21"
echo ===================================================================
echo   Student Assignment Management System - Server Launcher
echo ===================================================================
echo   Tomcat: Apache Tomcat 11.0.4
echo   Java:   JDK 21
echo   URL:    http://localhost:8080/StudentAssignmentSystem/
echo ===================================================================
call "C:\Users\hp\Downloads\apache-tomcat-11.0.24-windows-x64\apache-tomcat-11.0.4\bin\catalina.bat" run
endlocal
