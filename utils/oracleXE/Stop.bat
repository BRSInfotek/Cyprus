@Rem $Id: Stop.bat,V 1.1 2021/01/01 Sanjiv/Mukesh Exp $

@Echo Stopping database ....
@sqlplus "system/%CYPRUS_DB_SYSTEM%@%CYPRUS_DB_SERVER%:%CYPRUS_DB_PORT%/%CYPRUS_DB_NAME% AS SYSDBA" @%CYPRUS_HOME%\utils\%CYPRUS_DB_PATH%\Stop.sql

@Echo Stopping Listener ....
lsnrctl stop

@Echo Stopping (optional) Agent ....
agentctl stop

