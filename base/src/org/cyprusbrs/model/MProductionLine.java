
package org.cyprusbrs.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;

import org.cyprus.util.UtilCosting;
import org.cyprus.util.UtilPayment;



public class MProductionLine extends X_M_ProductionLine 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2823233331871756749L;

	public MProductionLine(Properties ctx, int M_ProductionLine_ID, String trxName) {
		super(ctx, M_ProductionLine_ID, trxName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MProductionLine (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MProductionLine
	
	/**
	 * 
	 */
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true if can be saved
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		/// Code commented by Mukesh as per discussion by Surya @20220411
//		if (getM_Product_ID() != 0 && is_ValueChanged(COLUMNNAME_M_Product_ID))
//		{
//            BigDecimal CurrentCostPrice = UtilCosting.getCurrentCostPrice(getM_Product_ID(),getAD_Client_ID(),getAD_Org_ID(),getM_AttributeSetInstance_ID());
//            setCurrentCostPrice (CurrentCostPrice);
//		}
		return true;
	}	//	beforeSave

	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return true
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!success)
			return false;
		
		//	Create MA
		//if (newRecord && success 
		//	&& m_isManualEntry && getM_AttributeSetInstance_ID() == 0)
		//	createMA();
		return true;
	}	//	afterSave
	
	/**
	 * 	Create Material Allocations for new Instances
	 */
}	//	MInventoryLine

