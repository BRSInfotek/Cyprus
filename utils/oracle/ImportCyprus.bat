@Echo	Cyprus Database Import		$Revision: 1.9 $

@Rem $Id: ImportCyprus.bat,V 1.1 2021/01/01 Sanjiv/Mukesh Exp $

@Echo	Importing Cyprus DB from %CYPRUS_HOME%\data\Cyprus.dmp (%CYPRUS_DB_NAME%)

@if (%CYPRUS_HOME%) == () goto environment
@if (%CYPRUS_DB_NAME%) == () goto environment
@if (%CYPRUS_DB_SERVER%) == () goto environment
@if (%CYPRUS_DB_PORT%) == () goto environment
@Rem Must have parameters systemAccount CyprusID CyprusPwd
@if (%1) == () goto usage
@if (%2) == () goto usage
@if (%3) == () goto usage

@echo -------------------------------------
@echo Re-Create DB user
@echo -------------------------------------
@sqlplus %1@%CYPRUS_DB_SERVER%:%CYPRUS_DB_PORT%/%CYPRUS_DB_NAME% @%CYPRUS_HOME%\Utils\%CYPRUS_DB_PATH%\CreateUser.sql %2 %3

@echo -------------------------------------
@echo Import Cyprus.dmp
@echo -------------------------------------
@imp %1@%CYPRUS_DB_SERVER%:%CYPRUS_DB_PORT%/%CYPRUS_DB_NAME% FILE=%CYPRUS_HOME%\data\Cyprus.dmp FROMUSER=(reference) TOUSER=%2 STATISTICS=RECALCULATE

echo -------------------------------------
echo Create SQLJ 
echo -------------------------------------
call %CYPRUS_HOME%\Utils\%CYPRUS_DB_PATH%\create %CYPRUS_DB_USER%/%CYPRUS_DB_PASSWORD%

@echo --------========--------========--------========--------
@echo System Check - The Import phase showed warnings. 
@echo This is OK as long as the following does not show errors
@echo --------========--------========--------========--------
@sqlplus %2/%3@%CYPRUS_DB_SERVER%:%CYPRUS_DB_PORT%/%CYPRUS_DB_NAME% @%CYPRUS_HOME%\Utils\%CYPRUS_DB_PATH%\AfterImport.sql

@goto end

:environment
@Echo Please make sure that the enviroment variables are set correctly:
@Echo		CYPRUS_HOME	e.g. D:\Cyprus
@Echo		CYPRUS_DB_NAME	e.g. dev1.cyprus.org

:usage
@echo Usage:		%0 <systemAccount> <CyprusID> <CyprusPwd>
@echo Example:	%0 system/manager Cyprus Cyprus

:end
