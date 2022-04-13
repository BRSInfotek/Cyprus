@Title Cyprus Environment Check

@Rem $Id: RUN_Env.bat,V 1.1 2021/01/01 Sanjiv/Mukesh Exp $

@if (%CYPRUS_HOME%) == () (CALL myEnvironment.bat) else (CALL %CYPRUS_HOME%\utils\myEnvironment.bat)

@Echo General ...
@Echo PATH      = %PATH%
@Echo CLASSPTH  = %CLASSPATH%

@Echo .
@Echo Homes ...
@Echo CYPRUS_HOME        = %CYPRUS_HOME%
@Echo JAVA_HOME            = %JAVA_HOME%
@Echo CYPRUS_DB_URL      = %CYPRUS_DB_URL%

@Echo .
@Echo Database ...
@Echo CYPRUS_DB_USER     = %CYPRUS_DB_USER%
@Echo CYPRUS_DB_PASSWORD = %CYPRUS_DB_PASSWORD%
@Echo CYPRUS_DB_PATH     = %CYPRUS_DB_PATH%

@Echo .. Oracle specifics
@Echo CYPRUS_DB_NAME      = %CYPRUS_DB_NAME%
@Echo CYPRUS_DB_SYSTEM   = %CYPRUS_DB_SYSTEM%

%JAVA_HOME%\bin\java -version

@Echo .
@Echo Java Version should be "1.4.2"
@Echo ---------------------------------------------------------------
@Pause

@Echo .
@Echo ---------------------------------------------------------------
@Echo Database Connection Test (1) ... %CYPRUS_DB_NAME%
@Echo If this fails, verify the CYPRUS_DB_NAME setting with Oracle Net Manager
@Echo You should see an OK at the end
@Pause
tnsping %CYPRUS_DB_NAME%

@Echo .
@Echo ---------------------------------------------------------------
@Echo Database Connection Test (3) ... system/%CYPRUS_DB_SYSTEM% in %CYPRUS_DB_HOME%
@Echo If this test fails, verify the system password in CYPRUS_DB_SYSTEM
@Pause
sqlplus system/%CYPRUS_DB_SYSTEM%@%CYPRUS_DB_NAME% @%CYPRUS_DB_HOME%\Test.sql

@Echo .
@Echo ---------------------------------------------------------------
@Echo Checking Database Size
@Pause
sqlplus system/%CYPRUS_DB_SYSTEM%@%CYPRUS_DB_NAME% @%CYPRUS_DB_HOME%\CheckDB.sql %CYPRUS_DB_USER%

@Echo .
@Echo ---------------------------------------------------------------
@Echo Database Connection Test (4) ... %CYPRUS_DB_USER%/%CYPRUS_DB_PASSWORD%
@Echo This may fail, if you have not imported the Cyprus database yet - Just enter EXIT and run this script again after the import
@Pause
sqlplus %CYPRUS_DB_USER%/%CYPRUS_DB_PASSWORD%@%CYPRUS_DB_NAME% @%CYPRUS_DB_HOME%\Test.sql

@Echo .
@Echo Done
@pause
