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
import java.util.ArrayList;

import org.cyprusbrs.model.MAccount;
import org.cyprusbrs.model.MAcctSchema;
import org.cyprusbrs.model.MInventory;
import org.cyprusbrs.model.MInventoryLine;
import org.cyprusbrs.model.ProductCost;
import org.cyprusbrs.util.DB;
import org.cyprusbrs.util.Env;

/**
 *  Post Inventory Documents.
 *  <pre>
 *  Table:              M_Inventory (321)
 *  Document Types:     MMI
 *  </pre>
 *  @author Jorg Janke
 *  @author Armen Rizal, Goodwill Consulting
 * 			<li>BF [ 1745154 ] Cost in Reversing Material Related Docs
 * 	@author red1
 * 			<li>BF [ 2982994 ]  Internal Use Inventory does not reverse Accts
 *  @version  $Id: Doc_Inventory.java,v 1.3 2006/07/30 00:53:33 jjanke Exp $
 */
public class Doc_Inventory extends Doc
{
	private int				m_Reversal_ID = 0;
	private String			m_DocStatus = "";
	
	/**
	 *  Constructor
	 * 	@param ass accounting schemata
	 * 	@param rs record
	 * 	@param trxName trx
	 */
	public Doc_Inventory (MAcctSchema[] ass, ResultSet rs, String trxName)
	{
		super (ass, MInventory.class, rs, DOCTYPE_MatInventory, trxName);
	}   //  Doc_Inventory
	
	private String setcost()
	{
		//set current cost price for m_cost
		        MInventory inventory = (MInventory)getPO();
				MInventoryLine[] linesInv = inventory.getLines(false);
				Boolean isOpeningStock=inventory.isOpeningStock();
				if(isOpeningStock)
				for (int j = 0; j < linesInv.length; j++)
				{
					MInventoryLine lineInventory = linesInv[j];
					int M_InventoryLine_ID = lineInventory.getM_InventoryLine_ID();
					
					
					String sqlchk = "SELECT prca.costingmethod from M_Product pr, M_Product_Category_Acct prca,M_Inventoryline invl" 
							 + " WHERE pr.M_Product_Category_ID=prca.M_Product_Category_ID "  
							 + "AND invl.M_Product_ID=pr.M_Product_ID "  
							 + "AND invl.M_Inventoryline_ID=" + M_InventoryLine_ID;
					PreparedStatement pstmt = null;		
					ResultSet rs = null;
					try {
						 pstmt = DB.prepareStatement(sqlchk, null);
						  rs = pstmt.executeQuery();
						 	if (rs.next()) {
								String costingmethod = rs.getString(1);
								if(costingmethod == null)
								{					
//					String sql = "SELECT invl.currentcostprice , invl.M_Product_ID,acctsch.C_AcctSchema_ID,ce.M_CostElement_ID,ce.AD_Client_ID,ce.AD_Org_ID "  
//							+ "FROM M_Inventoryline invl, M_Product pr,M_Product_Category_Acct prca, C_AcctSchema acctsch,M_CostElement ce " 
//							+ "WHERE invl.M_Product_ID=pr.M_Product_ID "  
//							+ "AND pr.M_Product_Category_ID=prca.M_Product_Category_ID "  
//							+ "AND prca.C_AcctSchema_ID=acctsch.C_AcctSchema_ID "  
//							+ "AND acctsch.costingmethod=ce.costingmethod "
//							+ "AND ce.CostElementType='M' "
//							+ "AND invl.ad_client_id=ce.ad_client_id "  
//							+ "AND invl.M_Inventoryline_ID=" + M_InventoryLine_ID;
					
					String sql = "SELECT cost.CUMULATEDQTY,csd.CUMULATEDQTY,invl.currentcostprice,invl.M_Product_ID,acctsch.C_AcctSchema_ID,ce.M_CostElement_ID,ce.ad_client_id,ce.ad_org_id "
							+ "FROM M_Cost cost, m_product pr,M_Product_Category_Acct prca, C_AcctSchema acctsch,M_CostElement ce,M_Inventoryline invl,M_CostDetail csd " 
							+ "WHERE pr.M_Product_ID = cost.M_Product_ID "  
							+ "AND pr.M_Product_Category_ID = prca.M_Product_Category_ID " 
							+ "AND prca.C_AcctSchema_ID = acctsch.C_AcctSchema_ID " 
							+ "AND acctsch.costingmethod=ce.costingmethod "
							+ "AND ce.CostElementType = 'M' " 
							+ "AND ce.M_CostElement_ID = cost.M_CostElement_ID "  
							+ "AND invl.M_Product_ID = pr.M_Product_ID " 
							+ "AND invl.M_Inventoryline_ID = csd.M_Inventoryline_ID "  
							+ "AND cost.ad_client_id = ce.ad_client_id " 
							+ "AND invl.M_Inventoryline_ID=" + M_InventoryLine_ID;
					 pstmt = null;		
					 rs = null;
					try {
						 pstmt = DB.prepareStatement(sql, null);
						  rs = pstmt.executeQuery();
						 	if (rs.next()) {
						 		int costcumulatedqty = rs.getInt(1);
						 		int costdetailcumulatedqty = rs.getInt(2);
								int currentcostprice = rs.getInt(3);
								int M_Product_ID = rs.getInt(4);
								int C_AcctSchema_ID = rs.getInt(5);
								int M_CostElement_ID = rs.getInt(6);
								int AD_Client_ID = rs.getInt(7);
								int AD_Org_ID = rs.getInt(8);
								int costcumulatedamt = currentcostprice * costcumulatedqty;
								int costdetailcumulatedamt = currentcostprice * costdetailcumulatedqty;
								String sql1 = "UPDATE M_Cost SET CurrentCostPrice=" + currentcostprice +" , CUMULATEDAMT=" + costcumulatedamt +"  WHERE M_Product_ID=? AND C_AcctSchema_ID=? AND M_CostElement_ID=? AND AD_Client_ID=? AND AD_Org_ID=?";
								 pstmt = null;			
								try {
									 pstmt = DB.prepareStatement(sql1, null);
									 pstmt.setInt(1, M_Product_ID);
									 pstmt.setInt(2, C_AcctSchema_ID);
									 pstmt.setInt(3, M_CostElement_ID);
									 pstmt.setInt(4, AD_Client_ID);
									 pstmt.setInt(5, AD_Org_ID);
									 int no = pstmt.executeUpdate();
									 log.fine("Lines -> #" + no);
								    } catch (Exception e) {
								        
								    }
								String sql2 = "UPDATE M_CostDetail  SET CurrentCostPrice=" + currentcostprice +" , CUMULATEDAMT=" + costdetailcumulatedamt +"  WHERE M_Product_ID=? AND C_AcctSchema_ID=? AND M_InventoryLine_ID=? AND AD_Client_ID=? AND AD_Org_ID=?";
								 pstmt = null;			
									try {
										 pstmt = DB.prepareStatement(sql2, null);
										 pstmt.setInt(1, M_Product_ID);
										 pstmt.setInt(2, C_AcctSchema_ID);
										 pstmt.setInt(3, M_InventoryLine_ID);
										 pstmt.setInt(4, AD_Client_ID);
										 pstmt.setInt(5, AD_Org_ID);
										 int no = pstmt.executeUpdate();
										 log.fine("Lines -> #" + no);
									    } catch (Exception e) {
									        
									    }
						 	}
//							rs.close();
//							pstmt.close();
//							pstmt = null;
						 	}
					 catch (Exception e) {
					        
					    }
								}
								
								else
								{
//									String sql = "SELECT invl.currentcostprice , invl.M_Product_ID,acctsch.C_AcctSchema_ID,ce.M_CostElement_ID,ce.AD_Client_ID,ce.AD_Org_ID "  
//											+ "FROM M_Inventoryline invl, M_Product pr,M_Product_Category_Acct prca, C_AcctSchema acctsch,M_CostElement ce " 
//											+ "WHERE invl.M_Product_ID=pr.M_Product_ID "  
//											+ "AND pr.M_Product_Category_ID=prca.M_Product_Category_ID "  
//											+ "AND prca.C_AcctSchema_ID=acctsch.C_AcctSchema_ID "  
//											+ "AND prca.costingmethod=ce.costingmethod "
//											+ "AND ce.CostElementType='M' "
//											+ "AND invl.ad_client_id=ce.ad_client_id "  
//											+ "AND invl.M_Inventoryline_ID=" + M_InventoryLine_ID;
									
									String sql = "SELECT cost.CUMULATEDQTY,csd.CUMULATEDQTY,invl.currentcostprice,invl.M_Product_ID,acctsch.C_AcctSchema_ID,ce.M_CostElement_ID,ce.ad_client_id,ce.ad_org_id "
											+ "FROM M_Cost cost, m_product pr,M_Product_Category_Acct prca, C_AcctSchema acctsch,M_CostElement ce,M_Inventoryline invl,M_CostDetail csd " 
											+ "WHERE pr.M_Product_ID = cost.M_Product_ID "  
											+ "AND pr.M_Product_Category_ID = prca.M_Product_Category_ID " 
											+ "AND prca.C_AcctSchema_ID = acctsch.C_AcctSchema_ID " 
											+ "AND prca.costingmethod = ce.costingmethod "
											+ "AND ce.CostElementType = 'M' " 
											+ "AND ce.M_CostElement_ID = cost.M_CostElement_ID "  
											+ "AND invl.M_Product_ID = pr.M_Product_ID " 
											+ "AND invl.M_Inventoryline_ID = csd.M_Inventoryline_ID "  
											+ "AND cost.ad_client_id = ce.ad_client_id " 
											+ "AND invl.M_Inventoryline_ID=" + M_InventoryLine_ID;
									 pstmt = null;		
									 rs = null;
									try {
										 pstmt = DB.prepareStatement(sql, null);
										  rs = pstmt.executeQuery();
										 	if (rs.next()) {
										 		int costcumulatedqty = rs.getInt(1);
										 		int costdetailcumulatedqty = rs.getInt(2);
												int currentcostprice = rs.getInt(3);
												int M_Product_ID = rs.getInt(4);
												int C_AcctSchema_ID = rs.getInt(5);
												int M_CostElement_ID = rs.getInt(6);
												int AD_Client_ID = rs.getInt(7);
												int AD_Org_ID = rs.getInt(8);
												int costcumulatedamt = currentcostprice * costcumulatedqty;
												int costdetailcumulatedamt = currentcostprice * costdetailcumulatedqty;
												String sql1 = "UPDATE M_Cost SET CurrentCostPrice=" + currentcostprice +" , CUMULATEDAMT=" + costcumulatedamt +"  WHERE M_Product_ID=? AND C_AcctSchema_ID=? AND M_CostElement_ID=? AND AD_Client_ID=? AND AD_Org_ID=?";
												 pstmt = null;			
												try {
													 pstmt = DB.prepareStatement(sql1, null);
													 pstmt.setInt(1, M_Product_ID);
													 pstmt.setInt(2, C_AcctSchema_ID);
													 pstmt.setInt(3, M_CostElement_ID);
													 pstmt.setInt(4, AD_Client_ID);
													 pstmt.setInt(5, AD_Org_ID);
													 int no = pstmt.executeUpdate();
													 log.fine("Lines -> #" + no);
												    } catch (Exception e) {
												        
												    }
												String sql2 = "UPDATE M_CostDetail  SET CurrentCostPrice=" + currentcostprice +" , CUMULATEDAMT=" + costdetailcumulatedamt +"  WHERE M_Product_ID=? AND C_AcctSchema_ID=? AND M_InventoryLine_ID=? AND AD_Client_ID=? AND AD_Org_ID=?";
												 pstmt = null;			
													try {
														 pstmt = DB.prepareStatement(sql2, null);
														 pstmt.setInt(1, M_Product_ID);
														 pstmt.setInt(2, C_AcctSchema_ID);
														 pstmt.setInt(3, M_InventoryLine_ID);
														 pstmt.setInt(4, AD_Client_ID);
														 pstmt.setInt(5, AD_Org_ID);
														 int no = pstmt.executeUpdate();
														 log.fine("Lines -> #" + no);
													    } catch (Exception e) {
													        
													    }
										 	}
//											rs.close();
//											pstmt.close();
//											pstmt = null;
										 	}
									 catch (Exception e) {
									        
									    }
												}
								}
						 	
						 	rs.close();
							pstmt.close();
							pstmt = null;
						 	
						 	}
					
								
					     catch (Exception e) {
					        
					    }
								}
						 
						 	
					return "";
				
				
				// end current cost price for m_cost
	}

	/**
	 *  Load Document Details
	 *  @return error message or null
	 */
	protected String loadDocumentDetails()
	{
		/// Code commented by Mukesh as per discussion by Surya @20220411
//		String costupdate = setcost();
		setC_Currency_ID (NO_CURRENCY);
		MInventory inventory = (MInventory)getPO();
		setDateDoc (inventory.getMovementDate());
		setDateAcct(inventory.getMovementDate());
		m_Reversal_ID = inventory.getReversal_ID();//store original (voided/reversed) document
		m_DocStatus = inventory.getDocStatus();
		//	Contained Objects
		p_lines = loadLines(inventory);
		log.fine("Lines=" + p_lines.length);
		return null;
	}   //  loadDocumentDetails

	/**
	 *	Load Invoice Line
	 *	@param inventory inventory
	 *  @return DocLine Array
	 */
	private DocLine[] loadLines(MInventory inventory)
	{
		ArrayList<DocLine> list = new ArrayList<DocLine>();
		MInventoryLine[] lines = inventory.getLines(false);
		for (int i = 0; i < lines.length; i++)
		{
			MInventoryLine line = lines[i];
			//	nothing to post
			if (line.getQtyBook().compareTo(line.getQtyCount()) == 0
				&& line.getQtyInternalUse().signum() == 0)
				continue;
			//
			DocLine docLine = new DocLine (line, this); 
			BigDecimal Qty = line.getQtyInternalUse();
			if (Qty.signum() != 0)
				Qty = Qty.negate();		//	Internal Use entered positive
			else
			{
				BigDecimal QtyBook = line.getQtyBook();
				BigDecimal QtyCount = line.getQtyCount();
				Qty = QtyCount.subtract(QtyBook);
			}
			docLine.setQty (Qty, false);		// -5 => -5
			docLine.setReversalLine_ID(line.getReversalLine_ID());
			log.fine(docLine.toString());
			list.add (docLine);
		}

		//	Return Array
		DocLine[] dls = new DocLine[list.size()];
		list.toArray(dls);
		return dls;
	}	//	loadLines

	/**
	 *  Get Balance
	 *  @return Zero (always balanced)
	 */
	public BigDecimal getBalance()
	{
		BigDecimal retValue = Env.ZERO;
		return retValue;
	}   //  getBalance

	/**
	 *  Create Facts (the accounting logic) for
	 *  MMI.
	 *  <pre>
	 *  Inventory
	 *      Inventory       DR      CR
	 *      InventoryDiff   DR      CR   (or Charge)
	 *  </pre>
	 *  @param as account schema
	 *  @return Fact
	 */
	public ArrayList<Fact> createFacts (MAcctSchema as)
	{
		//  create Fact Header
		Fact fact = new Fact(this, as, Fact.POST_Actual);
		setC_Currency_ID(as.getC_Currency_ID());

		//  Line pointers
		FactLine dr = null;
		FactLine cr = null;

		for (int i = 0; i < p_lines.length; i++)
		{
			DocLine line = p_lines[i];
			// MZ Goodwill
			// if Physical Inventory CostDetail is exist then get Cost from Cost Detail 
			BigDecimal costs = line.getProductCosts(as, line.getAD_Org_ID(), true, "M_InventoryLine_ID=?");
			// end MZ
			if (costs == null || costs.signum() == 0)
			{
				p_Error = "No Costs for " + line.getProduct().getName();
				return null;
			}
			//  Inventory       DR      CR
			dr = fact.createLine(line,
				line.getAccount(ProductCost.ACCTTYPE_P_Asset, as),
				as.getC_Currency_ID(), costs);
			//  may be zero difference - no line created.
			if (dr == null)
				continue;
			dr.setM_Locator_ID(line.getM_Locator_ID());
			if (m_DocStatus.equals(MInventory.DOCSTATUS_Reversed) && m_Reversal_ID !=0 && line.getReversalLine_ID() != 0)
			{
				//	Set AmtAcctDr from Original Phys.Inventory
				if (!dr.updateReverseLine (MInventory.Table_ID, 
						m_Reversal_ID, line.getReversalLine_ID(),Env.ONE))
				{
					p_Error = "Original Physical Inventory not posted yet";
					return null;
				}
			}
			
			//  InventoryDiff   DR      CR
			//	or Charge
			MAccount invDiff = null;
			if (m_DocStatus.equals(MInventory.DOCSTATUS_Reversed)
					&& m_Reversal_ID != 0
					&& line.getReversalLine_ID() != 0
					&& line.getC_Charge_ID() != 0) {
				invDiff = line.getChargeAccount(as, costs);
			} else {
				invDiff = line.getChargeAccount(as, costs.negate());
			}

			if (invDiff == null)
				invDiff = getAccount(Doc.ACCTTYPE_InvDifferences, as);
			cr = fact.createLine(line, invDiff,
				as.getC_Currency_ID(), costs.negate());
			if (cr == null)
				continue;
			cr.setM_Locator_ID(line.getM_Locator_ID());
			cr.setQty(line.getQty().negate());
			if (line.getC_Charge_ID() != 0)	//	explicit overwrite for charge
				cr.setAD_Org_ID(line.getAD_Org_ID());

			if (m_DocStatus.equals(MInventory.DOCSTATUS_Reversed) && m_Reversal_ID !=0 && line.getReversalLine_ID() != 0)
			{
				//	Set AmtAcctCr from Original Phys.Inventory
				if (!cr.updateReverseLine (MInventory.Table_ID, 
						m_Reversal_ID, line.getReversalLine_ID(),Env.ONE))
				{
					p_Error = "Original Physical Inventory not posted yet";
					return null;
				}
				costs = cr.getAcctBalance(); //get original cost
			}

			//	Cost Detail
			 /* Source move to MInventory.createCostDetail()
			MCostDetail.createInventory(as, line.getAD_Org_ID(), 
				line.getM_Product_ID(), line.getM_AttributeSetInstance_ID(), 
				line.get_ID(), 0, 
				costs, line.getQty(), 
				line.getDescription(), getTrxName());*/
		}
		//
		ArrayList<Fact> facts = new ArrayList<Fact>();
		facts.add(fact);
		return facts;
	}   //  createFact

}   //  Doc_Inventory
