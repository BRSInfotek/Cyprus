/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.
 * This program is free software; you can redistribute it and/or modify it
 * under the terms version 2 of the GNU General Public License as published
 * by the Free Software Foundation. This program is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along 
 * with this program; if not, write to the Free Software Foundation, Inc., 
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
 * You may reach us at: ComPiere, Inc. - http://www.adempiere.org/license.html
 * 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA or info@adempiere.org 
 *****************************************************************************/
package org.cyprusbrs.cm.extend;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

/**
 *	Search the index for results
 *	
 *  @author Yves Sandfort
 *  @version $Id$
 */
public class indexSearch extends org.cyprusbrs.cm.Extend
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2182007427785520124L;

	public indexSearch (HttpServletRequest request, Properties ctx)
	{
		super (request, ctx);
	}
	
	public boolean doIt()
	{
		return true;
	}
}
