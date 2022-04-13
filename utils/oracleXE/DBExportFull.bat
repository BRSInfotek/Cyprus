@Echo	Cyprus Full Database Export 	$Revision: 1.6 $

@Rem $Id: DBExportFull.bat,V 1.1 2021/01/01 Sanjiv/Mukesh Exp $

@Echo Saving database %1@%CYPRUS_DB_NAME% to %CYPRUS_HOME%\data\ExpDatFull.dmp

@if (%CYPRUS_HOME%) == () goto environment
@if (%CYPRUS_DB_NAME%) == () goto environment
@if (%CYPRUS_DB_SERVER%) == () goto environment
@if (%CYPRUS_DB_PORT%) == () goto environment
@Rem Must have parameter: systemAccount
@if (%1) == () goto usage


@sqlplus %1/%2@%CYPRUS_DB_SERVER%:%CYPRUS_DB_PORT%/%CYPRUS_DB_NAME% @%CYPRUS_HOME%\utils\%CYPRUS_DB_PATH%\Daily.sql


@exp %1/%2@%CYPRUS_DB_SERVER%:%CYPRUS_DB_PORT%/%CYPRUS_DB_NAME% FILE=%CYPRUS_HOME%\data\ExpDatFull.dmp Log=%CYPRUS_HOME%\data\ExpDatFull.log CONSISTENT=Y STATISTICS=NONE FULL=Y

@cd %CYPRUS_HOME%\data
@jar cvfM data\ExpDatFull.jar ExpDatFull.dmp  ExpDatFull.log

@goto end

:environment
@Echo Please make sure that the enviroment variables are set correctly:
@Echo		CYPRUS_HOME	e.g. D:\Cyprus
@Echo		CYPRUS_DB_NAME	e.g. dev1.cyprus.org

:usage
@echo Usage:	%0 <systemAccount>
@echo Examples:	%0 system/manager

:end
