@Title	Sign jars in %CYPRUS_HOME%\lib\
@Rem
@Rem	Sign all jars for WebStart with your certificate
@Rem	Keystore located at c:\cyprus\cyprus-all\keystore\myKeystore
@Rem	Keystore passwords both %KEY_PASSWORD% (default myPassword)
@Rem	Jar files are located in the deployment directory %CYPRUS_HOME%\lib
@Rem
@Rem	After this, you need to RUN_setup 
@Rem	to generate/copy the webstart/jnlp distribution

jarsigner -keystore c:\cyprus\cyprus-all2\keystore\myKeystore -storepass %KEY_PASSWORD% -keypass %KEY_PASSWORD% %CYPRUS_HOME%\lib\CClient.jar cyprus
jarsigner -keystore c:\cyprus\cyprus-all2\keystore\myKeystore -storepass %KEY_PASSWORD% -keypass %KEY_PASSWORD% %CYPRUS_HOME%\lib\CTools.jar cyprus
jarsigner -keystore c:\cyprus\cyprus-all2\keystore\myKeystore -storepass %KEY_PASSWORD% -keypass %KEY_PASSWORD% %CYPRUS_HOME%\lib\oracle.jar cyprus
jarsigner -keystore c:\cyprus\cyprus-all2\keystore\myKeystore -storepass %KEY_PASSWORD% -keypass %KEY_PASSWORD% %CYPRUS_HOME%\lib\jPDFPrinterDemo.jar cyprus
jarsigner -keystore c:\cyprus\cyprus-all2\keystore\myKeystore -storepass %KEY_PASSWORD% -keypass %KEY_PASSWORD% %CYPRUS_HOME%\lib\jPDFPrinterProd.jar cyprus

@Echo	After this execute RUN_setup
@pause