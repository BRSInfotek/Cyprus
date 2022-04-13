@Title Install Cyprus Server
@Rem  $Header: /cvsroot/cyprus/install/Cyprus/RUN_setup.bat,V 1.1 2021/01/01 Sanjiv/Mukesh Exp $
@Echo off

@if (%CYPRUS_HOME%) == () (CALL myEnvironment.bat) else (CALL %CYPRUS_HOME%\utils\myEnvironment.bat)

@Set JAVA=%JAVA_HOME%\bin\java

@Echo =======================================
@Echo Sign Database Build
@Echo =======================================
@SET CP=%CYPRUS_HOME%\lib\CInstall.jar;%CYPRUS_HOME%\lib\Cyprus.jar;%CYPRUS_HOME%\lib\CCTools.jar;%CYPRUS_HOME%\lib\oracle.jar;%CYPRUS_HOME%\lib\jboss.jar;%CYPRUS_HOME%\lib\postgresql.jar;

@"%JAVA%" -classpath %CP% -DCYPRUS_HOME=%CYPRUS_HOME% org.adempiere.process.SignDatabaseBuild
