package org.cyprus.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Logger;

import org.cyprus.exceptions.CyprusException;
import org.cyprusbrs.model.MInOut;
import org.cyprusbrs.model.MInventory;
import org.cyprusbrs.model.MMovement;
import org.cyprusbrs.util.DB;

public final class UtilProduct {

	/**	Logger			*/
	private static Logger log = Logger.getLogger(UtilProduct.class.getName());
	
	private UtilProduct(){		// nothing
		
	}
	
	public static boolean checkReverseTransaction(Class<?> class1, Integer trasactionId)
	{
		String tableName=getTableName(class1);
		String tableNameLine=tableName+"LINE";
		String tableNameId=tableName+"_ID";
		String sqlQuery="SELECT MIL.M_PRODUCT_ID, MIL.M_LOCATOR_ID, MI.AD_ORG_ID, MI.MOVEMENTDATE FROM "+tableName+" MI "+
				" INNER JOIN "+tableNameLine+" MIL ON (MI."+tableNameId+"=MIL."+tableNameId+") WHERE MI."+tableNameId+"="+trasactionId;
		
//		String sqlQuery="SELECT M_PRODUCT_ID,AD_ORG_ID, M_LOCATOR_ID FROM "+tableName+"LINE WHERE "+tableName+"_ID="+trasactionId;
		ResultSet rs=null;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sqlQuery, null);
			rs = pstmt.executeQuery();
			while (rs.next())
			{ 
				log.info("This is product Id ::: "+rs.getString("M_PRODUCT_ID"));
				log.info("This is M_LOCATOR_ID  ::: "+rs.getString("M_LOCATOR_ID"));
				log.info("This is AD_ORG_ID ::: "+rs.getString("AD_ORG_ID"));
				log.info("This is MOVEMENTDATE ::: "+rs.getString("MOVEMENTDATE"));
//				log.info("This is  ::: "+rs.getString("M_PRODUCT_ID"));

			}
			rs.close();
			pstmt.close();
			rs = null;
			pstmt = null;	
		}
		catch(Exception e){e.printStackTrace();}
		
		log.info(sqlQuery);
		return false;
	}

	private static String getTableName(Class<?> class1) {	
		
		String tableName="";
		if(class1.equals(MInOut.class))
		{
			log.info("Material Receipt, Shipment (Customer), Return to Vendor, Customer Return ");
			tableName=MInOut.Table_Name.toUpperCase();
		}
		else if(class1.equals(MInventory.class))
		{
			log.info("Physical Inventory, Internal Use Inventory");
			tableName=MInventory.Table_Name.toUpperCase();
		}
		else if(class1.equals(MMovement.class))
		{
			log.info("Inventory Move");
			tableName=MMovement.Table_Name.toUpperCase();
		}
//		else if(class1.equals(MProduction.class))
//		{
//			log.info("Production"); // need to ask question if PLAN text in end of like ProductionPlan
//			tableName=MProduction.Table_Name.toUpperCase();
//		}
		return tableName;
	}
	
	
}
