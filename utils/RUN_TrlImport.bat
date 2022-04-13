@Rem $Id: RUN_TrlImport.bat,V 1.1 2021/01/01 Sanjiv/Mukesh Exp $

@if (%CYPRUS_HOME%) == () (CALL myEnvironment.bat Server) else (CALL %CYPRUS_HOME%\utils\myEnvironment.bat Server)
@Title Import Translation - %CYPRUS_HOME% (%CYPRUS_DB_NAME%)

@SET AD_LANGUAGE=de_DE
@SET DIRECTORY=%CYPRUS_HOME%\data\%AD_LANGUAGE%

@echo This Procedure imports language %AD_LANGUAGE% from directory %DIRECTORY%
@pause

@"%JAVA_HOME%\bin\java" -cp %CLASSPATH% org.compiere.install.Translation %DIRECTORY% %AD_LANGUAGE% import

@pause
