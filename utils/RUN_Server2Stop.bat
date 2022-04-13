@if (%CYPRUS_HOME%) == () (CALL myEnvironment.bat Server) else (CALL %CYPRUS_HOME%\utils\myEnvironment.bat Server)
@Title Cyprus Server Stop - %CYPRUS_HOME%

@Rem $Id: RUN_Server2Stop.bat,V 1.1 2021/01/01 Sanjiv/Mukesh Exp $

@IF '%CYPRUS_APPS_TYPE%' == 'jboss' GOTO JBOSS
@GOTO UNSUPPORTED

:JBOSS
@Set NOPAUSE=Yes
@Set JBOSS_LIB=%JBOSS_HOME%\lib
@Set JBOSS_SERVERLIB=%JBOSS_HOME%\server\cyprus\lib
@Set JBOSS_CLASSPATH=%CYPRUS_HOME%\lib\jboss.jar;%JBOSS_LIB%\jboss-system.jar

@CD %JBOSS_HOME%\bin
Call shutdown --server=jnp://%CYPRUS_APPS_SERVER%:%CYPRUS_JNP_PORT% --shutdown

@Echo Done Stopping Cyprus Apps Server %CYPRUS_HOME% (%CYPRUS_DB_NAME%)
@GOTO END

:UNSUPPORTED
@Echo Apps Server stop of %CYPRUS_APPS_TYPE% not supported

:END
@Rem Sleep 30
@CHOICE /C YN /T 30 /D N > NUL

@Exit