package org.cyprus.crm.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;

import org.cyprusbrs.util.DB;

public class MOpportunityLine extends X_C_OpportunityLine {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6063043158967503917L;

	/** Parent					*/
	private MSalesOpportunity		m_parent = null;
	
	public MOpportunityLine(Properties ctx, int C_OpportunityLine_ID, String trxName) {
		super(ctx, C_OpportunityLine_ID, trxName);
		// TODO Auto-generated constructor stub
	}
	
	public MOpportunityLine(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param saleOpp
	 */
	public MOpportunityLine(MSalesOpportunity saleOpp) {
		this (saleOpp.getCtx(), 0, saleOpp.get_TrxName());
		setClientOrg(saleOpp);
		setC_SalesOpportunity_ID(saleOpp.getC_SalesOpportunity_ID());
		setM_PriceList_ID(saleOpp.getM_PriceList_ID());
		setC_Currency_ID(saleOpp.getC_Currency_ID());
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
		return updateHeaderTax();
	}	//	afterSave
	
	protected boolean beforeSave (boolean newRecord)
	{
		// Calculate Tax Amount by Anshul @23062021 as per excel sheet given by Mukesh
//		System.out.println(getLineNetAmt());
//		if(getLineNetAmt().compareTo(Env.ZERO) != 0 || is_ValueChanged(MBankStatementLine.COLUMNNAME_C_Tax_ID))
//		{
//			int p_C_Currency_ID=DB.getSQLValue(null, "Select C_Currency_ID from C_SalesOpportunity Where C_SalesOpportunity_ID=?", getC_SalesOpportunity_ID());
//			BigDecimal taxamt = UtilTax.getTaxAmountFromStdPrecision(getLineNetAmt(), getC_Tax_ID(), p_C_Currency_ID);
//			setTaxAmt (taxamt);
//		}
//		if(is_ValueChanged(MBankStatementLine.COLUMNNAME_C_Tax_ID))
//		{
//			BigDecimal lineNetAmount=getLineNetAmt().add(getTaxAmt());
//			setLineNetAmt(lineNetAmount);
//		}
		return true;
		// End of the code
	}
	
	
	/**
	 * Set Defaults from sales OpportUnity
	 * @param saleOpprt
	 */
	public void setOpportunityLine (MSalesOpportunity saleOpprt)
	{
		setClientOrg(saleOpprt);
		setC_Currency_ID(saleOpprt.getC_Currency_ID());
		setM_PriceList_ID(saleOpprt.getM_PriceList_ID());
	}	//	setOrder
	
	
	/**
	 * 	After Delete
	 *	@param success success
	 *	@return deleted
	 */
	protected boolean afterDelete (boolean success)
	{
		if (!success)
			return success;
		return updateHeaderTax();
	}	//	afterDelete
	
	
	public void setLineNetAmt ()
	{
		BigDecimal bd = getPlannedPrice().multiply(getPlannedQty());	
		/// Need to check precision also
//		if (bd.scale() > getPrecision())
//			bd = bd.setScale(getPrecision(), BigDecimal.ROUND_HALF_UP);
		super.setLineNetAmt (bd);
	}
	
	/**
	 *	Update Tax & Header
	 *	@return true if header updated
	 */
	
	/**
	 * 	Get Parent
	 *	@return parent
	 */
	public MSalesOpportunity getParent()
	{
		if (m_parent == null)
			m_parent = new MSalesOpportunity(getCtx(), getC_SalesOpportunity_ID(), get_TrxName());
		return m_parent;
		
	}	//	getParent
	private boolean updateHeaderTax()
	{		
		//Update Order Header Line Net Amount
		String sql = "UPDATE C_SalesOpportunity i"
			+ " SET TotalLines="
				+ "(SELECT COALESCE(SUM(LineNetAmt),0) FROM C_OpportunityLine il WHERE i.C_SalesOpportunity_ID=il.C_SalesOpportunity_ID) "
			+ "WHERE C_SalesOpportunity_ID=" + getC_SalesOpportunity_ID();
		int no = DB.executeUpdate(sql, get_TrxName());
		if (no != 1)
			log.warning("(1) #" + no);
		
		//Update Order Header Line Net Amount
		sql = "UPDATE C_SalesOpportunity i"
			+ " SET GrandTotal="
				+ "(SELECT COALESCE(SUM(LineTotalAmount),0) FROM C_OpportunityLine il WHERE i.C_SalesOpportunity_ID=il.C_SalesOpportunity_ID) "
			+ "WHERE C_SalesOpportunity_ID=" + getC_SalesOpportunity_ID();
		 no = DB.executeUpdate(sql, get_TrxName());
		if (no != 1)
			log.warning("(2) #" + no);
		
		return no == 1;
	}	//	updateHeaderTax
}
