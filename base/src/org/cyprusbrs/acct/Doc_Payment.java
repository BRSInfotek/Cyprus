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
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Level;

import org.cyprusbrs.model.MAccount;
import org.cyprusbrs.model.MAcctSchema;
import org.cyprusbrs.model.MBankAccount;
import org.cyprusbrs.model.MBankStatement;
import org.cyprusbrs.model.MBankStatementLine;
import org.cyprusbrs.model.MCharge;
import org.cyprusbrs.model.MPayment;
import org.cyprusbrs.model.MPaymentAllocate;
import org.cyprusbrs.model.MPeriod;
import org.cyprusbrs.util.Env;
import org.cyprus.util.UtilTax;

/**
 *  Post Invoice Documents.
 *  <pre>
 *  Table:              C_Payment (335)
 *  Document Types      ARP, APP
 *  </pre>
 *  @author Jorg Janke
 *  @version  $Id: Doc_Payment.java,v 1.3 2006/07/30 00:53:33 jjanke Exp $
 */
public class Doc_Payment extends Doc
{
	/**
	 *  Constructor
	 * 	@param ass accounting schemata
	 * 	@param rs record
	 * 	@param trxName trx
	 */
	public Doc_Payment (MAcctSchema[] ass, ResultSet rs, String trxName)
	{
		super (ass, MPayment.class, rs, null, trxName);
	}	//	Doc_Payment
	
	/**	Tender Type				*/
	private String		m_TenderType = null;
	
	private DocTax[]        m_taxes = null;
	/** Prepayment				*/
	private boolean		m_Prepayment = false;
	/** Bank Account			*/
	private int			m_C_BankAccount_ID = 0;

	/**
	 *  Load Specific Document Details
	 *  @return error message or null
	 */
	protected String loadDocumentDetails ()
	{
		MPayment pay = (MPayment)getPO();
		setDateDoc(pay.getDateTrx());
		m_TenderType = pay.getTenderType();
		m_Prepayment = pay.isPrepayment();
		m_C_BankAccount_ID = pay.getC_BankAccount_ID();
		//	Amount
		setAmount(Doc.AMTTYPE_Gross, pay.getPayAmt());
		if(!pay.IsMultiCharge())
		m_taxes = UtilTax.loadTaxes(get_ID(),UtilTax.PAYMENT);		
//		log.fine("Lines=" + p_lines.length + ", Taxes=" + m_taxes.length);
	//	System.out.println("----------------------------Lines=" + p_lines.length);
		return null;
	}   //  loadDocumentDetails
	
//	private Doc[] load(MPayment pay)
//	{
//		System.out.println("----------------------------");
//		ArrayList<Doc> list = new ArrayList<Doc>();
//		if(!pay.IsMultiCharge())
//		{
////		MPaymentAllocate[] lines = pay.getLines(false);
//			MPayment[] payment = pay.get(false);
//		for (int i = 0; i < payment.length; i++)
//		{
//			pay = payment[i];
////			DocLine_Allocation docLine = new DocLine_Allocation(line, this); 
//			Doc_Payment docpay = new Doc_Payment(pay.get, this); 
//			//	Set Date Acct
//			if (i == 0)
//				setDateAcct(pay.getDateAcct());
//			MPeriod period = MPeriod.get(getCtx(), pay.getDateAcct(), pay.getAD_Org_ID());
////			if (period != null && period.isOpen(DOCTYPE_Payment, line.getDateAcct()))
////				pay.setC_Period_ID(period.getC_Period_ID());
//			//
//			list.add(pay);
//		}
//		}

//		//	Return Array
//		DocLine[] dls = new DocLine[list.size()];
//		list.toArray(dls);
//		return dls;
//	}	//	loadLines



	
	/**************************************************************************
	 *  Get Source Currency Balance - always zero
	 *  @return Zero (always balanced)
	 */
//	public BigDecimal getBalance()
//	{
//		BigDecimal retValue = Env.ZERO;
//	//	log.config( toString() + " Balance=" + retValue);
//		return retValue;
//	}   //  getBalance
	
	
	public BigDecimal getBalance()
	{
		System.out.println("----------------------------");

		BigDecimal retValue = Env.ZERO;
		StringBuffer sb = new StringBuffer (" [");
		//  Total
		retValue = retValue.add(getAmount(Doc.AMTTYPE_Gross));
		sb.append(getAmount(Doc.AMTTYPE_Gross));
		//  - Lines
//		for (int i = 0; i < p_lines.length; i++)
//		{
	//		BigDecimal lineBalance = ((DocLine_Bank)p_lines[i]).getStmtAmt();
			BigDecimal Balance = this.getAmount();
			retValue = retValue.subtract(Balance);
			sb.append("-").append(Balance);
	//	}
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
	 *  ARP, APP.
	 *  <pre>
	 *  ARP
	 *      BankInTransit   DR
	 *      UnallocatedCash         CR
	 *      or Charge/C_Prepayment
	 *  APP
	 *      PaymentSelect   DR
	 *      or Charge/V_Prepayment
	 *      BankInTransit           CR
	 *  CashBankTransfer
	 *      -
	 *  </pre>
	 *  @param as accounting schema
	 *  @return Fact
	 */
	public ArrayList<Fact> createFacts (MAcctSchema as)
	{
		//  create Fact Header
		Fact fact = new Fact(this, as, Fact.POST_Actual);
		//	Cash Transfer
		if ("X".equals(m_TenderType))
		{
			ArrayList<Fact> facts = new ArrayList<Fact>();
			facts.add(fact);
			return facts;
		}

		int AD_Org_ID = getBank_Org_ID();		//	Bank Account Org
		MPayment pay = new MPayment(getCtx(),get_ID(),getTrxName());
		MAccount acct = null;
		if (getDocumentType().equals(DOCTYPE_ARReceipt))
		{
			//	Asset
			FactLine fl = fact.createLine(null, getAccount(Doc.ACCTTYPE_BankInTransit, as),
				getC_Currency_ID(), getAmount(), null);
			if (fl != null && AD_Org_ID != 0)
				fl.setAD_Org_ID(AD_Org_ID);
			//	
			
			if (getC_Charge_ID() != 0)
			{

				/// Updated by Anshul @20210708
              	//  TaxDue                  CR
				BigDecimal totalTaxAmount=Env.ZERO;
				
				System.out.println(m_taxes.length);
				for(DocTax j:m_taxes) /// Need to remove this for loop... Single time run
				{
					System.out.println(j.getName()+" "+j.getAccount(DocTax.ACCTTYPE_TaxDue, as));
					BigDecimal	amt = j.getTaxAmount();
					log.info(amt+"");
					System.out.println(amt+" -------------------- "+j.getAccount(DocTax.ACCTTYPE_TaxDue, as).getAccount_ID()+"  pay.getTaxAmt() :  "+pay.getTaxAmt());
					if (!j.isSalesTax() && pay.getTaxAmt() !=null && pay.getTaxAmt().signum() != 0) // Parent Tax
					{
						fl = fact.createLine(null,
								j.getAccount(DocTax.ACCTTYPE_TaxDue, as),
								pay.getC_Currency_ID(), null,pay.getTaxAmt());
						if (fl != null)
							fl.setC_Tax_ID(j.getC_Tax_ID());
						
						totalTaxAmount=pay.getTaxAmt();
					}
					else if(pay.getTaxAmt() !=null && pay.getTaxAmt().signum() != 0) // Child Tax
					{
						DocTax[] m_sum_taxes=UtilTax.loadSummaryTaxes(get_ID(),UtilTax.PAYMENT);
						log.fine("Taxes=" + m_sum_taxes.length);
						System.out.println("Taxes=" + m_sum_taxes.length);
						for(DocTax childTax:m_sum_taxes)
						{
							fl = fact.createLine(null,
									childTax.getAccount(DocTax.ACCTTYPE_TaxDue, as),
							pay.getC_Currency_ID(), null,childTax.getAmount());
							if (fl != null)
								fl.setC_Tax_ID(childTax.getC_Tax_ID());
							
							totalTaxAmount=totalTaxAmount.add(childTax.getAmount());
						}
					}
				}
				
				System.out.println("totalTaxAmount =" + totalTaxAmount);
				fl = fact.createLine(null, MCharge.getAccount(getC_Charge_ID(), as, getAmount()),
						getC_Currency_ID(), null, getAmount().subtract(totalTaxAmount));
			}
			
			else if (m_Prepayment)
				acct = getAccount(Doc.ACCTTYPE_C_Prepayment, as);
			else
				acct = getAccount(Doc.ACCTTYPE_UnallocatedCash, as);
			fl = fact.createLine(null, acct,
				getC_Currency_ID(), null, getAmount());
			if (fl != null && AD_Org_ID != 0
				&& getC_Charge_ID() == 0)		//	don't overwrite charge
				fl.setAD_Org_ID(AD_Org_ID);
		}
		//  APP
		else if (getDocumentType().equals(DOCTYPE_APPayment))
		{
			FactLine fl = null;
			if (getC_Charge_ID() != 0)
			{
				
			
			BigDecimal totalTaxAmount=Env.ZERO;
			
			for(DocTax j:m_taxes)
			{
				System.out.println(j.getName()+" "+j.getAccount(DocTax.ACCTTYPE_TaxCredit, as));
				BigDecimal	amt = j.getTaxAmount();
				log.info(amt+"");
				System.out.println(amt+" -------------------- "+j.getAccount(DocTax.ACCTTYPE_TaxCredit, as).getAccount_ID()+"  pay.getTaxAmt() :  "+pay.getTaxAmt());
				if (!j.isSalesTax() && pay.getTaxAmt() !=null && pay.getTaxAmt().signum() != 0) // Parent Tax
				{
			       fl = fact.createLine(null,
							j.getAccount(DocTax.ACCTTYPE_TaxCredit, as),
							pay.getC_Currency_ID(), pay.getTaxAmt(),null);
					if (fl != null)
						fl.setC_Tax_ID(j.getC_Tax_ID());
					
					totalTaxAmount=pay.getTaxAmt();
				}
				else if(pay.getTaxAmt() !=null && pay.getTaxAmt().signum() != 0) // Child Tax
				{
					DocTax[] m_sum_taxes=UtilTax.loadSummaryTaxes(get_ID(),UtilTax.PAYMENT);
					log.fine("Taxes=" + m_sum_taxes.length);
					System.out.println("Taxes=" + m_sum_taxes.length);
					for(DocTax childTax:m_sum_taxes)
					{
						System.out.println("childTax.getAmount() =" + childTax.getAmount());
						fl = fact.createLine(null,
								childTax.getAccount(DocTax.ACCTTYPE_TaxCredit, as),
						pay.getC_Currency_ID(), childTax.getAmount(),null);
						if (fl != null)
							fl.setC_Tax_ID(childTax.getC_Tax_ID());
						
						totalTaxAmount=totalTaxAmount.add(childTax.getAmount());
					}
				}
				
			}
				System.out.println(" totalTaxAmount "+totalTaxAmount);
		
				
				fl = fact.createLine(null, MCharge.getAccount(getC_Charge_ID(), as, getAmount()),
						getC_Currency_ID(),(getAmount().subtract(totalTaxAmount)), null);

		
		}
			// End of code by Anshul @20210708
			
			else if (m_Prepayment)
				acct = getAccount(Doc.ACCTTYPE_V_Prepayment, as);
			else
				acct = getAccount(Doc.ACCTTYPE_PaymentSelect, as);
			 fl = fact.createLine(null, acct,
				getC_Currency_ID(), getAmount(), null);
			if (fl != null && AD_Org_ID != 0
				&& getC_Charge_ID() == 0)		//	don't overwrite charge
				fl.setAD_Org_ID(AD_Org_ID);
			
			//	Asset
			fl = fact.createLine(null, getAccount(Doc.ACCTTYPE_BankInTransit, as),
				getC_Currency_ID(), null, getAmount());
			if (fl != null && AD_Org_ID != 0)
				fl.setAD_Org_ID(AD_Org_ID);
		}
		else
		{
			p_Error = "DocumentType unknown: " + getDocumentType();
			log.log(Level.SEVERE, p_Error);
			fact = null;
		}
		
		ArrayList<Fact> facts = new ArrayList<Fact>();
		facts.add(fact);
		return facts;
	}   //  createFact

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
	
	
	
}   //  Doc_Payment
