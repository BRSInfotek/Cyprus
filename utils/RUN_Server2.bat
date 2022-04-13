@if (%CYPRUS_HOME%) == () (CALL myEnvironment.bat Server) else (CALL %CYPRUS_HOME%\utils\myEnvironment.bat Server)
@Title Cyprus Server Start - %CYPRUS_HOME% (%CYPRUS_APPS_TYPE%)

@Rem $Id: RUN_Server2.bat,V 1.1 2021/01/01 Sanjiv/Mukesh Exp $

@Rem  To use your own Encryption class (implementing org.cyprusbrs.util.SecureInterface),
@Rem  you need to set it here (and in the client start script) - example:
@Rem  SET SECURE=-DCYPRUS_SECURE=org.cyprusbrs.util.Secure
@SET SECURE=


@IF '%CYPRUS_APPS_TYPE%' == 'jboss' GOTO JBOSS
@GOTO UNSUPPORTED

:JBOSS
@Set NOPAUSE=Yes
@Set JAVA_OPTS=-server %CYPRUS_JAVA_OPTIONS% %SECURE% -Dorg.cyprus.server.embedded=true

@Echo Start Cyprus Apps Server %CYPRUS_HOME% (%CYPRUS_DB_NAME%)
@Call %JBOSS_HOME%\bin\run -c cyprus -b %CYPRUS_APPS_SERVER%
@Echo Done Cyprus Apps Server %CYPRUS_HOME% (%CYPRUS_DB_NAME%)
@GOTO END

:UNSUPPORTED
@Echo Apps Server start of %CYPRUS_APPS_TYPE% not supported

:END
@Rem Sleep 60
@CHOICE /C YN /T 60 /D N > NUL

@Exit