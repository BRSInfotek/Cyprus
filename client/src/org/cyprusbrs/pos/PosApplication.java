package org.cyprusbrs.pos;

import java.awt.KeyboardFocusManager;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Properties;

import javax.swing.JFrame;

import org.cyprusbrs.Cyprus;
import org.cyprusbrs.apps.ADialog;
import org.cyprusbrs.apps.AEnv;
import org.cyprusbrs.apps.AKeyboardFocusManager;
import org.cyprusbrs.apps.ALogin;
import org.cyprusbrs.model.MSession;
import org.cyprusbrs.swing.CFrame;
import org.cyprusbrs.util.DB;
import org.cyprusbrs.util.Env;
import org.cyprusbrs.util.Msg;
import org.cyprusbrs.util.Splash;

public class PosApplication {
	
	private Properties m_ctx;

	PosApplication() {
		Cyprus.startup(true);	//	needs to be here for UI
		Splash splash = Splash.getSplash();
		final CFrame frame = new CFrame();
		//  Focus Traversal
		KeyboardFocusManager.setCurrentKeyboardFocusManager(AKeyboardFocusManager.get());
	//	FocusManager.getCurrentManager().setDefaultFocusTraversalPolicy(AFocusTraversalPolicy.get());
	//	this.setFocusTraversalPolicy(AFocusTraversalPolicy.get());


		ALogin login = new ALogin(splash);
		if (!login.initLogin())		//	no automatic login
		{
			//	Center the window
			try
			{
				AEnv.showCenterScreen(login);	//	HTML load errors
			}
			catch (Exception ex)
			{
			}
			if (!login.isConnected() || !login.isOKpressed())
				AEnv.exit(1);
		}

		//  Check Build
		if (!DB.isBuildOK(m_ctx))
			AEnv.exit(1);

		//  Check DB	(AppsServer Version checked in Login)
		DB.isDatabaseOK(m_ctx);
	
		splash.setText(Msg.getMsg(m_ctx, "Loading"));
		splash.toFront();
		splash.paint(splash.getGraphics());
	
		//
		if (!Cyprus.startupEnvironment(true)) // Load Environment
			System.exit(1);		
		MSession.get (Env.getCtx(), true);		//	Start Session
		
		int m_WindowNo = Env.createWindowNo(frame);
		
	//  Default Image
		frame.setIconImage(Cyprus.getImage16());

		// Setting close operation/listener - teo_sarca [ 1684168 ]
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowListener() {
			public void windowClosing(WindowEvent e) {
				if (!ADialog.ask(0, null, "ExitApplication?"))
					return;
				frame.dispose();
			}
			public void windowActivated(WindowEvent e) {}
			public void windowClosed(WindowEvent e) {}
			public void windowDeactivated(WindowEvent e) {}
			public void windowDeiconified(WindowEvent e) {}
			public void windowIconified(WindowEvent e) {}
			public void windowOpened(WindowEvent e) {}
		});

		PosBasePanel pos = new PosBasePanel();
		pos.init(0,frame);
		frame.pack();
		splash.dispose();
		splash = null;	
		frame.setVisible(true);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		new PosApplication();

	}

}
