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
package org.cyprusbrs.process;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.cyprus.exceptions.AdempiereException;
import org.cyprusbrs.model.PO;
import org.cyprusbrs.util.DisplayType;
import org.cyprusbrs.util.Env;
import org.cyprusbrs.util.Ini;
import org.cyprusbrs.util.Msg;
import org.cyprusbrs.util.Util;

/**
 *  Process Information (Value Object)
 *
 *  @author     Jorg Janke
 *  @version    $Id: ProcessInfo.java,v 1.2 2006/07/30 00:54:44 jjanke Exp $
 *  @author victor.perez@e-evolution.com 
 *  @see FR 1906632 http://sourceforge.net/tracker/?func=detail&atid=879335&aid=1906632&group_id=176962
 */
public class ProcessInfo implements Serializable
{
	
	public ProcessInfo (String title, int processId, int tableId, int recordId, boolean managedTransaction)
	{
		this(title, processId , tableId , recordId);
		this.managedTransaction = managedTransaction;
	}
	
	/**
	 *  Constructor
	 *  @param Title Title
	 *  @param AD_Process_ID AD_Process_ID
	 *  @param Table_ID AD_Table_ID
	 *  @param Record_ID Record_ID
	 */
	public ProcessInfo (String Title, int AD_Process_ID, int Table_ID, int Record_ID)
	{
		setTitle (Title);
		setAD_Process_ID(AD_Process_ID);
		setTable_ID (Table_ID);
		setRecord_ID (Record_ID);
		if (Ini.isPropertyBool(Ini.P_PRINTPREVIEW))
			m_printPreview = true;
		else
			m_printPreview = false;
	}   //  ProcessInfo

	/**
	 *  Constructor
	 *  @param Title Title
	 *  @param AD_Process_ID AD_Process_ID
	 *   */
	public ProcessInfo (String Title, int AD_Process_ID)
	{
		this (Title, AD_Process_ID, 0, 0);
	}   //  ProcessInfo

	/**	Serialization Info	**/
	static final long serialVersionUID = -1993220053515488725L;
	

	/** Title of the Process/Report */
	private String				m_Title;
	/** Process ID                  */
	private int					m_AD_Process_ID;
	/** Table ID if the Process	    */
	private int					m_Table_ID;
	/** Record ID if the Process    */
	private int					m_Record_ID;
	/** User_ID        					*/
	private Integer	 			m_AD_User_ID;
	/** Client_ID        				*/
	private Integer 			m_AD_Client_ID;
	/** Class Name 						*/
	private String				m_ClassName = null;

	//  -- Optional --

	/** Process Instance ID         */
	private int					m_AD_PInstance_ID = 0;

	/** Summary of Execution        */
	private String    			m_Summary = "";
	/** Execution had an error      */
	private boolean     		m_Error = false;


	/*	General Data Object			*/
	private Serializable		m_SerializableObject = null;
	/*	General Data Object			*/
	private transient Object	m_TransientObject = null;
	/** Estimated Runtime           */
	private int          		m_EstSeconds = 5;
	/** Batch						*/
	private boolean				m_batch = false;
	/** Process timed out				*/
	private boolean				m_timeout = false;
	
	/* Alias table selection */
	private String aliasTableSelection;

	/**	Log Info					*/
	private ArrayList<ProcessInfoLog> m_logs = null;

	/**	Log Info					*/
	private ProcessInfoParameter[]	m_parameter = null;
	
	/** Transaction Name 			*/
	private String				m_transactionName = null;
	
	private boolean				m_printPreview = false;

	private boolean				m_reportingProcess = false;
	//FR 1906632
	private File 			    m_pdf_report = null;
	
	/**	Table Name for open window after running	*/
	private String 				resultTableName = null;
	
	private boolean 			managedTransaction = true;
//	FR [ 244 ]
	private boolean 			isSelection = false;
	
	private String 				reportType = null;
	/**	Multi-Selection Keys		*/
	private List<Integer>		keySelection = null;
	
	private File 				reportAsFile = null;
	
	private LinkedHashMap<Integer, LinkedHashMap<String, Object>> selection = null;
	/**	Log Info					*/
	private Hashtable<String, ProcessInfoParameter> parameters = null;
	
	
	// Bug #1926 - fix is to provide interface info to the process
	/** A flag indicating the type of interface in use.  Value can be
	 *  INTERFACE_TYPE_NOT_SET, INTERFACE_TYPE_ZK or INTERFACE_TYPE_SWING
	 *  */
	private String	interfaceType;
	
	// Values for the interface_type flag
		/** 
		 * A flag value for the interface type indicating the interface type
		 * is not set.  This is a valid value if the process is not being run
		 * through a UI dialog.
		 */
		public static String		INTERFACE_TYPE_NOT_SET = "not set";
		
	/**
	 * A flag value for the interface type indicating the process is 
	 * being run from a dialog in a web client
	 */
	public static String		INTERFACE_TYPE_ZK = "zk";
	
	/**
	 * A flag value for the interface type indicating the process is 
	 * being run from a dialog in a SWING client
	 */
	public static String		INTERFACE_TYPE_SWING = "swing";
	private int tableSelectionId;
	private int m_AD_ReportTemplate_ID;
	private int m_AD_BView_ID;
	private boolean m_BV_PrintFormat;

	/**
	 *  String representation
	 *  @return String representation
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("ProcessInfo[");
		sb.append(m_Title)
			.append(",Process_ID=").append(m_AD_Process_ID);
		if (m_AD_PInstance_ID != 0)
			sb.append(",AD_PInstance_ID=").append(m_AD_PInstance_ID);
		if (m_Record_ID != 0)
			sb.append(",Record_ID=").append(m_Record_ID);
		if (m_ClassName != null)
			sb.append(",ClassName=").append(m_ClassName);
		sb.append(",Error=").append(isError());
		if (m_TransientObject != null)
			sb.append(",Transient=").append(m_TransientObject);
		if (m_SerializableObject != null)
			sb.append(",Serializable=").append(m_SerializableObject);
		sb.append(",Summary=").append(getSummary())
			.append(",Log=").append(m_logs == null ? 0 : m_logs.size());
		//	.append(getLogInfo(false));
		sb.append("]");
		return sb.toString();
	}   //  toString

	
	/**************************************************************************
	 * 	Set Summary
	 * 	@param summary summary (will be translated)
	 */
	public void setSummary (String summary)
	{
		m_Summary = summary;
	}	//	setSummary
	/**
	 * Method getSummary
	 * @return String
	 */
	public String getSummary ()
	{
		return Util.cleanAmp(m_Summary);
	}	//	getSummary

	/**
	 * Method setSummary
	 * @param translatedSummary String
	 * @param error boolean
	 */
	public void setSummary (String translatedSummary, boolean error)
	{
		setSummary (translatedSummary);
		setError(error);
	}	//	setSummary
	/**
	 * Method addSummary
	 * @param additionalSummary String
	 */
	public void addSummary (String additionalSummary)
	{
		m_Summary += additionalSummary;
	}	//	addSummary

	/**
	 * Method setError
	 * @param error boolean
	 */
	public void setError (boolean error)
	{
		m_Error = error;
	}	//	setError
	/**
	 * Method isError
	 * @return boolean
	 */
	public boolean isError ()
	{
		return m_Error;
	}	//	isError

	/**
	 *	Batch
	 * 	@param batch true if batch processing
	 */
	public void setIsBatch (boolean batch)
	{
		m_batch = batch;
	}	//	setTimeout
	
	/**
	 *	Batch - i.e. UI not blocked
	 *	@return boolean
	 */
	public boolean isBatch()
	{
		return m_batch;
	}	//	isBatch

	/**
	 *	Timeout
	 * 	@param timeout true still running
	 */
	public void setIsTimeout (boolean timeout)
	{
		m_timeout = timeout;
	}	//	setTimeout
	
	/**
	 * FR [ 244 ]
	 * Set the flag for know if is from SB or not
	 * @param isSelection
	 */
	public void setIsSelection(boolean isSelection) {
		this.isSelection = isSelection;
	}
	/**
	 *	Timeout - i.e process did not complete
	 *	@return boolean
	 */
	public boolean isTimeout()
	{
		return m_timeout;
	}	//	isTimeout

	/**
	 *	Set Log of Process.
	 *  <pre>
	 *  - Translated Process Message
	 *  - List of log entries
	 *      Date - Number - Msg
	 *  </pre>
	 *	@param html if true with HTML markup
	 *	@return Log Info
	 */
	public String getLogInfo (boolean html)
	{
		if (m_logs == null)
			return "";
		//
		StringBuffer sb = new StringBuffer ();
		SimpleDateFormat dateFormat = DisplayType.getDateFormat(DisplayType.DateTime);
		if (html)
			sb.append("<table width=\"100%\" border=\"1\" cellspacing=\"0\" cellpadding=\"2\">");
		//
		for (int i = 0; i < m_logs.size(); i++)
		{
			if (html)
				sb.append("<tr>");
			else if (i > 0)
				sb.append("\n");
			//
			ProcessInfoLog log = m_logs.get(i);
			/**
			if (log.getP_ID() != 0)
				sb.append(html ? "<td>" : "")
					.append(log.getP_ID())
					.append(html ? "</td>" : " \t");	**/
			//
			if (log.getP_Date() != null)
				sb.append(html ? "<td>" : "")
					.append(dateFormat.format(log.getP_Date()))
					.append(html ? "</td>" : " \t");
			//
			if (log.getP_Number() != null)
				sb.append(html ? "<td>" : "")
					.append(log.getP_Number())
					.append(html ? "</td>" : " \t");
			//
			if (log.getP_Msg() != null)
				sb.append(html ? "<td>" : "")
					.append(Msg.parseTranslation(Env.getCtx(), log.getP_Msg()))
					.append(html ? "</td>" : "");
			//
			if (html)
				sb.append("</tr>");
		}
		if (html)
			sb.append("</table>");
		return sb.toString();
	 }	//	getLogInfo

	/**
	 * 	Get ASCII Log Info
	 *	@return Log Info
	 */
	public String getLogInfo ()
	{
		return getLogInfo(false);
	}	//	getLogInfo

	/**
	 * Method getAD_PInstance_ID
	 * @return int
	 */
	public int getAD_PInstance_ID()
	{
		return m_AD_PInstance_ID;
	}
	/**
	 * Method setAD_PInstance_ID
	 * @param AD_PInstance_ID int
	 */
	public void setAD_PInstance_ID(int AD_PInstance_ID)
	{
		m_AD_PInstance_ID = AD_PInstance_ID;
	}

	/**
	 * Method getAD_Process_ID
	 * @return int
	 */
	public int getAD_Process_ID()
	{
		return m_AD_Process_ID;
	}
	/**
	 * Method setAD_Process_ID
	 * @param AD_Process_ID int
	 */
	public void setAD_Process_ID(int AD_Process_ID)
	{

//		m_AD_Process_ID = AD_Process_ID;
		this.m_AD_Process_ID = AD_Process_ID;
	}

	/**
	 * Method getClassName
	 * @return String or null
	 */
	public String getClassName()
	{
		return m_ClassName;
	}
	
	/**
	 * Method setClassName
	 * @param ClassName String
	 */
	public void setClassName(String ClassName)
	{
		m_ClassName = ClassName;
		if (m_ClassName != null && m_ClassName.length() == 0)
			m_ClassName = null;
	}	//	setClassName

	/**
	 * Method getTransientObject
	 * @return Object
	 */
	public Object getTransientObject()
	{
		return m_TransientObject;
	}
	/**
	 * Method setTransientObject
	 * @param TransientObject Object
	 */
	public void setTransientObject (Object TransientObject)
	{
		m_TransientObject = TransientObject;
	}

	/**
	 * Method getSerializableObject
	 * @return Serializable
	 */
	public Serializable getSerializableObject()
	{
		return m_SerializableObject;
	}
	/**
	 * Method setSerializableObject
	 * @param SerializableObject Serializable
	 */
	public void setSerializableObject (Serializable SerializableObject)
	{
		m_SerializableObject = SerializableObject;
	}

	/**
	 * Method getEstSeconds
	 * @return int
	 */
	public int getEstSeconds()
	{
		return m_EstSeconds;
	}
	/**
	 * Method setEstSeconds
	 * @param EstSeconds int
	 */
	public void setEstSeconds (int EstSeconds)
	{
		m_EstSeconds = EstSeconds;
	}


	/**
	 * Method getTable_ID
	 * @return int
	 */
	public int getTable_ID()
	{
		return m_Table_ID;
	}
	/**
	 * Method setTable_ID
	 * @param AD_Table_ID int
	 */
	public void setTable_ID(int AD_Table_ID)
	{
		m_Table_ID = AD_Table_ID;
	}

	/**
	 * Method getRecord_ID
	 * @return int
	 */
	public int getRecord_ID()
	{
		return m_Record_ID;
	}
	/**
	 * Method setRecord_ID
	 * @param Record_ID int
	 */
	public void setRecord_ID(int Record_ID)
	{
		m_Record_ID = Record_ID;
	}

	/**
	 * Method getTitle
	 * @return String
	 */
	public String getTitle()
	{
		return m_Title;
	}
	/**
	 * Method setTitle
	 * @param Title String
	 */
	public void setTitle (String Title)
	{
		m_Title = Title;
	}	//	setTitle


	/**
	 * Method setAD_Client_ID
	 * @param AD_Client_ID int
	 */
	public void setAD_Client_ID (int AD_Client_ID)
	{
		m_AD_Client_ID = new Integer (AD_Client_ID);
	}
	/**
	 * Method getAD_Client_ID
	 * @return Integer
	 */
	public Integer getAD_Client_ID()
	{
		return m_AD_Client_ID;
	}

	/**
	 * Method setAD_User_ID
	 * @param AD_User_ID int
	 */
	public void setAD_User_ID (int AD_User_ID)
	{
		m_AD_User_ID = new Integer (AD_User_ID);
	}
	/**
	 * Method getAD_User_ID
	 * @return Integer
	 */
	public Integer getAD_User_ID()
	{
		return m_AD_User_ID;
	}

	
	/**************************************************************************
	 * 	Get Parameter
	 *	@return Parameter Array
	 */
	public ProcessInfoParameter[] getParameter()
	{
		return m_parameter;
	}	//	getParameter

	/**
	 * 	Set Parameter
	 *	@param parameter Parameter Array
	 */
	public void setParameter (ProcessInfoParameter[] parameter)
	{
		m_parameter = parameter;
	}	//	setParameter

	
	/**************************************************************************
	 * 	Add to Log
	 *	@param Log_ID Log ID
	 *	@param P_ID Process ID
	 *	@param P_Date Process Date
	 *	@param P_Number Process Number
	 *	@param P_Msg Process Message
	 */
	public void addLog (int Log_ID, int P_ID, Timestamp P_Date, BigDecimal P_Number, String P_Msg)
	{
		addLog (new ProcessInfoLog (Log_ID, P_ID, P_Date, P_Number, P_Msg));
	}	//	addLog

	/**
	 * 	Add to Log
	 *	@param P_ID Process ID
	 *	@param P_Date Process Date
	 *	@param P_Number Process Number
	 *	@param P_Msg Process Message
	 */
	public void addLog (int P_ID, Timestamp P_Date, BigDecimal P_Number, String P_Msg)
	{
		addLog (new ProcessInfoLog (P_ID, P_Date, P_Number, P_Msg));
	}	//	addLog

	/**
	 * 	Add to Log
	 *	@param logEntry log entry
	 */
	public void addLog (ProcessInfoLog logEntry)
	{
		if (logEntry == null)
			return;
		if (m_logs == null)
			m_logs = new ArrayList<ProcessInfoLog>();
		m_logs.add (logEntry);
	}	//	addLog


	/**
	 * Method getLogs
	 * @return ProcessInfoLog[]
	 */
	public ProcessInfoLog[] getLogs()
	{
		if (m_logs == null)
			return null;
		ProcessInfoLog[] logs = new ProcessInfoLog[m_logs.size()];
		m_logs.toArray (logs);
		return logs;
	}	//	getLogs

	/**
	 * Method getIDs
	 * @return int[]
	 */
	public int[] getIDs()
	{
		if (m_logs == null)
			return null;
		int[] ids = new int[m_logs.size()];
		for (int i = 0; i < m_logs.size(); i++)
			ids[i] = m_logs.get(i).getP_ID();
		return ids;
	}	//	getIDs

	/**
	 * Method getLogList
	 * @return ArrayList
	 */
	public ArrayList<ProcessInfoLog> getLogList()
	{
		return m_logs;
	}
	/**
	 * Method setLogList
	 * @param logs ArrayList
	 */
	public void setLogList (ArrayList<ProcessInfoLog> logs)
	{
		m_logs = logs;
	}
	
	/**
	 * Get transaction name for this process
	 * @return String
	 */
	public String getTransactionName()
	{
		return m_transactionName;
	}

	/**
	 * Set transaction name from this process
	 * @param trxName
	 */
	public void setTransactionName(String trxName)
	{
		m_transactionName = trxName;
	}
	
	/**
	 * Set print preview flag, only relevant if this is a reporting process
	 * @param b
	 */
	public void setPrintPreview(boolean b)
	{
		m_printPreview = b;
	}
	
	/**
	 * Is print preview instead of direct print ? Only relevant if this is a reporting process 
	 * @return boolean
	 */
	public boolean isPrintPreview()
	{
		return m_printPreview;
	}
	
	/**
	 * Is this a reporting process ?
	 * @return boolean
	 */
	public boolean isReportingProcess() 
	{
		return m_reportingProcess;
	}
	
	/**
	 * Set is this a reporting process
	 * @param f
	 */
	public void setReportingProcess(boolean f)
	{
		m_reportingProcess = f;
	}
	
	//FR 1906632
	/**
	 * Set PDF file generate to Jasper Report
	 * @param PDF File 
	 */
	public void setPDFReport(File f)
	{
		m_pdf_report = f;
	}	
	
	/**
	 * Get PDF file generate to Jasper Report
	 * @param f
	 */
	public File getPDFReport()
	{
		return m_pdf_report;
	}	
		
	/**
	 * Set Selection keys
	 * @param selection
	 */
	public void setSelectionKeys(List<Integer> selection) {
		keySelection = selection;
		setIsSelection(selection != null && selection.size() > 0);
	}
	
	/**
	 * Get Selection keys (used just for key without values)
	 * @return
	 */
	public List<Integer> getSelectionKeys() {
		return keySelection;
	}
	
	public void setReportType(String type)
	{
		reportType = type;
	}
	
	public String getReportType()
	{
		return reportType;
	}
	/**
	 * Method setTable_ID
	 * @param tableSelectionId
	 */
	public void setTableSelectionId(int tableSelectionId)
	{
		this.tableSelectionId = tableSelectionId;
	}

	/**
	 * Method tableSelectionId
	 * @return int
	 */
	public int getTableSelectionId()
	{
		return tableSelectionId;
	}
	
	/**
	 * Set Selection Parameters
	 * @param selection
	 */
	public void setSelectionValues(LinkedHashMap<Integer, LinkedHashMap<String, Object>> selection) {
		this.selection = selection;
		setIsSelection(selection != null && selection.size() > 0);
		//	fill key
		if(selection != null) {
			keySelection = new ArrayList<Integer>();
			for(Entry<Integer,LinkedHashMap<String, Object>> records : selection.entrySet()) {
				keySelection.add(records.getKey());
			}
		}
	}
	
	/**
	 * Get Selection
	 * @return
	 */
	public LinkedHashMap<Integer, LinkedHashMap<String, Object>> getSelectionValues() {
		return selection;
	}
	
	/**
	 * get instances for selection
	 * @param trxName
	 * @return
	 * @throws AdempiereException
     */
	public List<?> getInstancesForSelection(String trxName) throws AdempiereException
	{
		return PO.getInstances( getTableSelectionId() , getSelectionKeys(), trxName);
	}
	
	/**
	 * Get a value of selection from a key
	 * @param key
	 * @param columnName
	 * @return
	 */
	public Object getSelection(int key, String columnName) {
		if(selection != null) {
			LinkedHashMap<String, Object> record = selection.get(key);
			if(record != null) {
				return record.get(columnName);
			}
		}
		//	Default
		return null;
	}
	
	/**
	 * Get a selection value like int from key and column name
	 * @param key
	 * @param columnName
	 * @return int with value
	 * FR [ 352 ]
	 */
	public int getSelectionAsInt(int key, String columnName) {
		Object retValue = getSelection(key, columnName);
		//	For null
		if(retValue == null)
			return 0;
		if(retValue instanceof Number)
			return ((Number) retValue).intValue();
		//	Default
		return 0;
	}
	
	/**
	 * Get Parameter from Name
	 * @param parameterName
	 * @return ProcessInfoParameter
	 * FR [ 325 ]
	 */
	public ProcessInfoParameter getInfoParameter(String parameterName) {
		//	Valid null
		if(parameters == null)
			return null;
		//	Default
		return parameters.get(parameterName);
	}
	
	/**
	 * Get a parameter like int from Name
	 * @param parameterName
	 * @return int with value
	 * FR [ 325 ]
	 */
	public int getParameterAsInt(String parameterName) {
		ProcessInfoParameter parameter = getInfoParameter(parameterName);
		//	For null
		if(parameter == null)
			return 0;
		//	Default
		return parameter.getParameterAsInt();
	}
	/**
	 * Get a parameter like Timestamp from Name
	 * @param parameterName
	 * @return Timestamp with value
	 * FR [ 325 ]
	 */
	public Timestamp getParameterAsTimestamp(String parameterName) {
		ProcessInfoParameter parameter = getInfoParameter(parameterName);
		//	For null
		if(parameter == null)
			return null;
		//	Default
		return parameter.getParameterAsTimestamp();
	}
	
	/**
	 * Get a parameter like Timestamp from Name
	 * @param parameterName
	 * @return Timestamp with value
	 * FR [ 325 ]
	 */
	public Timestamp getParameterToAsTimestamp(String parameterName) {
		ProcessInfoParameter parameter = getInfoParameter(parameterName);
		//	For null
		if(parameter == null)
			return null;
		//	Default
		return parameter.getParameterToAsTimestamp();
	}
	
	/**
	 * Get a parameter like BigDecimal from Name
	 * @param parameterName
	 * @return BigDecimal with value
	 * FR [ 325 ]
	 */
	public BigDecimal getParameterAsBigDecimal(String parameterName) {
		ProcessInfoParameter parameter = getInfoParameter(parameterName);
		//	For null
		if(parameter == null)
			return null;
		//	Default
		return parameter.getParameterAsBigDecimal();
	}
	
	/**
	 * Get a parameter like boolean from Name
	 * @param parameterName
	 * @return boolean with value
	 * FR [ 325 ]
	 */
	public boolean getParameterAsBoolean(String parameterName) {
		ProcessInfoParameter parameter = getInfoParameter(parameterName);
		//	For null
		if(parameter == null)
			return false;
		//	Default
		return parameter.getParameterAsBoolean();
	}
	/**
	 * Get a parameter like String from Name
	 * @param parameterName
	 * @return String with value
	 * FR [ 325 ]
	 */
	public String getParameterAsString(String parameterName) {
		ProcessInfoParameter parameter = getInfoParameter(parameterName);
		//	For null
		if(parameter == null)
			return null;
		//	Default
		return parameter.getParameterAsString();
	}
	/**
	 * Get a selection value like BigDecimal from key and column name
	 * @param key
	 * @param columnName
	 * @return BigDecimal with value
	 * FR [ 352 ]
	 */
	public BigDecimal getSelectionAsBigDecimal(int key, String columnName) {
		Object retValue = getSelection(key, columnName);
		//	For null
		if(retValue == null)
			return null;
		if(retValue instanceof BigDecimal)
			return (BigDecimal) retValue;
		//	Default
		return null;
	}
	/**
	 * Get a selection value like String from key and column name
	 * @param key
	 * @param columnName
	 * @return String with value
	 * FR [ 352 ]
	 */
	public String getSelectionAsString(int key, String columnName) {
		Object retValue = getSelection(key, columnName);
		//	For null
		if(retValue == null)
			return null;
		if(retValue instanceof String)
			return (String) retValue;
		//	Default
		return null;
	}
	
	public void setManagedTransaction(boolean managedTransaction)
	{
		this.managedTransaction = managedTransaction;
	}

	public boolean isManagedTransaction()
	{
		return managedTransaction;
	}
	
	/**
	 * Set report as file
	 * @param reportAsFile
	 */
	public void setReportAsFile(File reportAsFile) {
		this.reportAsFile = reportAsFile;
	}
	
	/**
	 * Get Report as File
	 * @return
	 */
	public File getReportAsFile() {
		return reportAsFile;
	}
	
	// metas: end
	
		//metas: cg
		//03040
		/**
		 * @return the window No
		 */
		public int getWindowNo()
		{
			return windowNo;
		}

		/**
		 * @param window No the window No to set
		 */
		public void setWindowNo(int windowNo)
		{
			this.windowNo = windowNo;
		}

		private int          		windowNo = 0;
		
		/**
		 * Get the interface type this process is being run from.  The interface type
		 * can be used by the process to perform UI type actions from within the process
		 * or in the {@link #postProcess(boolean)}
		 * @return The InterfaceType which will be one of 
		 * <li> {@link #INTERFACE_TYPE_NOT_SET}
		 * <li> {@link #INTERFACE_TYPE_SWING} or
		 * <li> {@link #INTERFACE_TYPE_ZK}
		 */
		public String getInterfaceType() {
			
			if (interfaceType == null || interfaceType.isEmpty())
				interfaceType = INTERFACE_TYPE_NOT_SET;
			
			return interfaceType;
		}

		/**
		 * Sets the Interface Type
		 * @param uiType which must equal one of the following: 
		 * <li> {@link #INTERFACE_TYPE_NOT_SET} (default)
		 * <li> {@link #INTERFACE_TYPE_SWING} or
		 * <li> {@link #INTERFACE_TYPE_ZK}
		 * The interface should be set by UI dialogs that start the process.
		 * @throws IllegalArgumentException if the interfaceType is not recognized.
		 */
		public void setInterfaceType(String uiType) {
			// Limit value to known types
			if (uiType.equals(INTERFACE_TYPE_NOT_SET)
				||uiType.equals(INTERFACE_TYPE_ZK)
				||uiType.equals(INTERFACE_TYPE_SWING) )
			{
				this.interfaceType = uiType;
			}
			else
			{
				throw new IllegalArgumentException("Unknown interface type " + uiType);
			}
		}
		
		/**
		 * Get result table Name
		 * @return
		 */
		public String getResultTableName() {
			return resultTableName;
		}
		
		/**
		 * Validate if result can be open
		 * @return
		 */
		public boolean isOpenResult() {
			return !Util.isEmpty(getResultTableName());
		}
		
		/**
		 * set table alias for  selection
		 * @param aliasTableSelection
		 */
		public void setAliasForTableSelection(String aliasTableSelection)
		{
			this.aliasTableSelection = aliasTableSelection;
		}

		/**
		 * Get Selection Table Alias
		 * @return
		 */
		public String getAliasForTableSelection()
		{
			return aliasTableSelection;
		}
		
		
		
		  public int getAD_ReportTemplate_ID() {
			    return this.m_AD_ReportTemplate_ID;
			  }
			  
			  public void setAD_ReportTemplate_ID(int AD_ReportTemplate_ID) {
			    this.m_AD_ReportTemplate_ID = AD_ReportTemplate_ID;
			  }
			  
			  public int getAD_BView_ID() {
				    return this.m_AD_BView_ID;
				  }
				  
				  public void setAD_BView_ID(int AD_BView_ID) {
				    this.m_AD_BView_ID = AD_BView_ID;
				  }
				  
				  public boolean getBV_PrintFormat() {
					    return this.m_BV_PrintFormat;
					  }
					  
					  public void setBV_PrintFormat(boolean printFormat) {
					    this.m_BV_PrintFormat = printFormat;
					  }

	
}   //  ProcessInfo
