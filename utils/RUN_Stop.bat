@if (%CYPRUS_HOME%) == () (CALL myEnvironment.bat Server) else (CALL %CYPRUS_HOME%\utils\myEnvironment.bat Server)
@Title Stop Cyprus  - %CYPRUS_HOME% (%CYPRUS_DB_NAME%)

@Rem $Id: RUN_Stop.bat,V 1.1 2021/01/01 Sanjiv/Mukesh Exp $

@CALL %CYPRUS_HOME%\utils\RUN_Server2Stop.bat

@CALL %CYPRUS_DB_PATH%\Stop.bat

