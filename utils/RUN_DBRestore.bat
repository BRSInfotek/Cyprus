@Rem $Id: RUN_DBRestore.bat,V 1.1 2021/01/01 Sanjiv/Mukesh Exp $

@if (%CYPRUS_HOME%) == () (CALL myEnvironment.bat Server) else (CALL %CYPRUS_HOME%\utils\myEnvironment.bat Server)
@Title Restore Cyprus Database from Export - %CYPRUS_HOME% (%CYPRUS_DB_NAME%)


@echo Re-Create Cyprus User and import %CYPRUS_HOME%\data\ExpDat.dmp
@dir %CYPRUS_HOME%\data\ExpDat.dmp
@echo == The import will show warnings. This is OK ==
@pause

@Rem Parameter: <systemAccount> <cyprusID> <cyprusPwd>
@Rem globalqss - cruiz - 2007-10-09 - added fourth parameter for postgres (ignored in oracle)
@call %CYPRUS_DB_PATH%\DBRestore system/%CYPRUS_DB_SYSTEM% %CYPRUS_DB_USER% %CYPRUS_DB_PASSWORD% %CYPRUS_DB_SYSTEM%

@Call %CYPRUS_HOME%\utils\RUN_SignDatabaseBuild.bat > NUL 2>&1

@pause
