// Usage:   WinEnv CYPRUS_HOME JAVA_HOME
// Example: WinEnv C:\Cyprus C:\Program Files\Java\jdk1.8.0_261
// Example: cscript WinEnv.js C:\Cyprus C:\Program Files\Java\jdk1.8.0_261
//
// WinEnv.js - Set up Windows Environment

// $Id: WinEnv.js,V 1.1 2021/01/01 Sanjiv/Mukesh Exp $


// Get Objects
var Shell = new ActiveXObject("WScript.Shell");
var DesktopPath = Shell.SpecialFolders("Desktop");
var Args = WScript.Arguments;
var SysEnv = Shell.Environment("SYSTEM");

if (Args.length != 2)
{
  WScript.Echo("Usage: cscript WinEnv.js CYPRUS_HOME JAVA_HOME"
	+ "\nExample:\ncscript WinEnv.js C:\\Cyprus \"C:\Program Files\Java\jdk1.8.0_261\"");
  WScript.Quit (1);
}


// Set Environment Variables
var home = Args(0).replace("\"","");
SysEnv("CYPRUS_HOME") = home;
WScript.Echo ("SET CYPRUS_HOME="+ home);

home = Args(1).replace("\"","");
SysEnv("JAVA_HOME") = home;
WScript.Echo ("SET JAVA_HOME="+ home);


// Check that JAVA_HOME is in PATH
var pathString = SysEnv("PATH"); // Shell.ExpandEnvironmentStrings("%PATH%");
var index = pathString.indexOf(home);
if (index == -1)
{
  SysEnv("PATH") = home + "\\bin;" + pathString;
  var index_2 = SysEnv("PATH").indexOf(home);
  if (index_2 == -1)
    WScript.Echo ("Path NOT changed - run program as Administrator!");
  else
    WScript.Echo ("Path Changed = " + SysEnv("PATH"));
}
else
  WScript.Echo ("Path is OK = " + SysEnv("PATH"));


// Create Cyprus.exe Shortcut
// http://msdn.microsoft.com/library/default.asp?url=/library/en-us/script56/html/wsMthCreateShortcut.asp
var link = Shell.CreateShortcut(DesktopPath + "\\Cyprus.lnk");
link.TargetPath = Args(0) + "\\lib\\Cyprus.exe";
link.Arguments = "-debug";
link.Description = "Cyprus Client";
link.IconLocation = Args(0) + "\\lib\\Cyprus.exe,0";
link.WorkingDirectory = Args(0);
link.WindowStyle = 3;
link.HotKey = "CTRL+ALT+SHIFT+C";
link.Save();
WScript.Echo ("Created Shortcut Cyprus.lnk");

// Create Web Site Shortcut
var urlLink = Shell.CreateShortcut(DesktopPath + "\\Cyprus Web Site.url");
urlLink.TargetPath = "https://www.cypruserp.com";
urlLink.Save();
WScript.Echo ("Created Shortcut Cyprus Web Site.url");

WScript.Echo ("Done");
