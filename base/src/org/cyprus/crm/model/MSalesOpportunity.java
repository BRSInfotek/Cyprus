package org.cyprus.crm.model;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;

public class MSalesOpportunity extends X_C_SalesOpportunity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2346330562725222455L;
	
	public MSalesOpportunity(Properties ctx, int C_SalesOpportunity_ID, String trxName) {
		super(ctx, C_SalesOpportunity_ID, trxName);
		// TODO Auto-generated constructor stub
	}
	
	public MSalesOpportunity(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param lead
	 */
	public MSalesOpportunity(MLead lead) {
		
		this (lead.getCtx(), 0, lead.get_TrxName());
		setClientOrg(lead);
		setDescription(lead.getDescription());
		setC_Lead_ID(lead.getC_Lead_ID());
		
		// date
		setOpportunityDate(new Timestamp(System.currentTimeMillis()));
		setConversionDate(new Timestamp(System.currentTimeMillis()));
		setEnquiryDate(lead.getEnquiryDate());
		setProposalDate(null); /// Should be null if we create from Lead window
		
		setM_Warehouse_ID(lead.getM_Warehouse_ID());
		setM_PriceList_ID(lead.getM_PriceList_ID());
		setC_Currency_ID(lead.getC_Currency_ID());
		setC_ConversionType_ID(lead.getC_ConversionType_ID());
		setProbability(90); // default set 
		setStatus(STATUS_ConvertedToOpportunity);
		
	}

}
