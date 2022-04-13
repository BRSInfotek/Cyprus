/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2007 ComPiere, Inc. All Rights Reserved.                *
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
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.cyprus.crm.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.cyprusbrs.model.*;
import org.cyprusbrs.util.KeyNamePair;

/** Generated Interface for C_SalesOpportunity
 *  @author Adempiere (generated) 
 *  @version Release 1.1 Supported By Cyprus
 */
public interface I_C_SalesOpportunity 
{

    /** TableName=C_SalesOpportunity */
    public static final String Table_Name = "C_SalesOpportunity";

    /** AD_Table_ID=1000045 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 1 - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(1);

    /** Load Meta Data */

    /** Column name AD_Client_ID */
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";

	/** Get Client.
	  * Client/Tenant for this installation.
	  */
	public int getAD_Client_ID();

    /** Column name AD_Org_ID */
    public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";

	/** Set Organization.
	  * Organizational entity within client
	  */
	public void setAD_Org_ID (int AD_Org_ID);

	/** Get Organization.
	  * Organizational entity within client
	  */
	public int getAD_Org_ID();

    /** Column name AD_User_ID */
    public static final String COLUMNNAME_AD_User_ID = "AD_User_ID";

	/** Set User/Contact.
	  * User within the system - Internal or Business Partner Contact
	  */
	public void setAD_User_ID (int AD_User_ID);

	/** Get User/Contact.
	  * User within the system - Internal or Business Partner Contact
	  */
	public int getAD_User_ID();

	public I_AD_User getAD_User() throws RuntimeException;

    /** Column name C_BPartner_ID */
    public static final String COLUMNNAME_C_BPartner_ID = "C_BPartner_ID";

	/** Set Business Partner .
	  * Identifies a Business Partner
	  */
	public void setC_BPartner_ID (int C_BPartner_ID);

	/** Get Business Partner .
	  * Identifies a Business Partner
	  */
	public int getC_BPartner_ID();

	public I_C_BPartner getC_BPartner() throws RuntimeException;

    /** Column name C_BPartner_Location_ID */
    public static final String COLUMNNAME_C_BPartner_Location_ID = "C_BPartner_Location_ID";

	/** Set Partner Location.
	  * Identifies the (ship to) address for this Business Partner
	  */
	public void setC_BPartner_Location_ID (int C_BPartner_Location_ID);

	/** Get Partner Location.
	  * Identifies the (ship to) address for this Business Partner
	  */
	public int getC_BPartner_Location_ID();

	public I_C_BPartner_Location getC_BPartner_Location() throws RuntimeException;

    /** Column name C_ConversionType_ID */
    public static final String COLUMNNAME_C_ConversionType_ID = "C_ConversionType_ID";

	/** Set Currency Type.
	  * Currency Conversion Rate Type
	  */
	public void setC_ConversionType_ID (int C_ConversionType_ID);

	/** Get Currency Type.
	  * Currency Conversion Rate Type
	  */
	public int getC_ConversionType_ID();

	public I_C_ConversionType getC_ConversionType() throws RuntimeException;

    /** Column name C_Currency_ID */
    public static final String COLUMNNAME_C_Currency_ID = "C_Currency_ID";

	/** Set Currency.
	  * The Currency for this record
	  */
	public void setC_Currency_ID (int C_Currency_ID);

	/** Get Currency.
	  * The Currency for this record
	  */
	public int getC_Currency_ID();

	public I_C_Currency getC_Currency() throws RuntimeException;

    /** Column name C_Lead_ID */
    public static final String COLUMNNAME_C_Lead_ID = "C_Lead_ID";

	/** Set Lead	  */
	public void setC_Lead_ID (int C_Lead_ID);

	/** Get Lead	  */
	public int getC_Lead_ID();

	public I_C_Lead getC_Lead() throws RuntimeException;

    /** Column name C_Order_ID */
    public static final String COLUMNNAME_C_Order_ID = "C_Order_ID";

	/** Set Sales Quotation .
	  * Order
	  */
	public void setC_Order_ID (int C_Order_ID);

	/** Get Sales Quotation .
	  * Order
	  */
	public int getC_Order_ID();

	public I_C_Order getC_Order() throws RuntimeException;

    /** Column name C_PaymentTerm_ID */
    public static final String COLUMNNAME_C_PaymentTerm_ID = "C_PaymentTerm_ID";

	/** Set Payment Term.
	  * The terms of Payment (timing, discount)
	  */
	public void setC_PaymentTerm_ID (int C_PaymentTerm_ID);

	/** Get Payment Term.
	  * The terms of Payment (timing, discount)
	  */
	public int getC_PaymentTerm_ID();

	public I_C_PaymentTerm getC_PaymentTerm() throws RuntimeException;

    /** Column name C_SalesOpportunity_ID */
    public static final String COLUMNNAME_C_SalesOpportunity_ID = "C_SalesOpportunity_ID";

	/** Set Sales Opportunity	  */
	public void setC_SalesOpportunity_ID (int C_SalesOpportunity_ID);

	/** Get Sales Opportunity	  */
	public int getC_SalesOpportunity_ID();

    /** Column name CloseOpportunity */
    public static final String COLUMNNAME_CloseOpportunity = "CloseOpportunity";

	/** Set Close Sales Opportunity	  */
	public void setCloseOpportunity (String CloseOpportunity);

	/** Get Close Sales Opportunity	  */
	public String getCloseOpportunity();

    /** Column name ConversionDate */
    public static final String COLUMNNAME_ConversionDate = "ConversionDate";

	/** Set Conversion Date.
	  * Date for selecting conversion rate
	  */
	public void setConversionDate (Timestamp ConversionDate);

	/** Get Conversion Date.
	  * Date for selecting conversion rate
	  */
	public Timestamp getConversionDate();

    /** Column name Created */
    public static final String COLUMNNAME_Created = "Created";

	/** Get Created.
	  * Date this record was created
	  */
	public Timestamp getCreated();

    /** Column name CreatedBy */
    public static final String COLUMNNAME_CreatedBy = "CreatedBy";

	/** Get Created By.
	  * User who created this records
	  */
	public int getCreatedBy();

    /** Column name Description */
    public static final String COLUMNNAME_Description = "Description";

	/** Set Description.
	  * Optional short description of the record
	  */
	public void setDescription (String Description);

	/** Get Description.
	  * Optional short description of the record
	  */
	public String getDescription();

    /** Column name DocumentNo */
    public static final String COLUMNNAME_DocumentNo = "DocumentNo";

	/** Set Document No.
	  * Document sequence number of the document
	  */
	public void setDocumentNo (String DocumentNo);

	/** Get Document No.
	  * Document sequence number of the document
	  */
	public String getDocumentNo();

    /** Column name EnquiryDate */
    public static final String COLUMNNAME_EnquiryDate = "EnquiryDate";

	/** Set Enquiry Date	  */
	public void setEnquiryDate (Timestamp EnquiryDate);

	/** Get Enquiry Date	  */
	public Timestamp getEnquiryDate();

    /** Column name GenerateQuotation */
    public static final String COLUMNNAME_GenerateQuotation = "GenerateQuotation";

	/** Set Generate Quotation.
	  * It generates Quotation from Sales Opportunity
	  */
	public void setGenerateQuotation (String GenerateQuotation);

	/** Get Generate Quotation.
	  * It generates Quotation from Sales Opportunity
	  */
	public String getGenerateQuotation();

    /** Column name GrandTotal */
    public static final String COLUMNNAME_GrandTotal = "GrandTotal";

	/** Set Grand Total.
	  * Total amount of document
	  */
	public void setGrandTotal (BigDecimal GrandTotal);

	/** Get Grand Total.
	  * Total amount of document
	  */
	public BigDecimal getGrandTotal();

    /** Column name Help */
    public static final String COLUMNNAME_Help = "Help";

	/** Set Comment/Help.
	  * Comment or Hint
	  */
	public void setHelp (String Help);

	/** Get Comment/Help.
	  * Comment or Hint
	  */
	public String getHelp();

    /** Column name IsActive */
    public static final String COLUMNNAME_IsActive = "IsActive";

	/** Set Active.
	  * The record is active in the system
	  */
	public void setIsActive (boolean IsActive);

	/** Get Active.
	  * The record is active in the system
	  */
	public boolean isActive();

    /** Column name M_PriceList_ID */
    public static final String COLUMNNAME_M_PriceList_ID = "M_PriceList_ID";

	/** Set Price List.
	  * Unique identifier of a Price List
	  */
	public void setM_PriceList_ID (int M_PriceList_ID);

	/** Get Price List.
	  * Unique identifier of a Price List
	  */
	public int getM_PriceList_ID();

	public I_M_PriceList getM_PriceList() throws RuntimeException;

    /** Column name M_Warehouse_ID */
    public static final String COLUMNNAME_M_Warehouse_ID = "M_Warehouse_ID";

	/** Set Warehouse.
	  * Storage Warehouse and Service Point
	  */
	public void setM_Warehouse_ID (int M_Warehouse_ID);

	/** Get Warehouse.
	  * Storage Warehouse and Service Point
	  */
	public int getM_Warehouse_ID();

	public I_M_Warehouse getM_Warehouse() throws RuntimeException;

    /** Column name OpportunityDate */
    public static final String COLUMNNAME_OpportunityDate = "OpportunityDate";

	/** Set Opportunity Date	  */
	public void setOpportunityDate (Timestamp OpportunityDate);

	/** Get Opportunity Date	  */
	public Timestamp getOpportunityDate();

    /** Column name PaymentRule */
    public static final String COLUMNNAME_PaymentRule = "PaymentRule";

	/** Set Payment Rule.
	  * How you pay the invoice
	  */
	public void setPaymentRule (String PaymentRule);

	/** Get Payment Rule.
	  * How you pay the invoice
	  */
	public String getPaymentRule();

    /** Column name Probability */
    public static final String COLUMNNAME_Probability = "Probability";

	/** Set Probability %	  */
	public void setProbability (int Probability);

	/** Get Probability %	  */
	public int getProbability();

    /** Column name Processed */
    public static final String COLUMNNAME_Processed = "Processed";

	/** Set Processed.
	  * The document has been processed
	  */
	public void setProcessed (boolean Processed);

	/** Get Processed.
	  * The document has been processed
	  */
	public boolean isProcessed();

    /** Column name ProposalDate */
    public static final String COLUMNNAME_ProposalDate = "ProposalDate";

	/** Set Proposal Date	  */
	public void setProposalDate (Timestamp ProposalDate);

	/** Get Proposal Date	  */
	public Timestamp getProposalDate();

    /** Column name Ref_BPartner_ID */
    public static final String COLUMNNAME_Ref_BPartner_ID = "Ref_BPartner_ID";

	/** Set Prospect	  */
	public void setRef_BPartner_ID (int Ref_BPartner_ID);

	/** Get Prospect	  */
	public int getRef_BPartner_ID();

	public I_C_BPartner getRef_BPartner() throws RuntimeException;

    /** Column name Ref_BPartner_Location_ID */
    public static final String COLUMNNAME_Ref_BPartner_Location_ID = "Ref_BPartner_Location_ID";

	/** Set Propect Location.
	  * Identifies the (ship to) address for this Business Partner
	  */
	public void setRef_BPartner_Location_ID (int Ref_BPartner_Location_ID);

	/** Get Propect Location.
	  * Identifies the (ship to) address for this Business Partner
	  */
	public int getRef_BPartner_Location_ID();

	public I_C_BPartner_Location getRef_BPartner_Location() throws RuntimeException;

    /** Column name Ref_User_ID */
    public static final String COLUMNNAME_Ref_User_ID = "Ref_User_ID";

	/** Set Prospect User/Contact.
	  * User within the system - Internal or Business Partner Contact
	  */
	public void setRef_User_ID (int Ref_User_ID);

	/** Get Prospect User/Contact.
	  * User within the system - Internal or Business Partner Contact
	  */
	public int getRef_User_ID();

	public I_AD_User getRef_User() throws RuntimeException;

    /** Column name Status */
    public static final String COLUMNNAME_Status = "Status";

	/** Set Status.
	  * Status of the currently running check
	  */
	public void setStatus (String Status);

	/** Get Status.
	  * Status of the currently running check
	  */
	public String getStatus();

    /** Column name TotalLines */
    public static final String COLUMNNAME_TotalLines = "TotalLines";

	/** Set Total Lines.
	  * Total of all document lines
	  */
	public void setTotalLines (BigDecimal TotalLines);

	/** Get Total Lines.
	  * Total of all document lines
	  */
	public BigDecimal getTotalLines();

    /** Column name Updated */
    public static final String COLUMNNAME_Updated = "Updated";

	/** Get Updated.
	  * Date this record was updated
	  */
	public Timestamp getUpdated();

    /** Column name UpdatedBy */
    public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

	/** Get Updated By.
	  * User who updated this records
	  */
	public int getUpdatedBy();
}
