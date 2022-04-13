@if (%CYPRUS_HOME%) == () (CALL myEnvironment.bat Server) else (CALL %CYPRUS_HOME%\utils\myEnvironment.bat Server)
@Title Export Database ExpDat.jar - %CYPRUS_HOME%

@Rem $Id: RUN_PutExportTemplate.bat,V 1.1 2021/01/01 Sanjiv/Mukesh Exp $

@Echo ........ Export DB
@call %CYPRUS_DB_PATH%\DBExport %CYPRUS_DB_USER%/%CYPRUS_DB_PASSWORD%

@Rem 	Echo ........ Stop DB
@Rem	sqlplus "system/%CYPRUS_DB_SYSTEM% AS SYSDBA" @%CYPRUS_HOME%\utils\%CYPRUS_BP_PATH%\Stop.sql

@Title Transafer Database ExpDat.jar - %CYPRUS_HOME%\data
@Echo Transfer Database ExpDat.jar - %CYPRUS_HOME%\data

@Echo ........ FTP
@ping @CYPRUS_FTP_SERVER@
@cd %CYPRUS_HOME%\data
@dir ExpDat.*

@ftp -s:%CYPRUS_HOME%\utils\ftpPutExport.txt

@Echo ........ Done
@pause
