package org.cyprus.plaf;

import org.jdesktop.swingx.plaf.basic.BasicLookAndFeelAddons;

public class CyprusLookAndFeelAddons extends BasicLookAndFeelAddons {
	  public void initialize() {
		    super.initialize();
		    loadDefaults(getDefaults());
		  }

		  public void uninitialize() {
		    super.uninitialize();
		    unloadDefaults(getDefaults());
		  }
		  
		  private Object[] getDefaults() {
		    Object[] defaults =
		      new Object[] {
//		        "DirectoryChooserUI",
//		        "org.jdesktop.jdnc.swing.plaf.windows.WindowsDirectoryChooserUI",
		    };
		    return defaults;
		  }
}
