@Rem $Id: Start.bat,V 1.1 2021/01/01 Sanjiv/Mukesh Exp $

@Echo Starting Listener ....
lsnrctl start

@Echo Starting Database ....
@sqlplus "system/%CYPRUS_DB_SYSTEM%@%CYPRUS_DB_SERVER%:%CYPRUS_DB_PORT%/%CYPRUS_DB_NAME% AS SYSDBA" @%CYPRUS_HOME%\utils\%CYPRUS_DB_PATH%\Start.sql

@Echo Starting optional agent ....
agentctl start

@Echo ------------------------
lsnrctl status
