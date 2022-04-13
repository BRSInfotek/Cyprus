/******************************************************************************
 * Copyright (C) 2009 Low Heng Sin                                            *
 * Copyright (C) 2009 Idalica Corporation                                     *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 *****************************************************************************/
package org.cyprusbrs.apps.form;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.cyprus.plaf.CyprusPLAF;
import org.cyprusbrs.apps.ADialog;
import org.cyprusbrs.apps.ADialogDialog;
import org.cyprusbrs.apps.ConfirmPanel;
import org.cyprusbrs.apps.ProcessCtl;
import org.cyprusbrs.apps.StatusBar;
import org.cyprusbrs.minigrid.IDColumn;
import org.cyprusbrs.minigrid.MiniTable;
import org.cyprusbrs.model.MQuery;
import org.cyprusbrs.model.MTable;
import org.cyprusbrs.model.PrintInfo;
import org.cyprusbrs.plaf.CompiereColor;
import org.cyprusbrs.print.MPrintFormat;
import org.cyprusbrs.print.ReportCtl;
import org.cyprusbrs.print.ReportEngine;
import org.cyprusbrs.print.Viewer;
import org.cyprusbrs.process.ProcessInfo;
import org.cyprusbrs.process.ProcessInfoUtil;
import org.cyprusbrs.swing.CPanel;
import org.cyprusbrs.swing.CTabbedPane;
import org.cyprusbrs.swing.CTextPane;
import org.cyprusbrs.util.ASyncProcess;
import org.cyprusbrs.util.CLogger;
import org.cyprusbrs.util.Env;
import org.cyprusbrs.util.Msg;

/**
 * Generate custom form panel
 * 
 */
public class VGenPanel extends CPanel implements ActionListener, ChangeListener, TableModelListener, ASyncProcess
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8154208229173738517L;

	private GenForm genForm;
	
	/**	Window No			*/
	private int         	m_WindowNo = 0;
	/**	FormFrame			*/
	private FormFrame 		m_frame;

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(VInOutGen.class);
	//
	
	private CTabbedPane tabbedPane = new CTabbedPane();
	private CPanel selPanel = new CPanel();
	private CPanel selNorthPanel = new CPanel();
	private BorderLayout selPanelLayout = new BorderLayout();
	
	private FlowLayout northPanelLayout = new FlowLayout();
	private ConfirmPanel confirmPanelSel = new ConfirmPanel(true);
	private ConfirmPanel confirmPanelGen = new ConfirmPanel(false, true, false, false, false, false, true);
	private StatusBar statusBar = new StatusBar();
	private CPanel genPanel = new CPanel();
	private BorderLayout genLayout = new BorderLayout();
	private CTextPane info = new CTextPane();
	private JScrollPane scrollPane = new JScrollPane();
	private MiniTable miniTable = new MiniTable();

	public VGenPanel(GenForm genForm, int WindowNo, FormFrame frame)
	{
		log.info("");
		this.genForm = genForm;		
		m_WindowNo = WindowNo;
		m_frame = frame;
		
		try
		{
			jbInit();
			dynInit();
			frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
			frame.getContentPane().add(statusBar, BorderLayout.SOUTH);
		}
		catch(Exception ex)
		{
			log.log(Level.SEVERE, "init", ex);
		}
	}	//	init
	
	/**
	 *	Static Init.
	 *  <pre>
	 *  selPanel (tabbed)
	 *      fOrg, fBPartner
	 *      scrollPane & miniTable
	 *  genPanel
	 *      info
	 *  </pre>
	 *  @throws Exception
	 */
	void jbInit() throws Exception
	{
		CompiereColor.setBackground(this);
		//
		selPanel.setLayout(selPanelLayout);
		
		selNorthPanel.setLayout(northPanelLayout);
		northPanelLayout.setAlignment(FlowLayout.LEFT);
		tabbedPane.add(selPanel, Msg.getMsg(Env.getCtx(), "Select"));
		selPanel.add(selNorthPanel, BorderLayout.NORTH);
		selPanel.setName("selPanel");
		selPanel.add(confirmPanelSel, BorderLayout.SOUTH);
		selPanel.add(scrollPane, BorderLayout.CENTER);
		scrollPane.getViewport().add(miniTable, null);
		confirmPanelSel.addActionListener(this);
		//
		tabbedPane.add(genPanel, Msg.getMsg(Env.getCtx(), "Generate"));
		genPanel.setLayout(genLayout);
		genPanel.add(info, BorderLayout.CENTER);
		genPanel.setEnabled(false);
		info.setBackground(CyprusPLAF.getFieldBackground_Inactive());
		info.setEditable(false);
		genPanel.add(confirmPanelGen, BorderLayout.SOUTH);
		confirmPanelGen.addActionListener(this);
	}	//	jbInit
	
	/**
	 *	Dynamic Init.
	 *	- Create GridController & Panel
	 *	- AD_Column_ID from C_Order
	 */
	private void dynInit()
	{
		genForm.configureMiniTable(miniTable);
		
		miniTable.setRowSelectionAllowed(true);
		
		miniTable.getModel().addTableModelListener(this);
		//	Info
		statusBar.setStatusDB(" ");
		//	Tabbed Pane Listener
		tabbedPane.addChangeListener(this);
	}	//	dynInit
	
	/**
	 * 	Dispose
	 */
	public void dispose()
	{
		if (m_frame != null)
			m_frame.dispose();
		m_frame = null;
	}	//	dispose
	
	/**
	 *	Action Listener
	 *  @param e event
	 */
	public void actionPerformed (ActionEvent e)
	{
		log.info("Cmd=" + e.getActionCommand());
		//
		if (e.getActionCommand().equals(ConfirmPanel.A_CANCEL))
		{
			dispose();
			return;
		}
		
		try
		{
			genForm.validate();
		}
		catch (Exception ex)
		{
			ADialog.error(m_WindowNo, this, "Error", ex.getLocalizedMessage());
		}
	}	//	actionPerformed

	/**
	 *	Change Listener (Tab changed)
	 *  @param e event
	 */
	public void stateChanged (ChangeEvent e)
	{
		int index = tabbedPane.getSelectedIndex();
		genForm.setSelectionActive(index == 0);
	}	//	stateChanged

	/**
	 *  Table Model Listener
	 *  @param e event
	 */
	public void tableChanged(TableModelEvent e)
	{
		int rowsSelected = 0;
		int rows = miniTable.getRowCount();
		for (int i = 0; i < rows; i++)
		{
			IDColumn id = (IDColumn)miniTable.getValueAt(i, 0);     //  ID in column 0
			if (id != null && id.isSelected())
				rowsSelected++;
		}
		statusBar.setStatusDB(" " + rowsSelected + " ");
	}   //  tableChanged
	
	/**
	 *	Save Selection & return selecion Query or ""
	 *  @return where clause like C_Order_ID IN (...)
	 */
	public void saveSelection()
	{
		//  ID selection may be pending
		miniTable.editingStopped(new ChangeEvent(this));
		genForm.saveSelection(miniTable);
	}	//	saveSelection
	
	/**************************************************************************
	 *	Generate Shipments/Invoices
	 */
	public void generate()
	{
		info.setText(genForm.generate());
		ProcessCtl worker = new ProcessCtl(this, Env.getWindowNo(this), genForm.getProcessInfo(), genForm.getTrx());
		worker.start();
		//
	}
	
	/**
	 *  Complete generating shipments/invoices.
	 *  Called from Unlock UI
	 *  @param pi process info
	 */
	public void generateComplete(ProcessInfo pi)
	{
		//  Switch Tabs
		tabbedPane.setSelectedIndex(1);
		//
		ProcessInfoUtil.setLogFromDB(pi);
		StringBuffer iText = new StringBuffer();
		iText.append("<b>").append(pi.getSummary())
			.append("</b><br>(")
			.append(Msg.getMsg(Env.getCtx(), genForm.getTitle()))
			//  Shipments are generated depending on the Delivery Rule selection in the Order
			.append(")<br>")
			.append(pi.getLogInfo(true));
		info.setText(iText.toString());

		//	Reset Selection
		/*
		String sql = "UPDATE C_Order SET IsSelected='N' WHERE " + m_whereClause;
		int no = DB.executeUpdate(sql, null);
		log.config("Reset=" + no);*/

		//	Get results
		int[] ids = pi.getIDs();
		if (ids == null || ids.length == 0)
			return;
		log.config("PrintItems=" + ids.length);

		confirmPanelGen.getOKButton().setEnabled(false);
		//	OK to print
		if (ADialog.ask(m_WindowNo, this, genForm.getAskPrintMsg()))
		{
			this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			int retValue = ADialogDialog.A_CANCEL;	//	see also ProcessDialog.printShipments/Invoices
			do
			{
				//	Loop through all items
				for (int i = 0; i < ids.length; i++)
				{
					int Record_ID = ids[i];
					
					if(genForm.getPrintFormat() != null)
					{
						MPrintFormat format = genForm.getPrintFormat();
						MTable table = MTable.get(Env.getCtx(),format.getAD_Table_ID());
						MQuery query = new MQuery(table.getTableName());
						query.addRestriction(table.getTableName() + "_ID", MQuery.EQUAL, Record_ID);
						//	Engine
						PrintInfo info = new PrintInfo(table.getTableName(),table.get_Table_ID(), Record_ID);               
						ReportEngine re = new ReportEngine(Env.getCtx(), format, query, info);
						re.print();
						new Viewer(m_frame.getGraphicsConfiguration(), re);
					}
					else
					ReportCtl.startDocumentPrint(genForm.getReportEngineType(), Record_ID, this, Env.getWindowNo(this), true);
					
				}
				ADialogDialog d = new ADialogDialog (m_frame,
					Env.getHeader(Env.getCtx(), m_WindowNo),
					Msg.getMsg(Env.getCtx(), "PrintoutOK?"),
					JOptionPane.QUESTION_MESSAGE);
				retValue = d.getReturnCode();
			}
			while (retValue == ADialogDialog.A_CANCEL);
			this.setCursor(Cursor.getDefaultCursor());
		}	//	OK to print

		//
		confirmPanelGen.getOKButton().setEnabled(true);
	} 
	
	/**************************************************************************
	 *  Lock User Interface.
	 *  Called from the Worker before processing
	 *  @param pi process info
	 */
	public void lockUI (ProcessInfo pi)
	{
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		setEnabled(false);
	}   //  lockUI

	/**
	 *  Unlock User Interface.
	 *  Called from the Worker when processing is done
	 *  @param pi result of execute ASync call
	 */
	public void unlockUI (ProcessInfo pi)
	{
		setEnabled(true);
		setCursor(Cursor.getDefaultCursor());
		//
		generateComplete(pi);
	}   //  unlockUI

	/**
	 *  Is the UI locked (Internal method)
	 *  @return true, if UI is locked
	 */
	public boolean isUILocked()
	{
		return isEnabled();
	}   //  isUILocked
	
	/**
	 *  Method to be executed async.
	 *  Called from the Worker
	 *  @param pi ProcessInfo
	 */
	public void executeASync (ProcessInfo pi)
	{
	}   //  executeASync
	
	public CPanel getParameterPanel()
	{
		return selNorthPanel;
	}
	
	public MiniTable getMiniTable()
	{
		return miniTable;
	}
	
	public StatusBar getStatusBar()
	{
		return statusBar;
	}
}