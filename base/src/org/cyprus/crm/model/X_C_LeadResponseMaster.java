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

import java.sql.ResultSet;
import java.util.Properties;
import org.cyprusbrs.model.*;
import org.cyprusbrs.util.KeyNamePair;

/** Generated Model for C_LeadResponseMaster
 *  @author Adempiere (generated) 
 *  @version Release 1.1 Supported By Cyprus - $Id$ */
public class X_C_LeadResponseMaster extends PO implements I_C_LeadResponseMaster, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20211224L;

    /** Standard Constructor */
    public X_C_LeadResponseMaster (Properties ctx, int C_LeadResponseMaster_ID, String trxName)
    {
      super (ctx, C_LeadResponseMaster_ID, trxName);
      /** if (C_LeadResponseMaster_ID == 0)
        {
			setC_LeadResponseMaster_ID (0);
			setName (null);
			setValue (null);
        } */
    }

    /** Load Constructor */
    public X_C_LeadResponseMaster (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_C_LeadResponseMaster[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Lead Response Master.
		@param C_LeadResponseMaster_ID Lead Response Master	  */
	public void setC_LeadResponseMaster_ID (int C_LeadResponseMaster_ID)
	{
		if (C_LeadResponseMaster_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_LeadResponseMaster_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_LeadResponseMaster_ID, Integer.valueOf(C_LeadResponseMaster_ID));
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

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getName());
    }

	/** Set Search Key.
		@param Value 
		Search key for the record in the format required - must be unique
	  */
	public void setValue (String Value)
	{
		set_Value (COLUMNNAME_Value, Value);
	}

	/** Get Search Key.
		@return Search key for the record in the format required - must be unique
	  */
	public String getValue () 
	{
		return (String)get_Value(COLUMNNAME_Value);
	}
}