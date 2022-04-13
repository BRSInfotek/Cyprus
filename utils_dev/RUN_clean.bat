@Title Cyprus Clean
@Rem $Header: /cvsroot/cyprus/utils_dev/RUN_clean.bat,V 1.1 2021/01/01 Sanjiv/Mukesh Exp $

@Rem Check java home
@IF NOT EXIST "%JAVA_HOME%\bin" ECHO "** JAVA_HOME NOT found"
@SET PATH=%JAVA_HOME%\bin;%PATH%

@Rem Check jdk
@IF NOT EXIST "%JAVA_HOME%\lib\tools.jar" ECHO "** Need Full Java SDK **"

@Rem Set ant classpath
@SET ANT_CLASSPATH=%CLASSPATH%;..\tools\lib\ant.jar;..\tools\lib\ant-launcher.jar;..\tools\lib\ant-swing.jar;..\tools\lib\ant-commons-net.jar;..\tools\lib\commons-net-1.4.0.jar
@SET ANT_CLASSPATH=%ANT_CLASSPATH%;"%JAVA_HOME%\lib\tools.jar"

@echo Cleanup ...
@"%JAVA_HOME%\bin\java" -classpath %ANT_CLASSPATH% -Dant.home="." %ANT_PROPERTIES% org.apache.tools.ant.Main clean

@Pause
@exit
