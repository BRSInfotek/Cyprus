@if (%CYPRUS_HOME%) == () (CALL myEnvironment.bat Server) else (CALL %CYPRUS_HOME%\utils\myEnvironment.bat Server)
@Title Download Cyprus.jar Database into %CYPRUS_HOME%\data

@Rem $Id: RUN_GetCyprusTemplate.bat,V 1.1 2021/01/01 Sanjiv/Mukesh Exp $

@Echo Download Cyprus.jar Database into %CYPRUS_HOME%\data

@ping @CYPRUS_FTP_SERVER@
@cd %CYPRUS_HOME%\data
@del Cyprus.jar

@ftp -s:%CYPRUS_HOME%\utils\ftpGetCyprus.txt

@Echo Unpacking ...
@jar xvf Cyprus.jar

@Echo ........ Received

@cd %CYPRUS_HOME%\utils
@START RUN_ImportCyprus.bat

@pause
