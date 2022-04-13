package org.cyprus.util;

import java.sql.Timestamp;
import java.util.logging.Logger;

import org.cyprusbrs.util.DB;

public class UtilTransaction {
	

	/**	Logger			*/
	private static Logger log = Logger.getLogger(UtilTransaction.class.getName());
	
	public final static String PHYSICALINVENTORY="PHYSICALINVENTORY"; 
	public final static String MATERIALRECEIPT="MATERIALRECEIPT";
	public final static String INTERNALMOVE="INTERNALMOVE";
	
	private UtilTransaction() {// nothing
	}
	
	/// New code updated by Anshul @20210730
	public static boolean isCompleteBackDatedRecordTransaction(Timestamp p_DATE, String p_TYPE,int p_AD_CLIENT_ID,int p_AD_ORG_ID, int p_M_PRODUCT_ID)
	{ 
		log.info("Parameters : p_DATE : "+p_DATE+" : p_AD_CLIENT_ID "+p_AD_CLIENT_ID+" : p_AD_ORG_ID "+p_AD_ORG_ID+" : p_M_PRODUCT_ID "+p_M_PRODUCT_ID);
		final String sql="SELECT FN_GET_TOTAL_COUNT_BACKDATED_COMPLETE_RECORDS_TRANSACTION(?,?, ?, ?,?) FROM DUAL";
		int value = DB.getSQLValue(null, sql, new Object[]{p_DATE,p_TYPE, p_AD_CLIENT_ID,p_AD_ORG_ID,p_M_PRODUCT_ID});
		if( value >0)
			return true;
		else
			return false;
	}
	/**
	 * @author Anshul
	 * @param p_DATE
	 * @param p_TYPE
	 * @param p_AD_CLIENT_ID
	 * @param p_AD_ORG_ID
	 * @param p_M_PRODUCT_ID
	 * @return
	 */
	public static boolean isVoidBackDatedRecordTransaction(Timestamp p_DATE, String p_TYPE, int p_AD_CLIENT_ID,int p_AD_ORG_ID, int p_M_PRODUCT_ID)
	{ 
		log.info("Parameters : p_DATE : "+p_DATE+" : p_AD_CLIENT_ID "+p_AD_CLIENT_ID+" : p_AD_ORG_ID "+p_AD_ORG_ID+" : p_C_CASHBOOK_ID "+p_M_PRODUCT_ID);
		final String sql="SELECT FN_GET_TOTAL_COUNT_BACKDATED_VOID_RECORDS_TRANSACTION(?,?, ?, ?,?) FROM DUAL";
		int value = DB.getSQLValue(null, sql, new Object[]{p_DATE, p_TYPE, p_AD_CLIENT_ID,p_AD_ORG_ID,p_M_PRODUCT_ID});
		if( value >0)
			return true;
		else
			return false;
	}
	
}
