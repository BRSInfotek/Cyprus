@if (%CYPRUS_HOME%) == () (CALL myEnvironment.bat Server) else (CALL %CYPRUS_HOME%\utils\myEnvironment.bat Server)
@Title Start Cyprus  - %CYPRUS_HOME% (%CYPRUS_DB_NAME%)

@Rem $Id: RUN_Start.bat,V 1.1 2021/01/01 Sanjiv/Mukesh Exp $

@Echo Starting Database
@CALL %CYPRUS_DB_PATH%\Start.bat

@START %CYPRUS_HOME%\utils\RUN_Server2.bat 
