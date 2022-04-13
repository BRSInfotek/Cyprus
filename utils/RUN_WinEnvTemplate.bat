@Title Set Windows Environment
@Rem $Id: RUN_WinEnvTemplate.bat,V 1.1 2021/01/01 Sanjiv/Mukesh Exp $

@Echo ===================================
@Echo Setup Client Environment
@Echo ===================================

@cscript //nologo @CYPRUS_HOME@\utils\WinEnv.js "@CYPRUS_HOME@" "@JAVA_HOME@"

