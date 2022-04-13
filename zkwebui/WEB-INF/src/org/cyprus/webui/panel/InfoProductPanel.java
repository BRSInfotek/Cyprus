/******************************************************************************
 * Product: Posterita Ajax UI                                                 *
 * Copyright (C) 2007 Posterita Ltd.  All Rights Reserved.                    *
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
 * Posterita Ltd., 3, Draper Avenue, Quatre Bornes, Mauritius                 *
 * or via info@posterita.org or http://www.posterita.org/                     *
 *****************************************************************************/

package org.cyprus.webui.panel;

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
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;

import org.cyprus.webui.apps.AEnv;
import org.cyprus.webui.component.Button;
import org.cyprus.webui.component.Checkbox;
import org.cyprus.webui.component.ConfirmPanel;
import org.cyprus.webui.component.Grid;
import org.cyprus.webui.component.GridFactory;
import org.cyprus.webui.component.Label;
import org.cyprus.webui.component.ListItem;
import org.cyprus.webui.component.ListModelTable;
import org.cyprus.webui.component.Listbox;
import org.cyprus.webui.component.ListboxFactory;
import org.cyprus.webui.component.Row;
import org.cyprus.webui.component.Rows;
import org.cyprus.webui.component.Tab;
import org.cyprus.webui.component.Tabbox;
import org.cyprus.webui.component.Tabpanel;
import org.cyprus.webui.component.Tabpanels;
import org.cyprus.webui.component.Tabs;
import org.cyprus.webui.component.Textbox;
import org.cyprus.webui.component.WListbox;
import org.cyprus.webui.editor.WPAttributeEditor;
import org.cyprus.webui.editor.WSearchEditor;
import org.cyprus.webui.editor.WTableDirEditor;
import org.cyprus.webui.session.SessionManager;
import org.cyprusbrs.apps.search.Info_Column;
import org.cyprusbrs.minigrid.ColumnInfo;
import org.cyprusbrs.minigrid.IDColumn;
import org.cyprusbrs.model.MClient;
import org.cyprusbrs.model.MDocType;
import org.cyprusbrs.model.MQuery;
import org.cyprusbrs.model.MRole;
import org.cyprusbrs.util.CLogMgt;
import org.cyprusbrs.util.DB;
import org.cyprusbrs.util.Env;
import org.cyprusbrs.util.KeyNamePair;
import org.cyprusbrs.util.Msg;
import org.cyprusbrs.util.Util;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkex.zul.Borderlayout;
import org.zkoss.zkex.zul.Center;
import org.zkoss.zkex.zul.North;
import org.zkoss.zkex.zul.South;

/**
 * Search Product and return selection
 * This class is based on org.cyprusbrs.apps.search.InfoPAttribute written by Jorg Janke
 * @author Elaine
 *
 * Zk Port
 * @author Elaine
 * @version	InfoPayment.java Adempiere Swing UI 3.4.1
 */
public class InfoProductPanel extends InfoPanel implements EventListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6804975825156657866L;
	private Label lblValue = new Label();
	private Textbox fieldValue = new Textbox();
	private Label lblName = new Label();
	private Textbox fieldName = new Textbox();
	private Label lblUPC = new Label();
	private Textbox fieldUPC = new Textbox();
	private Label lblSKU = new Label();
	private Textbox fieldSKU = new Textbox();
	private Label lblPriceList = new Label();
	private Listbox pickPriceList = new Listbox();
	private Label lblWarehouse = new Label();
	private Listbox pickWarehouse = new Listbox();
	private Label lblVendor = new Label();
	private Textbox fieldVendor = new Textbox();
	// Elaine 2008/11/21
	private Label lblProductCategory = new Label();
	private Listbox pickProductCategory = new Listbox();
	//
	private Label lblAS = new Label();
	private Listbox pickAS = new Listbox();
	private WTableDirEditor fWarehouse_ID = null;
	private Label lblBlank = new Label();

	// Elaine 2008/11/25
	private Borderlayout borderlayout = new Borderlayout();
	private Textbox fieldDescription = new Textbox();
	
	private WTableDirEditor fProductCategory_ID = null;
	Tabbox tabbedPane = new Tabbox();
	WListbox warehouseTbl = ListboxFactory.newDataTable();
    String m_sqlWarehouse;
    WListbox substituteTbl = ListboxFactory.newDataTable();
    String m_sqlSubstitute;
    WListbox relatedTbl = ListboxFactory.newDataTable();
    String m_sqlRelated;
    //Available to Promise Tab
	private WListbox 			m_tableAtp = ListboxFactory.newDataTable();
	private int 				m_M_Product_ID = 0;
	
	private int					m_M_Warehouse_ID = 0;
	private int 				m_M_PriceList_ID = 0;
    int mWindowNo = 0;
    //

	/**	Search Button				*/
	private Button	m_InfoPAttributeButton = new Button();
	/** Instance Button				*/
	private Button	m_PAttributeButton = null;
	/** SQL From				*/
	private static final String s_productFrom =
		"M_Product p"
		+ " LEFT OUTER JOIN M_ProductPrice pr ON (p.M_Product_ID=pr.M_Product_ID AND pr.IsActive='Y')"
		+ " LEFT OUTER JOIN M_AttributeSet pa ON (p.M_AttributeSet_ID=pa.M_AttributeSet_ID)"
		+ " LEFT OUTER JOIN M_Product_PO ppo ON (p.M_Product_ID=ppo.M_Product_ID)"
		+ " LEFT OUTER JOIN C_BPartner bp ON (ppo.C_BPartner_ID=bp.C_BPartner_ID)";

	/**  Array of Column Info    */
	private static ColumnInfo[] s_productLayout = null;
	private static int INDEX_NAME = 0;
	private static int INDEX_PATTRIBUTE = 0;


	/** ASI							*/
	private int			m_M_AttributeSetInstance_ID = -1;
	/** Locator						*/
	private int			m_M_Locator_ID = 0;
	
	private Checkbox checkOnlyStock;
	private Checkbox checkShowDetail;

	private String		m_pAttributeWhere = null;
	private int			m_C_BPartner_ID = 0;
	
	
	private int fieldID = 0;

	private WTableDirEditor fPriceList_ID = null;
	
	private WSearchEditor fVendor_ID = WSearchEditor.createBPartner(0);
	// Elaine 2008/11/21

	
	//

	private Label lblASI = new Label();
	private WTableDirEditor fAS_ID = null;
	private WPAttributeEditor fASI_ID = null;
	private Checkbox checkAND;


	// Elaine 2008/11/25
	
	private Textbox fieldPAttributes = new Textbox();
	private Tabbox detailTabBox = new Tabbox();
	
	private WListbox vendorTbl = ListboxFactory.newDataTable();
	private String m_sqlVendor;
    //Available to Promise Tab
    private Info_Column[]		m_layoutATP = null;

	private ListModelTable 		m_modelAtp = null;	

	/** Instance Button				*/
	

	protected int m_ATP_M_Warehouse_ID;

	/**
	 *	Standard Constructor
	 * 	@param WindowNo window no
	 * 	@param M_Warehouse_ID warehouse
	 * 	@param M_PriceList_ID price list
	 * 	@param value    Query Value or Name if enclosed in @
	 * 	@param whereClause where clause
	 */
	public InfoProductPanel(int windowNo,
		int M_Warehouse_ID, int M_PriceList_ID, boolean multipleSelection,String value,
		 String whereClause)
	{
		this(windowNo, M_Warehouse_ID, M_PriceList_ID, multipleSelection, value, whereClause, true);
	}

	/**
	 *	Standard Constructor
	 * 	@param WindowNo window no
	 * 	@param M_Warehouse_ID warehouse
	 * 	@param M_PriceList_ID price list
	 * 	@param value    Query Value or Name if enclosed in @
	 * 	@param whereClause where clause
	 */
	public InfoProductPanel(int windowNo,
		int M_Warehouse_ID, int M_PriceList_ID, boolean multipleSelection,String value,
		 String whereClause, boolean lookup)
	{
		super (windowNo, "p", "M_Product_ID",multipleSelection, whereClause, lookup);
		log.info(value + ", Wh=" + M_Warehouse_ID + ", PL=" + M_PriceList_ID + ", WHERE=" + whereClause);
		setTitle(Msg.getMsg(Env.getCtx(), "InfoProduct"));
		//
		initComponents();
		init();
		initInfo (value, M_Warehouse_ID, M_PriceList_ID);
		m_C_BPartner_ID = Env.getContextAsInt(Env.getCtx(), windowNo, "C_BPartner_ID");

        int no = contentPanel.getRowCount();
        setStatusLine(Integer.toString(no) + " " + Msg.getMsg(Env.getCtx(), "SearchRows_EnterQuery"), false);
        setStatusDB(Integer.toString(no));
		//	AutoQuery
		if (value != null && value.length() > 0)
        {
			executeQuery();
            renderItems();
        }

		tabbedPane.setSelectedIndex(0);

		p_loadedOK = true;

		//Begin - fer_luck @ centuryon
		mWindowNo = windowNo; // Elaine 2008/12/16
		//End - fer_luck @ centuryon

	}	//	InfoProductPanel

	/**
	 *	initialize fields
	 */
	private void initComponents()
	{
		lblValue = new Label();
		lblValue.setValue(Util.cleanAmp(Msg.translate(Env.getCtx(), "Value")));
		lblName = new Label();
		lblName.setValue(Util.cleanAmp(Msg.translate(Env.getCtx(), "Name")));
		lblUPC =  new Label();
		lblUPC.setValue(Msg.translate(Env.getCtx(), "UPC"));
		lblSKU = new Label();
		lblSKU.setValue(Msg.translate(Env.getCtx(), "SKU"));
		lblPriceList = new Label();
		lblPriceList.setValue(Msg.getMsg(Env.getCtx(), "PriceListVersion"));
		// Elaine 2008/11/21
		lblProductCategory = new Label();
		lblProductCategory.setValue(Msg.translate(Env.getCtx(), "M_Product_Category_ID"));
		//
		lblAS = new Label();
		lblAS.setValue(Msg.translate(Env.getCtx(), "M_AttributeSet_ID"));
		lblWarehouse = new Label();
		lblWarehouse.setValue(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "Warehouse")));
		lblVendor = new Label();
		lblVendor.setValue(Msg.translate(Env.getCtx(), "Vendor"));

		m_InfoPAttributeButton.setImage("/images/PAttribute16.png");
		m_InfoPAttributeButton.setTooltiptext(Msg.getMsg(Env.getCtx(), "PAttribute"));
		m_InfoPAttributeButton.addEventListener(Events.ON_CLICK,this);

		fieldValue = new Textbox();
		fieldValue.setMaxlength(40);
		fieldName = new Textbox();
		fieldName.setMaxlength(40);
		fieldUPC = new Textbox();
		fieldUPC.setMaxlength(40);
		fieldSKU = new Textbox();
		fieldSKU.setMaxlength(40);
		pickPriceList = new Listbox();
		pickPriceList.setRows(0);
		pickPriceList.setMultiple(false);
		pickPriceList.setMold("select");
		pickPriceList.setWidth("150px");
		pickPriceList.addEventListener(Events.ON_SELECT, this);

		// Elaine 2008/11/21
		pickProductCategory = new Listbox();
		pickProductCategory.setRows(0);
		pickProductCategory.setMultiple(false);
		pickProductCategory.setMold("select");
		pickProductCategory.setWidth("150px");
		pickProductCategory.addEventListener(Events.ON_SELECT, this);
		//
		pickAS = new Listbox();
		pickAS.setRows(0);
		pickAS.setMultiple(false);
		pickAS.setMold("select");
		pickAS.setWidth("150px");
		pickAS.addEventListener(Events.ON_SELECT, this);

		pickWarehouse = new Listbox();
		pickWarehouse.setRows(0);
		pickWarehouse.setMultiple(false);
		pickWarehouse.setMold("select");
		pickWarehouse.setWidth("150px");
		pickWarehouse.addEventListener(Events.ON_SELECT, this);

		fieldVendor = new Textbox();
		fieldVendor.setMaxlength(40);

        contentPanel.setVflex(true);
	}	//	initComponents

	private void init()
	{
    	Grid grid = GridFactory.newGridLayout();

		Rows rows = new Rows();
		grid.appendChild(rows);

		Row row = new Row();
		rows.appendChild(row);
		row.appendChild(lblValue.rightAlign());
		row.appendChild(fieldValue);
		row.appendChild(lblUPC.rightAlign());
		row.appendChild(fieldUPC);
		row.appendChild(lblWarehouse.rightAlign());
		row.appendChild(pickWarehouse);
		row.appendChild(m_InfoPAttributeButton);

		row = new Row();
		row.setSpans("1, 1, 1, 1, 1, 1, 1, 2");
		rows.appendChild(row);
		row.appendChild(lblName.rightAlign());
		row.appendChild(fieldName);
		row.appendChild(lblSKU.rightAlign());
		row.appendChild(fieldSKU);
		row.appendChild(lblVendor.rightAlign());
		row.appendChild(fieldVendor);
		//

		row = new Row();
		rows.appendChild(row);
		row.appendChild(lblPriceList.rightAlign());
		row.appendChild(pickPriceList);
		row.appendChild(lblProductCategory.rightAlign());
		row.appendChild(pickProductCategory);
		row.appendChild(lblAS.rightAlign());
		row.appendChild(pickAS);
		
		row = new Row();
		rows.appendChild(row);
		row.appendChild(statusBar);
		row.setSpans("6");
		statusBar.setEastVisibility(false);

		// Product Attribute Instance
		m_PAttributeButton = confirmPanel.createButton(ConfirmPanel.A_PATTRIBUTE);
		confirmPanel.addComponentsLeft(m_PAttributeButton);
		m_PAttributeButton.addActionListener(this);
		m_PAttributeButton.setEnabled(false);

        // Elaine 2008/11/25
        fieldDescription.setMultiline(true);
		fieldDescription.setReadonly(true);

		//
        ColumnInfo[] s_layoutWarehouse = new ColumnInfo[]{
        		new ColumnInfo(Msg.translate(Env.getCtx(), "Warehouse"), "Warehouse", String.class),
        		new ColumnInfo(Msg.translate(Env.getCtx(), "QtyAvailable"), "sum(QtyAvailable)", Double.class),
        		new ColumnInfo(Msg.translate(Env.getCtx(), "QtyOnHand"), "sum(QtyOnHand)", Double.class),
        		new ColumnInfo(Msg.translate(Env.getCtx(), "QtyReserved"), "sum(QtyReserved)", Double.class)};
        /**	From Clause							*/
        String s_sqlFrom = " M_PRODUCT_STOCK_V ";
        /** Where Clause						*/
        String s_sqlWhere = "Value = ?";
        m_sqlWarehouse = warehouseTbl.prepareTable(s_layoutWarehouse, s_sqlFrom, s_sqlWhere, false, "M_PRODUCT_STOCK_V");
		m_sqlWarehouse += " Group By Warehouse, documentnote ";
		warehouseTbl.setMultiSelection(false);
        warehouseTbl.autoSize();
        warehouseTbl.getModel().addTableModelListener(this);

        ColumnInfo[] s_layoutSubstitute = new ColumnInfo[]{
        		new ColumnInfo(Msg.translate(Env.getCtx(), "Warehouse"), "orgname", String.class),
        		new ColumnInfo(
    					Msg.translate(Env.getCtx(), "Value"),
    					"(Select Value from M_Product p where p.M_Product_ID=M_PRODUCT_SUBSTITUTERELATED_V.Substitute_ID)",
    					String.class),
    			new ColumnInfo(Msg.translate(Env.getCtx(), "Name"), "Name", String.class),
    			new ColumnInfo(Msg.translate(Env.getCtx(), "QtyAvailable"), "QtyAvailable", Double.class),
  	        	new ColumnInfo(Msg.translate(Env.getCtx(), "QtyOnHand"), "QtyOnHand", Double.class),
    	        new ColumnInfo(Msg.translate(Env.getCtx(), "QtyReserved"), "QtyReserved", Double.class),
  	        	new ColumnInfo(Msg.translate(Env.getCtx(), "PriceStd"), "PriceStd", Double.class)};
        s_sqlFrom = "M_PRODUCT_SUBSTITUTERELATED_V";
        s_sqlWhere = "M_Product_ID = ? AND M_PriceList_Version_ID = ? and RowType = 'S'";
        m_sqlSubstitute = substituteTbl.prepareTable(s_layoutSubstitute, s_sqlFrom, s_sqlWhere, false, "M_PRODUCT_SUBSTITUTERELATED_V");
        substituteTbl.setMultiSelection(false);
        substituteTbl.autoSize();
        substituteTbl.getModel().addTableModelListener(this);

        ColumnInfo[] s_layoutRelated = new ColumnInfo[]{
        		new ColumnInfo(Msg.translate(Env.getCtx(), "Warehouse"), "orgname", String.class),
        		new ColumnInfo(
    					Msg.translate(Env.getCtx(), "Value"),
    					"(Select Value from M_Product p where p.M_Product_ID=M_PRODUCT_SUBSTITUTERELATED_V.Substitute_ID)",
    					String.class),
    			new ColumnInfo(Msg.translate(Env.getCtx(), "Name"), "Name", String.class),
    			new ColumnInfo(Msg.translate(Env.getCtx(), "QtyAvailable"), "QtyAvailable", Double.class),
  	        	new ColumnInfo(Msg.translate(Env.getCtx(), "QtyOnHand"), "QtyOnHand", Double.class),
    	        new ColumnInfo(Msg.translate(Env.getCtx(), "QtyReserved"), "QtyReserved", Double.class),
  	        	new ColumnInfo(Msg.translate(Env.getCtx(), "PriceStd"), "PriceStd", Double.class)};
        s_sqlFrom = "M_PRODUCT_SUBSTITUTERELATED_V";
        s_sqlWhere = "M_Product_ID = ? AND M_PriceList_Version_ID = ? and RowType = 'R'";
        m_sqlRelated = relatedTbl.prepareTable(s_layoutRelated, s_sqlFrom, s_sqlWhere, false, "M_PRODUCT_SUBSTITUTERELATED_V");
        relatedTbl.setMultiSelection(false);
        relatedTbl.autoSize();
        relatedTbl.getModel().addTableModelListener(this);

        //Available to Promise Tab
        m_tableAtp.setMultiSelection(false);

        tabbedPane.setHeight("100%");
		Tabpanels tabPanels = new Tabpanels();
		tabbedPane.appendChild(tabPanels);
		Tabs tabs = new Tabs();
		tabbedPane.appendChild(tabs);

		Tab tab = new Tab(Msg.translate(Env.getCtx(), "Warehouse"));
		tabs.appendChild(tab);
		Tabpanel desktopTabPanel = new Tabpanel();
		desktopTabPanel.setHeight("100%");
		desktopTabPanel.appendChild(warehouseTbl);
		tabPanels.appendChild(desktopTabPanel);

		tab = new Tab(Msg.translate(Env.getCtx(), "Description"));
		tabs.appendChild(tab);
		desktopTabPanel = new Tabpanel();
		desktopTabPanel.setHeight("100%");
		fieldDescription.setWidth("99%");
		fieldDescription.setHeight("99%");
		desktopTabPanel.appendChild(fieldDescription);
		tabPanels.appendChild(desktopTabPanel);

		tab = new Tab(Msg.translate(Env.getCtx(), "Substitute_ID"));
		tabs.appendChild(tab);
		desktopTabPanel = new Tabpanel();
		desktopTabPanel.setHeight("100%");
		desktopTabPanel.appendChild(substituteTbl);
		tabPanels.appendChild(desktopTabPanel);

		tab = new Tab(Msg.translate(Env.getCtx(), "RelatedProduct_ID"));
		tabs.appendChild(tab);
		desktopTabPanel = new Tabpanel();
		desktopTabPanel.setHeight("100%");
		desktopTabPanel.appendChild(relatedTbl);
		tabPanels.appendChild(desktopTabPanel);

		tab = new Tab(Msg.getMsg(Env.getCtx(), "ATP"));
		tabs.appendChild(tab);
		desktopTabPanel = new Tabpanel();
		desktopTabPanel.setHeight("100%");
		desktopTabPanel.appendChild(m_tableAtp);
		tabPanels.appendChild(desktopTabPanel);
		//
		int height = SessionManager.getAppDesktop().getClientInfo().desktopHeight * 90 / 100;
		int width = SessionManager.getAppDesktop().getClientInfo().desktopWidth * 80 / 100;

        borderlayout.setWidth("100%");
        borderlayout.setHeight("100%");
        if (isLookup())
        	borderlayout.setStyle("border: none; position: relative");
        else
        	borderlayout.setStyle("border: none; position: absolute");
        Center center = new Center();
        center.setAutoscroll(true);
        center.setFlex(true);
		borderlayout.appendChild(center);
		center.appendChild(contentPanel);
		South south = new South();
		int detailHeight = (height * 25 / 100);
		south.setHeight(detailHeight + "px");
		south.setCollapsible(true);
		south.setSplittable(true);
		south.setFlex(true);
		south.setTitle(Msg.translate(Env.getCtx(), "WarehouseStock"));
		south.setTooltiptext(Msg.translate(Env.getCtx(), "WarehouseStock"));
		borderlayout.appendChild(south);
		south.appendChild(tabbedPane);

        Borderlayout mainPanel = new Borderlayout();
        mainPanel.setWidth("100%");
        mainPanel.setHeight("100%");
        North north = new North();
        mainPanel.appendChild(north);
        north.appendChild(grid);
        center = new Center();
        mainPanel.appendChild(center);
        center.appendChild(borderlayout);
        south = new South();
        mainPanel.appendChild(south);
        south.appendChild(confirmPanel);
        if (!isLookup())
        {
        	mainPanel.setStyle("position: absolute");
        }

		this.appendChild(mainPanel);
		if (isLookup())
		{
			this.setWidth(width + "px");
			this.setHeight(height + "px");
		}

		contentPanel.addActionListener(new EventListener() {
			public void onEvent(Event event) throws Exception {
				int row = contentPanel.getSelectedRow();
				if (row >= 0) {
					int M_Warehouse_ID = 0;
					ListItem listitem = pickWarehouse.getSelectedItem();
					if (listitem != null)
						M_Warehouse_ID = (Integer)listitem.getValue();

					int M_PriceList_Version_ID = 0;
					listitem = pickPriceList.getSelectedItem();
					if (listitem != null)
						M_PriceList_Version_ID = (Integer)listitem.getValue();

        			refresh(contentPanel.getValueAt(row,2), M_Warehouse_ID, M_PriceList_Version_ID);
        			borderlayout.getSouth().setOpen(true);
				}
			}
		});
	}

	@Override
	protected void insertPagingComponent() {
		North north = new North();
		north.appendChild(paging);
		borderlayout.appendChild(north);
	}

	/**
	 * 	Refresh Query
	 */
	private void refresh(Object obj, int M_Warehouse_ID, int M_PriceList_Version_ID)
	{
		//int M_Product_ID = 0;
		String sql = m_sqlWarehouse;
		//Add description to the query
		sql = sql.replace(" FROM", ", DocumentNote FROM");
		log.finest(sql);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setString(1, (String)obj);
			rs = pstmt.executeQuery();
			fieldDescription.setText("");
			warehouseTbl.loadTable(rs);
			rs = pstmt.executeQuery();
			if(rs.next())
				if(rs.getString("DocumentNote") != null)
					fieldDescription.setText(rs.getString("DocumentNote"));
		}
		catch (Exception e)
		{
			log.log(Level.WARNING, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}

		try {
			sql = "SELECT M_Product_ID FROM M_Product WHERE Value = ?";
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setString(1, (String)obj);
			rs = pstmt.executeQuery();
			if(rs.next())
				m_M_Product_ID = rs.getInt(1);
		} catch (Exception e) {
			log.log(Level.WARNING, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}

		sql = m_sqlSubstitute;
		log.finest(sql);
		try {
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, m_M_Product_ID);
			pstmt.setInt(2, M_PriceList_Version_ID);
			rs = pstmt.executeQuery();
			substituteTbl.loadTable(rs);
			rs.close();
		} catch (Exception e) {
			log.log(Level.WARNING, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}

		sql = m_sqlRelated;
		log.finest(sql);
		try {
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, m_M_Product_ID);
			pstmt.setInt(2, M_PriceList_Version_ID);
			rs = pstmt.executeQuery();
			relatedTbl.loadTable(rs);
			rs.close();
		} catch (Exception e) {
			log.log(Level.WARNING, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		initAtpTab(M_Warehouse_ID);
	}	//	refresh

	/**
	 *	Dynamic Init
	 *
	 * @param value value
	 * @param M_Warehouse_ID warehouse
	 * @param M_PriceList_ID price list
	 */
	private void initInfo (String value, int M_Warehouse_ID, int M_PriceList_ID)
	{
		//	Pick init
		fillPicks(M_PriceList_ID);
		int M_PriceList_Version_ID = findPLV (M_PriceList_ID);
		//	Set Value or Name
		if (value.startsWith("@") && value.endsWith("@"))
			fieldName.setText(value.substring(1,value.length()-1));
		else
			fieldValue.setText(value);
		//	Set Warehouse
		if (M_Warehouse_ID == 0)
			M_Warehouse_ID = Env.getContextAsInt(Env.getCtx(), "#M_Warehouse_ID");
		if (M_Warehouse_ID != 0)
			setWarehouse (M_Warehouse_ID);
		// 	Set PriceList Version
		if (M_PriceList_Version_ID != 0)
			setPriceListVersion (M_PriceList_Version_ID);

		//	Create Grid
		StringBuffer where = new StringBuffer();
		where.append("p.IsActive='Y'");
		if (M_Warehouse_ID != 0)
			where.append(" AND p.IsSummary='N'");
		//  dynamic Where Clause
		if (p_whereClause != null && p_whereClause.length() > 0)
			where.append(" AND ")   //  replace fully qalified name with alias
				.append(Util.replace(p_whereClause, "M_Product.", "p."));
		//
		prepareTable(getProductLayout(),
			s_productFrom,
			where.toString(),
			"QtyAvailable DESC, Margin DESC");

		//
		pickWarehouse.addEventListener(Events.ON_SELECT,this);
		pickPriceList.addEventListener(Events.ON_SELECT,this);
		pickProductCategory.addEventListener(Events.ON_SELECT, this); // Elaine 2008/11/21
		pickAS.addEventListener(Events.ON_SELECT, this);
	}	//	initInfo

	/**
	 *	Fill Picks with values
	 *
	 * @param M_PriceList_ID price list
	 */
	private void fillPicks (int M_PriceList_ID)
	{
		//	Price List
		String SQL = "SELECT M_PriceList_Version.M_PriceList_Version_ID,"
			+ " M_PriceList_Version.Name || ' (' || c.Iso_Code || ')' AS ValueName "
			+ "FROM M_PriceList_Version, M_PriceList pl, C_Currency c "
			+ "WHERE M_PriceList_Version.M_PriceList_ID=pl.M_PriceList_ID"
			+ " AND pl.C_Currency_ID=c.C_Currency_ID"
			+ " AND M_PriceList_Version.IsActive='Y' AND pl.IsActive='Y'";
		//	Same PL currency as original one
		if (M_PriceList_ID != 0)
			SQL += " AND EXISTS (SELECT * FROM M_PriceList xp WHERE xp.M_PriceList_ID=" + M_PriceList_ID
				+ " AND pl.C_Currency_ID=xp.C_Currency_ID)";
		//	Add Access & Order
		SQL = MRole.getDefault().addAccessSQL (SQL, "M_PriceList_Version", true, false)	// fully qualidfied - RO
			+ " ORDER BY M_PriceList_Version.Name";
		try
		{
			pickPriceList.appendItem("",new Integer(0));
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				pickPriceList.appendItem(rs.getString(2),new Integer(rs.getInt(1)));
			}
			rs.close();
			pstmt.close();

			//	Warehouse
			SQL = MRole.getDefault().addAccessSQL (
				"SELECT M_Warehouse_ID, Value || ' - ' || Name AS ValueName "
				+ "FROM M_Warehouse "
				+ "WHERE IsActive='Y'",
					"M_Warehouse", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO)
				+ " ORDER BY Value";
			pickWarehouse.appendItem("", new Integer(0));
			pstmt = DB.prepareStatement(SQL, null);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				pickWarehouse.appendItem(rs.getString("ValueName"), new Integer(rs.getInt("M_Warehouse_ID")));
			}
			rs.close();
			pstmt.close();

			// Elaine 2008/11/21
			//	Product Category
			SQL = MRole.getDefault().addAccessSQL (
				"SELECT M_Product_Category_ID, Value || ' - ' || Name FROM M_Product_Category WHERE IsActive='Y'",
					"M_Product_Category", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO)
				+ " ORDER BY Value";
			for (KeyNamePair kn : DB.getKeyNamePairs(SQL, true)) {
				pickProductCategory.addItem(kn);
			}

			//	Attribute Sets
			SQL = MRole.getDefault().addAccessSQL (
				"SELECT M_AttributeSet_ID, Name FROM M_AttributeSet WHERE IsActive='Y'",
					"M_AttributeSet", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO)
				+ " ORDER BY Name";
			for (KeyNamePair kn : DB.getKeyNamePairs(SQL, true)) {
				pickAS.addItem(kn);
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, SQL, e);
			setStatusLine(e.getLocalizedMessage(), true);
		}
	}	//	fillPicks

	/**
	 *	Set Warehouse
	 *
	 * 	@param M_Warehouse_ID warehouse
	 */
	private void setWarehouse(int M_Warehouse_ID)
	{
		for (int i = 0; i < pickWarehouse.getItemCount(); i++)
		{
			 Integer key = (Integer) pickWarehouse.getItemAtIndex(i).getValue();
			if (key == M_Warehouse_ID)
			{
				pickWarehouse.setSelectedIndex(i);
				return;
			}
		}
	}	//	setWarehouse

	/**
	 *	Set PriceList
	 *
	 * @param M_PriceList_Version_ID price list
	 */
	private void setPriceListVersion(int M_PriceList_Version_ID)
	{
		log.config("M_PriceList_Version_ID=" + M_PriceList_Version_ID);
		for (int i = 0; i < pickPriceList.getItemCount(); i++)
		{
			Integer key = (Integer) pickPriceList.getItemAtIndex(i).getValue();
			if (key == M_PriceList_Version_ID)
			{
				pickPriceList.setSelectedIndex(i);
				return;
			}
		}
		log.fine("NOT found");
	}	//	setPriceList

	/**
	 *	Find Price List Version and update context
	 *
	 * @param M_PriceList_ID price list
	 * @return M_PriceList_Version_ID price list version
	 */
	private int findPLV (int M_PriceList_ID)
	{
		Timestamp priceDate = null;
		//	Sales Order Date
		String dateStr = Env.getContext(Env.getCtx(), p_WindowNo, "DateOrdered");
		if (dateStr != null && dateStr.length() > 0)
			priceDate = Env.getContextAsDate(Env.getCtx(), p_WindowNo, "DateOrdered");
		else	//	Invoice Date
		{
			dateStr = Env.getContext(Env.getCtx(), p_WindowNo, "DateInvoiced");
			if (dateStr != null && dateStr.length() > 0)
				priceDate = Env.getContextAsDate(Env.getCtx(), p_WindowNo, "DateInvoiced");
		}
		//	Today
		if (priceDate == null)
			priceDate = new Timestamp(System.currentTimeMillis());
		//
		log.config("M_PriceList_ID=" + M_PriceList_ID + " - " + priceDate);
		int retValue = 0;
		String sql = "SELECT plv.M_PriceList_Version_ID, plv.ValidFrom "
			+ "FROM M_PriceList pl, M_PriceList_Version plv "
			+ "WHERE pl.M_PriceList_ID=plv.M_PriceList_ID"
			+ " AND plv.IsActive='Y'"
			+ " AND pl.M_PriceList_ID=? "					//	1
			+ "ORDER BY plv.ValidFrom DESC";
		//	find newest one
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, M_PriceList_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next() && retValue == 0)
			{
				Timestamp plDate = rs.getTimestamp(2);
				if (!priceDate.before(plDate))
					retValue = rs.getInt(1);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		Env.setContext(Env.getCtx(), p_WindowNo, "M_PriceList_Version_ID", retValue);
		return retValue;
	}	//	findPLV


	/**************************************************************************
	 *	Construct SQL Where Clause and define parameters
	 *  (setParameters needs to set parameters)
	 *  Includes first AND
	 *  @return SQL WHERE clause
	 */
	public String getSQLWhere()
	{
		StringBuffer where = new StringBuffer();

		//	Optional PLV
		int M_PriceList_Version_ID = 0;
		ListItem listitem = pickPriceList.getSelectedItem();
		if (listitem != null)
			M_PriceList_Version_ID = (Integer)listitem.getValue();
		if (M_PriceList_Version_ID != 0)
			where.append(" AND pr.M_PriceList_Version_ID=?");

		// Elaine 2008/11/29
		//  Optional Product Category
		if (getM_Product_Category_ID() > 0) {
			where.append(" AND p.M_Product_Category_ID=?");
		}
		//

		//  Optional Attribute Set
		if (getM_AttributeSet_ID() > 0) {
			where.append(" AND p.M_AttributeSet_ID=?");
		}

		//	Product Attribute Search
		if (m_pAttributeWhere != null)
		{
			where.append(m_pAttributeWhere);
			return where.toString();
		}

		//  => Value
		String value = fieldValue.getText().toUpperCase();
		if (!(value.equals("") || value.equals("%")))
			where.append(" AND UPPER(p.Value) LIKE ?");

		//  => Name
		String name = fieldName.getText().toUpperCase();
		if (!(name.equals("") || name.equals("%")))
			where.append(" AND UPPER(p.Name) LIKE ?");

		//  => UPC
		String upc = fieldUPC.getText().toUpperCase();
		if (!(upc.equals("") || upc.equals("%")))
			where.append(" AND UPPER(p.UPC) LIKE ?");

		//  => SKU
		String sku = fieldSKU.getText().toUpperCase();
		if (!(sku.equals("") || sku.equals("%")))
			where.append(" AND UPPER(p.SKU) LIKE ?");
		//	=> Vendor
		String vendor = fieldVendor.getText().toUpperCase();
		if (!(vendor.equals("") || vendor.equals("%")))
			where.append(" AND UPPER(bp.Name) LIKE ? AND ppo.IsCurrentVendor='Y' AND ppo.IsActive='Y'"); // Elaine 2008/12/16

		return where.toString();
	}	//	getSQLWhere

	/**
	 *  Set Parameters for Query
	 *  (as defined in getSQLWhere)	 *
	 * @param pstmt pstmt
	 *  @param forCount for counting records
	 * @throws SQLException
	 */
	protected void setParameters(PreparedStatement pstmt, boolean forCount) throws SQLException
	{
		int index = 1;

		//  => Warehouse
		int M_Warehouse_ID = 0;
		ListItem listitem = pickWarehouse.getSelectedItem();
		if (listitem != null)
			M_Warehouse_ID = (Integer)listitem.getValue();
		if (!forCount)	//	parameters in select
		{
			for (int i = 0; i < p_layout.length; i++)
			{
				if (p_layout[i].getColSQL().indexOf('?') != -1)
					pstmt.setInt(index++, M_Warehouse_ID);
			}
		}
		log.fine("M_Warehouse_ID=" + M_Warehouse_ID + " (" + (index-1) + "*)");

		//  => PriceList
		int M_PriceList_Version_ID = 0;
		ListItem lstitem = pickPriceList.getSelectedItem();
		if (lstitem != null)
			M_PriceList_Version_ID = (Integer)lstitem.getValue();
		if (M_PriceList_Version_ID != 0)
		{
			pstmt.setInt(index++, M_PriceList_Version_ID);
			log.fine("M_PriceList_Version_ID=" + M_PriceList_Version_ID);
		}
		// Elaine 2008/11/29
		//  => Product Category
		int M_Product_Category_ID = getM_Product_Category_ID();
		if (M_Product_Category_ID > 0) {
			pstmt.setInt(index++, M_Product_Category_ID);
			log.fine("M_Product_Category_ID=" + M_Product_Category_ID);
		}
		//
		int M_AttributeSet_ID = getM_AttributeSet_ID();
		if (M_AttributeSet_ID > 0) {
			pstmt.setInt(index++, M_AttributeSet_ID);
			log.fine("M_AttributeSet_ID=" + M_AttributeSet_ID);
		}
		//	Rest of Parameter in Query for Attribute Search
		if (m_pAttributeWhere != null)
			return;

		//  => Value
		String value = fieldValue.getText().toUpperCase();
		if (!(value.equals("") || value.equals("%")))
		{
			if (!value.endsWith("%"))
				value += "%";
			pstmt.setString(index++, value);
			log.fine("Value: " + value);
		}

		//  => Name
		String name = fieldName.getText().toUpperCase();
		if (!(name.equals("") || name.equals("%")))
		{
			if (!name.endsWith("%"))
				name += "%";
			pstmt.setString(index++, name);
			log.fine("Name: " + name);
		}

		//  => UPC
		String upc = fieldUPC.getText().toUpperCase();
		if (!(upc.equals("") || upc.equals("%")))
		{
			if (!upc.endsWith("%"))
				upc += "%";
			pstmt.setString(index++, upc);
			log.fine("UPC: " + upc);
		}

		//  => SKU
		String sku = fieldSKU.getText().toUpperCase();
		if (!(sku.equals("") || sku.equals("%")))
		{
			if (!sku.endsWith("%"))
				sku += "%";
			pstmt.setString(index++, sku);
			log.fine("SKU: " + sku);
		}

		//  => Vendor
		String vendor = fieldVendor.getText().toUpperCase();
		if (!(vendor.equals("") || vendor.equals("%")))
		{
			if (!vendor.endsWith("%"))
				vendor += "%";
			pstmt.setString(index++, vendor);
			log.fine("Vendor: " + vendor);
		}
	}   //  setParameters

	/**
	 * 	Query per Product Attribute.
	 *  <code>
	 * 	Available synonyms:
	 *		M_Product p
	 *		M_ProductPrice pr
	 *		M_AttributeSet pa
	 *	</code>
	 */
	private void cmd_InfoPAttribute()
	{
		InfoPAttributePanel ia = new InfoPAttributePanel(this);
		m_pAttributeWhere = ia.getWhereClause();
		if (m_pAttributeWhere != null)
		{
			executeQuery();
			renderItems();
		}
	}	//	cmdInfoAttribute

	/**
	 *	Show History
	 */
	protected void showHistory()
	{
		log.info("");
		Integer M_Product_ID = getSelectedRowKey();
		if (M_Product_ID == null)
			return;
		int M_Warehouse_ID = 0;
		ListItem listitem = pickWarehouse.getSelectedItem();
		if (listitem != null)
			M_Warehouse_ID = (Integer)listitem.getValue();
		int M_AttributeSetInstance_ID = m_M_AttributeSetInstance_ID;
		if (m_M_AttributeSetInstance_ID < -1)	//	not selected
			M_AttributeSetInstance_ID = 0;
		//
		InvoiceHistory ih = new InvoiceHistory (this, 0,
			M_Product_ID.intValue(), M_Warehouse_ID, M_AttributeSetInstance_ID);
		ih.setVisible(true);
		ih = null;
	}	//	showHistory

	/**
	 *	Has History
	 *
	 * @return true (has history)
	 */
	protected boolean hasHistory()
	{
		return true;
	}	//	hasHistory

	// Elaine 2008/12/16
	/**
	 *	Zoom
	 */
	public void zoom()
	{
		log.info("");
		Integer M_Product_ID = getSelectedRowKey();
		if (M_Product_ID == null)
			return;

		MQuery query = new MQuery("M_Product");
		query.addRestriction("M_Product_ID", MQuery.EQUAL, M_Product_ID);
		query.setRecordCount(1);
		int AD_WindowNo = getAD_Window_ID("M_Product", true);	//	SO
		AEnv.zoom (AD_WindowNo, query);
	}	//	zoom
	//

	/**
	 *	Has Zoom
	 *  @return (has zoom)
	 */
	protected boolean hasZoom()
	{
		return true;
	}	//	hasZoom

	/**
	 *	Customize
	 */
	protected void customize()
	{
		log.info("");
	}	//	customize

	/**
	 *	Has Customize
	 *  @return false (no customize)
	 */
	protected boolean hasCustomize()
	{
		return false;	//	for now
	}	//	hasCustomize

	/**
	 *	Save Selection Settings for PriceList
	 */
	protected void saveSelectionDetail()
	{
		//  publish for Callout to read
		Integer ID = getSelectedRowKey();
		Env.setContext(Env.getCtx(), p_WindowNo, Env.TAB_INFO, "M_Product_ID", ID == null ? "0" : ID.toString());
		ListItem pickPL = (ListItem)pickPriceList.getSelectedItem();
		if (pickPL!=null)
		{
            Env.setContext(Env.getCtx(), p_WindowNo, Env.TAB_INFO, "M_PriceList_Version_ID",pickPL.getValue().toString());
        }
		ListItem pickWH = (ListItem)pickWarehouse.getSelectedItem();
		if (pickWH != null)
        {
            Env.setContext(Env.getCtx(), p_WindowNo, Env.TAB_INFO, "M_Warehouse_ID",pickWH.getValue().toString());
        }
		//
		if (m_M_AttributeSetInstance_ID == -1)	//	not selected
		{
			Env.setContext(Env.getCtx(), p_WindowNo, Env.TAB_INFO, "M_AttributeSetInstance_ID", "0");
			Env.setContext(Env.getCtx(), p_WindowNo, Env.TAB_INFO, "M_Locator_ID", "0");
		}
		else
		{
			Env.setContext(Env.getCtx(), p_WindowNo, Env.TAB_INFO, "M_AttributeSetInstance_ID",
				String.valueOf(m_M_AttributeSetInstance_ID));
			Env.setContext(Env.getCtx(), p_WindowNo, Env.TAB_INFO, "M_Locator_ID",
				String.valueOf(m_M_Locator_ID));
		}
	}	//	saveSelectionDetail

	/**
	 *  Get Product Layout
	 *
	 * @return array of Column_Info
	 */
	protected ColumnInfo[] getProductLayout()
	{
		if (s_productLayout != null)
			return s_productLayout;
		//  Euro 13
		MClient client = MClient.get(Env.getCtx());
		if ("FRIE".equals(client.getValue()))
		{
			final ColumnInfo[] frieLayout = {
				new ColumnInfo(" ", "p.M_Product_ID", IDColumn.class),
				new ColumnInfo(Msg.translate(Env.getCtx(), "Name"), "p.Name", String.class),
				new ColumnInfo(Msg.translate(Env.getCtx(), "QtyAvailable"), "bomQtyAvailable(p.M_Product_ID,?,0) AS QtyAvailable", Double.class, true, true, null),
				new ColumnInfo(Msg.translate(Env.getCtx(), "PriceList"), "bomPriceList(p.M_Product_ID, pr.M_PriceList_Version_ID) AS PriceList",  BigDecimal.class),
				new ColumnInfo(Msg.translate(Env.getCtx(), "PriceStd"), "bomPriceStd(p.M_Product_ID, pr.M_PriceList_Version_ID) AS PriceStd", BigDecimal.class),
				new ColumnInfo("Einzel MWSt", "pr.PriceStd * 1.16", BigDecimal.class),
				new ColumnInfo("Einzel kompl", "(pr.PriceStd+13) * 1.16", BigDecimal.class),
				new ColumnInfo("Satz kompl", "((pr.PriceStd+13) * 1.16) * 4", BigDecimal.class),
				new ColumnInfo(Msg.translate(Env.getCtx(), "QtyOnHand"), "bomQtyOnHand(p.M_Product_ID,?,0) AS QtyOnHand", Double.class),
				new ColumnInfo(Msg.translate(Env.getCtx(), "QtyReserved"), "bomQtyReserved(p.M_Product_ID,?,0) AS QtyReserved", Double.class),
				new ColumnInfo(Msg.translate(Env.getCtx(), "QtyOrdered"), "bomQtyOrdered(p.M_Product_ID,?,0) AS QtyOrdered", Double.class),
				new ColumnInfo(Msg.translate(Env.getCtx(), "Discontinued").substring(0, 1), "p.Discontinued", Boolean.class),
				new ColumnInfo(Msg.translate(Env.getCtx(), "Margin"), "bomPriceStd(p.M_Product_ID, pr.M_PriceList_Version_ID)-bomPriceLimit(p.M_Product_ID, pr.M_PriceList_Version_ID) AS Margin", BigDecimal.class),
				new ColumnInfo(Msg.translate(Env.getCtx(), "PriceLimit"), "bomPriceLimit(p.M_Product_ID, pr.M_PriceList_Version_ID) AS PriceLimit", BigDecimal.class),
				new ColumnInfo(Msg.translate(Env.getCtx(), "IsInstanceAttribute"), "pa.IsInstanceAttribute", Boolean.class)
			};
			INDEX_NAME = 2;
			INDEX_PATTRIBUTE = frieLayout.length - 1;	//	last item
			s_productLayout = frieLayout;
			return s_productLayout;
		}
		//
		if (s_productLayout == null)
		{
			ArrayList<ColumnInfo> list = new ArrayList<ColumnInfo>();
			list.add(new ColumnInfo(" ", "p.M_Product_ID", IDColumn.class));
			list.add(new ColumnInfo(Msg.translate(Env.getCtx(), "Discontinued").substring(0, 1), "p.Discontinued", Boolean.class));
			list.add(new ColumnInfo(Msg.translate(Env.getCtx(), "Value"), "p.Value", String.class));
			list.add(new ColumnInfo(Msg.translate(Env.getCtx(), "Name"), "p.Name", String.class));
			list.add(new ColumnInfo(Msg.translate(Env.getCtx(), "QtyAvailable"), "bomQtyAvailable(p.M_Product_ID,?,0) AS QtyAvailable", Double.class, true, true, null));
			list.add(new ColumnInfo(Msg.translate(Env.getCtx(), "PriceList"), "bomPriceList(p.M_Product_ID, pr.M_PriceList_Version_ID) AS PriceList",  BigDecimal.class));
			list.add(new ColumnInfo(Msg.translate(Env.getCtx(), "PriceStd"), "bomPriceStd(p.M_Product_ID, pr.M_PriceList_Version_ID) AS PriceStd", BigDecimal.class));
			list.add(new ColumnInfo(Msg.translate(Env.getCtx(), "QtyOnHand"), "bomQtyOnHand(p.M_Product_ID,?,0) AS QtyOnHand", Double.class));
			list.add(new ColumnInfo(Msg.translate(Env.getCtx(), "QtyReserved"), "bomQtyReserved(p.M_Product_ID,?,0) AS QtyReserved", Double.class));
			list.add(new ColumnInfo(Msg.translate(Env.getCtx(), "QtyOrdered"), "bomQtyOrdered(p.M_Product_ID,?,0) AS QtyOrdered", Double.class));
			if (isUnconfirmed())
			{
				list.add(new ColumnInfo(Msg.translate(Env.getCtx(), "QtyUnconfirmed"), "(SELECT SUM(c.TargetQty) FROM M_InOutLineConfirm c INNER JOIN M_InOutLine il ON (c.M_InOutLine_ID=il.M_InOutLine_ID) INNER JOIN M_InOut i ON (il.M_InOut_ID=i.M_InOut_ID) WHERE c.Processed='N' AND i.M_Warehouse_ID=? AND il.M_Product_ID=p.M_Product_ID) AS QtyUnconfirmed", Double.class));
				list.add(new ColumnInfo(Msg.translate(Env.getCtx(), "QtyUnconfirmedMove"), "(SELECT SUM(c.TargetQty) FROM M_MovementLineConfirm c INNER JOIN M_MovementLine ml ON (c.M_MovementLine_ID=ml.M_MovementLine_ID) INNER JOIN M_Locator l ON (ml.M_LocatorTo_ID=l.M_Locator_ID) WHERE c.Processed='N' AND l.M_Warehouse_ID=? AND ml.M_Product_ID=p.M_Product_ID) AS QtyUnconfirmedMove", Double.class));
			}
			list.add(new ColumnInfo(Msg.translate(Env.getCtx(), "Margin"), "bomPriceStd(p.M_Product_ID, pr.M_PriceList_Version_ID)-bomPriceLimit(p.M_Product_ID, pr.M_PriceList_Version_ID) AS Margin", BigDecimal.class));
			list.add(new ColumnInfo(Msg.translate(Env.getCtx(), "Vendor"), "bp.Name", String.class));
			list.add(new ColumnInfo(Msg.translate(Env.getCtx(), "PriceLimit"), "bomPriceLimit(p.M_Product_ID, pr.M_PriceList_Version_ID) AS PriceLimit", BigDecimal.class));
			list.add(new ColumnInfo(Msg.translate(Env.getCtx(), "IsInstanceAttribute"), "pa.IsInstanceAttribute", Boolean.class));
			s_productLayout = new ColumnInfo[list.size()];
			list.toArray(s_productLayout);
			INDEX_NAME = 3;
			INDEX_PATTRIBUTE = s_productLayout.length - 1;	//	last item
		}
		return s_productLayout;
	}   //  getProductLayout

	/**
	 * 	System has Unforfirmed records
	 *	@return true if unconfirmed
	 */
	private boolean isUnconfirmed()
	{
		int no = DB.getSQLValue(null,
			"SELECT COUNT(*) FROM M_InOutLineConfirm WHERE AD_Client_ID=?",
			Env.getAD_Client_ID(Env.getCtx()));
		if (no > 0)
			return true;
		no = DB.getSQLValue(null,
			"SELECT COUNT(*) FROM M_MovementLineConfirm WHERE AD_Client_ID=?",
			Env.getAD_Client_ID(Env.getCtx()));
		return no > 0;
	}	//	isUnconfirmed

    public void onEvent(Event e)
    {
    	Component component = e.getTarget();

    	// Elaine 2008/12/16
		//  don't requery if fieldValue and fieldName are empty
		if ((e.getTarget() == pickWarehouse || e.getTarget() == pickPriceList)
			&& (fieldValue.getText().length() == 0 && fieldName.getText().length() == 0))
			return;
		//

    	if(component == m_InfoPAttributeButton)
    	{
    		cmd_InfoPAttribute();
    		return;
    	}

    	m_pAttributeWhere = null;
    	// Query Product Attribure Instance
		int row = contentPanel.getSelectedRow();
		if (component.equals(m_PAttributeButton) && row != -1)
		{
			Integer productInteger = getSelectedRowKey();
			String productName = (String)contentPanel.getValueAt(row, INDEX_NAME);

			ListItem warehouse = pickWarehouse.getSelectedItem();
			if (productInteger == null || productInteger.intValue() == 0 || warehouse == null)
				return;

			int M_Warehouse_ID = 0;
			if(warehouse.getValue() != null)
				M_Warehouse_ID = ((Integer)warehouse.getValue()).intValue();

			String title = warehouse.getLabel() + " - " + productName;
			InfoPAttributeInstancePanel pai = new InfoPAttributeInstancePanel(this, title,
				M_Warehouse_ID, 0, productInteger.intValue(), m_C_BPartner_ID);
			m_M_AttributeSetInstance_ID = pai.getM_AttributeSetInstance_ID();
			m_M_Locator_ID = pai.getM_Locator_ID();
			if (m_M_AttributeSetInstance_ID != -1)
				dispose(true);
			return;
		}
		//
		super.onEvent(e);
    }

	/**
	 *  Enable PAttribute if row selected/changed
	 */
	protected void enableButtons ()
	{
		m_M_AttributeSetInstance_ID = -1;
		if (m_PAttributeButton != null)
		{
			int row = contentPanel.getSelectedRow();
			boolean enabled = false;
			if (row >= 0)
			{
				Object value = contentPanel.getValueAt(row, INDEX_PATTRIBUTE);
				enabled = Boolean.TRUE.equals(value);
			}
			m_PAttributeButton.setEnabled(enabled);
		}

		super.enableButtons();
	}   //  enableButtons

    // Elaine 2008/11/26
	/**
	 *	Query ATP
	 */
	private void initAtpTab (int  m_M_Warehouse_ID)
	{
		//	Header
		Vector<String> columnNames = new Vector<String>();
		columnNames.add(Msg.translate(Env.getCtx(), "Date"));
		columnNames.add(Msg.translate(Env.getCtx(), "QtyOnHand"));
		columnNames.add(Msg.translate(Env.getCtx(), "C_BPartner_ID"));
		columnNames.add(Msg.translate(Env.getCtx(), "QtyOrdered"));
		columnNames.add(Msg.translate(Env.getCtx(), "QtyReserved"));
		columnNames.add(Msg.translate(Env.getCtx(), "M_Locator_ID"));
		columnNames.add(Msg.translate(Env.getCtx(), "M_AttributeSetInstance_ID"));
		columnNames.add(Msg.translate(Env.getCtx(), "DocumentNo"));
		columnNames.add(Msg.translate(Env.getCtx(), "M_Warehouse_ID"));

		//	Fill Storage Data
		boolean showDetail = CLogMgt.isLevelFine();
		String sql = "SELECT s.QtyOnHand, s.QtyReserved, s.QtyOrdered,"
			+ " productAttribute(s.M_AttributeSetInstance_ID), s.M_AttributeSetInstance_ID,";
		if (!showDetail)
			sql = "SELECT SUM(s.QtyOnHand), SUM(s.QtyReserved), SUM(s.QtyOrdered),"
				+ " productAttribute(s.M_AttributeSetInstance_ID), 0,";
		sql += " w.Name, l.Value "
			+ "FROM M_Storage s"
			+ " INNER JOIN M_Locator l ON (s.M_Locator_ID=l.M_Locator_ID)"
			+ " INNER JOIN M_Warehouse w ON (l.M_Warehouse_ID=w.M_Warehouse_ID) "
			+ "WHERE M_Product_ID=?";
		if (m_M_Warehouse_ID != 0)
			sql += " AND l.M_Warehouse_ID=?";
		if (m_M_AttributeSetInstance_ID > 0)
			sql += " AND s.M_AttributeSetInstance_ID=?";
		sql += " AND (s.QtyOnHand<>0 OR s.QtyReserved<>0 OR s.QtyOrdered<>0)";
		if (!showDetail)
			sql += " GROUP BY productAttribute(s.M_AttributeSetInstance_ID), w.Name, l.Value";
		sql += " ORDER BY l.Value";

		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		double qty = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, m_M_Product_ID);
			if (m_M_Warehouse_ID != 0)
				pstmt.setInt(2, m_M_Warehouse_ID);
			if (m_M_AttributeSetInstance_ID > 0)
				pstmt.setInt(3, m_M_AttributeSetInstance_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				Vector<Object> line = new Vector<Object>(9);
				line.add(null);							//  Date
				double qtyOnHand = rs.getDouble(1);
				qty += qtyOnHand;
				line.add(new Double(qtyOnHand));  		//  Qty
				line.add(null);							//  BPartner
				line.add(new Double(rs.getDouble(3)));  //  QtyOrdered
				line.add(new Double(rs.getDouble(2)));  //  QtyReserved
				line.add(rs.getString(7));      		//  Locator
				String asi = rs.getString(4);
				if (showDetail && (asi == null || asi.length() == 0))
					asi = "{" + rs.getInt(5) + "}";
				line.add(asi);							//  ASI
				line.add(null);							//  DocumentNo
				line.add(rs.getString(6));  			//	Warehouse
				data.add(line);
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}

		//	Orders
		sql = "SELECT o.DatePromised, ol.QtyReserved,"
			+ " productAttribute(ol.M_AttributeSetInstance_ID), ol.M_AttributeSetInstance_ID,"
			+ " dt.DocBaseType, bp.Name,"
			+ " dt.PrintName || ' ' || o.DocumentNo As DocumentNo, w.Name "
			+ "FROM C_Order o"
			+ " INNER JOIN C_OrderLine ol ON (o.C_Order_ID=ol.C_Order_ID)"
			+ " INNER JOIN C_DocType dt ON (o.C_DocType_ID=dt.C_DocType_ID)"
			+ " INNER JOIN M_Warehouse w ON (ol.M_Warehouse_ID=w.M_Warehouse_ID)"
			+ " INNER JOIN C_BPartner bp  ON (o.C_BPartner_ID=bp.C_BPartner_ID) "
			+ "WHERE ol.QtyReserved<>0"
			+ " AND ol.M_Product_ID=?";
		if (m_M_Warehouse_ID != 0)
			sql += " AND ol.M_Warehouse_ID=?";
		if (m_M_AttributeSetInstance_ID > 0)
			sql += " AND ol.M_AttributeSetInstance_ID=?";
		sql += " ORDER BY o.DatePromised";
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, m_M_Product_ID);
			if (m_M_Warehouse_ID != 0)
				pstmt.setInt(2, m_M_Warehouse_ID);
			if (m_M_AttributeSetInstance_ID > 0)
				pstmt.setInt(3, m_M_AttributeSetInstance_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				Vector<Object> line = new Vector<Object>(9);
				line.add(rs.getTimestamp(1));			//  Date
				double oq = rs.getDouble(2);
				String DocBaseType = rs.getString(5);
				Double qtyReserved = null;
				Double qtyOrdered = null;
				if (MDocType.DOCBASETYPE_PurchaseOrder.equals(DocBaseType))
				{
					qtyOrdered = new Double(oq);
					qty += oq;
				}
				else
				{
					qtyReserved = new Double(oq);
					qty -= oq;
				}
				line.add(new Double(qty)); 		 		//  Qty
				line.add(rs.getString(6));				//  BPartner
				line.add(qtyOrdered);					//  QtyOrdered
				line.add(qtyReserved);					//  QtyReserved
				line.add(null);				      		//  Locator
				String asi = rs.getString(3);
				if (showDetail && (asi == null || asi.length() == 0))
					asi = "{" + rs.getInt(4) + "}";
				line.add(asi);							//  ASI
				line.add(rs.getString(7));				//  DocumentNo
				line.add(rs.getString(8));  			//	Warehouse
				data.add(line);
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}

		//  Table
		ListModelTable model = new ListModelTable(data);
		m_tableAtp.setData(model, columnNames);
		//
		m_tableAtp.setColumnClass(0, Timestamp.class, true);   //  Date
		m_tableAtp.setColumnClass(1, Double.class, true);      //  Quantity
		m_tableAtp.setColumnClass(2, String.class, true);      //  Partner
		m_tableAtp.setColumnClass(3, Double.class, true);      //  Quantity
		m_tableAtp.setColumnClass(4, Double.class, true);      //  Quantity
		m_tableAtp.setColumnClass(5, String.class, true);   	  //  Locator
		m_tableAtp.setColumnClass(6, String.class, true);   	  //  ASI
		m_tableAtp.setColumnClass(7, String.class, true);      //  DocNo
		m_tableAtp.setColumnClass(8, String.class, true);   	  //  Warehouse
		//
		m_tableAtp.autoSize();
	}	//	initAtpTab
	//

    // Elaine 2008/11/21
    public int getM_Product_Category_ID()
    {
		int M_Product_Category_ID = 0;

		ListItem pickPC = (ListItem)pickProductCategory.getSelectedItem();
		if (pickPC!=null)
			M_Product_Category_ID = Integer.parseInt(pickPC.getValue().toString());

		return M_Product_Category_ID;
	}
    //
    
    public int getM_AttributeSet_ID()
    {
		int M_AttributeSet_ID = 0;

		ListItem itemAS = (ListItem)pickAS.getSelectedItem();
		if (itemAS!=null)
			M_AttributeSet_ID = Integer.parseInt(itemAS.getValue().toString());

		return M_AttributeSet_ID;
	}
    
    /**
	 *	Standard Constructor
	 * 	@param WindowNo window no
	 * 	@param M_Warehouse_ID warehouse
	 * 	@param M_PriceList_ID price list
	 *  @param record_id The record ID to find
	 *  @param value Query Value or Name if enclosed in @
	 * 	@param multiSelection multiple selections
	 *  @param saveResults  True if results will be saved, false for info only
	 * 	@param whereClause where clause
	 *  @param modal True if the column has a lookup - open modal
	 */
	public InfoProductPanel(int windowNo, boolean modal,
		int M_Warehouse_ID, int M_PriceList_ID, int record_id, String value,
		boolean multipleSelection, boolean saveResults, String whereClause)
	{
		super (windowNo, modal, "p", "M_Product_ID",multipleSelection, saveResults, whereClause);
		log.info(value + ", Wh=" + M_Warehouse_ID + ", PL=" + M_PriceList_ID + ", WHERE=" + whereClause);
		setTitle(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "InfoProduct")));
		m_M_Warehouse_ID = M_Warehouse_ID;
		m_M_PriceList_ID = M_PriceList_ID;
		//
		//	Modify where clause to fit with column info definitions
		StringBuffer where = new StringBuffer();
		where.append("p.IsActive='Y'");
		//  Modify Where Clause
		if (whereClause != null && whereClause.length() > 0)
			where.append(" AND ")   //  replace fully qualified name with alias
				.append(Util.replace(whereClause, "M_Product.", "p."));
		setWhereClause(where.toString());
		//
		statInit();
		initInfo (record_id, value, M_Warehouse_ID, M_PriceList_ID, false);

        if(autoQuery() || record_id != 0 || (value != null && value.length() > 0 && value != "%"))
        {
        	prepareAndExecuteQuery();
    	}
        
		p_loadedOK = true;
		
	}	//	InfoProductPanel
	
	private void statInit()
	{
		//  Fill the grid, setup the center data table & add the tabs
		initComponents();
		
		Rows rows = new Rows();

		Row row = new Row();
		rows.appendChild(row);
		row.setSpans("1, 1, 1, 1, 1, 1");
		row.appendChild(lblValue.rightAlign());
		row.appendChild(fieldValue);
		row.appendChild(lblWarehouse.rightAlign());
		row.appendChild(fWarehouse_ID.getComponent());
		row.appendChild(lblBlank.rightAlign());
		row.appendChild(checkOnlyStock);

		row = new Row();
		rows.appendChild(row);
		row.setSpans("1, 1, 1, 1, 1, 1");
		row.appendChild(lblName.rightAlign());
		row.appendChild(fieldName);
		row.appendChild(lblPriceList.rightAlign());
		row.appendChild(fPriceList_ID.getComponent());
		row.appendChild(lblAS.rightAlign());
		row.appendChild(fAS_ID.getComponent());
		//

		row = new Row();
		rows.appendChild(row);
		row.setSpans("1, 1, 1, 1, 1, 1");
		row.appendChild(lblUPC.rightAlign());
		row.appendChild(fieldUPC);
		row.appendChild(lblProductCategory.rightAlign());
		row.appendChild(fProductCategory_ID.getComponent());
		row.appendChild(lblASI.rightAlign());
		row.appendChild(fASI_ID.getComponent());
		
		row = new Row();
		rows.appendChild(row);
		row.setSpans("1, 1, 1, 1, 1, 1");
		row.appendChild(lblSKU.rightAlign());
		row.appendChild(fieldSKU);
		row.appendChild(lblVendor.rightAlign());
		row.appendChild(fVendor_ID.getComponent());
		row.appendChild(lblBlank.rightAlign());
		row.appendChild(checkAND);
		
		//
        ColumnInfo[] s_layoutWarehouse = new ColumnInfo[]{
        		new ColumnInfo(" ", "M_Warehouse_ID", IDColumn.class),
        		new ColumnInfo(Msg.translate(Env.getCtx(), "WarehouseName"), "WarehouseName", String.class),
        		new ColumnInfo(Msg.translate(Env.getCtx(), "QtyAvailable"), "sum(QtyAvailable)", Double.class, true, true, null),
        		new ColumnInfo(Msg.translate(Env.getCtx(), "QtyOnHand"), "sum(QtyOnHand)", Double.class),
           		new ColumnInfo(Msg.translate(Env.getCtx(), "QtyReserved"), "sum(QtyReserved)", Double.class),
           		new ColumnInfo(Msg.translate(Env.getCtx(), "QtyOrdered"), "sum(QtyOrdered)", Double.class)};
//        		new ColumnInfo(Msg.translate(Env.getCtx(), "DocumentNote"), "DocumentNote", String.class)};
        /**	From Clause							*/
        String s_sqlFrom = " M_PRODUCT_STOCK_V ";
        /** Where Clause						*/
        String s_sqlWhere = "(QtyOnHand <> 0 OR QtyAvailable <> 0 OR QtyReserved <> 0 OR QtyOrdered <> 0) AND M_Product_ID = ?";
//      String s_sqlWhere = "M_Product_ID = ?";
        m_sqlWarehouse = warehouseTbl.prepareTable(s_layoutWarehouse, s_sqlFrom, s_sqlWhere, false, "M_PRODUCT_STOCK_V");
		m_sqlWarehouse += " Group By M_Warehouse_ID, WarehouseName ";
		m_sqlWarehouse += " Order By sum(QtyOnHand) DESC, WarehouseName ";
		warehouseTbl.setMultiSelection(false);
        warehouseTbl.autoSize();
        warehouseTbl.setShowTotals(true);
        //warehouseTbl.getModel().addTableModelListener(this);
        warehouseTbl.setAttribute("zk_component_ID", "Lookup_Data_Warehouse");
		

        ColumnInfo[] s_layoutSubstitute = new ColumnInfo[]{
        		new ColumnInfo(Msg.translate(Env.getCtx(), "Warehouse"), "orgname", String.class),
        		new ColumnInfo(Msg.translate(Env.getCtx(), "Description"), "description", String.class),
        		new ColumnInfo(Msg.translate(Env.getCtx(), "Value"), "value", String.class),
    			new ColumnInfo(Msg.translate(Env.getCtx(), "Name"), "Name", String.class),
    			new ColumnInfo(Msg.translate(Env.getCtx(), "QtyAvailable"), "QtyAvailable", Double.class, true, true, null),
  	        	new ColumnInfo(Msg.translate(Env.getCtx(), "QtyOnHand"), "QtyOnHand", Double.class),
    	        new ColumnInfo(Msg.translate(Env.getCtx(), "QtyReserved"), "QtyReserved", Double.class),
  	        	new ColumnInfo(Msg.translate(Env.getCtx(), "PriceStd"), "PriceStd", Double.class)};
        s_sqlFrom = "M_PRODUCT_SUBSTITUTERELATED_V";
        s_sqlWhere = "M_Product_ID = ? AND M_PriceList_Version_ID = ? and RowType = 'S'";
        m_sqlSubstitute = substituteTbl.prepareTable(s_layoutSubstitute, s_sqlFrom, s_sqlWhere, false, "M_PRODUCT_SUBSTITUTERELATED_V");
        substituteTbl.setMultiSelection(false);
        substituteTbl.autoSize();
        substituteTbl.getModel().addTableModelListener(this);
        substituteTbl.setAttribute("zk_component_ID", "Lookup_Data_Substitute");

        ColumnInfo[] s_layoutRelated = new ColumnInfo[]{
        		new ColumnInfo(Msg.translate(Env.getCtx(), "Warehouse"), "orgname", String.class),
        		new ColumnInfo(Msg.translate(Env.getCtx(), "Description"), "description", String.class),
        		new ColumnInfo(Msg.translate(Env.getCtx(), "Value"), "value", String.class),
    			new ColumnInfo(Msg.translate(Env.getCtx(), "Name"), "Name", String.class),
    			new ColumnInfo(Msg.translate(Env.getCtx(), "QtyAvailable"), "QtyAvailable", Double.class, true, true, null),
  	        	new ColumnInfo(Msg.translate(Env.getCtx(), "QtyOnHand"), "QtyOnHand", Double.class),
    	        new ColumnInfo(Msg.translate(Env.getCtx(), "QtyReserved"), "QtyReserved", Double.class),
  	        	new ColumnInfo(Msg.translate(Env.getCtx(), "PriceStd"), "PriceStd", Double.class)};
        s_sqlFrom = "M_PRODUCT_SUBSTITUTERELATED_V";
        s_sqlWhere = "M_Product_ID = ? AND M_PriceList_Version_ID = ? and RowType = 'R'";
        m_sqlRelated = relatedTbl.prepareTable(s_layoutRelated, s_sqlFrom, s_sqlWhere, false, "M_PRODUCT_SUBSTITUTERELATED_V");
        relatedTbl.setMultiSelection(false);
        relatedTbl.autoSize();
        relatedTbl.getModel().addTableModelListener(this);
        relatedTbl.setAttribute("zk_component_ID", "Lookup_Data_Related");

        //Available to Promise Tab
        m_tableAtp.setMultiSelection(false);
        m_tableAtp.autoSize();
        m_tableAtp.setShowTotals(true);
        m_tableAtp.setAttribute("zk_component_ID", "Lookup_Data_ATP");

        //Vendor tab
        ColumnInfo[] s_layoutVendor = new ColumnInfo[]{
        		new ColumnInfo(Msg.translate(Env.getCtx(), "Vendor"), "(SELECT bp.Name FROM C_BPartner bp WHERE bp.C_BPartner_ID = M_PRODUCT_PO.C_BPartner_ID)", String.class),
        		new ColumnInfo(Msg.translate(Env.getCtx(), "IsCurrentVendor"), "IsCurrentVendor", Boolean.class),
        		new ColumnInfo(Msg.translate(Env.getCtx(), "C_UOM_ID"), "(SELECT Name FROM C_UOM WHERE C_UOM_ID = M_PRODUCT_PO.C_UOM_ID)", String.class),
        		new ColumnInfo(Msg.translate(Env.getCtx(), "C_Currency_ID"), "(SELECT iso_code FROM C_Currency WHERE C_Currency_ID = M_PRODUCT_PO.C_Currency_ID)", String.class),
        		new ColumnInfo(Msg.translate(Env.getCtx(), "PriceList"), "PriceList", BigDecimal.class),
        		new ColumnInfo(Msg.translate(Env.getCtx(), "PricePO"), "PricePO", BigDecimal.class),
        		new ColumnInfo(Msg.translate(Env.getCtx(), "VendorProductNo"), "VendorProductNo", String.class),
        		new ColumnInfo(Msg.translate(Env.getCtx(), "Order_Min"), "Order_Min", Double.class),
        		new ColumnInfo(Msg.translate(Env.getCtx(), "DeliveryTime_Promised"), "DeliveryTime_Promised", Double.class),
        		new ColumnInfo(Msg.translate(Env.getCtx(), "DeliveryTime_Actual"), "DeliveryTime_Actual", Double.class)
    		};
        s_sqlFrom = "M_PRODUCT_PO";
        s_sqlWhere = "M_Product_ID = ?";
        m_sqlVendor = vendorTbl.prepareTable(s_layoutVendor, s_sqlFrom, s_sqlWhere, false, "M_PRODUCT_PO");
        vendorTbl.setMultiSelection(false);
        vendorTbl.autoSize();
        vendorTbl.setAttribute("zk_component_ID", "Lookup_Data_Vendor");

        detailTabBox.setHeight("100%");
        Tabpanels tabPanels = new Tabpanels();
		detailTabBox.appendChild(tabPanels);
		Tabs tabs = new Tabs();
		detailTabBox.appendChild(tabs);

		Tab tab = new Tab(Util.cleanAmp(Msg.translate(Env.getCtx(), "Warehouse")));
		tab.addEventListener(Events.ON_SELECT, this);
		tabs.appendChild(tab);
		Tabpanel desktopTabPanel = new Tabpanel();
		desktopTabPanel.setHeight("100%");
		desktopTabPanel.appendChild(warehouseTbl);
		tabPanels.appendChild(desktopTabPanel);

		tab = new Tab(Msg.translate(Env.getCtx(), "Description"));
		tab.addEventListener(Events.ON_SELECT, this);
		tabs.appendChild(tab);
		desktopTabPanel = new Tabpanel();
		desktopTabPanel.setHeight("100%");
		fieldDescription.setWidth("99%");
		fieldDescription.setHeight("99%");
		desktopTabPanel.appendChild(fieldDescription);
		tabPanels.appendChild(desktopTabPanel);

		tab = new Tab(Msg.translate(Env.getCtx(), "ProductAttribute"));
		tab.addEventListener(Events.ON_SELECT, this);
		tabs.appendChild(tab);
		desktopTabPanel = new Tabpanel();
		desktopTabPanel.setHeight("100%");
		fieldPAttributes.setWidth("99%");
		fieldPAttributes.setHeight("99%");
		desktopTabPanel.appendChild(fieldPAttributes);
		tabPanels.appendChild(desktopTabPanel);
		 
		tab = new Tab(Msg.translate(Env.getCtx(), "Substitute_ID"));
		tab.addEventListener(Events.ON_SELECT, this);
		tabs.appendChild(tab);
		desktopTabPanel = new Tabpanel();
		desktopTabPanel.setHeight("100%");
		desktopTabPanel.appendChild(substituteTbl);
		tabPanels.appendChild(desktopTabPanel);

		tab = new Tab(Msg.translate(Env.getCtx(), "RelatedProduct_ID"));
		tab.addEventListener(Events.ON_SELECT, this);
		tabs.appendChild(tab);
		desktopTabPanel = new Tabpanel();
		desktopTabPanel.setHeight("100%");
		desktopTabPanel.appendChild(relatedTbl);
		tabPanels.appendChild(desktopTabPanel);

		tab = new Tab(Msg.getMsg(Env.getCtx(), "ATP"));
		tab.addEventListener(Events.ON_SELECT, this);
		tabs.appendChild(tab);
		desktopTabPanel = new Tabpanel();
		desktopTabPanel.setHeight("100%");
		desktopTabPanel.appendChild(m_tableAtp);		
		tabPanels.appendChild(desktopTabPanel);

		tab = new Tab(Util.cleanAmp(Msg.translate(Env.getCtx(), "Vendor")));
		tab.addEventListener(Events.ON_SELECT, this);
		tabs.appendChild(tab);
		desktopTabPanel = new Tabpanel();
		desktopTabPanel.setHeight("100%");
		desktopTabPanel.appendChild(vendorTbl);
		tabPanels.appendChild(desktopTabPanel);

		tabs.setAttribute("zk_component_ID", "Subordinate_Tabs");

		//  Add the tabs to the center south layout
		Borderlayout tabLayout = new Borderlayout();
		//  
		North north = new North();
		tabLayout.appendChild(north);
		north.appendChild(checkShowDetail);
		//
		Center center = new Center();
		tabLayout.appendChild(center);
		center.appendChild(detailTabBox);

		//  Set main panel elements.  The other elements are handled by the info.java class
		p_criteriaGrid.appendChild(rows);
		p_centerSouth.appendChild(tabLayout);
		p_centerSouth.setTitle(Msg.translate(Env.getCtx(), "WarehouseStock"));
		p_centerSouth.setTooltiptext(Msg.translate(Env.getCtx(), "WarehouseStock"));
		super.setSizes();

		warehouseTbl.addActionListener(new EventListener() {
			public void onEvent(Event event) throws Exception {
				if (warehouseTbl.getRowCount() > 0)
				{
					int selectedRow = warehouseTbl.getSelectedRow();
					if (selectedRow<0) selectedRow = 0;

					Object wh_data = warehouseTbl.getValueAt(selectedRow, warehouseTbl.getKeyColumnIndex());
		            
					if (wh_data != null && wh_data instanceof IDColumn)
		            {
		            	IDColumn dataColumn = (IDColumn) wh_data;
		            	m_ATP_M_Warehouse_ID = dataColumn.getRecord_ID();
		            }
					else
					{
						m_ATP_M_Warehouse_ID = m_M_Warehouse_ID;
					}
				} 
				else
				{
					m_ATP_M_Warehouse_ID = m_M_Warehouse_ID;
				}
			}
		});

	}
	
	/**
	 *	Dynamic Init
	 *
	 * @param record_id   M_Product_ID if known, otherwise, 0
	 * @param value value
	 * @param M_Warehouse_ID warehouse
	 * @param M_PriceList_ID price list
	 */
	private void initInfo (int record_id, String value, int M_Warehouse_ID, int M_PriceList_ID, boolean reset)
	{
		if (!(record_id == 0) && value != null && value.length() > 0)
		{
			log.severe("Received both a record_id and a value: " + record_id + " - " + value);
		}

		//  In case of reset, clear all parameters to ensure we are at a known starting point.
		if(reset)
		{
			clearParameters();
			p_resetColumns = true;
		}
		//  Set values
        if (!(record_id == 0))  // A record is defined
        {
        	fieldID = record_id;
        	fWarehouse_ID.setValue(new Integer(M_Warehouse_ID).intValue());
        	fPriceList_ID.setValue(findPLV(M_PriceList_ID));

        } 
        else
        {
        	fieldID = 0;
        	
        	String id;
			if (value != null && value.length() > 0) //  The VLookup failed to find uniqueness across the direct access SQL fields
			{
				//  Match the query performed by the VLookup.  See getDirectAccessSQL().
				if (value.startsWith("@") && value.endsWith("@"))
				{
					fieldName.setText(value.substring(1,value.length()-1));
				}
				else
				{
					fieldValue.setText(value);
					fieldName.setText(value);
					fieldUPC.setText(value);
					fieldSKU.setText(value);
				}
				//
				fWarehouse_ID.setValue(0);
	        	//
	        	fPriceList_ID.setValue(0);
	        	//
	        	checkAND.setSelected(false); //  Use OR
	        	
			}
			else
			{
				//  No field or value - the general case
				//  Try to find other criteria in the context
				//  M_Product_ID - only if visible
				id = Env.getContext(Env.getCtx(), p_WindowNo, p_TabNo, "M_Product_ID", true);
				if (id != null && id.length() != 0 && (new Integer(id).intValue() > 0))
				{
					fieldID = new Integer(id).intValue();
				}
				
				id = Env.getContext(Env.getCtx(), p_WindowNo, p_TabNo, "M_PriceList_Version_ID", true);
				if (id != null && id.length() != 0 && (new Integer(id).intValue() > 0))
				{
					fPriceList_ID.setValue(new Integer(id).intValue());
				}
				else
				{	
						//  OK - make a good guess
						fPriceList_ID.setValue(findPLV(M_PriceList_ID));
				}

				//  M_Warehouse_ID - general context
				if(M_Warehouse_ID == 0)
				{
					id = Env.getContext(Env.getCtx(), "#M_Warehouse_ID");
					if (id != null && id.length() != 0 && (new Integer(id).intValue() > 0))
					{
						fWarehouse_ID.setValue(new Integer(id).intValue());
					}
					else 
					{
						id = Env.getContext(Env.getCtx(), p_WindowNo, "M_Warehouse_ID");
						if (id != null && id.length() != 0 && (new Integer(id).intValue() > 0))
						{
							fWarehouse_ID.setValue(new Integer(id).intValue());
						}
					}
				}
				else
				{
		        	fWarehouse_ID.setValue(new Integer(M_Warehouse_ID).intValue());
				}
				
				id = Env.getContext(Env.getCtx(), p_WindowNo, p_TabNo, "C_BPartner_ID", false);
				boolean isSOTrx = "Y".equals(Env.getContext(Env.getCtx(), p_WindowNo, p_TabNo, "IsSOTrx", false));
				if (id != null && id.length() != 0 && (new Integer(id).intValue() > 0) && !isSOTrx)
				{
					fVendor_ID.setValue(new Integer(id).intValue());
				}			
			}
		}

		if (!isValidVObject(fWarehouse_ID))
		{
			//  Disable the stock button
			checkOnlyStock.setSelected(false);
			checkOnlyStock.setEnabled(false);
		}
		else
			checkOnlyStock.setEnabled(true);
	}	//	initInfo

	  /**
		 *  Clear all fields and set default values in check boxes
		 */
		private void clearParameters()
		{
			//  Clear fields and set defaults
			fieldValue.setText("");
			fieldName.setText("");
			fieldUPC.setText("");
			fieldSKU.setText("");
	    	fWarehouse_ID.setValue(null);
	    	fPriceList_ID.setValue(null);
	    	fProductCategory_ID.setValue(null);
	    	fVendor_ID.setValue(null);
	    	fAS_ID.setValue(null);
	    	fASI_ID.setValue(null);
	    	checkOnlyStock.setSelected(false);  	//  Show everything
			checkAND.setSelected(true); 		//  Use AND
		}

    
}	//	InfoProduct
