package org.cyprus.util;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.logging.Logger;

import org.cyprusbrs.util.DB;
import org.cyprusbrs.util.Env;

public class UtilCosting {

	/**	Logger			*/
	private static Logger log = Logger.getLogger(UtilCosting.class.getName());

	private UtilCosting() {// nothing
	}

	public static BigDecimal getCurrentCostPrice(Integer p_M_PRODUCT_ID,Integer p_AD_CLIENT_ID, Integer p_AD_ORG_ID, Integer P_M_ATTRIBUTESETINSTANCE_ID)
	{	
		final String sql="SELECT FN_GET_CURRENTCOSTPRICE(?,?,?,?) FROM DUAL";
		BigDecimal value = DB.getSQLValueBD(null, sql, new Object[]{p_M_PRODUCT_ID,p_AD_CLIENT_ID,p_AD_ORG_ID,P_M_ATTRIBUTESETINSTANCE_ID});
		log.info("Parameters : p_M_PRODUCT_ID : "+p_M_PRODUCT_ID+" : p_AD_CLIENT_ID "+p_AD_CLIENT_ID+" : p_AD_ORG_ID "+p_AD_ORG_ID+" : P_M_ATTRIBUTESETINSTANCE_ID "+P_M_ATTRIBUTESETINSTANCE_ID+" : Return Data "+value);
		if(value!=null && value.compareTo(Env.ZERO)!=0)
			return value;
		else
			return Env.ZERO;	
	}
	
	public static BigDecimal getCurrentStock(Integer p_M_PRODUCT_ID,Integer p_M_ATTRIBUTESETINSTANCE_ID)
	{	
		final String sql="SELECT QTYONHAND FROM M_STORAGE WHERE M_PRODUCT_ID = ? AND M_ATTRIBUTESETINSTANCE_ID = ?";
		BigDecimal value = DB.getSQLValueBD(null, sql, new Object[]{p_M_PRODUCT_ID});
		log.info("Parameters : p_M_PRODUCT_ID : "+p_M_PRODUCT_ID+" : p_M_ATTRIBUTESETINSTANCE_ID : "+p_M_ATTRIBUTESETINSTANCE_ID+" : Return Data "+value);
		if(value!=null && value.compareTo(Env.ZERO)!=0)
			return value;
	else
		return Env.ZERO;	
	}
	
	public static BigDecimal getCurrentStock(Integer p_M_PRODUCT_ID,Integer p_AD_CLIENT_ID, Integer p_AD_ORG_ID, Timestamp p_MOVEMENTDATE )
	{	
		final String sql = "select sum(movementqty) from m_transaction where M_PRODUCT_ID = ? AND AD_CLIENT_ID = ? AND AD_ORG_ID = ? AND MOVEMENTDATE <= ?";
		BigDecimal value = DB.getSQLValueBD(null, sql, new Object[]{p_M_PRODUCT_ID,p_AD_CLIENT_ID,p_AD_ORG_ID,p_MOVEMENTDATE});
		log.info("Parameters : p_M_PRODUCT_ID : "+p_M_PRODUCT_ID+" : p_AD_CLIENT_ID : "+p_AD_CLIENT_ID+" : p_AD_ORG_ID : "+p_AD_ORG_ID+" : p_MOVEMENTDATE : "+p_MOVEMENTDATE+" : Return Data "+value);
		if(value!=null && value.compareTo(Env.ZERO)!=0)
			return value;
	else
		return Env.ZERO;	
	}

}
