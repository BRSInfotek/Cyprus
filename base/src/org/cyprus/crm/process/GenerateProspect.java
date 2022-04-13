/**
 * 
 */
package org.cyprus.crm.process;

import java.util.logging.Logger;

import org.cyprusbrs.model.MBPartner;
import org.cyprusbrs.model.MBPartnerLocation;
import org.cyprusbrs.model.MUser;
import org.cyprusbrs.process.SvrProcess;
import org.cyprusbrs.util.DB;
import org.cyprus.crm.model.MLead;

/**
 * @author Mukesh
 *	process name : org.cyprus.crm.process.GenerateProspect
 */



public class GenerateProspect extends SvrProcess {

	private static Logger log = Logger.getLogger(GenerateProspect.class.getName());

	/**	Lead ID			*/
	int v_C_Lead_ID = 0;
	
	String prospectName=null;
	
	@Override
	protected void prepare() {
		
		// TODO Auto-generated method stub
				v_C_Lead_ID = getRecord_ID();
	}

	@Override
	protected String doIt() throws Exception {
		
		String returnVal="Prospect Customer Name : ";
		log.info("p_C_Lead_ID=" + v_C_Lead_ID );
		if (v_C_Lead_ID == 0)
			throw new Exception ("Lead not found -  p_C_Lead_ID=" +  v_C_Lead_ID);
		
		MLead lead=new MLead(getCtx(), v_C_Lead_ID, get_TrxName());
		if(lead.getBPType().equalsIgnoreCase(MLead.BPTYPE_New))
		{	
			MBPartner bPartner=lead.createProspectBPartner();
			if(bPartner!=null)
			{
				prospectName=bPartner.getName();
				log.info("Propect Customer Name is "+bPartner.getName());
				MBPartnerLocation location=lead.createBPartnerLocation(bPartner);
				log.info("Propect Customer location "+location.getC_Location_ID());
				MUser user=lead.createProspectUser(bPartner);
				log.info("Propect Customer User "+user.getName());

				lead.setBPType(MLead.BPTYPE_Prospect);
				lead.setRef_BPartner_ID(bPartner.getC_BPartner_ID());
				lead.setRef_BPartner_Location_ID(location.getC_BPartner_Location_ID());
				lead.setRef_User_ID(user.getAD_User_ID());
				if(lead.save(get_TrxName()))
				{
					DB.commit(true, get_TrxName());
				}
			}
		}
		else
		{
			return "No new prospect customer to add";
		}
		return returnVal;
	}

	
}
