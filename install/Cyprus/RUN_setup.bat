@Title Install Cyprus Server
@Rem  $Header: /cvsroot/cyprus/install/Cyprus/RUN_setup.bat,V 1.1 2021/01/01 Sanjiv/Mukesh Exp $
@Echo off


@if not "%JAVA_HOME%" == "" goto JAVA_HOME_OK
@Set JAVA=java
@Echo JAVA_HOME is not set.  
@Echo You may not be able to start the required Setup window !!
@Echo Set JAVA_HOME to the directory of your local 1.8 JDK.
@Echo If you experience problems, run utils/WinEnv.js
@Echo Example: cscript utils\WinEnv.js C:\Cyprus "C:\Program Files\Java\jdk1.8.0_281"
goto START

:JAVA_HOME_OK
@Set JAVA=%JAVA_HOME%\bin\java


:START
@Echo =======================================
@Echo Starting Setup Dialog ...
@Echo =======================================
@SET CP=lib\CInstall.jar;lib\Cyprus.jar;lib\CCTools.jar;lib\oracle.jar;lib\jboss.jar;lib\postgresql.jar;

@Rem Trace Level Parameter, e.g. SET ARGS=ALL
@SET ARGS=CONFIG

@Rem To test the OCI driver, add -DTestOCI=Y to the command - example:
@Rem %JAVA% -classpath %CP% -DCYPRUS_HOME=%CYPRUS_HOME% -DTestOCI=Y org.cyprusbrs.install.Setup %ARGS%

@"%JAVA%" -classpath %CP% -DCYPRUS_HOME=%CYPRUS_HOME% org.cyprusbrs.install.Setup %ARGS%
@Echo ErrorLevel = %ERRORLEVEL%
@IF NOT ERRORLEVEL = 1 GOTO NEXT
@Echo ***************************************
@Echo Check the error message above.
@Echo ***************************************
@Echo Make sure that the environment is set correctly!
@Echo Set environment variable JAVA_HOME manually
@Echo or use WinEnv.js in the util directory
@Echo ***************************************
@Pause
@Exit


:NEXT

cd utils

@Rem ===================================
@Rem Sign Database Build
@Rem ===================================
@Call RUN_SignDatabaseBuild.bat > NUL 2>&1

@Rem ===================================
@Rem Setup Cyprus Environment
@Rem ===================================
@Call RUN_WinEnv.bat

@Rem ===================================
@Rem Run Ant directly
@Rem ===================================
@Rem %JAVA% -classpath lib\CInstall.jar; -DCYPRUS_HOME=%CYPRUS_HOME% -Dant.home="." org.apache.tools.ant.launch.Launcher setup


@Rem ================================
@Rem Test local Connection
@Rem ================================
@Rem %JAVA% -classpath lib\Cyprus.jar;lib\CyprusCLib.jar org.cyprusbrs.install.ConnectTest localhost


@Echo .
@Echo For problems, check log file in base directory
@Rem Wait 10 second
@PING 1.1.1.1 -n 1 -w 10000 > NUL
