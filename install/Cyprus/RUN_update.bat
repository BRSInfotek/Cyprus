@Title Install Cyprus Server
@Rem  $Header: /cvsroot/cyprus/install/Cyprus/RUN_setup.bat,V 1.1 2021/01/01 Sanjiv/Mukesh Exp $
@Echo off


@if not "%JAVA_HOME%" == "" goto JAVA_HOME_OK
@Set JAVA=java
@Echo JAVA_HOME is not set.  
@Echo You may not be able to start the required Setup window !!
@Echo Set JAVA_HOME to the directory of your local 1.5 JDK.
@Echo If you experience problems, run utils/WinEnv.js
@Echo Example: cscript utils\WinEnv.js C:\Cyprus "C:\Program Files\Java\jdk1.5.0_04"
goto START

:JAVA_HOME_OK
@Set JAVA=%JAVA_HOME%\bin\java

:START
@"%JAVA%" -classpath lib\CInstall.jar; -DCYPRUS_HOME=%CYPRUS_HOME% -Dant.home="." org.apache.tools.ant.launch.Launcher update
@Rem Sleep 10
@CHOICE /C YN /T 10 /D N > NUL