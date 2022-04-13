package org.cyprus.util;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.cyprusbrs.acct.DocTax;
import org.cyprusbrs.util.DB;
import org.cyprusbrs.util.Env;

public class UtilTax {

	
	/**	Logger			*/
	private static Logger log = Logger.getLogger(UtilTax.class.getName());
	
	public static final String BANKSTATEMENT = "BANKSTATEMENT";
	public static final String CASHJOURNAL = "CASHJOURNAL";
	public static final String INVOICE = "INVOICE";
	public static final String PAYMENT = "PAYMENT";
	
	
	private UtilTax() {// nothing
	}
	
/**
 * "The system will calculate the tax amount automatically. Assume that Bank Charge is selected with a value of 100 and select tax having the 5% as tax then the following formula to be executed:
	Formula1 = 100/1.05 = 95.2381
	Formul2 = 100 - 9502381 = 4.761905 (Tax Amount)"
 * @param amount
 * @param p_C_Tax_ID
 * @param p_C_Currency_ID
 * @return BigDecimal
 * @author Anshul : Method to calculate TaxAmount by Anshul @23062021 as per excel file given by Mukesh 
 */
	public static BigDecimal getTaxAmountFromStdPrecision(BigDecimal p_Amount, Integer p_C_Tax_ID, Integer p_C_Currency_ID)
	{	
		final String sql="SELECT FN_GET_TAX_AMOUNT(?, ?, ?) FROM DUAL";
		BigDecimal value = DB.getSQLValueBD(null, sql, new Object[]{p_Amount,p_C_Tax_ID, p_C_Currency_ID});
		log.info("Parameters : P_AMOUNT : "+p_Amount+" : p_C_Tax_ID "+p_C_Tax_ID+" : p_C_Currency_ID "+p_C_Currency_ID+" : Return Data "+value);
		if(value!=null && value.compareTo(Env.ZERO)!=0)
		return value;
		else
		return Env.ZERO;
		
//		MTax tax=MTax.get(Env.getCtx(), p_C_Tax_ID); 
//		BigDecimal rate = tax.getRate().divide(Env.ONEHUNDRED,2,RoundingMode.HALF_UP);
//		BigDecimal amt = Env.ONE.add(rate);			
//		amt = amount.divide(amt,MCurrency.getStdPrecision(Env.getCtx(), p_C_Currency_ID),RoundingMode.HALF_UP);
//		BigDecimal actualamt = amount.subtract(amt);
//		log.info("Parameters : ChargeAmt : "+amount+" : p_C_Tax_ID "+p_C_Tax_ID+" : p_C_Currency_ID "+p_C_Currency_ID+" : Return Data "+amt);
//		if(actualamt!=null && actualamt.compareTo(Env.ZERO)!=0)
//			return actualamt;
//		else
//			return Env.ZERO;
	}
	
	public static DocTax[] loadTaxes(Integer p_GetID, String p_Class)
	{
		ArrayList<DocTax> list = new ArrayList<DocTax>();	
		String sql = null;
		if(BANKSTATEMENT.equalsIgnoreCase(p_Class))
		{
		 sql = "select CB.C_Tax_ID as C_Tax_ID ,t.Name as Name,t.Rate as Rate,CB.ChargeAmt as ChargeAmt,CB.TaxAmt as TaxAmt,t.IsSummary as IsSummary "
			 + " from C_Tax t,C_BankStatementLine CB" 
			 + " Where t.C_Tax_ID=CB.C_Tax_ID AND CB.C_BankStatement_ID=?";
		}
		if(CASHJOURNAL.equalsIgnoreCase(p_Class))
		{
		 sql = "select CB.C_Tax_ID as C_Tax_ID ,t.Name as Name,t.Rate as Rate,CB.Amount as ChargeAmt,CB.TaxAmt as TaxAmt,t.IsSummary as IsSummary "
			 + " from C_Tax t,C_CashLine CB" 
			 + " Where t.C_Tax_ID=CB.C_Tax_ID AND CB.C_Cash_ID=?";
		}
		if(PAYMENT.equalsIgnoreCase(p_Class))	
		{
			boolean  IsMultiCharge = "N".equals("N");
			sql = "select IsMultiCharge from C_Payment where C_Payment_ID=?";
			PreparedStatement pstmt1 = null;
			ResultSet rs1 = null;
			try
			{
				pstmt1 = DB.prepareStatement(sql, null);
				pstmt1.setInt(1, p_GetID);
				rs1 = pstmt1.executeQuery();
				//
				while (rs1.next())
				{
					
					 IsMultiCharge = "Y".equals(rs1.getString("IsMultiCharge"));
								
				}
			}
			catch (SQLException e)
			{
				log.log(Level.SEVERE, sql, e);
				return null;
			}
			finally {
				DB.close(rs1, pstmt1);
				rs1 = null; pstmt1 = null;
			}
			
			if(!IsMultiCharge)
//			{
//				sql = "select paymentallocate.C_Tax_ID as C_Tax_ID ,t.Name as Name,t.Rate as Rate,paymentallocate.Amount as ChargeAmt,paymentallocate.TaxAmt as TaxAmt,t.IsSummary as IsSummary "
//						 + " from C_Tax t,C_PaymentAllocate paymentallocate" 
//						 + " Where t.C_Tax_ID=paymentallocate.C_Tax_ID AND paymentallocate.C_Payment_ID=?";
//			}
			
				sql = "select payment.C_Tax_ID as C_Tax_ID ,t.Name as Name,t.Rate as Rate,payment.PayAmt as ChargeAmt,payment.TaxAmt as TaxAmt,t.IsSummary as IsSummary "
						 + " from C_Tax t,C_Payment payment" 
						 + " Where t.C_Tax_ID=payment.C_Tax_ID AND payment.C_Payment_ID=?";
			
		}
				
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, p_GetID);
			rs = pstmt.executeQuery();
			//
			while (rs.next())
			{
				int C_Tax_ID = rs.getInt("C_Tax_ID");
				String name = rs.getString("Name");
				BigDecimal rate = rs.getBigDecimal("Rate");
				BigDecimal ChargeAmt = rs.getBigDecimal("ChargeAmt");
				BigDecimal taxAmt = rs.getBigDecimal("TaxAmt");
				boolean IsSummary = "Y".equals(rs.getString("IsSummary"));
					
				DocTax taxLine = new DocTax(C_Tax_ID, name, rate, 
		  			ChargeAmt, taxAmt, IsSummary);	
				System.out.println(taxLine.toString());
				log.fine(taxLine.toString());
				list.add(taxLine);				
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
			return null;
		}
		finally {
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		//	Return Array
		DocTax[] fl = new DocTax[list.size()];
		list.toArray(fl);
		log.info(list+"");
		return fl;
	}
	
	public static DocTax[] loadSummaryTaxes(Integer p_GetID, String p_Class)
	{
		ArrayList<DocTax> list = new ArrayList<DocTax>();	
		String sql = null;
		if(BANKSTATEMENT.equalsIgnoreCase(p_Class))
		{
		 sql =               
				 "select t.C_Tax_ID as C_Tax_ID ,t.Name as Name,t.Rate as Rate, cb.chargeamt as chargeAmt," 
				+" FN_GET_TAX_AMOUNT_CHILD((cb.chargeamt-CB.TAXAMT),t.c_tax_id) as TaxAMount ,t.IsSummary as IsSummary"
				+" from C_Tax t, C_BankStatementLine CB"
				+" Where t.parent_Tax_ID=CB.c_tax_id AND CB.C_BankStatement_ID=?";
		}
		
		if(CASHJOURNAL.equalsIgnoreCase(p_Class))
		{
		 sql =               
				 "select t.C_Tax_ID as C_Tax_ID ,t.Name as Name,t.Rate as Rate, cb.chargeamt as chargeAmt," 
				+" FN_GET_TAX_AMOUNT_CHILD((cb.chargeamt-CB.TAXAMT),t.c_tax_id) as TaxAMount ,t.IsSummary as IsSummary"
				+" from C_Tax t, C_CashLine CB"
				+" Where t.parent_Tax_ID=CB.c_tax_id AND CB.C_Cash_ID=?";
		}
		
		if(PAYMENT.equalsIgnoreCase(p_Class))
		{
			boolean  IsMultiCharge = "N".equals("N");
			sql = "select IsMultiCharge from C_Payment where C_Payment_ID=?";
			PreparedStatement pstmt1 = null;
			ResultSet rs1 = null;
			try
			{
				pstmt1 = DB.prepareStatement(sql, null);
				pstmt1.setInt(1, p_GetID);
				rs1 = pstmt1.executeQuery();
				//
				while (rs1.next())
				{
					
					 IsMultiCharge = "Y".equals(rs1.getString("IsMultiCharge"));
								
				}
			}
			catch (SQLException e)
			{
				log.log(Level.SEVERE, sql, e);
				return null;
			}
			finally {
				DB.close(rs1, pstmt1);
				rs1 = null; pstmt1 = null;
			}
			if(!IsMultiCharge)
//			{
//				 sql =               
//						 "select t.C_Tax_ID as C_Tax_ID ,t.Name as Name,t.Rate as Rate, paymentallocate.Amount as chargeAmt," 
//						+" FN_GET_TAX_AMOUNT_CHILD((paymentallocate.Amount-paymentallocate.TAXAMT),t.c_tax_id) as TaxAMount ,t.IsSummary as IsSummary"
//						+" from C_Tax t, C_PaymentAllocate paymentallocate"
//						+" Where t.parent_Tax_ID=paymentallocate.c_tax_id AND paymentallocate.C_Payment_ID=?";
//			}
//			else
				
		//	{
				 sql =               
						 "select t.C_Tax_ID as C_Tax_ID ,t.Name as Name,t.Rate as Rate, payment.payamt as chargeAmt," 
						+" FN_GET_TAX_AMOUNT_CHILD((payment.payamt-payment.TAXAMT),t.c_tax_id) as TaxAMount ,t.IsSummary as IsSummary"
						+" from C_Tax t, C_Payment payment"
						+" Where t.parent_Tax_ID=payment.c_tax_id AND payment.C_Payment_ID=?";
		//	}
		}
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
	
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, p_GetID);
			rs = pstmt.executeQuery();
			//
			while (rs.next())
			{
				int C_Tax_ID = rs.getInt("C_Tax_ID");
				String name = rs.getString("Name");
				BigDecimal rate = rs.getBigDecimal("Rate");
				BigDecimal ChargeAmt = rs.getBigDecimal("chargeAmt");
				BigDecimal amount = rs.getBigDecimal("TaxAMount");
				boolean IsSummary = "Y".equals(rs.getString("IsSummary"));
						
					DocTax taxLine = new DocTax(C_Tax_ID, name, rate, 
				  			ChargeAmt, amount, IsSummary);	
					log.fine(taxLine.toString());
					list.add(taxLine);
			}
			
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
			return null;
		}
		finally {
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		//	Return Array
		DocTax[] fl = new DocTax[list.size()];
		list.toArray(fl);
		log.info(list+"");
		return fl;
	}//loadSummaryLevelTaxes
}
