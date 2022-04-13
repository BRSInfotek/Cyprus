package org.cyprus.util;

import java.util.logging.Logger;

import org.cyprusbrs.util.DB;

public final class UtilBankStatement {
	
	/**	Logger			*/
	private static Logger log = Logger.getLogger(UtilBankStatement.class.getName());
	
	private UtilBankStatement() {// nothing
		}
	/// @Mukesh -20201016 
	public static boolean isAlreadyBankStatement(int p_AD_Client_ID,int p_AD_Org_ID, int p_C_BankStatment_ID)
	{
		log.info("Parameters : p_AD_Client_ID : "+p_AD_Client_ID+" : p_AD_Org_ID "+p_AD_Org_ID+" : p_C_BankStatment_ID "+p_C_BankStatment_ID);
		final String sql="SELECT FN_GET_OLD_C_BANKSTATEMENT(?, ?, ?) FROM DUAL";
		String value = DB.getSQLValueString(null, sql, new Object[]{p_AD_Client_ID, p_AD_Org_ID, p_C_BankStatment_ID});
		if("Y".contentEquals(value))
			return true;
		return false;
	}
	
	public static boolean isSaveBackDatedRecord(int p_AD_ORG_ID,int p_AD_CLIENT_ID,int p_C_BANKACCOUNT_ID)
	{ 
		log.info("Parameters : p_AD_ORG_ID "+p_AD_ORG_ID+" : p_AD_CLIENT_ID "+p_AD_CLIENT_ID+" : p_C_BANKACCOUNT_ID "+p_C_BANKACCOUNT_ID);
		final String sql="SELECT FN_GET_TOTAL_COUNT_BACKDATED_SAVE_RECORDS(?,?,?) FROM DUAL";
		int value = DB.getSQLValue(null, sql, new Object[]{ p_AD_ORG_ID, p_AD_CLIENT_ID, p_C_BANKACCOUNT_ID});
		if( value >0)
			return true;
		else
			return false;
	}
}
