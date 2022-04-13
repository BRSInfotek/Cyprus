package org.cyprusbrs.process;

import org.cyprusbrs.model.MProduct;
import org.cyprusbrs.util.DB;

public class BOMVerify extends SvrProcess {

	
	/**	The Product			*/
	private int		p_M_Product_ID = 0;
	
	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		p_M_Product_ID = getRecord_ID();
	}

	@Override
	protected String doIt() throws Exception {
		
		
		if (p_M_Product_ID != 0)
		{
			log.info("M_Product_ID=" + p_M_Product_ID);
			MProduct m_product=new MProduct(getCtx(), p_M_Product_ID, get_TrxName());
			String validateProd=validateProduct(m_product);
			if(validateProd!=null && validateProd.length()>0)
			{
				log.warning (m_product.getName() + " Not Verified :  " + validateProd);
				return validateProd;
			}
			else
			{
				m_product.setIsVerified(true);
				if(m_product.save(get_TrxName()))
				{
					DB.commit(true, get_TrxName());
				}
			}
		}
//		log.info("M_Product_Category_ID=" + p_M_Product_Category_ID
//			+ ", IsReValidate=" + p_IsReValidate);
		//
		
		return "Verified";
	}

	/**
	 * Verified the BOM Product
	 * @param mProduct
	 * @return
	 */
	private String validateProduct(MProduct mProduct) {
		
		StringBuffer sbCheck=new StringBuffer(); 
		
		// a. All items in Bill of Material tab at least an item and all items should have at least 1 qty.
		String sqlBlank="select count(*) from M_Product_BOM where M_Product_ID=?";
		if(DB.getSQLValue(get_TrxName(), sqlBlank, mProduct.getM_Product_ID())<=0)
		{
			sbCheck.append("No any component added in Bill of Material");
			return sbCheck.toString();
		}
		
		String sqlZeroQty="select count(*) from M_Product_BOM where M_Product_ID=? AND bomqty<=0";
		if(DB.getSQLValue(get_TrxName(), sqlZeroQty, mProduct.getM_Product_ID())>0)
		{
			sbCheck.append("BOM qty not added any component in Bill of Material");
			return sbCheck.toString();
		}
		
		// b. If any item has already a BOM or sub BOM then it must have "Verifed" checkbox BOM. Otherwise it will not be verified parent BOM.
		
		String sqlSubBOM="select mpb.M_ProductBOM_ID,mp.name, mp.IsBOM, mp.IsVerified from M_Product_BOM mpb " + 
				" inner join M_Product mp ON (mpb.M_ProductBOM_ID=mp.M_Product_ID) " + 
				" where mpb.M_Product_ID=? and mp.IsBOM='Y' and mp.IsVerified='N'";// --- Check any item which are subBOM and it is not verified."
		if(DB.getSQLValue(get_TrxName(), sqlSubBOM, mProduct.getM_Product_ID())>0)
		{
			sbCheck.append("Sub BOM is not verified for any component in Bill of Material");
			return sbCheck.toString();
		}
		return sbCheck.toString();
	}

}
