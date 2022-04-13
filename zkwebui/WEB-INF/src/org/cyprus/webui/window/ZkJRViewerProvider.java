package org.cyprus.webui.window;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

import org.cyprus.webui.component.Window;
import org.cyprus.webui.session.SessionManager;
import org.cyprusbrs.report.JRViewerProvider;

public class ZkJRViewerProvider implements JRViewerProvider {

	public void openViewer(JasperPrint jasperPrint, String title)
			throws JRException {
		Window viewer = new ZkJRViewer(jasperPrint, title);
		
		viewer.setAttribute(Window.MODE_KEY, Window.MODE_EMBEDDED);
		viewer.setAttribute(Window.INSERT_POSITION_KEY, Window.INSERT_NEXT);
		SessionManager.getAppDesktop().showWindow(viewer);
	}

}
