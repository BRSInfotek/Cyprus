/******************************************************************************
 * Copyright (C) 2008 Elaine Tan                                              *
 * Copyright (C) 2008 Idalica Corporation
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
package org.cyprus.webui.dashboard;

import org.cyprus.webui.component.Button;
import org.cyprus.webui.component.Label;
import org.cyprusbrs.model.MUser;
import org.cyprusbrs.util.DB;
import org.cyprusbrs.util.Env;
import org.cyprusbrs.util.Msg;
import org.zkoss.image.Image;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Box;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Vbox;

/**
 * Dashboard item: User favourites
 * @author Elaine
 * @date Feb 28, 2021
 */
public class DPWelcome extends DashboardPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -481859785800845108L;
	
	private Box bxWelcom;
	
	private Button btnNotice;
	
	Label welcomInfo ;
	
	Button imageInfo ;

		
	public DPWelcome()
	{
		super();
        this.appendChild(createWelcomePanel());
	}
	
	private Box createWelcomePanel() {
		
		bxWelcom = new Vbox();
		
		welcomInfo= new Label();
		imageInfo= new Button();
		
//		bxWelcom.appendChild(imageInfo);
		bxWelcom.appendChild(welcomInfo);
		
		int AD_User_ID = Env.getContextAsInt(Env.getCtx(), "#AD_User_ID");
		MUser user=new MUser(Env.getCtx(), AD_User_ID, null);
		welcomInfo.setText("Welcome "+user.getName());
//		imageInfo.setImage("/images/GetMail16.png");
		imageInfo.setImage("https://homepages.cae.wisc.edu/~ece533/images/peppers.png");
		imageInfo.setEnabled(true);
		
		imageInfo.setWidth("80px");
		imageInfo.setHeight("160px");

//		imageInfo.setHoverImageContent(new Image("https://homepages.cae.wisc.edu/~ece533/images/peppers.png"));
		
		welcomInfo.setWidth("60px");
		welcomInfo.setHeight("30px");
		
		
		btnNotice = new Button();
//		bxWelcom.appendChild(btnNotice);
        btnNotice.setLabel(Msg.translate(Env.getCtx(), "AD_Note_ID") + " : 0");
        btnNotice.setTooltiptext(Msg.translate(Env.getCtx(), "AD_Note_ID"));
        btnNotice.setImage("/images/GetMail16.png");
        int AD_Menu_ID = DB.getSQLValue(null, "SELECT AD_Menu_ID FROM AD_Menu WHERE Name = 'Notice' AND IsSummary = 'N'");
        btnNotice.setName(String.valueOf(AD_Menu_ID));
//        btnNotice.addEventListener(Events.ON_CLICK, this);
		
		return bxWelcom;
	}

}
