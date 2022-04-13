package org.cyprus.util;

import java.sql.Timestamp;
import java.util.logging.Logger;

import org.cyprusbrs.util.DB;

public class UtilBankCashPayment {


	public static final String CASH="CASH";
	public static final String BANKSTATEMENT="BANKSTATEMENT";
	public static final String PAYMENT="PAYMENT";

	// implemented by Anil @20210623 as per excel file given by Mukesh
	// Do Not Complete Back Dated records
	/**	Logger			*/
	private static Logger log = Logger.getLogger(UtilBankCashPayment.class.getName());
	/**
	 * The system will not allow the user to complete the back dated bank statement record in the system. Back dated means that if any record exists in the future date
	 * @param p_StatementDate
	 * @param p_Type
	 * @param p_AD_Client_ID
	 * @return boolean
	 * @author Anshul/Anil
	 */
	public static boolean isCompleteBackDatedRecordBankStatement(Timestamp p_DATE,int p_AD_CLIENT_ID,int p_AD_ORG_ID, int p_C_BANKACCOUNT_ID)
	{ 
		log.info("Parameters : p_DATE : "+p_DATE+" : p_AD_CLIENT_ID "+p_AD_CLIENT_ID+" : p_AD_ORG_ID "+p_AD_ORG_ID+" : p_C_BANKACCOUNT_ID "+p_C_BANKACCOUNT_ID);
	final String sql="SELECT FN_GET_TOTAL_COUNT_BACKDATED_COMPLETE_RECORDS_BANKSTATEMENT(?, ?, ?,?) FROM DUAL";
	int value = DB.getSQLValue(null, sql, new Object[]{p_DATE, p_AD_CLIENT_ID,p_AD_ORG_ID,p_C_BANKACCOUNT_ID});
	if( value >0)
		return true;
	else
	return false;			
	}
	
	public static boolean isCompleteBackDatedRecordCashJournal(Timestamp p_DATE,int p_AD_CLIENT_ID,int p_AD_ORG_ID, int p_C_CASHBOOK_ID)
	{ 
		log.info("Parameters : p_DATE : "+p_DATE+" : p_AD_CLIENT_ID "+p_AD_CLIENT_ID+" : p_AD_ORG_ID "+p_AD_ORG_ID+" : p_C_CASHBOOK_ID "+p_C_CASHBOOK_ID);
	final String sql="SELECT FN_GET_TOTAL_COUNT_BACKDATED_COMPLETE_RECORDS_CASHJOURNAL(?, ?, ?,?) FROM DUAL";
	int value = DB.getSQLValue(null, sql, new Object[]{p_DATE, p_AD_CLIENT_ID,p_AD_ORG_ID,p_C_CASHBOOK_ID});
	if( value >0)
		return true;
	else
	return false;
	}
	
	/**
	 * The system will not allow the user to void the back dated bank statement record in the system. Back dated means that if any record exists in the future date
	 * @param p_StatementDate
	 * @param p_Type
	 * @param p_AD_Client_ID
	 * @return boolean
	 * @author Anshul/Anil
	 */
	
	public static boolean isVoidBackDatedRecordBankStatement(Timestamp p_DATE, int p_AD_CLIENT_ID,int p_AD_ORG_ID, int p_C_BANKACCOUNT_ID)
	{ 
		log.info("Parameters : p_DATE : "+p_DATE+" : p_AD_CLIENT_ID "+p_AD_CLIENT_ID+" : p_AD_ORG_ID "+p_AD_ORG_ID+" : p_C_BANKACCOUNT_ID "+p_C_BANKACCOUNT_ID);
		final String sql="SELECT FN_GET_TOTAL_COUNT_BACKDATED_VOID_RECORDS_BANKSTATEMENT(?, ?, ?,?) FROM DUAL";
		int value = DB.getSQLValue(null, sql, new Object[]{p_DATE, p_AD_CLIENT_ID,p_AD_ORG_ID,p_C_BANKACCOUNT_ID});
		if( value >0)
			return true;
		else
		return false;
       }
	
	public static boolean isVoidBackDatedRecordCashJournal(Timestamp p_DATE, int p_AD_CLIENT_ID,int p_AD_ORG_ID, int p_C_CASHBOOK_ID)
	{ 
		log.info("Parameters : p_DATE : "+p_DATE+" : p_AD_CLIENT_ID "+p_AD_CLIENT_ID+" : p_AD_ORG_ID "+p_AD_ORG_ID+" : p_C_CASHBOOK_ID "+p_C_CASHBOOK_ID);
		final String sql="SELECT FN_GET_TOTAL_COUNT_BACKDATED_VOID_RECORDS_CASHJOURNAL(?, ?, ?,?) FROM DUAL";
		int value = DB.getSQLValue(null, sql, new Object[]{p_DATE, p_AD_CLIENT_ID,p_AD_ORG_ID,p_C_CASHBOOK_ID});
		if( value >0)
			return true;
		else
		return false;
	}


}
