/******************************************************************************
  Product: Cyprus ERP & CRM Smart Business Solution                       
 This program is free software; you can redistribute it and/or modify it    
  based on GNU General Public License as published   
  This program is distributed in the hope   
  that it will be useful, but WITHOUT ANY WARRANTY; without even the implied 
  warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           
  See the GNU General Public License for more details.                       
*****************************************************************************/
package org.cyprus.webui.window;

import java.util.Vector;

import org.cyprus.webui.component.Button;
import org.cyprus.webui.component.Checkbox;
import org.cyprus.webui.component.Label;
import org.cyprus.webui.component.ListHead;
import org.cyprus.webui.component.ListHeader;
import org.cyprus.webui.component.Listbox;
import org.cyprus.webui.component.SimpleListModel;
import org.cyprus.webui.component.Tab;
import org.cyprus.webui.component.Tabbox;
import org.cyprus.webui.component.Tabpanel;
import org.cyprus.webui.component.Tabpanels;
import org.cyprus.webui.component.Tabs;
import org.cyprus.webui.component.ToolBarButton;
import org.cyprus.webui.component.Window;
import org.cyprus.webui.theme.ThemeManager;
import org.cyprusbrs.Cyprus;
import org.cyprusbrs.model.MUser;
import org.cyprusbrs.util.CLogErrorBuffer;
import org.cyprusbrs.util.CLogMgt;
import org.cyprusbrs.util.Env;
import org.cyprusbrs.util.Msg;
import org.zkoss.util.media.AMedia;
import org.zkoss.zhtml.Pre;
import org.zkoss.zhtml.Text;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SizeEvent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Vbox;

/**
 *
 * @author Mukesh
 *
 */
public class AboutWindow extends Window implements EventListener {

	/**
	 *
	 */
	private static final long serialVersionUID = -257313771447940626L;
	private Checkbox bErrorsOnly;
	private Listbox logTable;
	private Tabbox tabbox;
	private Tabpanels tabPanels;
	private Button btnDownload;
	private Button btnErrorEmail;

	public AboutWindow() {
		super();
		init();
	}

	private void init() {
		this.setWidth("500px");
		this.setHeight("450px");
		this.setPosition("center");
		this.setTitle(ThemeManager.getBrowserTitle());
		this.setClosable(true);
		this.setSizable(true);

		this.addEventListener(Events.ON_SIZE, this);

		Vbox layout = new Vbox();
		layout.setWidth("100%");
		layout.setParent(this);

		tabbox = new Tabbox();
		tabbox.setParent(layout);
		tabbox.setWidth("480px");
		tabbox.setHeight("380px");
//		tabbox.setSclass("lite");
		Tabs tabs = new Tabs();
		tabs.setParent(tabbox);
		tabPanels = new Tabpanels();
		tabPanels.setParent(tabbox);
		tabPanels.setWidth("480px");

		//about
		Tab tab = new Tab();
		tab.setLabel("About");
		tab.setParent(tabs);
		Tabpanel tabPanel = createAbout();
		tabPanel.setParent(tabPanels);

		//Credit
		tab = new Tab();
		tab.setLabel("Credit");
		tab.setParent(tabs);
		tabPanel = createCredit();
		tabPanel.setParent(tabPanels);

		//Info
		tab = new Tab();
		tab.setLabel("Info");
		tab.setParent(tabs);
		tabPanel = createInfo();
		tabPanel.setParent(tabPanels);

		//Trace
		tab = new Tab();
		tab.setLabel("Logs");
		tab.setParent(tabs);
		tabPanel = createTrace();
		tabPanel.setParent(tabPanels);

		Hbox hbox = new Hbox();
		hbox.setParent(layout);
		hbox.setPack("end");
		hbox.setWidth("100%");
		Button btnOk = new Button();
		btnOk.setImage("/images/Ok24.png");
		btnOk.addEventListener(Events.ON_CLICK, this);
		btnOk.setParent(hbox);

		this.setBorder("normal");
	}

	private Tabpanel createTrace() {
		Tabpanel tabPanel = new Tabpanel();
		Vbox vbox = new Vbox();
		vbox.setParent(tabPanel);
		vbox.setWidth("100%");
		vbox.setHeight("100%");

		Hbox hbox = new Hbox();
		bErrorsOnly = new Checkbox();
		bErrorsOnly.setLabel(Msg.getMsg(Env.getCtx(), "ErrorsOnly"));
		//default only show error
		bErrorsOnly.setChecked(true);
		bErrorsOnly.addEventListener(Events.ON_CHECK, this);
		hbox.appendChild(bErrorsOnly);
		btnDownload = new Button(Msg.getMsg(Env.getCtx(), "SaveFile"));
		btnDownload.addEventListener(Events.ON_CLICK, this);
		hbox.appendChild(btnDownload);
		btnErrorEmail = new Button(Msg.getMsg(Env.getCtx(), "SendEMail"));
		btnErrorEmail.addEventListener(Events.ON_CLICK, this);
		hbox.appendChild(btnErrorEmail);
		vbox.appendChild(hbox);

		Vector<String> columnNames = CLogErrorBuffer.get(true).getColumnNames(Env.getCtx());

		logTable = new Listbox();
		ListHead listHead = new ListHead();
		listHead.setParent(logTable);
		listHead.setSizable(true);
		for (Object obj : columnNames) {
			ListHeader header = new ListHeader(obj.toString());
			header.setWidth("100px");
			listHead.appendChild(header);
		}

		vbox.appendChild(logTable);
		logTable.setWidth("480px");
		logTable.setHeight("310px");
		logTable.setVflex(false);

		updateLogTable();

		return tabPanel;
	}

	private void updateLogTable() {
		Vector<Vector<Object>> data = CLogErrorBuffer.get(true).getLogData(bErrorsOnly.isChecked());
		SimpleListModel model = new SimpleListModel(data);
		model.setMaxLength(new int[]{0, 0, 0, 200, 0, 200});
		logTable.setItemRenderer(model);
		logTable.setModel(model);
	}

	private Tabpanel createInfo() {
		Tabpanel tabPanel = new Tabpanel();
		Div div = new Div();
		div.setParent(tabPanel);
		div.setHeight("100%");
		div.setStyle("overflow: auto;");
		Pre pre = new Pre();
		pre.setParent(div);
		Text text = new Text(CLogMgt.getInfo(null).toString());
		text.setParent(pre);

		return tabPanel;
	}

	private Tabpanel createCredit() {
		Tabpanel tabPanel = new Tabpanel();
		Vbox vbox = new Vbox();
		vbox.setParent(tabPanel);
		vbox.setWidth("100%");
		Hbox hbox = new Hbox();
		hbox.setParent(vbox);
		ToolBarButton link = new ToolBarButton();
		link.setImage("images/Cyprus.jpg");
		link.setParent(hbox);
		link.setHref("https://www.cypruserp.com/");
		link.setTarget("_blank");

		Separator separator = new Separator();
		separator.setParent(vbox);

		Div div = new Div();
		div.setParent(vbox);
		div.setWidth("100%");
		Label caption = new Label("Sponsors");
		caption.setStyle("font-weight: bold;");
		div.appendChild(caption);
		separator = new Separator();
		separator.setBar(true);
		separator.setParent(div);
		Vbox content = new Vbox();
		content.setWidth("100%");
		content.setParent(div);
		link = new ToolBarButton();
		link.setLabel("Surya Bhushan");
//		link.setHref("https://www.cypruserp.com/");
//		link.setTarget("_blank");
		link.setParent(content);

		separator = new Separator();
		separator.setParent(vbox);

		div = new Div();
		div.setParent(vbox);
		div.setWidth("100%");
		caption = new Label("Contributors");
		caption.setStyle("font-weight: bold;");
		div.appendChild(caption);
		separator = new Separator();
		separator.setBar(true);
		separator.setParent(div);
		content = new Vbox();
		content.setWidth("100%");
		content.setParent(div);
		link = new ToolBarButton();
		link.setLabel("Sanjiv");
//		link.setHref("https://www.cypruserp.com/");
//		link.setTarget("_blank");
		link.setParent(content);

		link = new ToolBarButton();
		link.setLabel("Mukesh");
//		link.setHref("https://www.cypruserp.com/");
//		link.setTarget("_blank");
		link.setParent(content);
		
		return tabPanel;
	}

	private Tabpanel createAbout() {
		Tabpanel tabPanel = new Tabpanel();

		Vbox vbox = new Vbox();
		vbox.setWidth("100%");
		vbox.setHeight("100%");
		vbox.setAlign("center");
		vbox.setPack("center");
		vbox.setParent(tabPanel);

		Image image = new Image(ThemeManager.getSmallLogo());
		image.setParent(vbox);

		Text text = new Text(Cyprus.getSubtitle());
		text.setParent(vbox);
		Separator separator = new Separator();
		separator.setParent(vbox);

		text = new Text(Cyprus.getVersion());
		text.setParent(vbox);

		separator = new Separator();
		separator.setParent(vbox);
		ToolBarButton link = new ToolBarButton();
		/// Updated by Mukesh @20201013				
//		link.setLabel("Cyprus Project Site");
		link.setLabel("Cyprus ERP");
		link.setHref("http://cypruserp.com/");
		link.setTarget("_blank");
		link.setParent(vbox);

		return tabPanel;
	}

	public void onEvent(Event event) throws Exception {
		if (event.getTarget() == bErrorsOnly) {
			this.updateLogTable();
		}
		else if (event.getTarget() == btnDownload)
			downloadLog();
		else if (event.getTarget() == btnErrorEmail)
			cmd_errorEMail();
		else if (event instanceof SizeEvent)
			doResize((SizeEvent)event);
		else if (Events.ON_CLICK.equals(event.getName()))
			this.detach();
	}

	private void doResize(SizeEvent event) {
		int width = Integer.parseInt(event.getWidth().substring(0, event.getWidth().length() - 2));
		int height = Integer.parseInt(event.getHeight().substring(0, event.getHeight().length() - 2));

		tabbox.setWidth((width - 20) + "px");
		tabbox.setHeight((height - 70) + "px");

		tabPanels.setWidth((width - 20) + "px");

		logTable.setHeight((height - 140) + "px");
		logTable.setWidth((width - 30) + "px");
	}

	private void downloadLog() {
		String log = CLogErrorBuffer.get(true).getErrorInfo(Env.getCtx(), bErrorsOnly.isChecked());
		AMedia media = new AMedia("trace.log", null, "text/plain", log.getBytes());
		Filedownload.save(media);
	}

	/**
	 * 	EMail Errors
	 */
	private void cmd_errorEMail()
	{
		new WEMailDialog(this,
			"EMail Trace",
			MUser.get(Env.getCtx()),
			"",			//	to
			"Cyprus Trace Info",
			CLogErrorBuffer.get(true).getErrorInfo(Env.getCtx(), bErrorsOnly.isSelected()),
			null);

	}	//	cmd_errorEMail
}
