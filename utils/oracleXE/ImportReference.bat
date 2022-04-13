@Echo	Cyprus Reference Database Import 	$Revision: 1.4 $

@Rem $Id: ImportReference.bat,V 1.1 2021/01/01 Sanjiv/Mukesh Exp $

@Echo	Importing Reference DB from %CYPRUS_HOME%\data\Reference.dmp

@if (%CYPRUS_HOME%) == () goto environment
@if (%CYPRUS_DB_NAME%) == () goto environment
@if (%CYPRUS_DB_SERVER%) == () goto environment
@if (%CYPRUS_DB_PORT%) == () goto environment
@Rem Must have parameter: systemAccount
@if (%1) == () goto usage

@echo -------------------------------------
@echo Re-Create new user
@echo -------------------------------------
@sqlplus %1@%CYPRUS_DB_SERVER%:%CYPRUS_DB_PORT%/%CYPRUS_DB_NAME% @%CYPRUS_HOME%\Utils\%CYPRUS_DB_PATH%\CreateUser.sql Reference Cyprus

@echo -------------------------------------
@echo Import Reference
@echo -------------------------------------
imp %1@%CYPRUS_DB_NAME% FILE=%CYPRUS_HOME%\data\Reference.dmp FROMUSER=(reference) TOUSER=reference

@echo -------------------------------------
@echo Check System
@echo Import may show some warnings. This is OK as long as the following does not show errors
@echo -------------------------------------
@sqlplus reference/cyprus@%CYPRUS_DB_SERVER%:%CYPRUS_DB_PORT%/%CYPRUS_DB_NAME% @%CYPRUS_HOME%\Utils\%CYPRUS_DB_PATH%\AfterImport.sql

@goto end

:environment
@Echo Please make sure that the enviroment variables are set correctly:
@Echo		CYPRUS_HOME	e.g. D:\Cyprus
@Echo		CYPRUS_DB_NAME e.g. dev1.cyprus.org

:usage
@echo Usage:		%0 <systemAccount>
@echo Examples:	%0 system/manager

:end
