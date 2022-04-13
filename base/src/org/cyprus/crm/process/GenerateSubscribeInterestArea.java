/**
 * 
 */
package org.cyprus.crm.process;

import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.cyprus.crm.model.MLead;
import org.cyprusbrs.model.I_R_InterestArea;
import org.cyprusbrs.model.MContactInterest;
import org.cyprusbrs.model.MInterestArea;
import org.cyprusbrs.model.Query;
import org.cyprusbrs.process.ProcessInfoParameter;
import org.cyprusbrs.process.SvrProcess;


/**
 * @author Mukesh
 * process name : org.cyprus.crm.process.GenerateSubscribeInterestArea
 */
public class GenerateSubscribeInterestArea extends SvrProcess {

	private static Logger log = Logger.getLogger(GenerateSubscribeInterestArea.class.getName());

	/**	Lead ID			*/
	private int v_C_Lead_ID = 0;
	
	private MInterestArea interestArea=null;
		
	private Integer p_R_InterestArea_ID=0;
	
	@Override
	protected void prepare() {
		
		// TODO Auto-generated method stub
		v_C_Lead_ID = getRecord_ID();
		
	// Parameter 
	
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("R_InterestArea_ID"))
				p_R_InterestArea_ID =  para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}

	@Override
	protected String doIt() throws Exception {
		
		StringBuffer returnVal=new StringBuffer("Interest Subscribed Area Created/Updated : ");
		log.info("p_C_Lead_ID=" + v_C_Lead_ID );
		if (v_C_Lead_ID == 0)
			throw new Exception ("Lead not found -  p_C_Lead_ID=" +  v_C_Lead_ID);
		
		MLead lead=new MLead(getCtx(), v_C_Lead_ID, get_TrxName());
		
		// Already Created
		
		interestArea = new Query(getCtx(), I_R_InterestArea.Table_Name, "R_InterestArea_ID=?", get_TrxName())
				.setParameters(p_R_InterestArea_ID)
				.firstOnly();
		if(interestArea!=null) // Create/Update
		{
			MContactInterest ci = MContactInterest.get(getCtx(), interestArea.getR_InterestArea_ID(), lead.getAD_User_ID(), interestArea.isActive(), get_TrxName());
			ci.setMobile(lead.getMobile());
			ci.setPhone(lead.getPhone());
			ci.setPhone2(lead.getPhone2());
			ci.setFax(lead.getFax());
			ci.setEMail(lead.getEMail());
			ci.subscribe(); // It will set Current Date
			ci.saveEx(get_TrxName());
		}
//		else if(lead.getBPType().equalsIgnoreCase(MLead.BPTYPE_Customer) || lead.getBPType().equalsIgnoreCase(MLead.BPTYPE_Prospect)) /// Created New
//		{
//			if(lead.getAD_User_ID()>0)
//			{
//				interestArea=new MInterestArea(getCtx(), 0, get_TrxName());
//				interestArea.setValue(lead.getContactName());
//				interestArea.setName(lead.getContactName());
//				interestArea.setC_Lead_ID(lead.getC_Lead_ID());
//				if(interestArea.save(get_TrxName()))
//					DB.commit(true, get_TrxName());
//				
//				if(interestArea!=null)
//				{
//					MContactInterest ci =new MContactInterest(getCtx(), interestArea.getR_InterestArea_ID(), lead.getAD_User_ID(), true, get_TrxName());
//					ci.setMobile(lead.getMobile());
//					ci.setPhone(lead.getPhone());
//					ci.setPhone2(lead.getPhone2());
//					ci.setFax(lead.getFax());
//					ci.setEMail(lead.getEMail());
//					ci.subscribe(); // It will set Current Date
//					ci.saveEx(get_TrxName());
//				}
//			}	
//		}
		
		if(interestArea!=null)
		return returnVal.append(interestArea.getName()).toString();
		else
		return "Not Created";
	}

}
