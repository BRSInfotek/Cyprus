@Echo	Cyprus Database Export 	$Revision: 1.8 $

@Rem $Id: DBExport.bat,V 1.1 2021/01/01 Sanjiv/Mukesh Exp $
@Rem 
@Echo Saving database %1@%CYPRUS_DB_NAME% to %CYPRUS_HOME%\data\ExpDat.dmp

@if (%CYPRUS_HOME%) == () goto environment
@if (%CYPRUS_DB_NAME%) == () goto environment
@if (%CYPRUS_DB_SERVER%) == () goto environment
@if (%CYPRUS_DB_PORT%) == () goto environment
@Rem Must have parameter: userAccount
@if (%1) == () goto usage

@Rem Cleanup
@sqlplus %1/%2@%CYPRUS_DB_SERVER%:%CYPRUS_DB_PORT%/%CYPRUS_DB_NAME% @%CYPRUS_HOME%\utils\%CYPRUS_DB_PATH%\Daily.sql

@Rem The Export
@exp %1/%2@%CYPRUS_DB_SERVER%:%CYPRUS_DB_PORT%/%CYPRUS_DB_NAME% FILE=%CYPRUS_HOME%\data\ExpDat.dmp Log=%CYPRUS_HOME%\data\ExpDat.log CONSISTENT=Y STATISTICS=NONE OWNER=%1

@cd %CYPRUS_HOME%\Data
@copy ExpDat.jar ExpDatOld.jar
@jar cvfM ExpDat.jar ExpDat.dmp ExpDat.log

@goto end

:environment
@Echo Please make sure that the enviroment variables are set correctly:
@Echo		CYPRUS_HOME	e.g. D:\Cyprus
@Echo		CYPRUS_DB_NAME 	e.g. cyprus.cyprus.org

:usage
@echo Usage:	%0 <userAccount>
@echo Examples:	%0 cyprus cyprus

:end
