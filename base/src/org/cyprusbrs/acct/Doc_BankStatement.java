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
package org.cyprusbrs.acct;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;

import org.cyprusbrs.model.MAccount;
import org.cyprusbrs.model.MAcctSchema;
import org.cyprusbrs.model.MBankAccount;
import org.cyprusbrs.model.MBankStatement;
import org.cyprusbrs.model.MBankStatementLine;
import org.cyprusbrs.model.MPeriod;
import org.cyprusbrs.util.DB;
import org.cyprusbrs.util.Env;
import org.cyprus.util.UtilTax;

/**
 *  Post Invoice Documents.
 *  <pre>
 *  Table:              C_BankStatement (392)
 *  Document Types:     CMB
 *  </pre>
 *  @author Jorg Janke
 *  @version  $Id: Doc_Bank.java,v 1.3 2006/07/30 00:53:33 jjanke Exp $
 *  
 *  FR [ 1840016 ] Avoid usage of clearing accounts - subject to C_AcctSchema.IsPostIfClearingEqual 
 *  Avoid posting if both accounts BankAsset and BankInTransit are equal
 *  @author victor.perez@e-evolution.com, e-Evolution http://www.e-evolution.com
 * 				<li>FR [ 2520591 ] Support multiples calendar for Org 
 * 				@see http://sourceforge.net/tracker2/?func=detail&atid=879335&aid=2520591&group_id=176962 
 *  
 */
public class Doc_BankStatement extends Doc
{
	/**
	 *  Constructor
	 * 	@param ass accounting schemata
	 * 	@param rs record
	 * 	@param trxName trx
	 */
	public Doc_BankStatement (MAcctSchema[] ass, ResultSet rs, String trxName)
	{
		super (ass, MBankStatement.class, rs, DOCTYPE_BankStatement, trxName);
	}	//	Doc_Bank
	
	
	/** Contained Optional Tax Lines - Updated by Anshul    */
	private DocTax[]        m_taxes = null;
	/** Currency Precision				*/
	private int				m_precision = -1;
	
	/** Bank Account			*/
	private int			m_C_BankAccount_ID = 0;

	/**
	 *  Load Specific Document Details
	 *  @return error message or null
	 */
	protected String loadDocumentDetails ()
	{
		MBankStatement bs = (MBankStatement)getPO();
		setDateDoc(bs.getStatementDate());
		setDateAcct(bs.getStatementDate());	//	Overwritten on Line Level
		
		m_C_BankAccount_ID = bs.getC_BankAccount_ID();
		//	Amounts
		setAmount(AMTTYPE_Gross, bs.getStatementDifference());

		//  Set Bank Account Info (Currency)
		MBankAccount ba = MBankAccount.get (getCtx(), m_C_BankAccount_ID);
		setC_Currency_ID (ba.getC_Currency_ID());

		//	Contained Objects
//		m_taxes = loadTaxes();
		m_taxes = UtilTax.loadTaxes(get_ID(),UtilTax.BANKSTATEMENT); // Updated by ANshul @20210901
		p_lines = loadLines(bs);
		log.fine("Lines=" + p_lines.length + ", Taxes=" + m_taxes.length);
		System.out.println("----------------------------Lines=" + p_lines.length);
		return null;
	}   //  loadDocumentDetails

	/**
	 * Load bank Statement Taxes
	 * @return
	 */
	private DocTax[] loadTaxes()
	{
		ArrayList<DocTax> list = new ArrayList<DocTax>();		
		String sql = "select CB.C_Tax_ID as C_Tax_ID ,t.Name as Name,t.Rate as Rate,CB.ChargeAmt as ChargeAmt,CB.TaxAmt as TaxAmt,t.IsSummary as IsSummary "
			 + " from C_Tax t,C_BankStatementLine CB" 
			 + " Where t.C_Tax_ID=CB.C_Tax_ID AND CB.C_BankStatement_ID=?";
				
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, getTrxName());
			pstmt.setInt(1, get_ID());
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
	
	/**
	 * Load bank Statement  line summary Taxes
	 * @return
	 */
	
	private DocTax[] loadSummaryTaxes()
	{
		ArrayList<DocTax> list = new ArrayList<DocTax>();			
		String sql =               
				 "select t.C_Tax_ID as C_Tax_ID ,t.Name as Name,t.Rate as Rate, cb.chargeamt as chargeAmt," 
				+" FN_GET_TAX_AMOUNT_CHILD((cb.chargeamt-CB.TAXAMT),t.c_tax_id) as TaxAMount ,t.IsSummary as IsSummary"
				+" from C_Tax t, C_BankStatementLine CB"
				+" Where t.parent_Tax_ID=CB.c_tax_id AND CB.C_BankStatement_ID=?";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
	
		try
		{
			pstmt = DB.prepareStatement(sql, getTrxName());
			pstmt.setInt(1, get_ID());
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
	
	/**
	 *	Load Invoice Line.
	 *	@param bs bank statement
	 *  4 amounts
	 *  AMTTYPE_Payment
	 *  AMTTYPE_Statement2
	 *  AMTTYPE_Charge
	 *  AMTTYPE_Interest
	 *  @return DocLine Array
	 */
	private DocLine[] loadLines(MBankStatement bs)
	{
		System.out.println("----------------------------");
		ArrayList<DocLine> list = new ArrayList<DocLine>();
		MBankStatementLine[] lines = bs.getLines(false);
		for (int i = 0; i < lines.length; i++)
		{
			MBankStatementLine line = lines[i];
			DocLine_Bank docLine = new DocLine_Bank(line, this);
			//	Set Date Acct
			if (i == 0)
				setDateAcct(line.getDateAcct());
			MPeriod period = MPeriod.get(getCtx(), line.getDateAcct(), line.getAD_Org_ID());
			if (period != null && period.isOpen(DOCTYPE_BankStatement, line.getDateAcct()))
				docLine.setC_Period_ID(period.getC_Period_ID());
			//
			list.add(docLine);
		}

		//	Return Array
		DocLine[] dls = new DocLine[list.size()];
		list.toArray(dls);
		return dls;
	}	//	loadLines

	
	/**************************************************************************
	 *  Get Source Currency Balance - subtracts line amounts from total - no rounding
	 *  @return positive amount, if total invoice is bigger than lines
	 */
	public BigDecimal getBalance()
	{
		System.out.println("----------------------------");

		BigDecimal retValue = Env.ZERO;
		StringBuffer sb = new StringBuffer (" [");
		//  Total
		retValue = retValue.add(getAmount(Doc.AMTTYPE_Gross));
		sb.append(getAmount(Doc.AMTTYPE_Gross));
		//  - Lines
		for (int i = 0; i < p_lines.length; i++)
		{
			BigDecimal lineBalance = ((DocLine_Bank)p_lines[i]).getStmtAmt();
			retValue = retValue.subtract(lineBalance);
			sb.append("-").append(lineBalance);
		}
		sb.append("]");
		//
		for(DocTax i:m_taxes)
		{
			System.out.println(i.toString());
			retValue = retValue.subtract(i.getTaxAmount());
			log.info(retValue+"");
			sb.append("-").append(i.getTaxAmount());
			log.info(sb.append("-").append(i.getTaxAmount())+"");
		}
	sb.append("]");
	
		log.fine(toString() + " Balance=" + retValue + sb.toString());
		return retValue;
	}   //  getBalance

	/**
	 *  Create Facts (the accounting logic) for
	 *  CMB.
	 *  <pre>
	 *      BankAsset       DR      CR  (Statement)
	 *      BankInTransit   DR      CR              (Payment)
	 *      Charge          DR          (Charge)
	 *      Interest        DR      CR  (Interest)
	 *  </pre>
	 *  @param as accounting schema
	 *  @return Fact
	 */
	public ArrayList<Fact> createFacts (MAcctSchema as)
	{
		System.out.println("----------------------------");

		//  create Fact Header
		Fact fact = new Fact(this, as, Fact.POST_Actual);
		// boolean isInterOrg = isInterOrg(as);

		//  Header -- there may be different currency amounts

		FactLine fl = null;
		int AD_Org_ID = getBank_Org_ID();	//	Bank Account Org
		//  Lines
		for (int i = 0; i < p_lines.length; i++)
		{
			DocLine_Bank line = (DocLine_Bank)p_lines[i];
			int C_BPartner_ID = line.getC_BPartner_ID();
			
			// Avoid usage of clearing accounts
			// If both accounts BankAsset and BankInTransit are equal
			// then remove the posting
			
			MAccount acct_bank_asset =  getAccount(Doc.ACCTTYPE_BankAsset, as);
			MAccount acct_bank_in_transit = getAccount(Doc.ACCTTYPE_BankInTransit, as);
			
			// if ((!as.isPostIfClearingEqual()) && acct_bank_asset.equals(acct_bank_in_transit) && (!isInterOrg)) {
			// don't validate interorg on banks for this - normally banks are balanced by orgs
			if ((!as.isPostIfClearingEqual()) && acct_bank_asset.equals(acct_bank_in_transit)) {
				// Not using clearing accounts
				// just post the difference (if any)
				
				BigDecimal amt_stmt_minus_trx = line.getStmtAmt().subtract(line.getTrxAmt());
				if (amt_stmt_minus_trx.compareTo(Env.ZERO) != 0) {

					//  BankAsset       DR      CR  (Statement minus Payment)
					fl = fact.createLine(line,
						getAccount(Doc.ACCTTYPE_BankAsset, as),
						line.getC_Currency_ID(), amt_stmt_minus_trx);
					if (fl != null && AD_Org_ID != 0)
						fl.setAD_Org_ID(AD_Org_ID);
					if (fl != null && C_BPartner_ID != 0)
						fl.setC_BPartner_ID(C_BPartner_ID);
					
				}
				
			} else {
			
				// Normal Adempiere behavior -- unchanged if using clearing accounts
				
				//  BankAsset       DR      CR  (Statement)
				fl = fact.createLine(line,
					getAccount(Doc.ACCTTYPE_BankAsset, as),
					line.getC_Currency_ID(), line.getStmtAmt());
				if (fl != null && AD_Org_ID != 0)
					fl.setAD_Org_ID(AD_Org_ID);
				if (fl != null && C_BPartner_ID != 0)
					fl.setC_BPartner_ID(C_BPartner_ID);
				
				//  BankInTransit   DR      CR              (Payment)
				fl = fact.createLine(line,
					getAccount(Doc.ACCTTYPE_BankInTransit, as),
					line.getC_Currency_ID(), line.getTrxAmt().negate());
				if (fl != null)
				{
					if (C_BPartner_ID != 0)
						fl.setC_BPartner_ID(C_BPartner_ID);
					if (AD_Org_ID != 0)
						fl.setAD_Org_ID(AD_Org_ID);
					else
						fl.setAD_Org_ID(line.getAD_Org_ID(true)); // from payment
				}
			
			}
			// End Avoid usage of clearing accounts
			
			//  Charge          DR          (Charge)
			if (line.getChargeAmt().compareTo(Env.ZERO) > 0) {
				
				//  TaxDue                  CR
				BigDecimal totalTaxAmount=Env.ZERO;
				/// Updated by Anshul @20210708
				System.out.println(m_taxes.length);
				for(DocTax j:m_taxes) /// Need to remove this for loop... Single time run
				{
					System.out.println(j.getName()+" "+j.getAccount(DocTax.ACCTTYPE_TaxDue, as));
					BigDecimal	amt = j.getTaxAmount();
					log.info(amt+"");
					System.out.println(amt+" -------------------- "+j.getAccount(DocTax.ACCTTYPE_TaxDue, as).getAccount_ID()+"  line.getTaxAmount() :  "+line.getTaxAmount());
					if (!j.isSalesTax() && line.getTaxAmount() !=null && line.getTaxAmount().signum() != 0) // Parent Tax
					{
						fl = fact.createLine(line,
								j.getAccount(DocTax.ACCTTYPE_TaxDue, as),
								line.getC_Currency_ID(), null,line.getTaxAmount());
						if (fl != null)
							fl.setC_Tax_ID(j.getC_Tax_ID());
						
						totalTaxAmount=line.getTaxAmount();
					}
					else if(line.getTaxAmount() !=null && line.getTaxAmount().signum() != 0)// Child Tax
					{
//						DocTax[] m_sum_taxes=loadSummaryTaxes();
						DocTax[] m_sum_taxes=UtilTax.loadSummaryTaxes(get_ID(),UtilTax.BANKSTATEMENT); // Updated by Anshul @20210901
						log.fine("Lines=" + p_lines.length + ", Taxes=" + m_sum_taxes.length);
						System.out.println("Lines=" + p_lines.length + ", Taxes=" + m_sum_taxes.length);
						for(DocTax childTax:m_sum_taxes)
						{
							fl = fact.createLine(line,
									childTax.getAccount(DocTax.ACCTTYPE_TaxDue, as),
							line.getC_Currency_ID(), null,childTax.getAmount());
							if (fl != null)
								fl.setC_Tax_ID(childTax.getC_Tax_ID());
							
							totalTaxAmount=totalTaxAmount.add(childTax.getAmount());
							if(totalTaxAmount.compareTo(Env.ZERO) != 0)
								break;
						}
					}
					if(totalTaxAmount.compareTo(Env.ZERO) != 0)
						break;
				}
				/// End of code by Anshul @20210708
				System.out.println("totalTaxAmount =" + totalTaxAmount);

				fl = fact.createLine(line,
						line.getChargeAccount(as, line.getChargeAmt().negate()),
						line.getC_Currency_ID(), null, line.getChargeAmt().subtract(totalTaxAmount));
//						line.getC_Currency_ID(), null, line.getChargeAmt());
				
			} else {
				
				//  TaxCredit                  CR
				BigDecimal totalTaxAmount=Env.ZERO;
				
				/// Updated by Anshul @20210708 when tax amount in negative
				for(DocTax j:m_taxes)
				{
					System.out.println(j.getName()+" "+j.getAccount(DocTax.ACCTTYPE_TaxCredit, as));
					BigDecimal	amt = j.getTaxAmount();
					log.info(amt+"");
					System.out.println(amt+" -------------------- "+j.getAccount(DocTax.ACCTTYPE_TaxCredit, as).getAccount_ID()+"  line.getTaxAmount() :  "+line.getTaxAmount());
					if (!j.isSalesTax() && line.getTaxAmount() !=null && line.getTaxAmount().signum() != 0) // Parent Tax
					{
						fl = fact.createLine(line,
								j.getAccount(DocTax.ACCTTYPE_TaxCredit, as),
								line.getC_Currency_ID(), line.getTaxAmount().negate(),null);
						if (fl != null)
							fl.setC_Tax_ID(j.getC_Tax_ID());
						
						totalTaxAmount=line.getTaxAmount();
					}
					else if(line.getTaxAmount() !=null && line.getTaxAmount().signum() != 0) // Child Tax
					{
//						DocTax[] m_sum_taxes=loadSummaryTaxes();
						DocTax[] m_sum_taxes=UtilTax.loadSummaryTaxes(get_ID(),UtilTax.BANKSTATEMENT); // Updated by Anshul @20210901
						log.fine("Lines=" + p_lines.length + ", Taxes=" + m_sum_taxes.length);
						System.out.println("Lines=" + p_lines.length + ", Taxes=" + m_sum_taxes.length);
						for(DocTax childTax:m_sum_taxes)
						{
							System.out.println("Lines=" + p_lines.length + ", childTax.getAmount() =" + childTax.getAmount());
							fl = fact.createLine(line,
									childTax.getAccount(DocTax.ACCTTYPE_TaxCredit, as),
							line.getC_Currency_ID(), childTax.getAmount().negate(),null);
							if (fl != null)
								fl.setC_Tax_ID(childTax.getC_Tax_ID());
							
							totalTaxAmount=totalTaxAmount.add(childTax.getAmount());
							if(totalTaxAmount.compareTo(Env.ZERO) != 0)
								break;
						}
					}
					if(totalTaxAmount.compareTo(Env.ZERO) != 0)
						break;
				}
					System.out.println(" totalTaxAmount "+totalTaxAmount);
					fl = fact.createLine(line,
							line.getChargeAccount(as, line.getChargeAmt().negate()),
							line.getC_Currency_ID(), (line.getChargeAmt().add(totalTaxAmount.abs())).negate(), null);
//							line.getC_Currency_ID(), line.getChargeAmt().negate(), null);	
				
				/// End of code by Anshul @20210708
			}
			if (fl != null && C_BPartner_ID != 0)
				fl.setC_BPartner_ID(C_BPartner_ID);

			//  Interest        DR      CR  (Interest)
			if (line.getInterestAmt().signum() < 0)
				fl = fact.createLine(line,
					getAccount(Doc.ACCTTYPE_InterestExp, as), getAccount(Doc.ACCTTYPE_InterestExp, as),
					line.getC_Currency_ID(), line.getInterestAmt().negate());
			else
				fl = fact.createLine(line,
					getAccount(Doc.ACCTTYPE_InterestRev, as), getAccount(Doc.ACCTTYPE_InterestRev, as),
					line.getC_Currency_ID(), line.getInterestAmt().negate());
			if (fl != null && C_BPartner_ID != 0)
				fl.setC_BPartner_ID(C_BPartner_ID);
			//
		//	fact.createTaxCorrection();
		
		/// By Mukesh @20210707	to Add Charge Amount And Account here
			
			
		}
		//
		ArrayList<Fact> facts = new ArrayList<Fact>();
		facts.add(fact);
		return facts;
	}   //  createFact

	/** Verify if the posting involves two or more organizations
	@return true if there are more than one org involved on the posting
	private boolean isInterOrg(MAcctSchema as) {
		MAcctSchemaElement elementorg = as.getAcctSchemaElement(MAcctSchemaElement.ELEMENTTYPE_Organization);
		if (elementorg == null || !elementorg.isBalanced()) {
			// no org element or not need to be balanced
			return false;
		}
		
		if (p_lines.length <= 0) {
			// no lines
			return false;
		}
		
		int startorg = getBank_Org_ID();
		if (startorg == 0)
			startorg = p_lines[0].getAD_Org_ID();
		// validate if the allocation involves more than one org
		for (int i = 0; i < p_lines.length; i++) {
			if (p_lines[i].getAD_Org_ID() != startorg)
				return true;
		}
		
		return false;
	}
	 */

	/**
	 * 	Get AD_Org_ID from Bank Account
	 * 	@return AD_Org_ID or 0
	 */
	private int getBank_Org_ID ()
	{
		if (m_C_BankAccount_ID == 0)
			return 0;
		//
		MBankAccount ba = MBankAccount.get(getCtx(), m_C_BankAccount_ID);
		return ba.getAD_Org_ID();
	}	//	getBank_Org_ID

}   //  Doc_Bank
