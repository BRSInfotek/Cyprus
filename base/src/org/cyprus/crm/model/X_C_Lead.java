/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2007 cyprusbrs, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * cyprusbrs, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@cyprusbrs.org or http://www.cyprusbrs.org/license.html           *
 *****************************************************************************/
/** Generated Model - DO NOT CHANGE */
package org.cyprus.crm.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import org.cyprusbrs.model.*;
import org.cyprusbrs.util.Env;
import org.cyprusbrs.util.KeyNamePair;

/** Generated Model for C_Lead
 *  @author Adempiere (generated) 
 *  @version Release 1.1 Supported By Cyprus - $Id$ */
public class X_C_Lead extends PO implements I_C_Lead, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20220111L;

    /** Standard Constructor */
    public X_C_Lead (Properties ctx, int C_Lead_ID, String trxName)
    {
      super (ctx, C_Lead_ID, trxName);
      /** if (C_Lead_ID == 0)
        {
			setBPType (null);
// N
			setC_LeadQualification_ID (0);
			setC_LeadSource_ID (0);
			setC_Lead_ID (0);
			setEnquiryDate (new Timestamp( System.currentTimeMillis() ));
// @#Date@
			setLeadRating (null);
// C
			setName (null);
			setSalesRep_ID (0);
			setStatus (null);
// 10
			setSummary (null);
        } */
    }

    /** Load Constructor */
    public X_C_Lead (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 1 - Org 
      */
    protected int get_AccessLevel()
    {
      return accessLevel.intValue();
    }

    /** Load Meta Data */
    protected POInfo initPO (Properties ctx)
    {
      POInfo poi = POInfo.getPOInfo (ctx, Table_ID, get_TrxName());
      return poi;
    }

    public String toString()
    {
      StringBuffer sb = new StringBuffer ("X_C_Lead[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_AD_User getAD_User() throws RuntimeException
    {
		return (I_AD_User)MTable.get(getCtx(), I_AD_User.Table_Name)
			.getPO(getAD_User_ID(), get_TrxName());	}

	/** Set User/Contact.
		@param AD_User_ID 
		User within the system - Internal or Business Partner Contact
	  */
	public void setAD_User_ID (int AD_User_ID)
	{
		if (AD_User_ID < 1) 
			set_Value (COLUMNNAME_AD_User_ID, null);
		else 
			set_Value (COLUMNNAME_AD_User_ID, Integer.valueOf(AD_User_ID));
	}

	/** Get User/Contact.
		@return User within the system - Internal or Business Partner Contact
	  */
	public int getAD_User_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_User_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Address 1.
		@param Address1 
		Address line 1 for this location
	  */
	public void setAddress1 (String Address1)
	{
		set_Value (COLUMNNAME_Address1, Address1);
	}

	/** Get Address 1.
		@return Address line 1 for this location
	  */
	public String getAddress1 () 
	{
		return (String)get_Value(COLUMNNAME_Address1);
	}

	/** Set Address 2.
		@param Address2 
		Address line 2 for this location
	  */
	public void setAddress2 (String Address2)
	{
		set_Value (COLUMNNAME_Address2, Address2);
	}

	/** Get Address 2.
		@return Address line 2 for this location
	  */
	public String getAddress2 () 
	{
		return (String)get_Value(COLUMNNAME_Address2);
	}

	/** BPType AD_Reference_ID=1000024 */
	public static final int BPTYPE_AD_Reference_ID=1000024;
	/** Customer = C */
	public static final String BPTYPE_Customer = "C";
	/** New = N */
	public static final String BPTYPE_New = "N";
	/** Prospect = P */
	public static final String BPTYPE_Prospect = "P";
	/** Set Business Partner Type.
		@param BPType Business Partner Type	  */
	public void setBPType (String BPType)
	{

		set_Value (COLUMNNAME_BPType, BPType);
	}

	/** Get Business Partner Type.
		@return Business Partner Type	  */
	public String getBPType () 
	{
		return (String)get_Value(COLUMNNAME_BPType);
	}

	public I_C_BP_Group getC_BP_Group() throws RuntimeException
    {
		return (I_C_BP_Group)MTable.get(getCtx(), I_C_BP_Group.Table_Name)
			.getPO(getC_BP_Group_ID(), get_TrxName());	}

	/** Set Business Partner Group.
		@param C_BP_Group_ID 
		Business Partner Group
	  */
	public void setC_BP_Group_ID (int C_BP_Group_ID)
	{
		if (C_BP_Group_ID < 1) 
			set_Value (COLUMNNAME_C_BP_Group_ID, null);
		else 
			set_Value (COLUMNNAME_C_BP_Group_ID, Integer.valueOf(C_BP_Group_ID));
	}

	/** Get Business Partner Group.
		@return Business Partner Group
	  */
	public int getC_BP_Group_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BP_Group_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_BPartner getC_BPartnerSR() throws RuntimeException
    {
		return (I_C_BPartner)MTable.get(getCtx(), I_C_BPartner.Table_Name)
			.getPO(getC_BPartnerSR_ID(), get_TrxName());	}

	/** Set BPartner (Agent).
		@param C_BPartnerSR_ID 
		Business Partner (Agent or Sales Rep)
	  */
	public void setC_BPartnerSR_ID (int C_BPartnerSR_ID)
	{
		if (C_BPartnerSR_ID < 1) 
			set_Value (COLUMNNAME_C_BPartnerSR_ID, null);
		else 
			set_Value (COLUMNNAME_C_BPartnerSR_ID, Integer.valueOf(C_BPartnerSR_ID));
	}

	/** Get BPartner (Agent).
		@return Business Partner (Agent or Sales Rep)
	  */
	public int getC_BPartnerSR_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartnerSR_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_BPartner getC_BPartner() throws RuntimeException
    {
		return (I_C_BPartner)MTable.get(getCtx(), I_C_BPartner.Table_Name)
			.getPO(getC_BPartner_ID(), get_TrxName());	}

	/** Set Business Partner .
		@param C_BPartner_ID 
		Identifies a Business Partner
	  */
	public void setC_BPartner_ID (int C_BPartner_ID)
	{
		if (C_BPartner_ID < 1) 
			set_Value (COLUMNNAME_C_BPartner_ID, null);
		else 
			set_Value (COLUMNNAME_C_BPartner_ID, Integer.valueOf(C_BPartner_ID));
	}

	/** Get Business Partner .
		@return Identifies a Business Partner
	  */
	public int getC_BPartner_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartner_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_BPartner_Location getC_BPartner_Location() throws RuntimeException
    {
		return (I_C_BPartner_Location)MTable.get(getCtx(), I_C_BPartner_Location.Table_Name)
			.getPO(getC_BPartner_Location_ID(), get_TrxName());	}

	/** Set Partner Location.
		@param C_BPartner_Location_ID 
		Identifies the (ship to) address for this Business Partner
	  */
	public void setC_BPartner_Location_ID (int C_BPartner_Location_ID)
	{
		if (C_BPartner_Location_ID < 1) 
			set_Value (COLUMNNAME_C_BPartner_Location_ID, null);
		else 
			set_Value (COLUMNNAME_C_BPartner_Location_ID, Integer.valueOf(C_BPartner_Location_ID));
	}

	/** Get Partner Location.
		@return Identifies the (ship to) address for this Business Partner
	  */
	public int getC_BPartner_Location_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartner_Location_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_Campaign getC_Campaign() throws RuntimeException
    {
		return (I_C_Campaign)MTable.get(getCtx(), I_C_Campaign.Table_Name)
			.getPO(getC_Campaign_ID(), get_TrxName());	}

	/** Set Campaign.
		@param C_Campaign_ID 
		Marketing Campaign
	  */
	public void setC_Campaign_ID (int C_Campaign_ID)
	{
		if (C_Campaign_ID < 1) 
			set_Value (COLUMNNAME_C_Campaign_ID, null);
		else 
			set_Value (COLUMNNAME_C_Campaign_ID, Integer.valueOf(C_Campaign_ID));
	}

	/** Get Campaign.
		@return Marketing Campaign
	  */
	public int getC_Campaign_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Campaign_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_City getC_City() throws RuntimeException
    {
		return (I_C_City)MTable.get(getCtx(), I_C_City.Table_Name)
			.getPO(getC_City_ID(), get_TrxName());	}

	/** Set City.
		@param C_City_ID 
		City
	  */
	public void setC_City_ID (int C_City_ID)
	{
		if (C_City_ID < 1) 
			set_Value (COLUMNNAME_C_City_ID, null);
		else 
			set_Value (COLUMNNAME_C_City_ID, Integer.valueOf(C_City_ID));
	}

	/** Get City.
		@return City
	  */
	public int getC_City_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_City_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ConversionType getC_ConversionType() throws RuntimeException
    {
		return (I_C_ConversionType)MTable.get(getCtx(), I_C_ConversionType.Table_Name)
			.getPO(getC_ConversionType_ID(), get_TrxName());	}

	/** Set Currency Type.
		@param C_ConversionType_ID 
		Currency Conversion Rate Type
	  */
	public void setC_ConversionType_ID (int C_ConversionType_ID)
	{
		if (C_ConversionType_ID < 1) 
			set_Value (COLUMNNAME_C_ConversionType_ID, null);
		else 
			set_Value (COLUMNNAME_C_ConversionType_ID, Integer.valueOf(C_ConversionType_ID));
	}

	/** Get Currency Type.
		@return Currency Conversion Rate Type
	  */
	public int getC_ConversionType_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ConversionType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_Country getC_Country() throws RuntimeException
    {
		return (I_C_Country)MTable.get(getCtx(), I_C_Country.Table_Name)
			.getPO(getC_Country_ID(), get_TrxName());	}

	/** Set Country.
		@param C_Country_ID 
		Country 
	  */
	public void setC_Country_ID (int C_Country_ID)
	{
		if (C_Country_ID < 1) 
			set_Value (COLUMNNAME_C_Country_ID, null);
		else 
			set_Value (COLUMNNAME_C_Country_ID, Integer.valueOf(C_Country_ID));
	}

	/** Get Country.
		@return Country 
	  */
	public int getC_Country_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Country_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_Currency getC_Currency() throws RuntimeException
    {
		return (I_C_Currency)MTable.get(getCtx(), I_C_Currency.Table_Name)
			.getPO(getC_Currency_ID(), get_TrxName());	}

	/** Set Currency.
		@param C_Currency_ID 
		The Currency for this record
	  */
	public void setC_Currency_ID (int C_Currency_ID)
	{
		if (C_Currency_ID < 1) 
			set_Value (COLUMNNAME_C_Currency_ID, null);
		else 
			set_Value (COLUMNNAME_C_Currency_ID, Integer.valueOf(C_Currency_ID));
	}

	/** Get Currency.
		@return The Currency for this record
	  */
	public int getC_Currency_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Currency_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_Greeting getC_Greeting() throws RuntimeException
    {
		return (I_C_Greeting)MTable.get(getCtx(), I_C_Greeting.Table_Name)
			.getPO(getC_Greeting_ID(), get_TrxName());	}

	/** Set Greeting.
		@param C_Greeting_ID 
		Greeting to print on correspondence
	  */
	public void setC_Greeting_ID (int C_Greeting_ID)
	{
		if (C_Greeting_ID < 1) 
			set_Value (COLUMNNAME_C_Greeting_ID, null);
		else 
			set_Value (COLUMNNAME_C_Greeting_ID, Integer.valueOf(C_Greeting_ID));
	}

	/** Get Greeting.
		@return Greeting to print on correspondence
	  */
	public int getC_Greeting_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Greeting_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_Job getC_Job() throws RuntimeException
    {
		return (I_C_Job)MTable.get(getCtx(), I_C_Job.Table_Name)
			.getPO(getC_Job_ID(), get_TrxName());	}

	/** Set Position.
		@param C_Job_ID 
		Job Position
	  */
	public void setC_Job_ID (int C_Job_ID)
	{
		if (C_Job_ID < 1) 
			set_Value (COLUMNNAME_C_Job_ID, null);
		else 
			set_Value (COLUMNNAME_C_Job_ID, Integer.valueOf(C_Job_ID));
	}

	/** Get Position.
		@return Job Position
	  */
	public int getC_Job_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Job_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_LeadQualification getC_LeadQualification() throws RuntimeException
    {
		return (I_C_LeadQualification)MTable.get(getCtx(), I_C_LeadQualification.Table_Name)
			.getPO(getC_LeadQualification_ID(), get_TrxName());	}

	/** Set Lead Qualification.
		@param C_LeadQualification_ID Lead Qualification	  */
	public void setC_LeadQualification_ID (int C_LeadQualification_ID)
	{
		if (C_LeadQualification_ID < 1) 
			set_Value (COLUMNNAME_C_LeadQualification_ID, null);
		else 
			set_Value (COLUMNNAME_C_LeadQualification_ID, Integer.valueOf(C_LeadQualification_ID));
	}

	/** Get Lead Qualification.
		@return Lead Qualification	  */
	public int getC_LeadQualification_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_LeadQualification_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_LeadResponseMaster getC_LeadResponseMaster() throws RuntimeException
    {
		return (I_C_LeadResponseMaster)MTable.get(getCtx(), I_C_LeadResponseMaster.Table_Name)
			.getPO(getC_LeadResponseMaster_ID(), get_TrxName());	}

	/** Set Lead Response Master.
		@param C_LeadResponseMaster_ID Lead Response Master	  */
	public void setC_LeadResponseMaster_ID (int C_LeadResponseMaster_ID)
	{
		if (C_LeadResponseMaster_ID < 1) 
			set_Value (COLUMNNAME_C_LeadResponseMaster_ID, null);
		else 
			set_Value (COLUMNNAME_C_LeadResponseMaster_ID, Integer.valueOf(C_LeadResponseMaster_ID));
	}

	/** Get Lead Response Master.
		@return Lead Response Master	  */
	public int getC_LeadResponseMaster_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_LeadResponseMaster_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_LeadSource getC_LeadSource() throws RuntimeException
    {
		return (I_C_LeadSource)MTable.get(getCtx(), I_C_LeadSource.Table_Name)
			.getPO(getC_LeadSource_ID(), get_TrxName());	}

	/** Set Lead Source.
		@param C_LeadSource_ID Lead Source	  */
	public void setC_LeadSource_ID (int C_LeadSource_ID)
	{
		if (C_LeadSource_ID < 1) 
			set_Value (COLUMNNAME_C_LeadSource_ID, null);
		else 
			set_Value (COLUMNNAME_C_LeadSource_ID, Integer.valueOf(C_LeadSource_ID));
	}

	/** Get Lead Source.
		@return Lead Source	  */
	public int getC_LeadSource_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_LeadSource_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Lead.
		@param C_Lead_ID Lead	  */
	public void setC_Lead_ID (int C_Lead_ID)
	{
		if (C_Lead_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_Lead_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_Lead_ID, Integer.valueOf(C_Lead_ID));
	}

	/** Get Lead.
		@return Lead	  */
	public int getC_Lead_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Lead_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_Project getC_Project() throws RuntimeException
    {
		return (I_C_Project)MTable.get(getCtx(), I_C_Project.Table_Name)
			.getPO(getC_Project_ID(), get_TrxName());	}

	/** Set Project.
		@param C_Project_ID 
		Financial Project
	  */
	public void setC_Project_ID (int C_Project_ID)
	{
		if (C_Project_ID < 1) 
			set_Value (COLUMNNAME_C_Project_ID, null);
		else 
			set_Value (COLUMNNAME_C_Project_ID, Integer.valueOf(C_Project_ID));
	}

	/** Get Project.
		@return Financial Project
	  */
	public int getC_Project_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Project_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_Region getC_Region() throws RuntimeException
    {
		return (I_C_Region)MTable.get(getCtx(), I_C_Region.Table_Name)
			.getPO(getC_Region_ID(), get_TrxName());	}

	/** Set Region.
		@param C_Region_ID 
		Identifies a geographical Region
	  */
	public void setC_Region_ID (int C_Region_ID)
	{
		if (C_Region_ID < 1) 
			set_Value (COLUMNNAME_C_Region_ID, null);
		else 
			set_Value (COLUMNNAME_C_Region_ID, Integer.valueOf(C_Region_ID));
	}

	/** Get Region.
		@return Identifies a geographical Region
	  */
	public int getC_Region_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Region_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_SalesOpportunity getC_SalesOpportunity() throws RuntimeException
    {
		return (I_C_SalesOpportunity)MTable.get(getCtx(), I_C_SalesOpportunity.Table_Name)
			.getPO(getC_SalesOpportunity_ID(), get_TrxName());	}

	/** Set Sales Opportunity.
		@param C_SalesOpportunity_ID Sales Opportunity	  */
	public void setC_SalesOpportunity_ID (int C_SalesOpportunity_ID)
	{
		if (C_SalesOpportunity_ID < 1) 
			set_Value (COLUMNNAME_C_SalesOpportunity_ID, null);
		else 
			set_Value (COLUMNNAME_C_SalesOpportunity_ID, Integer.valueOf(C_SalesOpportunity_ID));
	}

	/** Get Sales Opportunity.
		@return Sales Opportunity	  */
	public int getC_SalesOpportunity_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_SalesOpportunity_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_SalesRegion getC_SalesRegion() throws RuntimeException
    {
		return (I_C_SalesRegion)MTable.get(getCtx(), I_C_SalesRegion.Table_Name)
			.getPO(getC_SalesRegion_ID(), get_TrxName());	}

	/** Set Sales Region.
		@param C_SalesRegion_ID 
		Sales coverage region
	  */
	public void setC_SalesRegion_ID (int C_SalesRegion_ID)
	{
		if (C_SalesRegion_ID < 1) 
			set_Value (COLUMNNAME_C_SalesRegion_ID, null);
		else 
			set_Value (COLUMNNAME_C_SalesRegion_ID, Integer.valueOf(C_SalesRegion_ID));
	}

	/** Get Sales Region.
		@return Sales coverage region
	  */
	public int getC_SalesRegion_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_SalesRegion_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set City.
		@param City 
		Identifies a City
	  */
	public void setCity (String City)
	{
		set_Value (COLUMNNAME_City, City);
	}

	/** Get City.
		@return Identifies a City
	  */
	public String getCity () 
	{
		return (String)get_Value(COLUMNNAME_City);
	}

	/** Set Company Name.
		@param CompanyName Company Name	  */
	public void setCompanyName (String CompanyName)
	{
		set_Value (COLUMNNAME_CompanyName, CompanyName);
	}

	/** Get Company Name.
		@return Company Name	  */
	public String getCompanyName () 
	{
		return (String)get_Value(COLUMNNAME_CompanyName);
	}

	/** Set Contact Name.
		@param ContactName 
		Business Partner Contact Name
	  */
	public void setContactName (String ContactName)
	{
		set_Value (COLUMNNAME_ContactName, ContactName);
	}

	/** Get Contact Name.
		@return Business Partner Contact Name
	  */
	public String getContactName () 
	{
		return (String)get_Value(COLUMNNAME_ContactName);
	}

	/** Set D-U-N-S.
		@param DUNS 
		Dun & Bradstreet Number
	  */
	public void setDUNS (String DUNS)
	{
		set_Value (COLUMNNAME_DUNS, DUNS);
	}

	/** Get D-U-N-S.
		@return Dun & Bradstreet Number
	  */
	public String getDUNS () 
	{
		return (String)get_Value(COLUMNNAME_DUNS);
	}

	/** Set Description.
		@param Description 
		Optional short description of the record
	  */
	public void setDescription (String Description)
	{
		set_Value (COLUMNNAME_Description, Description);
	}

	/** Get Description.
		@return Optional short description of the record
	  */
	public String getDescription () 
	{
		return (String)get_Value(COLUMNNAME_Description);
	}

	/** Set Document No.
		@param DocumentNo 
		Document sequence number of the document
	  */
	public void setDocumentNo (String DocumentNo)
	{
		set_Value (COLUMNNAME_DocumentNo, DocumentNo);
	}

	/** Get Document No.
		@return Document sequence number of the document
	  */
	public String getDocumentNo () 
	{
		return (String)get_Value(COLUMNNAME_DocumentNo);
	}

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getDocumentNo());
    }

	/** Set EMail Address.
		@param EMail 
		Electronic Mail Address
	  */
	public void setEMail (String EMail)
	{
		set_Value (COLUMNNAME_EMail, EMail);
	}

	/** Get EMail Address.
		@return Electronic Mail Address
	  */
	public String getEMail () 
	{
		return (String)get_Value(COLUMNNAME_EMail);
	}

	/** Set Enquiry Date.
		@param EnquiryDate Enquiry Date	  */
	public void setEnquiryDate (Timestamp EnquiryDate)
	{
		set_Value (COLUMNNAME_EnquiryDate, EnquiryDate);
	}

	/** Get Enquiry Date.
		@return Enquiry Date	  */
	public Timestamp getEnquiryDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_EnquiryDate);
	}

	/** Set Fax.
		@param Fax 
		Facsimile number
	  */
	public void setFax (String Fax)
	{
		set_Value (COLUMNNAME_Fax, Fax);
	}

	/** Get Fax.
		@return Facsimile number
	  */
	public String getFax () 
	{
		return (String)get_Value(COLUMNNAME_Fax);
	}

	/** Set Follow-up Date.
		@param FollowupDate Follow-up Date	  */
	public void setFollowupDate (Timestamp FollowupDate)
	{
		set_Value (COLUMNNAME_FollowupDate, FollowupDate);
	}

	/** Get Follow-up Date.
		@return Follow-up Date	  */
	public Timestamp getFollowupDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_FollowupDate);
	}

	/** Set Generate Prospect.
		@param GenerateProspect 
		This used to generate the prospect
	  */
	public void setGenerateProspect (String GenerateProspect)
	{
		set_Value (COLUMNNAME_GenerateProspect, GenerateProspect);
	}

	/** Get Generate Prospect.
		@return This used to generate the prospect
	  */
	public String getGenerateProspect () 
	{
		return (String)get_Value(COLUMNNAME_GenerateProspect);
	}

	/** Set Generate Sales Opportunity.
		@param GenerateSalesOpportunity Generate Sales Opportunity	  */
	public void setGenerateSalesOpportunity (String GenerateSalesOpportunity)
	{
		set_Value (COLUMNNAME_GenerateSalesOpportunity, GenerateSalesOpportunity);
	}

	/** Get Generate Sales Opportunity.
		@return Generate Sales Opportunity	  */
	public String getGenerateSalesOpportunity () 
	{
		return (String)get_Value(COLUMNNAME_GenerateSalesOpportunity);
	}

	/** Set Grand Total.
		@param GrandTotal 
		Total amount of document
	  */
	public void setGrandTotal (BigDecimal GrandTotal)
	{
		set_Value (COLUMNNAME_GrandTotal, GrandTotal);
	}

	/** Get Grand Total.
		@return Total amount of document
	  */
	public BigDecimal getGrandTotal () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_GrandTotal);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Comment/Help.
		@param Help 
		Comment or Hint
	  */
	public void setHelp (String Help)
	{
		set_Value (COLUMNNAME_Help, Help);
	}

	/** Get Comment/Help.
		@return Comment or Hint
	  */
	public String getHelp () 
	{
		return (String)get_Value(COLUMNNAME_Help);
	}

	/** LeadRating AD_Reference_ID=1000018 */
	public static final int LEADRATING_AD_Reference_ID=1000018;
	/** Hot = H */
	public static final String LEADRATING_Hot = "H";
	/** Warm = W */
	public static final String LEADRATING_Warm = "W";
	/** Cold = C */
	public static final String LEADRATING_Cold = "C";
	/** Set Lead Rating.
		@param LeadRating Lead Rating	  */
	public void setLeadRating (String LeadRating)
	{

		set_Value (COLUMNNAME_LeadRating, LeadRating);
	}

	/** Get Lead Rating.
		@return Lead Rating	  */
	public String getLeadRating () 
	{
		return (String)get_Value(COLUMNNAME_LeadRating);
	}

	/** Set Lead Response Details.
		@param LeadResponseDetails Lead Response Details	  */
	public void setLeadResponseDetails (String LeadResponseDetails)
	{
		set_Value (COLUMNNAME_LeadResponseDetails, LeadResponseDetails);
	}

	/** Get Lead Response Details.
		@return Lead Response Details	  */
	public String getLeadResponseDetails () 
	{
		return (String)get_Value(COLUMNNAME_LeadResponseDetails);
	}

	public I_M_PriceList getM_PriceList() throws RuntimeException
    {
		return (I_M_PriceList)MTable.get(getCtx(), I_M_PriceList.Table_Name)
			.getPO(getM_PriceList_ID(), get_TrxName());	}

	/** Set Price List.
		@param M_PriceList_ID 
		Unique identifier of a Price List
	  */
	public void setM_PriceList_ID (int M_PriceList_ID)
	{
		if (M_PriceList_ID < 1) 
			set_Value (COLUMNNAME_M_PriceList_ID, null);
		else 
			set_Value (COLUMNNAME_M_PriceList_ID, Integer.valueOf(M_PriceList_ID));
	}

	/** Get Price List.
		@return Unique identifier of a Price List
	  */
	public int getM_PriceList_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_PriceList_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_M_Warehouse getM_Warehouse() throws RuntimeException
    {
		return (I_M_Warehouse)MTable.get(getCtx(), I_M_Warehouse.Table_Name)
			.getPO(getM_Warehouse_ID(), get_TrxName());	}

	/** Set Warehouse.
		@param M_Warehouse_ID 
		Storage Warehouse and Service Point
	  */
	public void setM_Warehouse_ID (int M_Warehouse_ID)
	{
		if (M_Warehouse_ID < 1) 
			set_Value (COLUMNNAME_M_Warehouse_ID, null);
		else 
			set_Value (COLUMNNAME_M_Warehouse_ID, Integer.valueOf(M_Warehouse_ID));
	}

	/** Get Warehouse.
		@return Storage Warehouse and Service Point
	  */
	public int getM_Warehouse_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Warehouse_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Mobile.
		@param Mobile 
		User Mobile Number
	  */
	public void setMobile (String Mobile)
	{
		set_Value (COLUMNNAME_Mobile, Mobile);
	}

	/** Get Mobile.
		@return User Mobile Number
	  */
	public String getMobile () 
	{
		return (String)get_Value(COLUMNNAME_Mobile);
	}

	/** Set NAICS/SIC.
		@param NAICS 
		Standard Industry Code or its successor NAIC - http://www.osha.gov/oshstats/sicser.html
	  */
	public void setNAICS (String NAICS)
	{
		set_Value (COLUMNNAME_NAICS, NAICS);
	}

	/** Get NAICS/SIC.
		@return Standard Industry Code or its successor NAIC - http://www.osha.gov/oshstats/sicser.html
	  */
	public String getNAICS () 
	{
		return (String)get_Value(COLUMNNAME_NAICS);
	}

	/** Set Name.
		@param Name 
		Alphanumeric identifier of the entity
	  */
	public void setName (String Name)
	{
		set_Value (COLUMNNAME_Name, Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	public String getName () 
	{
		return (String)get_Value(COLUMNNAME_Name);
	}

	/** Set Phone.
		@param Phone 
		Identifies a telephone number
	  */
	public void setPhone (String Phone)
	{
		set_Value (COLUMNNAME_Phone, Phone);
	}

	/** Get Phone.
		@return Identifies a telephone number
	  */
	public String getPhone () 
	{
		return (String)get_Value(COLUMNNAME_Phone);
	}

	/** Set 2nd Phone.
		@param Phone2 
		Identifies an alternate telephone number.
	  */
	public void setPhone2 (String Phone2)
	{
		set_Value (COLUMNNAME_Phone2, Phone2);
	}

	/** Get 2nd Phone.
		@return Identifies an alternate telephone number.
	  */
	public String getPhone2 () 
	{
		return (String)get_Value(COLUMNNAME_Phone2);
	}

	/** Set ZIP.
		@param Postal 
		Postal code
	  */
	public void setPostal (String Postal)
	{
		set_Value (COLUMNNAME_Postal, Postal);
	}

	/** Get ZIP.
		@return Postal code
	  */
	public String getPostal () 
	{
		return (String)get_Value(COLUMNNAME_Postal);
	}

	/** Set Processed.
		@param Processed 
		The document has been processed
	  */
	public void setProcessed (boolean Processed)
	{
		set_Value (COLUMNNAME_Processed, Boolean.valueOf(Processed));
	}

	/** Get Processed.
		@return The document has been processed
	  */
	public boolean isProcessed () 
	{
		Object oo = get_Value(COLUMNNAME_Processed);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	public I_R_InterestArea getR_InterestArea() throws RuntimeException
    {
		return (I_R_InterestArea)MTable.get(getCtx(), I_R_InterestArea.Table_Name)
			.getPO(getR_InterestArea_ID(), get_TrxName());	}

	/** Set Interest Area.
		@param R_InterestArea_ID 
		Interest Area or Topic
	  */
	public void setR_InterestArea_ID (int R_InterestArea_ID)
	{
		if (R_InterestArea_ID < 1) 
			set_Value (COLUMNNAME_R_InterestArea_ID, null);
		else 
			set_Value (COLUMNNAME_R_InterestArea_ID, Integer.valueOf(R_InterestArea_ID));
	}

	/** Get Interest Area.
		@return Interest Area or Topic
	  */
	public int getR_InterestArea_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_R_InterestArea_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_BPartner getRef_BPartner() throws RuntimeException
    {
		return (I_C_BPartner)MTable.get(getCtx(), I_C_BPartner.Table_Name)
			.getPO(getRef_BPartner_ID(), get_TrxName());	}

	/** Set Prospect.
		@param Ref_BPartner_ID Prospect	  */
	public void setRef_BPartner_ID (int Ref_BPartner_ID)
	{
		if (Ref_BPartner_ID < 1) 
			set_Value (COLUMNNAME_Ref_BPartner_ID, null);
		else 
			set_Value (COLUMNNAME_Ref_BPartner_ID, Integer.valueOf(Ref_BPartner_ID));
	}

	/** Get Prospect.
		@return Prospect	  */
	public int getRef_BPartner_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Ref_BPartner_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_BPartner_Location getRef_BPartner_Location() throws RuntimeException
    {
		return (I_C_BPartner_Location)MTable.get(getCtx(), I_C_BPartner_Location.Table_Name)
			.getPO(getRef_BPartner_Location_ID(), get_TrxName());	}

	/** Set Propect Partner Location.
		@param Ref_BPartner_Location_ID 
		Identifies the (ship to) address for this Business Partner
	  */
	public void setRef_BPartner_Location_ID (int Ref_BPartner_Location_ID)
	{
		if (Ref_BPartner_Location_ID < 1) 
			set_Value (COLUMNNAME_Ref_BPartner_Location_ID, null);
		else 
			set_Value (COLUMNNAME_Ref_BPartner_Location_ID, Integer.valueOf(Ref_BPartner_Location_ID));
	}

	/** Get Propect Partner Location.
		@return Identifies the (ship to) address for this Business Partner
	  */
	public int getRef_BPartner_Location_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Ref_BPartner_Location_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_AD_User getRef_User() throws RuntimeException
    {
		return (I_AD_User)MTable.get(getCtx(), I_AD_User.Table_Name)
			.getPO(getRef_User_ID(), get_TrxName());	}

	/** Set Prospect User/Contact.
		@param Ref_User_ID 
		User within the system - Internal or Business Partner Contact
	  */
	public void setRef_User_ID (int Ref_User_ID)
	{
		if (Ref_User_ID < 1) 
			set_Value (COLUMNNAME_Ref_User_ID, null);
		else 
			set_Value (COLUMNNAME_Ref_User_ID, Integer.valueOf(Ref_User_ID));
	}

	/** Get Prospect User/Contact.
		@return User within the system - Internal or Business Partner Contact
	  */
	public int getRef_User_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Ref_User_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Region.
		@param RegionName 
		Name of the Region
	  */
	public void setRegionName (String RegionName)
	{
		set_Value (COLUMNNAME_RegionName, RegionName);
	}

	/** Get Region.
		@return Name of the Region
	  */
	public String getRegionName () 
	{
		return (String)get_Value(COLUMNNAME_RegionName);
	}

	public I_AD_User getSalesRep() throws RuntimeException
    {
		return (I_AD_User)MTable.get(getCtx(), I_AD_User.Table_Name)
			.getPO(getSalesRep_ID(), get_TrxName());	}

	/** Set Sales Representative.
		@param SalesRep_ID 
		Sales Representative or Company Agent
	  */
	public void setSalesRep_ID (int SalesRep_ID)
	{
		if (SalesRep_ID < 1) 
			set_Value (COLUMNNAME_SalesRep_ID, null);
		else 
			set_Value (COLUMNNAME_SalesRep_ID, Integer.valueOf(SalesRep_ID));
	}

	/** Get Sales Representative.
		@return Sales Representative or Company Agent
	  */
	public int getSalesRep_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SalesRep_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Sales Volume in 1.000.
		@param SalesVolume 
		Total Volume of Sales in Thousands of Currency
	  */
	public void setSalesVolume (BigDecimal SalesVolume)
	{
		set_Value (COLUMNNAME_SalesVolume, SalesVolume);
	}

	/** Get Sales Volume in 1.000.
		@return Total Volume of Sales in Thousands of Currency
	  */
	public BigDecimal getSalesVolume () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_SalesVolume);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Status AD_Reference_ID=1000019 */
	public static final int STATUS_AD_Reference_ID=1000019;
	/** New = 10 */
	public static final String STATUS_New = "10";
	/** Intro Mail Sent = 11 */
	public static final String STATUS_IntroMailSent = "11";
	/** First Contact = 12 */
	public static final String STATUS_FirstContact = "12";
	/** Follow up Contact = 13 */
	public static final String STATUS_FollowUpContact = "13";
	/** Product Shown = 14 */
	public static final String STATUS_ProductShown = "14";
	/** Price Quoted = 15 */
	public static final String STATUS_PriceQuoted = "15";
	/** Negotiation = 16 */
	public static final String STATUS_Negotiation = "16";
	/** No Interest = 17 */
	public static final String STATUS_NoInterest = "17";
	/** Follow Up Later = 18 */
	public static final String STATUS_FollowUpLater = "18";
	/** Converted to Lead = 19 */
	public static final String STATUS_ConvertedToLead = "19";
	/** Given Up = 20 */
	public static final String STATUS_GivenUp = "20";
	/** Invalid Data = 21 */
	public static final String STATUS_InvalidData = "21";
	/** Closed = 22 */
	public static final String STATUS_Closed = "22";
	/** Converted to Quotation = 23 */
	public static final String STATUS_ConvertedToQuotation = "23";
	/** Set Status.
		@param Status 
		Status of the currently running check
	  */
	public void setStatus (String Status)
	{

		set_Value (COLUMNNAME_Status, Status);
	}

	/** Get Status.
		@return Status of the currently running check
	  */
	public String getStatus () 
	{
		return (String)get_Value(COLUMNNAME_Status);
	}

	/** Set Subscribe Interest Area.
		@param SubscribeInterestArea Subscribe Interest Area	  */
	public void setSubscribeInterestArea (String SubscribeInterestArea)
	{
		set_Value (COLUMNNAME_SubscribeInterestArea, SubscribeInterestArea);
	}

	/** Get Subscribe Interest Area.
		@return Subscribe Interest Area	  */
	public String getSubscribeInterestArea () 
	{
		return (String)get_Value(COLUMNNAME_SubscribeInterestArea);
	}

	/** Set Summary.
		@param Summary 
		Textual summary of this request
	  */
	public void setSummary (String Summary)
	{
		set_Value (COLUMNNAME_Summary, Summary);
	}

	/** Get Summary.
		@return Textual summary of this request
	  */
	public String getSummary () 
	{
		return (String)get_Value(COLUMNNAME_Summary);
	}

	/** Set Title.
		@param Title 
		Name this entity is referred to as
	  */
	public void setTitle (String Title)
	{
		set_Value (COLUMNNAME_Title, Title);
	}

	/** Get Title.
		@return Name this entity is referred to as
	  */
	public String getTitle () 
	{
		return (String)get_Value(COLUMNNAME_Title);
	}

	/** Set Total Lines.
		@param TotalLines 
		Total of all document lines
	  */
	public void setTotalLines (BigDecimal TotalLines)
	{
		set_Value (COLUMNNAME_TotalLines, TotalLines);
	}

	/** Get Total Lines.
		@return Total of all document lines
	  */
	public BigDecimal getTotalLines () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TotalLines);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set URL.
		@param URL 
		Full URL address - e.g. http://www.adempiere.org
	  */
	public void setURL (String URL)
	{
		set_Value (COLUMNNAME_URL, URL);
	}

	/** Get URL.
		@return Full URL address - e.g. http://www.adempiere.org
	  */
	public String getURL () 
	{
		return (String)get_Value(COLUMNNAME_URL);
	}
}