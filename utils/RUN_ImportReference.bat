@Rem $Id: RUN_ImportReference.bat,V 1.1 2021/01/01 Sanjiv/Mukesh Exp $

@if (%CYPRUS_HOME%) == () (CALL myEnvironment.bat Server) else (CALL %CYPRUS_HOME%\utils\myEnvironment.bat Server)
@Title Import Reference - %CYPRUS_HOME% (%CYPRUS_DB_NAME%)


@echo Re-Create Reference User and import %CYPRUS_HOME%\data\Cyprus.dmp - (%CYPRUS_DB_NAME%)
@dir %CYPRUS_HOME%\data\Cyprus.dmp
@echo == The import will show warnings. This is OK ==
@pause

@Rem Parameter: <systemAccount> <CyprusID> <CyprusPwd>
@call %CYPRUS_DB_PATH%\ImportCyprus system/%CYPRUS_DB_SYSTEM% reference reference

@pause
