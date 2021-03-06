/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.cyprus.webui.apps.form;

import java.sql.Timestamp;
import java.util.Properties;
import java.util.logging.Level;

import org.cyprus.webui.LayoutUtils;
import org.cyprus.webui.apps.AEnv;
import org.cyprus.webui.component.ConfirmPanel;
import org.cyprus.webui.component.Grid;
import org.cyprus.webui.component.GridFactory;
import org.cyprus.webui.component.Label;
import org.cyprus.webui.component.Panel;
import org.cyprus.webui.component.Row;
import org.cyprus.webui.component.Rows;
import org.cyprus.webui.editor.WDateEditor;
import org.cyprus.webui.editor.WLocatorEditor;
import org.cyprus.webui.editor.WSearchEditor;
import org.cyprus.webui.editor.WTableDirEditor;
import org.cyprus.webui.event.ValueChangeEvent;
import org.cyprus.webui.event.ValueChangeListener;
import org.cyprus.webui.panel.ADForm;
import org.cyprus.webui.panel.ADTabpanel;
import org.cyprus.webui.panel.CustomForm;
import org.cyprus.webui.panel.IFormController;
import org.cyprus.webui.panel.StatusBarPanel;
import org.cyprus.webui.session.SessionManager;
import org.cyprusbrs.apps.form.TrxMaterial;
import org.cyprusbrs.model.MLocatorLookup;
import org.cyprusbrs.model.MLookup;
import org.cyprusbrs.model.MLookupFactory;
import org.cyprusbrs.util.DisplayType;
import org.cyprusbrs.util.Env;
import org.cyprusbrs.util.Msg;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkex.zul.Borderlayout;
import org.zkoss.zkex.zul.Center;
import org.zkoss.zkex.zul.North;
import org.zkoss.zkex.zul.South;
import org.zkoss.zul.Separator;

/**
 * Material Transaction History
 *
 * @author Jorg Janke
 * @version $Id: VTrxMaterial.java,v 1.3 2006/07/30 00:51:28 jjanke Exp $
 */
public class WTrxMaterial extends TrxMaterial
	implements IFormController, EventListener, ValueChangeListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2141669182129214237L;
	
	private CustomForm form = new CustomForm();	

	/** GridController          */
	private ADTabpanel  m_gridController = null;

	//
	private Panel mainPanel = new Panel();
	private Borderlayout mainLayout = new Borderlayout();
	private Panel parameterPanel = new Panel();
	private Label orgLabel = new Label();
	private WTableDirEditor orgField;
	private Label locatorLabel = new Label();
	private WLocatorEditor locatorField;
	private Label productLabel = new Label();
	private WSearchEditor productField;
	private Label dateFLabel = new Label();
	private WDateEditor dateFField;
	private Label dateTLabel = new Label();
	private WDateEditor dateTField;
	private Label mtypeLabel = new Label();
	private WTableDirEditor mtypeField;
	private Grid parameterLayout = GridFactory.newGridLayout();
	private Panel southPanel = new Panel();
	private ConfirmPanel confirmPanel = new ConfirmPanel(true, true, false, false, false, true, false);
	private StatusBarPanel statusBar = new StatusBarPanel();


	/**
	 *	Initialize Panel
	 */
	public WTrxMaterial()
	{
		log.info("");
		try
		{
			dynParameter();
			zkInit();
			dynInit();			
		}
		catch(Exception ex)
		{
			log.log(Level.SEVERE, "", ex);
		}
	}	//	init
	
	/**
	 *  Static Init
	 *  @throws Exception
	 */
	void zkInit() throws Exception
	{
		form.appendChild(mainPanel);
		mainPanel.setStyle("width: 99%; height: 100%; border: none; padding: 0; margin: 0");
		mainPanel.appendChild(mainLayout);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		parameterPanel.appendChild(parameterLayout);
		//
		orgLabel.setText(Msg.translate(Env.getCtx(), "AD_Org_ID"));
		locatorLabel.setText(Msg.translate(Env.getCtx(), "M_Locator_ID"));
		productLabel.setText(Msg.translate(Env.getCtx(), "Product"));
		dateFLabel.setText(Msg.translate(Env.getCtx(), "DateFrom"));
		dateTLabel.setText(Msg.translate(Env.getCtx(), "DateTo"));
		mtypeLabel.setText(Msg.translate(Env.getCtx(), "MovementType"));
		//
		North north = new North();
		mainLayout.appendChild(north);
		north.appendChild(parameterPanel);
		
		Rows rows = parameterLayout.newRows();
		Row row = rows.newRow();
		row.appendChild(orgLabel.rightAlign());
		row.appendChild(orgField.getComponent());
		row.appendChild(mtypeLabel.rightAlign());
		row.appendChild(mtypeField.getComponent());
		row.appendChild(dateFLabel.rightAlign());
		row.appendChild(dateFField.getComponent());

		row = rows.newRow();
		row.appendChild(locatorLabel.rightAlign());
		row.appendChild(locatorField.getComponent());
		row.appendChild(productLabel.rightAlign());
		row.appendChild(productField.getComponent());
		row.appendChild(dateTLabel.rightAlign());
		row.appendChild(dateTField.getComponent());
		//
		southPanel.appendChild(confirmPanel);
		southPanel.appendChild(new Separator());
		southPanel.appendChild(statusBar);
		South south = new South();
		south.setStyle("border: none");
		mainLayout.appendChild(south);
		south.appendChild(southPanel);
		
		LayoutUtils.addSclass("status-border", statusBar);
	}   //  jbInit

	/**
	 *  Initialize Parameter fields
	 *  @throws Exception if Lookups cannot be initialized
	 */
	private void dynParameter() throws Exception
	{
		Properties ctx = Env.getCtx();
		//  Organization
		MLookup orgLookup = MLookupFactory.get (ctx, m_WindowNo, 0, 3660, DisplayType.TableDir);
		orgField = new WTableDirEditor("AD_Org_ID", false, false, true, orgLookup);
	//	orgField.addVetoableChangeListener(this);
		//  Locator
		MLocatorLookup locatorLookup = new MLocatorLookup(ctx, m_WindowNo);
		locatorField = new WLocatorEditor ("M_Locator_ID", false, false, true, locatorLookup, m_WindowNo);
	//	locatorField.addVetoableChangeListener(this);
		//  Product
		MLookup productLookup = MLookupFactory.get (ctx, m_WindowNo, 0, 3668, DisplayType.Search);
		productField = new WSearchEditor("M_Product_ID", false, false, true, productLookup);
		productField.addValueChangeListener(this);
		//  Movement Type
		MLookup mtypeLookup = MLookupFactory.get (ctx, m_WindowNo, 0, 3666, DisplayType.List);
		mtypeField = new WTableDirEditor("MovementType", false, false, true, mtypeLookup);
		//  Dates
		dateFField = new WDateEditor("DateFrom", false, false, true, Msg.getMsg(Env.getCtx(), "DateFrom"));
		dateTField = new WDateEditor("DateTo", false, false, true, Msg.getMsg(Env.getCtx(), "DateTo"));
		//
		confirmPanel.addActionListener(this);
		statusBar.setStatusLine("");
	}   //  dynParameter

	/**
	 *  Dynamic Layout (Grid).
	 * 	Based on AD_Window: Material Transactions
	 */
	private void dynInit()
	{
		super.dynInit(statusBar);
		//
		
		m_gridController = new ADTabpanel();
		m_gridController.init(null, m_WindowNo, m_mTab, m_mWindow);
		if (!m_gridController.isGridView())
			m_gridController.switchRowPresentation();
		Center center = new Center();
		mainLayout.appendChild(center);
		center.setFlex(true);
		center.appendChild(m_gridController);
	}   //  dynInit


	/**
	 * 	Dispose
	 */
	public void dispose()
	{
		SessionManager.getAppDesktop().closeActiveWindow();
	}	//	dispose

	
	/**************************************************************************
	 *  Action Listener
	 *  @param e event
	 */
	public void onEvent (Event e)
	{
		if (e.getTarget().getId().equals(ConfirmPanel.A_CANCEL))
			dispose();
		else if (e.getTarget().getId().equals(ConfirmPanel.A_REFRESH)
				|| e.getTarget().getId().equals(ConfirmPanel.A_OK))
			refresh();
		else if (e.getTarget().getId().equals(ConfirmPanel.A_ZOOM))
			zoom();
	}   //  actionPerformed

	
	/**************************************************************************
	 *  Property Listener
	 *  @param e event
	 */
	public void valueChange (ValueChangeEvent e)
	{
		if (e.getPropertyName().equals("M_Product_ID"))
			productField.setValue(e.getNewValue());
	}   //  vetoableChange


	
	/**************************************************************************
	 *  Refresh - Create Query and refresh grid
	 */
	private void refresh()
	{
		Object organization = orgField.getValue();
		Object locator = locatorField.getValue();
		Object product = productField.getValue();
		Object movementType = mtypeField.getValue();
		Timestamp movementDateFrom = (Timestamp)dateFField.getValue();
		Timestamp movementDateTo = (Timestamp)dateTField.getValue();
		
		refresh(organization, locator, product, movementType, movementDateFrom, movementDateTo, statusBar);
	}   //  refresh

	/**
	 *  Zoom
	 */
	public void zoom()
	{
		super.zoom();

		//  Zoom
		AEnv.zoom(AD_Window_ID, query);
	}   //  zoom
	
	public ADForm getForm() 
	{
		return form;
	}

}   //  VTrxMaterial
