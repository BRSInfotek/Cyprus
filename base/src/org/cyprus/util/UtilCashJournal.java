package org.cyprus.util;

import java.util.logging.Logger;

import org.cyprusbrs.util.DB;

public final class UtilCashJournal {
	
	/**	Logger			*/
	private static Logger log = Logger.getLogger(UtilCashJournal.class.getName());
	
	private UtilCashJournal() {// nothing
		}
	/// @Mukesh -20201016 
	public static boolean isAlreadyCashJournal(int p_AD_Client_ID,int p_AD_Org_ID, int p_C_CashBook_ID)
	{
		log.info("Parameters : p_AD_Client_ID : "+p_AD_Client_ID+" : p_AD_Org_ID "+p_AD_Org_ID+" : p_C_CashBook_ID "+p_C_CashBook_ID);
		final String sql="SELECT FN_GET_OLD_C_CASH(?, ?, ?) FROM DUAL";
		String value = DB.getSQLValueString(null, sql, new Object[]{p_AD_Client_ID, p_AD_Org_ID, p_C_CashBook_ID});
		if("Y".contentEquals(value))
			return true;
		return false;
	}
	
	//Start implemented by Anil @20210629 as per excel file given by Mukesh

	public static boolean isSaveBackDatedRecords(int p_AD_Org_ID,int p_AD_CLIENT_ID, int p_C_CashBook_ID)
	{ 
		log.info("Parameters : p_AD_Org_ID : "+p_AD_Org_ID+" : p_AD_CLIENT_ID "+p_AD_CLIENT_ID+" : p_C_CashBook_ID "+p_C_CashBook_ID);
		final String sql="select FN_GET_TOTAL_COUNT_BACKDATED_SAVE_RECORDS_CASHJOURNAL(?, ?, ?) FROM DUAL";
		int value = DB.getSQLValue(null, sql, new Object[]{p_AD_Org_ID, p_AD_CLIENT_ID, p_C_CashBook_ID});
		if( value >0)
			return true;
		else
			return false;
	}

	//End implemented by Anil @20210629 as per excel file given by Mukesh

}
