package org.cyprus.util;

import java.math.BigDecimal;
import java.util.logging.Logger;

import org.cyprusbrs.model.MCurrency;
import org.cyprusbrs.util.DB;
import org.cyprusbrs.util.Env;

public final class UtilPayment {

	/**	Logger			*/
	private static Logger log = Logger.getLogger(UtilPayment.class.getName());
	
	private UtilPayment() { // nothing
	}
	
	public static BigDecimal getTotalAmtFromAllocate(Integer p_AD_Client_ID,Integer p_AD_Org_ID,Integer p_C_Payment_ID)
	{
		log.info("Parameters : p_AD_Client_ID : "+p_AD_Client_ID+" : p_AD_Org_ID "+p_AD_Org_ID+" : p_C_Payment_ID "+p_C_Payment_ID);
		final String sql="SELECT FN_GET_PAYMENT_FROM_ALLOCATE(?, ?, ?) FROM DUAL";
		BigDecimal value = DB.getSQLValueBD(null, sql, new Object[]{p_AD_Client_ID, p_AD_Org_ID, p_C_Payment_ID});
		if(value!=null && value.compareTo(Env.ZERO)>0)
			return value;
		return Env.ZERO;
	}
	
	/**
	 * This method checks the standard precision from currency window and return the amount according
	 *  
	 */
	public static BigDecimal getRoundingAmountFromStdPrecision(BigDecimal amount, Integer p_C_Currency_ID)
	{
		if(amount!=null && p_C_Currency_ID>0)
		{	
			int getStdPrecision=MCurrency.getStdPrecision(Env.getCtx(), p_C_Currency_ID);			
			if(amount.stripTrailingZeros().scale()>getStdPrecision)
			{
				final String sql="SELECT FN_GET_ROUNDED_CURRENCY(?, ?) FROM DUAL";
				BigDecimal value = DB.getSQLValueBD(null, sql, new Object[]{amount, p_C_Currency_ID});
				log.info("Parameters : amount : "+amount+" : p_C_Currency_ID "+p_C_Currency_ID+" : Return Data "+value);
				if(value!=null)
					return value; 
			}
		}
		return amount;
	}
	
	
}
