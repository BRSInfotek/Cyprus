@if (%CYPRUS_HOME%) == () (CALL myEnvironment.bat Server) else (CALL %CYPRUS_HOME%\utils\myEnvironment.bat Server)
@Title Stop DataBase Service  - %CYPRUS_HOME% (%CYPRUS_DB_NAME%)

@Rem $Id: RUN_DBStop.bat,V 1.1 2021/01/01 Sanjiv/Mukesh Exp $

@CALL %CYPRUS_DB_PATH%\Stop.bat
@Echo Done stopping database %CYPRUS_HOME% (%CYPRUS_DB_NAME%)

@Rem Sleep 60
@CHOICE /C YN /T 60 /D N > NUL