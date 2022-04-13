@if (%CYPRUS_HOME%) == () (CALL myEnvironment.bat Server) else (CALL %CYPRUS_HOME%\utils\myEnvironment.bat Server)
@Title	Export Cyprus Database - %CYPRUS_HOME% (%CYPRUS_DB_NAME%)
@Rem 
@Rem $Id: RUN_DBExport.bat,V 1.1 2021/01/01 Sanjiv/Mukesh Exp $
@Rem 
@Rem Parameter: <cyprusDBuser>/<cyprusDBpassword>

@call %CYPRUS_DB_PATH%\DBExport %CYPRUS_DB_USER% %CYPRUS_DB_PASSWORD%
@Rem call %CYPRUS_DB_PATH%\DBExportFull system %CYPRUS_DB_SYSTEM%

@Echo If the following statement fails, fix your environment
IF (%CYPRUS_HOME%) == () (CALL myDBcopy.bat) else (CALL %CYPRUS_HOME%\utils\myDBcopy.bat)

@Rem Sleep 60
@CHOICE /C YN /T 60 /D N > NUL