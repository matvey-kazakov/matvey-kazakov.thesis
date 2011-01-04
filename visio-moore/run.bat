@echo off
if [%JAVA_HOME%] == [] goto no_javahome
SET RUNJAVA=%JAVA_HOME%\bin\java.exe
goto run
:no_javahome
echo JAVA_HOME is not set. Trying to use default PATH
SET RUNJAVA=java.exe
:run
SET JAVA_OPTS=-Dswing.defaultlaf=com.sun.java.swing.plaf.windows.WindowsLookAndFeel
%RUNJAVA% %JAVA_OPTS% -jar visio-moore.jar