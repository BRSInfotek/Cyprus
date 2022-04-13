/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.cyprusbrs.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.cyprusbrs.util.CLogger;
import org.cyprusbrs.util.DB;
import org.cyprusbrs.util.DisplayType;
import org.cyprusbrs.util.Env;
import org.cyprusbrs.util.Msg;
import org.cyprusbrs.util.TimeUtil;
import org.cyprusbrs.util.Trx;

/**
 *	Currency Conversion Rate Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MConversionRate.java,v 1.2 2006/07/30 00:58:18 jjanke Exp $
 */
public class MConversionRate extends X_C_Conversion_Rate
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8171829790483133141L;
	
	/**	Logger						*/
	private static CLogger		s_log = CLogger.getCLogger (MConversionRate.class);

	/**
	 *	Convert an amount to base Currency
	 *	@param ctx context
	 *  @param CurFrom_ID  The C_Currency_ID FROM
	 *  @param ConvDate conversion date - if null - use current date
	 *  @param C_ConversionType_ID conversion rate type - if 0 - use Default
	 *  @param Amt amount to be converted
	 * 	@param AD_Client_ID client
	 * 	@param AD_Org_ID organization
	 *  @return converted amount
	 */
	public static BigDecimal convertBase (Properties ctx,
		BigDecimal Amt, int CurFrom_ID, 
		Timestamp ConvDate, int C_ConversionType_ID, 
		int AD_Client_ID, int AD_Org_ID)
	{
		return convert (ctx, Amt, CurFrom_ID, MClient.get(ctx).getC_Currency_ID(), 
			ConvDate, C_ConversionType_ID, AD_Client_ID, AD_Org_ID);
	}	//	convertBase

	
	/**
	 *  Convert an amount with today's default rate
	 *	@param ctx context
	 *  @param CurFrom_ID  The C_Currency_ID FROM
	 *  @param CurTo_ID    The C_Currency_ID TO
	 *  @param Amt amount to be converted
	 * 	@param AD_Client_ID client
	 * 	@param AD_Org_ID organization
	 *  @return converted amount
	 */
	public static BigDecimal convert (Properties ctx,
		BigDecimal Amt, int CurFrom_ID, int CurTo_ID, 
		int AD_Client_ID, int AD_Org_ID)
	{
		return convert (ctx, Amt, CurFrom_ID, CurTo_ID, null, 0, AD_Client_ID, AD_Org_ID);
	}   //  convert

	/**
	 *	Convert an amount
	 *	@param ctx context
	 *  @param CurFrom_ID  The C_Currency_ID FROM
	 *  @param CurTo_ID    The C_Currency_ID TO
	 *  @param ConvDate conversion date - if null - use current date
	 *  @param C_ConversionType_ID conversion rate type - if 0 - use Default
	 *  @param Amt amount to be converted
	 * 	@param AD_Client_ID client
	 * 	@param AD_Org_ID organization
	 *  @return converted amount or null if no rate
	 */
	public static BigDecimal convert (Properties ctx,
		BigDecimal Amt, int CurFrom_ID, int CurTo_ID,
		Timestamp ConvDate, int C_ConversionType_ID, 
		int AD_Client_ID, int AD_Org_ID)
	{
		if (Amt == null)
			throw new IllegalArgumentException("Required parameter missing - Amt");
		if (CurFrom_ID == CurTo_ID || Amt.compareTo(Env.ZERO)==0)
			return Amt;
		//	Get Rate
		BigDecimal retValue = getRate (CurFrom_ID, CurTo_ID, 
			ConvDate, C_ConversionType_ID,
			AD_Client_ID, AD_Org_ID);
		if (retValue == null)
			return null;
			
		//	Get Amount in Currency Precision
		retValue = retValue.multiply(Amt);
		int stdPrecision = MCurrency.getStdPrecision(ctx, CurTo_ID);
		if (retValue.scale() > stdPrecision)
			retValue = retValue.setScale(stdPrecision, BigDecimal.ROUND_HALF_UP);
			
		return retValue;
	}	//	convert

	/**
	 * Sets system spot conversion rate for a single day.
	 * Checks for overlaps of spot rate is made. If an overlap is found, the overlapping
	 * rate is removed.
	 * 
	 *  @param CurFrom_ISO		Currency from ISO code
	 *  @param CurTo_ISO		Currency to ISO code
	 *  @param spotDate			If null, today's date is used.
	 *  @param multiplyRate		CurFrom_ISO * MultiplyRate = amount in CurTo_ISO
	 */
	public static void setRate(String CurFrom_ISO, String CurTo_ISO, java.util.Date spotDate, 
							   BigDecimal MultiplyRate) throws Exception {
		
		String trxName = Trx.createTrxName();
		Trx trx = Trx.get(trxName, true);
		Properties ctx = Env.getCtx();
		MCurrency curFrom = MCurrency.get(ctx, CurFrom_ISO);
		if (curFrom==null) throw new Exception("Invalid currency " + CurFrom_ISO);
		MCurrency curTo = MCurrency.get(ctx, CurTo_ISO);
		if (curTo==null) throw new Exception("Invalid currency " + CurTo_ISO);
		
		java.sql.Timestamp startTs;
		if (spotDate==null) {
			spotDate = Calendar.getInstance().getTime();
		}
		Calendar spotCal = Calendar.getInstance();
		spotCal.setTime(spotDate);
		spotCal.set(Calendar.HOUR_OF_DAY, 0);
		spotCal.set(Calendar.MINUTE, 0);
		spotCal.set(Calendar.SECOND, 0);
		spotCal.set(Calendar.MILLISECOND, 0);
		startTs = new java.sql.Timestamp(spotCal.getTimeInMillis());
		final String whereClause = "C_Currency_ID=? and C_Currency_ID_To=? and ValidFrom>=? and ValidTo<=? and C_ConversionType_ID=?";
		MConversionRate rate, updateRate = null;
		List<MConversionRate> rates = new Query(ctx, I_C_Conversion_Rate.Table_Name, whereClause,trxName)
				.setParameters(curFrom.get_ID(), curTo.get_ID(), startTs, startTs, MConversionType.TYPE_SPOT)
				.list();

		if (rates.size()>0) {
			for (Iterator<MConversionRate> it = rates.iterator(); it.hasNext();) {
				rate = it.next();
				if (!rate.getValidFrom().equals(rate.getValidTo())) {
					// Remove this since it's for more than one day
					rate.deleteEx(true, trxName);
				} else {
					updateRate = rate;
				}
			}
		}
		if (updateRate==null) {
			updateRate = new MConversionRate(ctx, 0, trxName);
			updateRate.setAD_Client_ID(0);
			updateRate.setAD_Org_ID(0);
			updateRate.setC_Currency_ID(curFrom.get_ID());
			updateRate.setC_Currency_ID_To(curTo.get_ID());
			updateRate.setValidFrom(startTs);
			updateRate.setValidTo(startTs);
			updateRate.setC_ConversionType_ID(MConversionType.TYPE_SPOT);
		}
		updateRate.setMultiplyRate(MultiplyRate);
		updateRate.saveEx(trxName);
		trx.commit(true);
		trx.close();
	}
	
	/**
	 *	Get Currency Conversion Rate
	 *  @param  CurFrom_ID  The C_Currency_ID FROM
	 *  @param  CurTo_ID    The C_Currency_ID TO
	 *  @param  ConvDate    The Conversion date - if null - use current date
	 *  @param  ConversionType_ID Conversion rate type - if 0 - use Default
	 * 	@param	AD_Client_ID client
	 * 	@param	AD_Org_ID	organization
	 *  @return currency Rate or null
	 */
	public static BigDecimal getRate (int CurFrom_ID, int CurTo_ID,
		Timestamp ConvDate, int ConversionType_ID, int AD_Client_ID, int AD_Org_ID)
	{
		if (CurFrom_ID == CurTo_ID)
			return Env.ONE;
		//	Conversion Type
		int C_ConversionType_ID = ConversionType_ID;
		if (C_ConversionType_ID == 0)
			C_ConversionType_ID = MConversionType.getDefault(AD_Client_ID);
		//	Conversion Date
		if (ConvDate == null)
			ConvDate = new Timestamp (System.currentTimeMillis());

		//	Get Rate
		String sql = "SELECT MultiplyRate "
			+ "FROM C_Conversion_Rate "
			+ "WHERE C_Currency_ID=?"					//	#1
			+ " AND C_Currency_ID_To=?"					//	#2
			+ " AND	C_ConversionType_ID=?"				//	#3
			+ " AND	? BETWEEN ValidFrom AND ValidTo"	//	#4	TRUNC (?) ORA-00932: inconsistent datatypes: expected NUMBER got TIMESTAMP
			+ " AND AD_Client_ID IN (0,?)"				//	#5
			+ " AND AD_Org_ID IN (0,?) "				//	#6
			+ "ORDER BY AD_Client_ID DESC, AD_Org_ID DESC, ValidFrom DESC";
		BigDecimal retValue = null;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, CurFrom_ID);
			pstmt.setInt(2, CurTo_ID);
			pstmt.setInt(3, C_ConversionType_ID);
			pstmt.setTimestamp(4, ConvDate);
			pstmt.setInt(5, AD_Client_ID);
			pstmt.setInt(6, AD_Org_ID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				retValue = rs.getBigDecimal(1);
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, "getRate", e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}			
		if (retValue == null)
			s_log.info ("getRate - not found - CurFrom=" + CurFrom_ID 
			  + ", CurTo=" + CurTo_ID
			  + ", " + ConvDate 
			  + ", Type=" + ConversionType_ID + (ConversionType_ID==C_ConversionType_ID ? "" : "->" + C_ConversionType_ID) 
			  + ", Client=" + AD_Client_ID 
			  + ", Org=" + AD_Org_ID);
		return retValue;
	}	//	getRate

	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_Conversion_Rate_ID id
	 *	@param trxName transaction
	 */
	public MConversionRate (Properties ctx, int C_Conversion_Rate_ID, String trxName)
	{
		super(ctx, C_Conversion_Rate_ID, trxName);
		if (C_Conversion_Rate_ID == 0)
		{
		//	setC_Conversion_Rate_ID (0);
		//	setC_Currency_ID (0);
		//	setC_Currency_ID_To (null);
			super.setDivideRate (Env.ZERO);
			super.setMultiplyRate (Env.ZERO);
			setValidFrom (new Timestamp(System.currentTimeMillis()));
		}
	}	//	MConversionRate

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MConversionRate (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MConversionRate

	/**
	 * 	New Constructor
	 *	@param po parent
	 *	@param C_ConversionType_ID conversion type
	 *	@param C_Currency_ID currency
	 *	@param C_Currency_ID_To currency to
	 *	@param MultiplyRate multiply rate
	 *	@param ValidFrom valid from
	 */
	public MConversionRate (PO po, 
		int C_ConversionType_ID, 
		int C_Currency_ID, int C_Currency_ID_To, 
		BigDecimal MultiplyRate, Timestamp ValidFrom)
	{
		this (po.getCtx(), 0, po.get_TrxName());
		setClientOrg(po);
		setC_ConversionType_ID (C_ConversionType_ID);
		setC_Currency_ID (C_Currency_ID);
		setC_Currency_ID_To (C_Currency_ID_To);
		//
		setMultiplyRate (MultiplyRate);
		setValidFrom(ValidFrom);
	}	//	MConversionRate

	/**
	 * 	Set Multiply Rate
	 * 	Sets also Divide Rate
	 *	@param MultiplyRate multiply rate
	 */
	public void setMultiplyRate (BigDecimal MultiplyRate)
	{
		if (MultiplyRate == null 
			|| MultiplyRate.compareTo(Env.ZERO) == 0 
			|| MultiplyRate.compareTo(Env.ONE) == 0)
		{
			super.setDivideRate(Env.ONE);
			super.setMultiplyRate(Env.ONE);
		}
		else
		{
			super.setMultiplyRate(MultiplyRate);
			double dd = 1 / MultiplyRate.doubleValue();
			super.setDivideRate(new BigDecimal(dd));
		}
	}	//	setMultiplyRate

	/**
	 *	Set Divide Rate.
	 *	Sets also Multiply Rate
	 *	@param	DivideRate divide rate
	 */
	public void setDivideRate (BigDecimal DivideRate)
	{
		if (DivideRate == null 
			|| DivideRate.compareTo(Env.ZERO) == 0 
			|| DivideRate.compareTo(Env.ONE) == 0)
		{
			super.setDivideRate(Env.ONE);
			super.setMultiplyRate(Env.ONE);
		}
		else
		{
			super.setDivideRate(DivideRate);
			double dd = 1 / DivideRate.doubleValue();
			super.setMultiplyRate(new BigDecimal(dd));
		}
	}	//	setDivideRate

	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("MConversionRate[");
		sb.append(get_ID())
			.append(",Currency=").append(getC_Currency_ID())
			.append(",To=").append(getC_Currency_ID_To())
			.append(", Multiply=").append(getMultiplyRate())
			.append(",Divide=").append(getDivideRate())
			.append(", ValidFrom=").append(getValidFrom());
		sb.append("]");
		return sb.toString();
	}	//	toString

	
	/**
	 * 	Before Save.
	 * 	- Same Currency
	 * 	- Date Range Check
	 * 	- Set To date to 2056
	 *	@param newRecord new
	 *	@return true if OK to save
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		//	From - To is the same
		if (getC_Currency_ID() == getC_Currency_ID_To())
		{
			log.saveError("Error", Msg.parseTranslation(getCtx(), "@C_Currency_ID@ = @C_Currency_ID@"));
			return false;
		}
		//	Nothing to convert
		if (getMultiplyRate().compareTo(Env.ZERO) <= 0)
		{
			log.saveError("Error", Msg.parseTranslation(getCtx(), "@MultiplyRate@ <= 0"));
			return false;
		}
		
		/// Checks before update or create the Conversion rate @20210311 by Mukesh
		String retMsg=appliedChecks();
		if(retMsg!=null && retMsg.length()>0)
		{
			log.saveError("Error",retMsg);
			return false;
		}

		//	Date Range Check
		Timestamp from = getValidFrom();
		if (getValidTo() == null)
			setValidTo (TimeUtil.getDay(2056, 1, 29));	//	 no exchange rates after my 100th birthday
		Timestamp to = getValidTo();
		
		if (to.before(from))
		{
			SimpleDateFormat df = DisplayType.getDateFormat(DisplayType.Date);
			log.saveError("Error", df.format(to) + " < " + df.format(from));
			return false;
		}
		
		return true;
	}	//	beforeSave
	
	/**
	 * checks before create the Conversion by Mukesh @20210311
	 * @return
	 */
	private String appliedChecks() 
	{
		
		/// Updated by Anshul @20210630
		List<Object> paramCase1=new ArrayList<Object>();
		paramCase1.add(getAD_Client_ID());
		paramCase1.add(getAD_Org_ID());
		paramCase1.add(getC_Currency_ID());
		paramCase1.add(getC_Currency_ID_To());
		paramCase1.add(getValidFrom());
		paramCase1.add(getValidTo());
		paramCase1.add(getC_ConversionType_ID());
		//paramCase1.add(isActive());

		String sqlCase1="SELECT FN_GET_TOTAL_COUNT_CONVERSION_RATE(?,?,?,?,?,?,?) FROM DUAL";
		Integer retCase1=DB.getSQLValue(get_TrxName(), sqlCase1, paramCase1);
		if(retCase1>0)
			return Msg.getMsg(getCtx(), "Cyprus_ConversionRate_AlreadyExist");//"cannot save as Already exist transaction ";
		
		/**
		 * 
		 * 2. Users not able to save Currency Conversion rate before current date..They must only save the transaction from today date to future date. 
		 */
//		Timestamp currentDate = new Timestamp(System.currentTimeMillis());
//		log.info(currentDate+"  "+getValidFrom());
//		Date curdate=new Date(currentDate.getTime());
//		Date validate=new Date(getValidFrom().getTime());
//		int checkDate= DateTimeComparator.getDateOnlyInstance().compare(validate,curdate);
//		if(checkDate<0)//validate.before(curdate))
//			return Msg.getMsg(getCtx(), "Cyprus_ConversionRate_CannotSaveBackDate");//"cannot save as Already exist transaction ";
//		
//		// Case 1 :
//		
//		List<Object> paramCase1=new ArrayList<Object>();
//		paramCase1.add(getAD_Client_ID());
//		paramCase1.add(getAD_Org_ID());
//		paramCase1.add(getC_Currency_ID());
//		paramCase1.add(getC_Currency_ID_To());
//		paramCase1.add(getValidFrom());
//		paramCase1.add(getValidTo());
//		paramCase1.add(getC_ConversionType_ID());
//		paramCase1.add(isActive());
//
//		String sqlCase1="SELECT COUNT(*) FROM C_CONVERSION_RATE WHERE AD_CLIENT_ID=? AND AD_ORG_ID=? AND C_CURRENCY_ID=? AND C_CURRENCY_ID_TO=? AND VALIDFROM=? AND VALIDTO=? AND C_CONVERSIONTYPE_ID=? AND ISACTIVE=?";
//		Integer retCase1=DB.getSQLValue(get_TrxName(), sqlCase1, paramCase1);
//		if(retCase1>0)
//			return Msg.getMsg(getCtx(), "Cyprus_ConversionRate_AlreadyExist");//"cannot save as Already exist transaction ";
		
		//Case 2 :
		/**
		 * Temporary comments by Mukesh
		 
		List<Object> paramCase2=new ArrayList<Object>();
		paramCase2.add(getAD_Client_ID());
		paramCase2.add(getAD_Org_ID());
		paramCase2.add(getC_Currency_ID());
		paramCase2.add(getC_Currency_ID_To());
		paramCase2.add(getValidFrom());
		paramCase2.add(getValidTo());
		paramCase2.add(getC_ConversionType_ID());
		paramCase2.add(isActive());

		String sqlCase2="SELECT COUNT(*) FROM C_CONVERSION_RATE WHERE AD_CLIENT_ID=? AND AD_ORG_ID=? AND C_CURRENCY_ID=? AND C_CURRENCY_ID_TO=? AND VALIDFROM BETWEEN ? AND ? AND C_CONVERSIONTYPE_ID=? AND ISACTIVE=?";
		Integer retCase2=DB.getSQLValue(get_TrxName(), sqlCase2, paramCase2);
		if(retCase2>0)
			return Msg.getMsg(getCtx(), "Cyprus_ConversionRate_AlreadyExistValidTransaction");
		
		String sqlCase3="SELECT COUNT(*) FROM C_CONVERSION_RATE WHERE AD_CLIENT_ID=? AND AD_ORG_ID=? AND C_CURRENCY_ID=? AND C_CURRENCY_ID_TO=? AND VALIDTO BETWEEN ? AND ? AND C_CONVERSIONTYPE_ID=? AND ISACTIVE=?";
		Integer retCase3=DB.getSQLValue(get_TrxName(), sqlCase3, paramCase2);
		if(retCase3>0)
			return Msg.getMsg(getCtx(), "Cyprus_ConversionRate_AlreadyExistValidTransaction");
		*/
		return null;
	}
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return saved
	 *  @author Mukesh : Need to create or update the Transaction @20210110
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!success)
			return success;
		
		log.info(" Currency "+getC_Currency_ID()+" Currency To "+getC_Currency_ID_To());
		
		// check Currency Already Created
//		Integer C_Conversion_Rate_ID=UtilCurrency.checkReverseCurrencyRate(getC_Currency_ID(),getC_Currency_ID_To());
		
		List<Object> params=new ArrayList<Object>();
		params.add(getC_Currency_ID_To());
		params.add(getC_Currency_ID());
		params.add(getAD_Client_ID());
		params.add(getAD_Org_ID());
		
		String sqlQuery="select C_Conversion_Rate_Id from C_Conversion_Rate WHERE c_currency_id=?"
						+ " and c_currency_id_to=? and Ad_Client_ID=? and Ad_Org_ID=?";
		Integer C_Conversion_Rate_ID=DB.getSQLValue(get_TrxName(), sqlQuery, params);
		
		log.info("Reverse Currency ID "+C_Conversion_Rate_ID);
		if(C_Conversion_Rate_ID>0) /// Reverse Currency is exist to update it
		{
			String sqlUp="Update C_Conversion_Rate set MULTIPLYRATE= ?, DIVIDERATE= ? where C_Conversion_Rate_Id= ?";
			Integer retUp=DB.executeUpdateEx(sqlUp, new Object[]{getDivideRate(), getMultiplyRate(), C_Conversion_Rate_ID },get_TrxName());
			log.info("Reverse Currency update MULTIPLYRATE "+getMultiplyRate()+" and  DIVIDERATE"+getDivideRate()+" Ret  "+retUp);
		}
		else{  // Create new reverse Conversion rate
			
		MConversionRate reverseConversion=new MConversionRate(getCtx(), 0, get_TrxName()); 
		copyValues(this, reverseConversion);
		reverseConversion.setClientOrg(getAD_Client_ID(), getAD_Org_ID());
		reverseConversion.setC_Currency_ID(getC_Currency_ID_To());
		reverseConversion.setC_Currency_ID_To(getC_Currency_ID());
		reverseConversion.setDivideRate(getMultiplyRate());
		reverseConversion.setMultiplyRate(getDivideRate());
		log.info("Not update Reverse Currency ID "+reverseConversion);
		if(!reverseConversion.save(get_TrxName()))
		{
			/// need to apply adempiere exception
			log.info("Not update Reverse Currency ID ");
		}
			
		}
			
		return true;
	}	//	afterSave
	
	/**
	 * Created by Mukesh @20210311
	 */
	protected boolean beforeDelete ()
	{
		log.saveError("Error", "Can not delete transaction");
		return false;
	}
	
}	//	MConversionRate
