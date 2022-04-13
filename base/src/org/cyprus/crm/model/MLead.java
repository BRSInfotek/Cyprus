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

import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;

import org.cyprus.exceptions.AdempiereException;
import org.cyprusbrs.model.MBPartner;
import org.cyprusbrs.model.MBPartnerLocation;
import org.cyprusbrs.model.MLocation;
import org.cyprusbrs.model.MUser;
import org.cyprusbrs.model.Query;

/**
 * 	Bank Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MBank.java,v 1.2 2006/07/30 00:51:05 jjanke Exp $
 */
public class MLead extends X_C_Lead
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3459010882027283811L;
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_Bank_ID bank
	 *	@param trxName trx
	 */
	public MLead (Properties ctx, int C_Lead_ID, String trxName)
	{
		super (ctx, C_Lead_ID, trxName);
	}	//	MBank

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName trx
	 */
	public MLead (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MLead

	
	/**
	 * 	Called before Save for Pre-Save Operation
	 * 	@param newRecord new record
	 *	@return true if record can be saved
	 */
	@Override
	protected boolean beforeSave(boolean newRecord)
	{
		System.out.println("Before Save "+toString());
		if(!newRecord)
		{	
			
			List<MLead> list = new Query(getCtx(), I_C_Lead.Table_Name, "C_Lead_ID=?", get_TrxName())
			.setParameters(get_ID())
			.list();
			MLead lead = list.stream().findFirst().get();
			/// Create Lead History
			MLeadHistory history=MLeadHistory.createLeadHistory(lead);
			
			if(lead.getDocumentNo()!=null && lead.getDocumentNo().compareTo(getDocumentNo())!=0)
				history.setDocumentNo(lead.getDocumentNo());
			
			if(lead.getC_LeadSource_ID()>0 && lead.getC_LeadSource_ID()!=getC_LeadSource_ID())
				history.setC_LeadSource_ID(lead.getC_LeadSource_ID());
			if(lead.getC_SalesRegion_ID()>0 && lead.getC_SalesRegion_ID()!=getC_SalesRegion_ID())
				history.setC_SalesRegion_ID(lead.getC_SalesRegion_ID());
			if(lead.getSalesRep_ID()>0 && lead.getSalesRep_ID()!=getSalesRep_ID())
				history.setSalesRep_ID(lead.getSalesRep_ID());
			if(lead.getName()!=null && lead.getName().compareTo(getName())!=0)
				history.setName(lead.getName());
			
			if(lead.getDescription()!=null  && lead.getDescription().compareTo(getDescription())!=0)
				history.setDescription(lead.getDescription());
			
			if(lead.getEnquiryDate()!=null && lead.getEnquiryDate().compareTo(getEnquiryDate())!=0)
				history.setEnquiryDate(lead.getEnquiryDate());
			if(lead.getFollowupDate()!=null && lead.getFollowupDate().compareTo(getFollowupDate())!=0)
				history.setFollowupDate(lead.getFollowupDate());
			
			if(lead.getLeadRating()!=null && lead.getLeadRating().compareTo(getLeadRating())!=0)
				history.setLeadRating(getLeadRating());
			
			if(lead.getC_LeadQualification_ID()>0 && lead.getC_LeadQualification_ID()!=getC_LeadQualification_ID())
				history.setC_LeadQualification_ID(lead.getC_LeadQualification_ID());
			if(lead.getStatus()!=null && lead.getStatus().compareTo(getStatus())!=0)
				history.setStatus(getStatus());
			
			if(lead.getC_Campaign_ID()>0 && lead.getC_Campaign_ID()!=getC_Campaign_ID())
				history.setC_Campaign_ID(lead.getC_Campaign_ID());
			if(lead.getSummary()!=null && lead.getSummary().compareTo(getSummary())!=0)
				history.setSummary(lead.getSummary());
			if(lead.getHelp()!=null && lead.getHelp().compareTo(getHelp())!=0)
				history.setHelp(lead.getHelp());
			if(lead.getBPType()!=null && lead.getBPType().compareTo(getBPType())!=0)
				history.setBPType(lead.getBPType());
			if(lead.getURL()!=null && lead.getURL().compareTo(getURL())!=0)
				history.setURL(lead.getURL());
			if(lead.getC_BP_Group_ID()>0 && lead.getC_BP_Group_ID()!=getC_BP_Group_ID())
				history.setC_BP_Group_ID(lead.getC_BP_Group_ID());
			
			if(lead.getCompanyName()!=null && lead.getCompanyName().compareTo(getCompanyName())!=0)
				history.setCompanyName(lead.getCompanyName());
			
			if(lead.getC_BPartner_ID()>0 && lead.getC_BPartner_ID()!=getC_BPartner_ID())
				history.setC_BPartner_ID(lead.getC_BPartner_ID());
			if(lead.getC_BPartner_Location_ID()>0 && lead.getC_BPartner_Location_ID()!=getC_BPartner_Location_ID())
				history.setC_BPartner_Location_ID(lead.getC_BPartner_Location_ID());
			if(lead.getAD_User_ID()>0 && lead.getAD_User_ID()!=getAD_User_ID())
				history.setAD_User_ID(lead.getAD_User_ID());
			
			if(lead.getRef_BPartner_ID()>0 && lead.getRef_BPartner_ID()!=getRef_BPartner_ID())
				history.setRef_BPartner_ID(lead.getRef_BPartner_ID());
			if(lead.getRef_BPartner_Location_ID()>0 && lead.getRef_BPartner_Location_ID()!=getRef_BPartner_Location_ID())
				history.setRef_BPartner_Location_ID(lead.getRef_BPartner_Location_ID());
			if(lead.getRef_User_ID()>0 && lead.getRef_User_ID()!=getRef_User_ID())
				history.setRef_User_ID(lead.getRef_User_ID());
			
			
			if(lead.getAddress1()!=null && lead.getAddress1().compareTo(getAddress1())!=0)
				history.setAddress1(lead.getAddress1());
			if(lead.getAddress2()!=null && lead.getAddress2().compareTo(getAddress2())!=0)
				history.setAddress2(lead.getAddress2());
			if(lead.getC_City_ID()>0 && lead.getC_City_ID()!=getC_City_ID())
				history.setC_City_ID(lead.getC_City_ID());
			if(lead.getCity()!=null && lead.getCity().compareTo(getCity())!=0)
				history.setCity(lead.getCity());
			
			if(lead.getC_Region_ID()>0 && lead.getC_Region_ID()!=getC_Region_ID())
				history.setC_Region_ID(lead.getC_Region_ID());
			if(lead.getRegionName()!=null && lead.getRegionName().compareTo(getRegionName())!=0)
				history.setRegionName(lead.getRegionName());
			
			if(lead.getC_Country_ID()>0 && lead.getC_Country_ID()!=getC_Country_ID())
				history.setC_Country_ID(lead.getC_Country_ID());
			if(lead.getPostal()!=null && lead.getPostal().compareTo(getPostal())!=0)
				history.setPostal(lead.getPostal());
			
			if(lead.getC_Greeting_ID()>0 && lead.getC_Greeting_ID()!=getC_Greeting_ID())
				history.setC_Greeting_ID(lead.getC_Greeting_ID());
			
			if(lead.getContactName()!=null && lead.getContactName().compareTo(getContactName())!=0)
				history.setContactName(lead.getContactName());
			if(lead.getMobile()!=null && lead.getMobile().compareTo(getMobile())!=0)
				history.setMobile(lead.getMobile());
			
			if(lead.getPhone()!=null && lead.getPhone().compareTo(getPhone())!=0)
				history.setPhone(lead.getPhone());
			if(lead.getPhone2()!=null && lead.getPhone2().compareTo(getPhone2())!=0)
				history.setPhone2(lead.getPhone2());
			if(lead.getFax()!=null && lead.getFax().compareTo(getFax())!=0)
				history.setFax(lead.getFax());
			
			if(lead.getEMail()!=null && lead.getEMail().compareTo(getEMail())!=0)
				history.setEMail(lead.getEMail());
			
			if(lead.getC_Job_ID()>0 && lead.getC_Job_ID()!=getC_Job_ID())
				history.setC_Job_ID(lead.getC_Job_ID());
			if(lead.getTitle()!=null && lead.getTitle().compareTo(getTitle())!=0)
				history.setTitle(lead.getTitle());
			
			if(lead.getC_LeadResponseMaster_ID()>0 && lead.getC_LeadResponseMaster_ID()!=getC_LeadResponseMaster_ID())
				history.setC_LeadResponseMaster_ID(lead.getC_LeadResponseMaster_ID());
			if(lead.getLeadResponseDetails()!=null && lead.getLeadResponseDetails().compareTo(getLeadResponseDetails())!=0)
				history.setLeadResponseDetails(lead.getLeadResponseDetails());
			
			if(lead.getM_Warehouse_ID()>0 && lead.getM_Warehouse_ID()!=getM_Warehouse_ID())
				history.setM_Warehouse_ID(lead.getM_Warehouse_ID());
			if(lead.getM_PriceList_ID()>0 && lead.getM_PriceList_ID()!=getM_PriceList_ID())
				history.setM_PriceList_ID(lead.getM_PriceList_ID());
			if(lead.getC_Currency_ID()>0 && lead.getC_Currency_ID()!=getC_Currency_ID())
				history.setC_Currency_ID(lead.getC_Currency_ID());
			if(lead.getC_ConversionType_ID()>0 && lead.getC_ConversionType_ID()!=getC_ConversionType_ID())
				history.setC_ConversionType_ID(lead.getC_ConversionType_ID());
			
			if(lead.getTotalLines()!=null && lead.getTotalLines().compareTo(getTotalLines())!=0)
				history.setTotalLines(lead.getTotalLines());
			
			if(!history.save(lead.get_TrxName()))
					throw new AdempiereException("Lead history is not saved");
		}
		return true;
	}	//	beforeSave
	
	
	/**
	 * @param lead
	 * @return
	 * Create Prospect Customer
	 */
	public MBPartner createProspectBPartner() {
			
		MBPartner partner=new MBPartner(getCtx(), 0, get_TrxName());
		partner.setValue(getContactName()); /// Need to ask question to Surya
		partner.setName(getName());
		partner.setURL(getURL());
		partner.setC_BP_Group_ID(getC_BP_Group_ID());
		partner.setC_Greeting_ID(getC_Greeting_ID());
		partner.setIsProspect(true); /// Prospect Customer
		if(partner.save(get_TrxName()))
			return partner;	
		return null;
	}
	
	/**
	 * @param lead
	 * @return
	 */
	public MBPartnerLocation createBPartnerLocation(MBPartner bPartner) {
		// TODO Auto-generated method stub
		MBPartnerLocation bPlocation=null;
		if(bPartner!=null)
		{	
			/// Create Location of BpLocation
			MLocation location=new MLocation(getCtx(), getC_Country_ID(), getC_Region_ID(), getCity(), get_TrxName());
			location.setAddress1(getAddress1());
			location.setAddress1(getAddress2());
			location.setPostal(getPostal());
			location.setRegionName(getRegionName()!=null?getRegionName():"");
			location.setCity(getCity()!=null?getCity():"");

			if(location.save(get_TrxName()))
			{	
				bPlocation=new MBPartnerLocation(bPartner);
				bPlocation.setC_Location_ID(location.getC_Location_ID());
				bPlocation.setPhone(getPhone());
				bPlocation.setPhone2(getPhone2());
				bPlocation.setFax(getFax());
				bPlocation.setName(getCity()!=null?getCity():".");
				if(bPlocation.save(get_TrxName()))
					return bPlocation;
			}
		}
		return bPlocation;
	}
	
	/**
	 * @param lead
	 * @return
	 */
	public MUser createProspectUser(MBPartner prospectBPartner) {

		MUser user=null;
		if(prospectBPartner!=null)
		{	
			user=new MUser(prospectBPartner);
			user.setTitle(getTitle());
			user.setEMail(getEMail());
			user.setMobile(getMobile());
			user.setName(getContactName());
			user.setC_Greeting_ID(getC_Greeting_ID());
			if(user.save(get_TrxName()))
				return user;
		}
		return user;
	}
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MLead[");
		sb.append (get_ID ()).append ("-").append(getName ()).append ("]");
		return sb.toString ();
	}	//	toString

	/**
	 * @param saleOpp
	 */
	public void setBPType(MSalesOpportunity saleOpp) {
		
		if(saleOpp!=null)
		{
			setBPType(BPTYPE_Prospect);
			setRef_BPartner_ID(saleOpp.getRef_BPartner_ID());
			setRef_BPartner_Location_ID(saleOpp.getRef_BPartner_Location_ID());
			setRef_User_ID(saleOpp.getRef_User_ID());
		}
	}
	
}	//	MBank
