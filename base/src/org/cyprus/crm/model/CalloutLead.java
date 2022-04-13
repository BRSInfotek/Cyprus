/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2006 cyprusbrs, Inc. All Rights Reserved.                *
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
 * cyprusbrs, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@cyprusbrs.org or http://www.cyprusbrs.org/license.html           *
 *****************************************************************************/
package org.cyprus.crm.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.cyprusbrs.model.CalloutEngine;
import org.cyprusbrs.model.GridField;
import org.cyprusbrs.model.GridTab;
import org.cyprusbrs.model.MBPartnerLocation;
import org.cyprusbrs.model.MCharge;
import org.cyprusbrs.model.MPriceList;
import org.cyprusbrs.model.MProduct;
import org.cyprusbrs.model.MProductPrice;
import org.cyprusbrs.model.MProductPricing;
import org.cyprusbrs.model.MQuery;
import org.cyprusbrs.model.MRole;
import org.cyprusbrs.model.MUOM;
import org.cyprusbrs.model.MUOMConversion;
import org.cyprusbrs.model.MUser;
import org.cyprusbrs.model.Query;
import org.cyprusbrs.util.DB;
import org.cyprusbrs.util.Env;

/**
 *	Lead Callouts.
 *	
 *  @author Mukesh Vishwakarma - CyprusERP
 */
/**
 * 
 * @author Mukesh
 *
 */
public class CalloutLead extends CalloutEngine
{
	
	/**
	 * @author Mukesh : @20211224
	 * @param ctx
	 * @param WindowNo
	 * @param mTab
	 * @param mField
	 * @param value
	 * @return
	 */
	
	// Callout path : org.cyprus.crm.model.CalloutLead.leadResponseDetails
	public String leadResponseDetails (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		Integer leadResponseMaster_ID = (Integer) mField.getValue();
		if (leadResponseMaster_ID == null || leadResponseMaster_ID.intValue() == 0)
			return "";
		String responseDesc=DB.getSQLValueStringEx(null, "select DESCRIPTION from C_LeadResponseMaster where C_LeadResponseMaster_ID=? and AD_Client_Id=?", 
				new Object[] {leadResponseMaster_ID,Env.getAD_Client_ID(Env.getCtx())});
		mTab.setValue(X_C_Lead.COLUMNNAME_LeadResponseDetails, responseDesc);
		
		return "";
	}
	
	/**
	 * @author Mukesh : @20211224
	 * @param ctx
	 * @param WindowNo
	 * @param mTab
	 * @param mField
	 * @param value
	 * @return
	 */
	// Callout path : org.cyprus.crm.model.CalloutLead.userDetails
	public String userDetails (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		Integer adUser_ID = (Integer) mField.getValue();
		if (adUser_ID == null || adUser_ID.intValue() == 0)
			return "";
		updateLeadInfo(adUser_ID,mTab);
		return "";
	}
	
	/**
	 * @author Mukesh CyprusERP	@20211224
	 * @param ctx
	 * @param WindowNo
	 * @param mTab
	 * @param mField
	 * @param value
	 * @return
	 */
	// Callout path : org.cyprus.crm.model.CalloutLead.bpartnerDetails
	public String bpartnerDetails (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		Integer bpartner_ID = (Integer) mField.getValue();
		if (bpartner_ID == null || bpartner_ID.intValue() == 0)
			return "";
		
		// User
		MUser[] userAll=MUser.getOfBPartner(ctx, bpartner_ID, null);
		List<MUser> listUser=Arrays.asList(userAll);
		MUser user = listUser.stream().findFirst().get();
		mTab.setValue(MLead.COLUMNNAME_AD_User_ID, user.getAD_User_ID());
		
		/// Location
		MBPartnerLocation[] locationAll=MBPartnerLocation.getForBPartner(ctx, bpartner_ID,null); 
		List<MBPartnerLocation> listLocation=Arrays.asList(locationAll);
		MBPartnerLocation location = listLocation.stream().findFirst().get();
		mTab.setValue(MLead.COLUMNNAME_C_BPartner_Location_ID, location.getC_BPartner_Location_ID());
		
		/// update lead details from user window
		updateLeadInfo(user.getAD_User_ID(), mTab);
		return ""; 
		
	}
	/**
	 * @author Mukesh CyprusERP @20211224
	 * @param ctx
	 * @param WindowNo
	 * @param mTab
	 * @param mField
	 * @param value
	 * @return
	 */
	// Callout path : org.cyprus.crm.model.CalloutLead.prospectDetails
	public String prospectDetails (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		Integer bpartner_ID = (Integer) mField.getValue();
		if (bpartner_ID == null || bpartner_ID.intValue() == 0)
			return "";
		
		// User
		MUser[] userAll=MUser.getOfBPartner(ctx, bpartner_ID, null);
		List<MUser> listUser=Arrays.asList(userAll);
		MUser user = listUser.stream().findFirst().get();
		mTab.setValue(X_C_Lead.COLUMNNAME_Ref_User_ID, user.getAD_User_ID());
		
		/// Location
		MBPartnerLocation[] locationAll=MBPartnerLocation.getForBPartner(ctx, bpartner_ID,null); 
		List<MBPartnerLocation> listLocation=Arrays.asList(locationAll);
		MBPartnerLocation location = listLocation.stream().findFirst().get();
		mTab.setValue(MLead.COLUMNNAME_Ref_BPartner_Location_ID, location.getC_BPartner_Location_ID());
		
		/// update lead details from user window
		updateLeadInfo(user.getAD_User_ID(), mTab);
		return ""; 
	}
	
	/**
	 * 
	 * @param adUser_ID @20211224
	 * @param mTab
	 */
	private void updateLeadInfo(Integer adUser_ID, GridTab mTab)
	{
		MQuery whereClause=new MQuery(MUser.Table_Name);
		whereClause.addRestriction(MUser.COLUMNNAME_AD_User_ID, MQuery.EQUAL, adUser_ID);
		List<MUser> list = new Query(Env.getCtx(), MUser.Table_Name, whereClause.getWhereClause(), null)
		.list();
		list.forEach(user->
		{
			mTab.setValue(MLead.COLUMNNAME_ContactName, user.getName());
			mTab.setValue(MLead.COLUMNNAME_Title, user.getTitle());
			mTab.setValue(MLead.COLUMNNAME_C_Job_ID, user.getC_Job_ID());
			mTab.setValue(MLead.COLUMNNAME_C_Greeting_ID, user.getC_Greeting_ID());
			mTab.setValue(MLead.COLUMNNAME_Phone, user.getPhone());
			mTab.setValue(MLead.COLUMNNAME_Phone2, user.getPhone2());
			mTab.setValue(MLead.COLUMNNAME_Fax, user.getFax());
			mTab.setValue(MLead.COLUMNNAME_EMail, user.getEMail());
			mTab.setValue(MLead.COLUMNNAME_Mobile, user.getMobile());
		});
	}

	/**
	 *	Order Line - Charge.
	 * 		- updates PriceActual from Charge
	 * 		- sets PriceLimit, PriceList to zero
	 * 	Calles tax
	 *  @param ctx context
	 *  @param WindowNo current Window No
	 *  @param mTab Grid Tab
	 *  @param mField Grid Field
	 *  @param value New Value
	 *  @return null or error message
	 */
	// Callout path : org.cyprus.crm.model.CalloutLead.charge
	public String charge (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		Integer C_Charge_ID = (Integer)value;
		if (C_Charge_ID == null || C_Charge_ID.intValue() == 0)
			return "";
		//	No Product defined
		if (mTab.getValue(MLeadInfo.COLUMNNAME_M_Product_ID) != null)
		{
			mTab.setValue(MLeadInfo.COLUMNNAME_C_Charge_ID, null);
			return "ChargeExclusively";
		}
		mTab.setValue(MLeadInfo.COLUMNNAME_M_AttributeSetInstance_ID, null);
		mTab.setValue(MLeadInfo.COLUMNNAME_C_UOM_ID, new Integer(100));	//	EA
		
		Env.setContext(ctx, WindowNo, "DiscountSchema", "N");
		String sql = "SELECT ChargeAmt, Description FROM C_Charge WHERE C_Charge_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, C_Charge_ID.intValue());
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				BigDecimal chargeAmt=rs.getBigDecimal (MCharge.COLUMNNAME_ChargeAmt);
				String description=rs.getString (MCharge.COLUMNNAME_Description);
				mTab.setValue (MLeadInfo.COLUMNNAME_Description, description);
				mTab.setValue (MLeadInfo.COLUMNNAME_PlannedPrice, chargeAmt);
				mTab.setValue (MLeadInfo.COLUMNNAME_BasePrice, chargeAmt);
				mTab.setValue (MLeadInfo.COLUMNNAME_PriceList, chargeAmt);
				mTab.setValue (MLeadInfo.COLUMNNAME_LineNetAmt, chargeAmt);
				mTab.setValue (MLeadInfo.COLUMNNAME_PlannedQty, Env.ONE);
				mTab.setValue (MLeadInfo.COLUMNNAME_BaseQty, Env.ONE);
				mTab.setValue (MLeadInfo.COLUMNNAME_Discount, Env.ZERO);
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
			return e.getLocalizedMessage();
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}

		return "";
	}	//	charge
	


	/**
	 *	Order Line - Amount.
	 *		- called from QtyOrdered, Discount and PriceActual
	 *		- calculates Discount or Actual Amount
	 *		- calculates LineNetAmt
	 *		- enforces PriceLimit
	 *  @param ctx context
	 *  @param WindowNo current Window No
	 *  @param mTab Grid Tab
	 *  @param mField Grid Field
	 *  @param value New Value
	 *  @return null or error message
	 */
//	org.cyprus.crm.model.CalloutLead.amt
	public String amt (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive() || value == null)
			return "";

//		if (steps) log.warning("init");
		int C_UOM_To_ID = Env.getContextAsInt(ctx, WindowNo, MLeadInfo.COLUMNNAME_C_UOM_ID);
		int M_Product_ID = Env.getContextAsInt(ctx, WindowNo, MLeadInfo.COLUMNNAME_M_Product_ID);
		int M_PriceList_ID = Env.getContextAsInt(ctx, WindowNo, "M_PriceList_ID");
		int c_lead_id=Env.getContextAsInt(ctx, WindowNo, MLeadInfo.COLUMNNAME_C_Lead_ID);
//		M_PriceList_ID=DB.getSQLValue(null, "SELECT M_PriceList_ID from C_LEAD where C_LEAD_ID="+c_lead_id);
		int StdPrecision = MPriceList.getStandardPrecision(ctx, M_PriceList_ID);
		BigDecimal QtyPlanned, QtyBase, PricePlanned, PriceBase, PriceLimit, Discount, PriceList;
		//	get values
		QtyPlanned = (BigDecimal)mTab.getValue(MLeadInfo.COLUMNNAME_PlannedQty);//"QtyEntered");
		QtyBase = (BigDecimal)mTab.getValue(MLeadInfo.COLUMNNAME_BaseQty);//"QtyOrdered");
		log.fine("QtyPlanned=" + QtyPlanned + ", QtyBase=" + QtyBase + ", UOM=" + C_UOM_To_ID);
		//
		PricePlanned = (BigDecimal)mTab.getValue(MLeadInfo.COLUMNNAME_PlannedPrice);//"PricePlanned");
		PriceBase = (BigDecimal)mTab.getValue(MLeadInfo.COLUMNNAME_BasePrice);//"PriceActual");
		Discount = (BigDecimal)mTab.getValue(MLeadInfo.COLUMNNAME_Discount);//"Discount");
		PriceLimit =Env.ZERO;// (BigDecimal)mTab.getValue("PriceLimit");
		PriceList = (BigDecimal)mTab.getValue(MLeadInfo.COLUMNNAME_PriceList);//"PriceList");
//		log.fine("PriceList=" + PriceList + ", Limit=" + PriceLimit + ", Precision=" + StdPrecision);
		log.fine("PricePlanned=" + PricePlanned + ", PriceBase=" + PriceBase + ", Discount=" + Discount);

		//		No Product
		if (M_Product_ID == 0)
		{
			// if price change sync price actual and entered
			// else ignore
			if (mField.getColumnName().equals(MLeadInfo.COLUMNNAME_BasePrice))//"PriceActual"))
			{
				mTab.setValue(MLeadInfo.COLUMNNAME_PlannedPrice, value);
			}
			else if (mField.getColumnName().equals(MLeadInfo.COLUMNNAME_PlannedPrice))//"PriceEntered"))
			{
				mTab.setValue(MLeadInfo.COLUMNNAME_BasePrice, value);
			}
		}
		//	Product Qty changed - recalc price
		else if ( //(mField.getColumnName().equals(MLeadInfo.COLUMNNAME_BaseQty) || //"QtyOrdered") 
			 mField.getColumnName().equals(MLeadInfo.COLUMNNAME_PlannedQty)//"QtyEntered")
			|| mField.getColumnName().equals(MLeadInfo.COLUMNNAME_M_Product_ID)) 
//			&& !"N".equals(Env.getContext(ctx, WindowNo, "DiscountSchema")))
		{
			int C_BPartner_ID = Env.getContextAsInt(ctx, WindowNo, "C_BPartner_ID");
			if (mField.getColumnName().equals(MLeadInfo.COLUMNNAME_PlannedQty))//"QtyEntered"))
				QtyBase = MUOMConversion.convertProductTo (ctx, M_Product_ID, 
					C_UOM_To_ID, QtyPlanned);
			if (QtyBase == null)
				QtyBase = QtyPlanned;
			boolean IsSOTrx = Env.getContext(ctx, WindowNo, "IsSOTrx").equals("Y");
			MProductPricing pp = new MProductPricing (M_Product_ID, C_BPartner_ID, QtyBase, IsSOTrx);
//			pp.setM_PriceList_ID(M_PriceList_ID);
			int M_PriceList_Version_ID = Env.getContextAsInt(ctx, WindowNo, "M_PriceList_Version_ID");
			pp.setM_PriceList_Version_ID(M_PriceList_Version_ID);
			Timestamp date = (Timestamp)mTab.getValue("DateOrdered");
			pp.setPriceDate(date);
			//
			PricePlanned = MUOMConversion.convertProductFrom (ctx, M_Product_ID, 
				C_UOM_To_ID, pp.getPriceStd());
			if (PricePlanned == null)
				PricePlanned = pp.getPriceStd();
			//
			log.fine("QtyChanged -> PriceActual=" + pp.getPriceStd() 
				+ ", PriceEntered=" + PricePlanned + ", Discount=" + pp.getDiscount());
			mTab.setValue(MLeadInfo.COLUMNNAME_BasePrice, pp.getPriceStd());
			mTab.setValue("Discount", pp.getDiscount());
			mTab.setValue(MLeadInfo.COLUMNNAME_PlannedPrice, PricePlanned);
			Env.setContext(ctx, WindowNo, "DiscountSchema", pp.isDiscountSchema() ? "Y" : "N");
		}
		else if (mField.getColumnName().equals(MLeadInfo.COLUMNNAME_BasePrice))
		{
			PriceBase = (BigDecimal)value;
			PricePlanned = MUOMConversion.convertProductFrom (ctx, M_Product_ID, 
				C_UOM_To_ID, PricePlanned);
			if (PricePlanned == null)
				PricePlanned = PriceBase;
			//
			log.fine("PriceActual=" + PriceBase 
				+ " -> PriceEntered=" + PricePlanned);
			mTab.setValue("PriceEntered", PricePlanned);
		}
		else if (mField.getColumnName().equals(MLeadInfo.COLUMNNAME_PlannedPrice))//"PriceEntered"))
		{
			PricePlanned = (BigDecimal)value;
			PriceBase = MUOMConversion.convertProductTo (ctx, M_Product_ID, 
				C_UOM_To_ID, PricePlanned);
			if (PriceBase == null)
				PriceBase = PricePlanned;
			//
			log.fine("PricePlanned=" + PricePlanned 
				+ " -> PriceBase=" + PriceBase);
			mTab.setValue(MLeadInfo.COLUMNNAME_BasePrice, PriceBase);
		}
		
		//  Discount entered - Calculate Actual/Entered
		if (mField.getColumnName().equals(MLeadInfo.COLUMNNAME_Discount))
		{
			if ( PriceList.doubleValue() != 0 )
				PriceBase = new BigDecimal ((100.0 - Discount.doubleValue()) / 100.0 * PriceList.doubleValue());
//			if (PriceActual.scale() > StdPrecision)
//				PriceActual = PriceActual.setScale(StdPrecision, BigDecimal.ROUND_HALF_UP);
			PricePlanned = MUOMConversion.convertProductFrom (ctx, M_Product_ID, 
				C_UOM_To_ID, PriceBase);
			if (PricePlanned == null)
				PricePlanned = PriceBase;
			mTab.setValue(MLeadInfo.COLUMNNAME_BasePrice, PriceBase);
			mTab.setValue(MLeadInfo.COLUMNNAME_PlannedPrice, PricePlanned);
		}
		//	calculate Discount
		else
		{
			if (PriceList.intValue() == 0)
				Discount = Env.ZERO;
			else
				Discount = new BigDecimal ((PriceList.doubleValue() - PriceBase.doubleValue()) / PriceList.doubleValue() * 100.0);
			if (Discount.scale() > 2)
				Discount = Discount.setScale(2, BigDecimal.ROUND_HALF_UP);
			mTab.setValue("Discount", Discount);
		}
		log.fine("PriceEntered=" + PricePlanned + ", Actual=" + PriceBase + ", Discount=" + Discount);

		//	Check PriceLimit
		String epl = Env.getContext(ctx, WindowNo, "EnforcePriceLimit");
		boolean enforce = Env.isSOTrx(ctx, WindowNo) && epl != null && epl.equals("Y");
		if (enforce && MRole.getDefault().isOverwritePriceLimit())
			enforce = false;
		//	Check Price Limit?
		if (enforce && PriceLimit.doubleValue() != 0.0
		  && PriceBase.compareTo(PriceLimit) < 0)
		{
			PriceBase = PriceLimit;
			PricePlanned = MUOMConversion.convertProductFrom (ctx, M_Product_ID, 
				C_UOM_To_ID, PriceLimit);
			if (PricePlanned == null)
				PricePlanned = PriceLimit;
			log.fine("(under) PriceEntered=" + PricePlanned + ", Actual" + PriceLimit);
			mTab.setValue (MLeadInfo.COLUMNNAME_BasePrice, PriceLimit);
			mTab.setValue (MLeadInfo.COLUMNNAME_PlannedPrice, PricePlanned);
			mTab.fireDataStatusEEvent ("UnderLimitPrice", "", false);
			//	Repeat Discount calc
			if (PriceList.intValue() != 0)
			{
				Discount = new BigDecimal ((PriceList.doubleValue () - PriceBase.doubleValue ()) / PriceList.doubleValue () * 100.0);
				if (Discount.scale () > 2)
					Discount = Discount.setScale (2, BigDecimal.ROUND_HALF_UP);
				mTab.setValue ("Discount", Discount);
			}
		}

		//	Line Net Amt
		BigDecimal LineNetAmt = QtyBase.multiply(PriceBase);
		if (LineNetAmt.scale() > StdPrecision)
			LineNetAmt = LineNetAmt.setScale(StdPrecision, BigDecimal.ROUND_HALF_UP);
		log.info("LineNetAmt=" + LineNetAmt);
		mTab.setValue("LineNetAmt", LineNetAmt);
		//
		return "";
	}	//	amt

	/**
	 *	Order Line - Quantity.
	 *		- called from C_UOM_ID, QtyEntered, QtyOrdered
	 *		- enforces qty UOM relationship
	 *  @param ctx context
	 *  @param WindowNo current Window No
	 *  @param mTab Grid Tab
	 *  @param mField Grid Field
	 *  @param value New Value
	 *  @return null or error message
	 *  PlannedQty :=> QtyEntered... Done MLeadInfo.COLUMNNAME_PlannedQty.... Done
	 *  BaseQty :=> QtyOrdered... Done ::  MLeadInfo.COLUMNNAME_BaseQty.... Done
	 *  PricePlanned :=>  PriceEntered... Done :: MLeadInfo.COLUMNNAME_PlannedPrice... Done
	 *  BasePrice :=> PriceActual... Done :: MLeadInfo.COLUMNNAME_BasePrice
	 */
//	org.cyprus.crm.model.CalloutLead.qty;
	public String qty (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive() || value == null)
			return "";
		int M_Product_ID = Env.getContextAsInt(ctx, WindowNo, MLeadInfo.COLUMNNAME_M_Product_ID);
//		if (steps) log.warning("init - M_Product_ID=" + M_Product_ID + " - " );
		BigDecimal BaseQty = Env.ZERO;
//		BigDecimal QtyEntered, PriceActual, PriceEntered;
		
		BigDecimal PlannedQty, BasePrice, PricePlanned;
		
		//	No Product
		if (M_Product_ID == 0)
		{
			PlannedQty = (BigDecimal)mTab.getValue(MLeadInfo.COLUMNNAME_PlannedQty);
			BaseQty = PlannedQty;
			mTab.setValue(MLeadInfo.COLUMNNAME_BaseQty, BaseQty);
		}
		//	UOM Changed - convert from Entered -> Product
		else if (mField.getColumnName().equals(MLeadInfo.COLUMNNAME_C_UOM_ID))
		{
			int C_UOM_To_ID = ((Integer)value).intValue();
			PlannedQty = (BigDecimal)mTab.getValue(MLeadInfo.COLUMNNAME_PlannedQty);
			BigDecimal PlannedQty1 = PlannedQty.setScale(MUOM.getPrecision(ctx, C_UOM_To_ID), BigDecimal.ROUND_HALF_UP);
			if (PlannedQty.compareTo(PlannedQty1) != 0)
			{
				log.fine("Corrected PlannedQty Scale UOM=" + C_UOM_To_ID 
					+ "; PlannedQty=" + PlannedQty + "->" + PlannedQty1);  
				PlannedQty = PlannedQty1;
				mTab.setValue(MLeadInfo.COLUMNNAME_PlannedQty, PlannedQty);
			}
			BaseQty = MUOMConversion.convertProductFrom (ctx, M_Product_ID, 
				C_UOM_To_ID, PlannedQty);
			if (BaseQty == null)
				BaseQty = PlannedQty;
			boolean conversion = PlannedQty.compareTo(BaseQty) != 0;
			BasePrice = (BigDecimal)mTab.getValue(MLeadInfo.COLUMNNAME_BasePrice);
			PricePlanned = MUOMConversion.convertProductFrom (ctx, M_Product_ID, 
				C_UOM_To_ID, BasePrice);
			if (PricePlanned == null)
				PricePlanned = BasePrice; 
			log.fine("UOM=" + C_UOM_To_ID 
				+ ", PlannedQty/BasePrice=" + PlannedQty + "/" + BasePrice
				+ " -> " + conversion 
				+ " BaseQty/PricePlanned=" + BaseQty + "/" + PricePlanned);
			Env.setContext(ctx, WindowNo, "UOMConversion", conversion ? "Y" : "N");
			mTab.setValue(MLeadInfo.COLUMNNAME_BaseQty, BaseQty);
			mTab.setValue(MLeadInfo.COLUMNNAME_PlannedPrice, PricePlanned);
		}
		//	PlannedQty changed - calculate BaseQty
		else if (mField.getColumnName().equals(MLeadInfo.COLUMNNAME_PlannedQty))
		{
			int C_UOM_To_ID = Env.getContextAsInt(ctx, WindowNo, "C_UOM_ID");
			PlannedQty = (BigDecimal)value;
			BigDecimal PlannedQty1 = PlannedQty.setScale(MUOM.getPrecision(ctx, C_UOM_To_ID), BigDecimal.ROUND_HALF_UP);
			if (PlannedQty.compareTo(PlannedQty1) != 0)
			{
				log.fine("Corrected PlannedQty Scale UOM=" + C_UOM_To_ID 
					+ "; PlannedQty=" + PlannedQty + "->" + PlannedQty1);  
				PlannedQty = PlannedQty1;
				mTab.setValue(MLeadInfo.COLUMNNAME_PlannedQty, PlannedQty);
			}
			BaseQty = MUOMConversion.convertProductFrom (ctx, M_Product_ID, 
				C_UOM_To_ID, PlannedQty);
			if (BaseQty == null)
				BaseQty = PlannedQty;
			boolean conversion = PlannedQty.compareTo(BaseQty) != 0;
			log.fine("UOM=" + C_UOM_To_ID 
				+ ", PlannedQty=" + PlannedQty
				+ " -> " + conversion 
				+ " BaseQty=" + BaseQty);
			Env.setContext(ctx, WindowNo, "UOMConversion", conversion ? "Y" : "N");
			mTab.setValue(MLeadInfo.COLUMNNAME_BaseQty, BaseQty);
		}
		//	BaseQty changed - calculate PlannedQty (should not happen)
		else if (mField.getColumnName().equals(MLeadInfo.COLUMNNAME_BaseQty))
		{
			int C_UOM_To_ID = Env.getContextAsInt(ctx, WindowNo, MLeadInfo.COLUMNNAME_C_UOM_ID);
			BaseQty = (BigDecimal)value;
			int precision = MProduct.get(ctx, M_Product_ID).getUOMPrecision(); 
			BigDecimal BaseQty1 = BaseQty.setScale(precision, BigDecimal.ROUND_HALF_UP);
			if (BaseQty.compareTo(BaseQty1) != 0)
			{
				log.fine("Corrected BaseQty Scale " 
					+ BaseQty + "->" + BaseQty1);  
				BaseQty = BaseQty1;
				mTab.setValue(MLeadInfo.COLUMNNAME_BaseQty, BaseQty);
			}
			PlannedQty = MUOMConversion.convertProductTo (ctx, M_Product_ID, 
				C_UOM_To_ID, BaseQty);
			if (PlannedQty == null)
				PlannedQty = BaseQty;
			boolean conversion = BaseQty.compareTo(PlannedQty) != 0;
			log.fine("UOM=" + C_UOM_To_ID 
				+ ", BaseQty=" + BaseQty
				+ " -> " + conversion 
				+ " PlannedQty=" + PlannedQty);
			Env.setContext(ctx, WindowNo, "UOMConversion", conversion ? "Y" : "N");
			mTab.setValue(MLeadInfo.COLUMNNAME_PlannedQty, PlannedQty);
		}
		else
		{
			PlannedQty = (BigDecimal)mTab.getValue(MLeadInfo.COLUMNNAME_PlannedQty);
			BaseQty = (BigDecimal)mTab.getValue(MLeadInfo.COLUMNNAME_BaseQty);
		}
		
		//
		return "";
	}	//	qty
	
	/**
	 * Get All details from Product window for Lead Info window
	 * @param ctx
	 * @param WindowNo
	 * @param mTab
	 * @param mField
	 * @param value
	 * @return
	 */
	//org.cyprus.crm.model.CalloutLead.product;
	public String product (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		Integer M_Product_ID = (Integer)value;
		if (M_Product_ID == null || M_Product_ID.intValue() == 0)
			return "";
		//
		mTab.setValue("C_Charge_ID", null);
		//	Set Attribute
		if (Env.getContextAsInt(ctx, WindowNo, Env.TAB_INFO, "M_Product_ID") == M_Product_ID.intValue()
			&& Env.getContextAsInt(ctx, WindowNo, Env.TAB_INFO, "M_AttributeSetInstance_ID") != 0)
			mTab.setValue("M_AttributeSetInstance_ID", Env.getContextAsInt(ctx, WindowNo, Env.TAB_INFO, "M_AttributeSetInstance_ID"));
		else
			mTab.setValue("M_AttributeSetInstance_ID", null);
			
		/*****	Price Calculation see also qty	****/
		MProduct prod=new MProduct(ctx, M_Product_ID, null);
		int M_PriceList_ID = Env.getContextAsInt(ctx, WindowNo, "M_PriceList_ID");
		int M_PriceList_Version_ID=DB.getSQLValue(null, "select  MAX(M_PriceList_Version_ID) from M_PriceList_Version where M_PriceList_ID="+M_PriceList_ID);
		mTab.setValue(MLeadInfo.COLUMNNAME_BaseQty, Env.ONE);
		mTab.setValue(MLeadInfo.COLUMNNAME_PlannedQty, Env.ONE);
		mTab.setValue(MLeadInfo.COLUMNNAME_C_UOM_ID, prod.getC_UOM_ID());
		if(M_PriceList_Version_ID>0)
		{	
			MProductPrice mpp=MProductPrice.get(ctx, M_PriceList_Version_ID, M_Product_ID, null);
			if(mpp!=null)
			{		
				BigDecimal Discount=Env.ZERO;
				BigDecimal PriceStd=mpp.getPriceStd()!=null?mpp.getPriceStd():Env.ZERO;
				mTab.setValue(MLeadInfo.COLUMNNAME_PlannedPrice, PriceStd);
				mTab.setValue(MLeadInfo.COLUMNNAME_PriceList, mpp.getPriceList()!=null?mpp.getPriceList():Env.ZERO);
				mTab.setValue(MLeadInfo.COLUMNNAME_BasePrice, PriceStd);
				mTab.setValue(MLeadInfo.COLUMNNAME_LineNetAmt, PriceStd);

				if(mpp.getPriceList().compareTo(Env.ZERO)>0)
				{
					Discount = new BigDecimal ((mpp.getPriceList().doubleValue() - mpp.getPriceList().doubleValue()) / mpp.getPriceList().doubleValue() * 100.0);
					if (Discount.scale() > 2)
						Discount = Discount.setScale(2, BigDecimal.ROUND_HALF_UP);
					mTab.setValue(MLeadInfo.COLUMNNAME_Discount, Discount);
				}
			}
		}
		//
		return "";
	}	//	product
	
}	//	CalloutBPartnerLocation