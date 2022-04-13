package org.cyprus.crm.model;

import java.sql.ResultSet;
import java.util.Properties;

public class MLeadHistory extends X_C_LeadHistory {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public MLeadHistory(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}

	public MLeadHistory(Properties ctx, int C_LeadHistory_ID, String trxName) {
		super(ctx, C_LeadHistory_ID, trxName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Create Lead History
	 * @param lead 
	 * @return MLeadHistory
	 */
	public static MLeadHistory createLeadHistory(MLead lead) {
		
		MLeadHistory history=new MLeadHistory (lead.getCtx(), 0, lead.get_TrxName());
		if (lead.get_ID() == 0)
			throw new IllegalArgumentException("Lead not saved");
		history.setAD_Client_ID(lead.getAD_Client_ID());
		history.setAD_Org_ID(lead.getAD_Org_ID());
		history.setC_Lead_ID(lead.getC_Lead_ID());
		history.setDocumentNo(null);
		history.setM_Warehouse_ID(0);
		history.setAD_User_ID(0);
		history.setC_Region_ID(0);
		history.setM_PriceList_ID(0);
		history.setC_BP_Group_ID(0);
		history.setC_Country_ID(0);
		history.setC_Currency_ID(0);
		history.setC_ConversionType_ID(0);
//		history.save(lead.get_TrxName());
		return history;
	}

	
	

}
