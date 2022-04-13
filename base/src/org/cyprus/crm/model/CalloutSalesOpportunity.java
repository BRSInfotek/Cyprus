package org.cyprus.crm.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.cyprus.util.UtilTax;
import org.cyprusbrs.model.CalloutEngine;
import org.cyprusbrs.model.GridField;
import org.cyprusbrs.model.GridTab;
import org.cyprusbrs.model.MBPartner;
import org.cyprusbrs.model.MBPartnerLocation;
import org.cyprusbrs.model.MPriceList;
import org.cyprusbrs.model.MProductPricing;
import org.cyprusbrs.model.MRole;
import org.cyprusbrs.model.MTax;
import org.cyprusbrs.model.MUOMConversion;
import org.cyprusbrs.model.MUser;
import org.cyprusbrs.model.Query;
import org.cyprusbrs.util.DB;
import org.cyprusbrs.util.Env;

public class CalloutSalesOpportunity extends CalloutEngine {

	/**
	 * 
	 * @param ctx
	 * @param WindowNo
	 * @param mTab
	 * @param mField
	 * @param value
	 * @return
	 */
	// Callout path : org.cyprus.crm.model.CalloutSalesOpportunity.leadDetails
	public String leadDetails (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		Integer c_Lead_ID = (Integer) mField.getValue();
		if (c_Lead_ID == null || c_Lead_ID.intValue() == 0)
			return "";
//		List<MLead> listLead = new Query(ctx, MLead.Table_Name, "C_Lead_ID=?", null)
//				.setParameters(c_Lead_ID)
//				.list();
//		MLead lead = listLead.stream().findFirst().get();
		
		MLead lead = new Query(ctx, MLead.Table_Name, "C_Lead_ID=?", null)
				.setParameters(c_Lead_ID)
				.firstOnly();
		
		if(lead!=null)
		{
			mTab.setValue(MSalesOpportunity.COLUMNNAME_Status, lead.getStatus());
			mTab.setValue(MSalesOpportunity.COLUMNNAME_Description, lead.getDescription());
			mTab.setValue(MSalesOpportunity.COLUMNNAME_EnquiryDate, lead.getEnquiryDate());
			if(lead.getBPType().equalsIgnoreCase(MLead.BPTYPE_Customer))
			{	
				mTab.setValue(MSalesOpportunity.COLUMNNAME_C_BPartner_ID, lead.getC_BPartner_ID());
				mTab.setValue(MSalesOpportunity.COLUMNNAME_C_BPartner_Location_ID, lead.getC_BPartner_Location_ID());
				mTab.setValue(MSalesOpportunity.COLUMNNAME_AD_User_ID, lead.getAD_User_ID());
			}
			else
			{
				mTab.setValue(MSalesOpportunity.COLUMNNAME_Ref_BPartner_ID, lead.getRef_BPartner_ID());
				mTab.setValue(MSalesOpportunity.COLUMNNAME_Ref_BPartner_Location_ID, lead.getRef_BPartner_Location_ID());
				mTab.setValue(MSalesOpportunity.COLUMNNAME_Ref_User_ID, lead.getRef_User_ID());
			}
			mTab.setValue(MSalesOpportunity.COLUMNNAME_M_Warehouse_ID, lead.getM_Warehouse_ID());
			mTab.setValue(MSalesOpportunity.COLUMNNAME_M_PriceList_ID, lead.getM_PriceList_ID());
			mTab.setValue(MSalesOpportunity.COLUMNNAME_C_Currency_ID, lead.getC_Currency_ID());
			mTab.setValue(MSalesOpportunity.COLUMNNAME_C_ConversionType_ID, lead.getC_ConversionType_ID());
		}
		return "";
	}
	
	// Callout path : org.cyprus.crm.model.CalloutSalesOpportunity.leadInfoDetails
	public String leadInfoDetails (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		Integer c_LeadInfo_ID = (Integer) mField.getValue();
		if (c_LeadInfo_ID == null || c_LeadInfo_ID.intValue() == 0)
			return "";
//		List<MLeadInfo> listLeadInfo = new Query(ctx, MLeadInfo.Table_Name, "C_LeadInfo_ID=?", null)
//				.setParameters(c_LeadInfo_ID)
//				.list();
//		MLeadInfo leadInfo = listLeadInfo.stream().findFirst().get();
		
		MLeadInfo leadInfo = new Query(ctx, MLeadInfo.Table_Name, "C_LeadInfo_ID=?", null)
				.setParameters(c_LeadInfo_ID)
				.firstOnly();
		
		MTax tax = new Query(ctx, MTax.Table_Name, "IsDefault=?", null)
			.setClient_ID()	
			.setOnlyActiveRecords(true)
			.setParameters("Y")
			.firstOnly();
		
		if(leadInfo!=null)
		{
			mTab.setValue(MOpportunityLine.COLUMNNAME_Description, leadInfo.getDescription());
			if(leadInfo.getM_Product_ID()>0)	
				mTab.setValue(MOpportunityLine.COLUMNNAME_M_Product_ID, leadInfo.getM_Product_ID());
			else	
				mTab.setValue(MOpportunityLine.COLUMNNAME_C_Charge_ID, leadInfo.getC_Charge_ID());
			mTab.setValue(MOpportunityLine.COLUMNNAME_M_AttributeSetInstance_ID, leadInfo.getM_AttributeSetInstance_ID());
			mTab.setValue(MOpportunityLine.COLUMNNAME_PlannedQty, leadInfo.getPlannedQty());

			mTab.setValue(MOpportunityLine.COLUMNNAME_C_UOM_ID, leadInfo.getC_UOM_ID());
			mTab.setValue(MOpportunityLine.COLUMNNAME_BaseQty, leadInfo.getBaseQty());
			mTab.setValue(MOpportunityLine.COLUMNNAME_PlannedPrice, leadInfo.getPlannedPrice());
			mTab.setValue(MOpportunityLine.COLUMNNAME_PriceList, leadInfo.getPriceList());
			mTab.setValue(MOpportunityLine.COLUMNNAME_BasePrice, leadInfo.getBasePrice());
			mTab.setValue(MOpportunityLine.COLUMNNAME_Discount, leadInfo.getDiscount());
			mTab.setValue(MOpportunityLine.COLUMNNAME_LineNetAmt, leadInfo.getLineNetAmt());
			mTab.setValue(MOpportunityLine.COLUMNNAME_LineTotalAmount, leadInfo.getLineNetAmt());

			if(tax!=null)
			{	
				int C_SalesOpportunity_ID = Env.getContextAsInt(ctx, WindowNo, WindowNo, MOpportunityLine.COLUMNNAME_C_SalesOpportunity_ID);
				int p_C_Currency_ID=DB.getSQLValue(null, "Select C_Currency_ID from C_SalesOpportunity Where C_SalesOpportunity_ID=?", C_SalesOpportunity_ID);
				BigDecimal taxAmount=UtilTax.getTaxAmountFromStdPrecision(leadInfo.getLineNetAmt(), tax.getC_Tax_ID(), p_C_Currency_ID);
				mTab.setValue(MOpportunityLine.COLUMNNAME_C_Tax_ID, tax.getC_Tax_ID());
				mTab.setValue(MOpportunityLine.COLUMNNAME_TaxAmt, taxAmount);
				mTab.setValue(MOpportunityLine.COLUMNNAME_LineTotalAmount, leadInfo.getLineNetAmt().add(taxAmount));
			}
			else
			mTab.setValue(MOpportunityLine.COLUMNNAME_C_Tax_ID, null);
		}
		return "";
	}
	
	// Callout path : org.cyprus.crm.model.CalloutSalesOpportunity.bpartnerDetails
	public String bpartnerDetails (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		Integer bpartner_ID = (Integer) mField.getValue();
		if (bpartner_ID == null || bpartner_ID.intValue() == 0)
			return "";
		
		MBPartner partner=new MBPartner(ctx, bpartner_ID, null);
		
		// User
		MUser[] userAll=MUser.getOfBPartner(ctx, bpartner_ID, null);
		List<MUser> listUser=Arrays.asList(userAll);
		MUser user = listUser.stream().findFirst().get();
		
		/// Location
		MBPartnerLocation[] locationAll=MBPartnerLocation.getForBPartner(ctx, bpartner_ID,null); 
		List<MBPartnerLocation> listLocation=Arrays.asList(locationAll);
		MBPartnerLocation location = listLocation.stream().findFirst().get();
		
		if(partner.isProspect())
		{
			mTab.setValue(MSalesOpportunity.COLUMNNAME_Ref_User_ID, user.getAD_User_ID());
			mTab.setValue(MSalesOpportunity.COLUMNNAME_Ref_BPartner_Location_ID, location.getC_BPartner_Location_ID());
		}
		else
		{
			mTab.setValue(MSalesOpportunity.COLUMNNAME_AD_User_ID, user.getAD_User_ID());
			mTab.setValue(MSalesOpportunity.COLUMNNAME_C_BPartner_Location_ID, location.getC_BPartner_Location_ID());
			mTab.setValue(MSalesOpportunity.COLUMNNAME_M_PriceList_ID, partner.getM_PriceList_ID());
		}
		return ""; 	
	}
	
	// Callout path : org.cyprus.crm.model.CalloutSalesOpportunity.taxAmount
		public String taxAmount (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
		{
			Integer tax_ID = (Integer) mField.getValue();
			if (tax_ID == null || tax_ID.intValue() == 0)
				return "";
			int C_SalesOpportunity_ID = Env.getContextAsInt(ctx, WindowNo, WindowNo, MOpportunityLine.COLUMNNAME_C_SalesOpportunity_ID);
			int p_C_Currency_ID=DB.getSQLValue(null, "Select C_Currency_ID from C_SalesOpportunity Where C_SalesOpportunity_ID=?", C_SalesOpportunity_ID);
			BigDecimal lineAmount=(BigDecimal)mTab.getValue(MOpportunityLine.COLUMNNAME_LineNetAmt);
			if(p_C_Currency_ID>0)
			{	
				BigDecimal taxAmount=UtilTax.getTaxAmountFromStdPrecision(lineAmount, tax_ID, p_C_Currency_ID);
				mTab.setValue(MOpportunityLine.COLUMNNAME_TaxAmt, taxAmount);
				mTab.setValue(MOpportunityLine.COLUMNNAME_LineTotalAmount, lineAmount.add(taxAmount));
			}
			return "";
		}
		
//		org.cyprus.crm.model.CalloutSalesOpportunity.amt
		public String amt (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
		{
			if (isCalloutActive() || value == null)
				return "";

//			if (steps) log.warning("init");
			int C_Tax_ID = Env.getContextAsInt(ctx, WindowNo, MOpportunityLine.COLUMNNAME_C_Tax_ID);
			int C_UOM_To_ID = Env.getContextAsInt(ctx, WindowNo, MOpportunityLine.COLUMNNAME_C_UOM_ID);
			int M_Product_ID = Env.getContextAsInt(ctx, WindowNo, MOpportunityLine.COLUMNNAME_M_Product_ID);
			int M_PriceList_ID = Env.getContextAsInt(ctx, WindowNo, "M_PriceList_ID");
			int C_SalesOpportunity_ID=Env.getContextAsInt(ctx, WindowNo, MOpportunityLine.COLUMNNAME_C_SalesOpportunity_ID);
//			M_PriceList_ID=DB.getSQLValue(null, "SELECT M_PriceList_ID from C_LEAD where C_LEAD_ID="+c_lead_id);
			int StdPrecision = MPriceList.getStandardPrecision(ctx, M_PriceList_ID);
			BigDecimal QtyPlanned, QtyBase, PricePlanned, PriceBase, PriceLimit, Discount, PriceList;
			//	get values
			QtyPlanned = (BigDecimal)mTab.getValue(MOpportunityLine.COLUMNNAME_PlannedQty);//"QtyEntered");
			QtyBase = (BigDecimal)mTab.getValue(MOpportunityLine.COLUMNNAME_BaseQty);//"QtyOrdered");
			log.fine("QtyPlanned=" + QtyPlanned + ", QtyBase=" + QtyBase + ", UOM=" + C_UOM_To_ID);
			//
			PricePlanned = (BigDecimal)mTab.getValue(MOpportunityLine.COLUMNNAME_PlannedPrice);//"PricePlanned");
			PriceBase = (BigDecimal)mTab.getValue(MOpportunityLine.COLUMNNAME_BasePrice);//"PriceActual");
			Discount = (BigDecimal)mTab.getValue(MOpportunityLine.COLUMNNAME_Discount);//"Discount");
			PriceLimit =Env.ZERO;// (BigDecimal)mTab.getValue("PriceLimit");
			PriceList = (BigDecimal)mTab.getValue(MOpportunityLine.COLUMNNAME_PriceList);//"PriceList");
//			log.fine("PriceList=" + PriceList + ", Limit=" + PriceLimit + ", Precision=" + StdPrecision);
			log.fine("PricePlanned=" + PricePlanned + ", PriceBase=" + PriceBase + ", Discount=" + Discount);

			//		No Product
			if (M_Product_ID == 0)
			{
				if (mField.getColumnName().equals(MOpportunityLine.COLUMNNAME_PlannedPrice))//"PriceEntered"))
				{
					mTab.setValue(MOpportunityLine.COLUMNNAME_BasePrice, value);
					if (PriceBase.compareTo(Env.ZERO)==0)
						PriceBase = PricePlanned;	
				}
			}
			//	Product Qty changed - recalc price
			else if ( //(mField.getColumnName().equals(MLeadInfo.COLUMNNAME_BaseQty) || //"QtyOrdered") 
				 mField.getColumnName().equals(MOpportunityLine.COLUMNNAME_PlannedQty)//"QtyEntered")
				|| mField.getColumnName().equals(MOpportunityLine.COLUMNNAME_M_Product_ID)) 
//				&& !"N".equals(Env.getContext(ctx, WindowNo, "DiscountSchema")))
			{
				int C_BPartner_ID = Env.getContextAsInt(ctx, WindowNo, "C_BPartner_ID");
				if (mField.getColumnName().equals(MOpportunityLine.COLUMNNAME_PlannedQty))//"QtyEntered"))
					QtyBase = MUOMConversion.convertProductTo (ctx, M_Product_ID, 
						C_UOM_To_ID, QtyPlanned);
				if (QtyBase == null)
					QtyBase = QtyPlanned;
				boolean IsSOTrx = Env.getContext(ctx, WindowNo, "IsSOTrx").equals("Y");
				MProductPricing pp = new MProductPricing (M_Product_ID, C_BPartner_ID, QtyBase, IsSOTrx);
//				pp.setM_PriceList_ID(M_PriceList_ID);
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
				mTab.setValue(MOpportunityLine.COLUMNNAME_BasePrice, pp.getPriceStd());
				mTab.setValue("Discount", pp.getDiscount());
				mTab.setValue(MOpportunityLine.COLUMNNAME_PlannedPrice, PricePlanned);
				Env.setContext(ctx, WindowNo, "DiscountSchema", pp.isDiscountSchema() ? "Y" : "N");
			}
			else if (mField.getColumnName().equals(MOpportunityLine.COLUMNNAME_BasePrice))
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
			else if (mField.getColumnName().equals(MOpportunityLine.COLUMNNAME_PlannedPrice))//"PriceEntered"))
			{
				PricePlanned = (BigDecimal)value;
				PriceBase = MUOMConversion.convertProductTo (ctx, M_Product_ID, 
					C_UOM_To_ID, PricePlanned);
				if (PriceBase == null)
					PriceBase = PricePlanned;
				//
				log.fine("PricePlanned=" + PricePlanned 
					+ " -> PriceBase=" + PriceBase);
				mTab.setValue(MOpportunityLine.COLUMNNAME_BasePrice, PriceBase);
			}
			
			//  Discount entered - Calculate Actual/Entered
			if (mField.getColumnName().equals(MOpportunityLine.COLUMNNAME_Discount))
			{
				if ( PriceList.doubleValue() != 0 )
					PriceBase = new BigDecimal ((100.0 - Discount.doubleValue()) / 100.0 * PriceList.doubleValue());
//				if (PriceActual.scale() > StdPrecision)
//					PriceActual = PriceActual.setScale(StdPrecision, BigDecimal.ROUND_HALF_UP);
				PricePlanned = MUOMConversion.convertProductFrom (ctx, M_Product_ID, 
					C_UOM_To_ID, PriceBase);
				if (PricePlanned == null)
					PricePlanned = PriceBase;
				mTab.setValue(MOpportunityLine.COLUMNNAME_BasePrice, PriceBase);
				mTab.setValue(MOpportunityLine.COLUMNNAME_PlannedPrice, PricePlanned);
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
				mTab.setValue (MOpportunityLine.COLUMNNAME_BasePrice, PriceLimit);
				mTab.setValue (MOpportunityLine.COLUMNNAME_PlannedPrice, PricePlanned);
				mTab.fireDataStatusEEvent ("UnderLimitPrice", "", false);
				//	Repeat Discount calc
				if (PriceList.intValue() != 0)
				{
					Discount = new BigDecimal ((PriceList.doubleValue () - PriceBase.doubleValue ()) / PriceList.doubleValue () * 100.0);
					if (Discount.scale () > 2)
						Discount = Discount.setScale (2, BigDecimal.ROUND_HALF_UP);
					mTab.setValue (MOpportunityLine.COLUMNNAME_Discount, Discount);
				}
			}

			//	Line Net Amt
			BigDecimal LineNetAmt = QtyBase.multiply(PriceBase);
			if (LineNetAmt.scale() > StdPrecision)
				LineNetAmt = LineNetAmt.setScale(StdPrecision, BigDecimal.ROUND_HALF_UP);
			log.info("LineNetAmt=" + LineNetAmt);
			mTab.setValue("LineNetAmt", LineNetAmt);
			//
			System.out.println("C_Tax_ID :: "+C_Tax_ID);
			if(C_Tax_ID>0)
			{
				int p_C_Currency_ID=DB.getSQLValue(null, "Select C_Currency_ID from C_SalesOpportunity Where C_SalesOpportunity_ID=?", C_SalesOpportunity_ID);
				BigDecimal taxAmount=UtilTax.getTaxAmountFromStdPrecision(LineNetAmt, C_Tax_ID, p_C_Currency_ID);
				mTab.setValue(MOpportunityLine.COLUMNNAME_TaxAmt, taxAmount);
				mTab.setValue(MOpportunityLine.COLUMNNAME_LineTotalAmount, LineNetAmt.add(taxAmount));
			}
			
			return "";
		}	//	amt
		
		
}
