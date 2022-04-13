package org.cyprus.crm.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;

import org.cyprusbrs.util.DB;

public class MLeadInfo extends X_C_LeadInfo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1588817429564913622L;
	
	/** Parent					*/
	private MLead			m_parent = null;
	
	
	public MLeadInfo(Properties ctx, int C_LeadInfo_ID, String trxName) {
		super(ctx, C_LeadInfo_ID, trxName);
		// TODO Auto-generated constructor stub
	}
	
	public MLeadInfo(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}

	/**************************************************************************
	 * 	Before Save
	 *	@param newRecord
	 *	@return true if it can be saved
	 */
	protected boolean beforeSave (boolean newRecord)
	{
//		Calculations & Rounding
		setLineNetAmt();	//	set Line Net Amount
//		setDiscount();
		
//		Get Defaults from Parent
		if ( getC_Currency_ID() == 0 || getM_PriceList_ID()==0)
			setLeadInfo (getParent());
		
		return true;
	}

	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return saved
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		System.out.println("This is message from After Save....");
		if (!success)
			return success;
		log.info("This is message from After Save");
		return updateHeaderAmount();
	}	//	afterSave
	
	/**
	 * 	After Delete
	 *	@param success success
	 *	@return deleted
	 */
	protected boolean afterDelete (boolean success)
	{
		if (!success)
			return success;
		return updateHeaderAmount();
	}	//	afterDelete
	
	/**
	 * 	Set Defaults from Order.
	 * 	Does not set Parent !!
	 * 	@param order order
	 */
	public void setLeadInfo (MLead lead)
	{
		setClientOrg(lead);
		setC_Currency_ID(lead.getC_Currency_ID());
		setM_PriceList_ID(lead.getM_PriceList_ID());
	}	//	setOrder
	
	public void setLineNetAmt ()
	{
		BigDecimal bd = getPlannedPrice().multiply(getPlannedQty());
		
		/// Need to check precision also
//		if (bd.scale() > getPrecision())
//			bd = bd.setScale(getPrecision(), BigDecimal.ROUND_HALF_UP);
		super.setLineNetAmt (bd);
	}
	
	
	
	
	
	/**
	 * 	Get Parent
	 *	@return parent
	 */
	public MLead getParent()
	{
		if (m_parent == null)
			m_parent = new MLead(getCtx(), getC_Lead_ID(), get_TrxName());
		return m_parent;
	}	//	getParent
	
	/**
	 * 	Set Header Info
	 *	@param order order
	 */
	public void setHeaderInfo (MLead lead)
	{
		m_parent = lead;
//		m_precision = new Integer(lead.getPrecision());
//		m_M_PriceList_ID = lead.getM_PriceList_ID();
	}	//	setHeaderInfo
	
	private boolean updateHeaderAmount()
	{
		//	Update Lead Header
		String sql = "UPDATE C_Lead i"
			+ " SET TotalLines="
				+ "(SELECT COALESCE(SUM(LineNetAmt),0) FROM C_LeadInfo il WHERE i.C_Lead_ID=il.C_Lead_ID) "
			+ "WHERE C_Lead_ID=" + getC_Lead_ID();
		
		log.info("This is message from Header Method... "+sql);
		int no = DB.executeUpdate(sql, get_TrxName());
		log.info("This is message from Header Method... retrun "+no);
		if (no != 1)
			log.warning("(1) #" + no);
		m_parent = null;
		return no == 1;
	}	//	updateHeaderTax	
}
