@Rem $Id: RUN_ImportCyprus.bat,V 1.1 2021/01/01 Sanjiv/Mukesh Exp $

@if (%CYPRUS_HOME%) == () (CALL myEnvironment.bat Server) else (CALL %CYPRUS_HOME%\utils\myEnvironment.bat Server)
@Title Import Cyprus - %CYPRUS_HOME% (%CYPRUS_DB_NAME%)


@echo Re-Create Cyprus User and import %CYPRUS_HOME%\data\Cyprus.dmp - (%CYPRUS_DB_NAME%)
@dir %CYPRUS_HOME%\data\Cyprus.dmp
@echo == The import will show warnings. This is OK ==
@pause

@Rem Parameter: <systemAccount> <CyprusID> <CyprusPwd>
@Rem globalqss - cruiz - 2007-10-09 - added fourth parameter for postgres (ignored in oracle)
@call %CYPRUS_DB_PATH%\ImportCyprus system/%CYPRUS_DB_SYSTEM% %CYPRUS_DB_USER% %CYPRUS_DB_PASSWORD% %CYPRUS_DB_SYSTEM%

@call %CYPRUS_HOME%\utils\RUN_SignDatabaseBuild > NUL 2>&1

@pause
