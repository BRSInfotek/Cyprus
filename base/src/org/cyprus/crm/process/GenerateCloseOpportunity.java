/**
 * 
 */
package org.cyprus.crm.process;

import java.util.logging.Logger;

import org.cyprusbrs.process.SvrProcess;
import org.cyprusbrs.util.DB;
import org.cyprus.crm.model.MLead;
import org.cyprus.crm.model.MSalesOpportunity;

/**
 * @author Mukesh
 * process name : org.cyprus.crm.process.GenerateCloseOpportunity
 */
public class GenerateCloseOpportunity extends SvrProcess {

	
	private static Logger log = Logger.getLogger(GenerateCloseOpportunity.class.getName());
	
	/**	Sales Opportunity ID			*/
	int v_C_SalesOpportunity_ID = 0;
	
	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		v_C_SalesOpportunity_ID=getRecord_ID();
	}

	@Override
	protected String doIt() throws Exception {
		
		String returnVal="Sales Opprtunity Customer Name : ";
		log.info("v_C_SalesOpportunity_ID=" + v_C_SalesOpportunity_ID );
		if (v_C_SalesOpportunity_ID == 0)
			throw new Exception ("Sales Opportunity not found -  v_C_SalesOpportunity_ID=" +  v_C_SalesOpportunity_ID);
		
		MSalesOpportunity saleOpp=new MSalesOpportunity(getCtx(), v_C_SalesOpportunity_ID, get_TrxName());
		
		if(saleOpp.getC_Lead_ID()>0)
		{
			MLead lead=new MLead(getCtx(), saleOpp.getC_Lead_ID(), get_TrxName());
			
			/// update process of Line 
			String sql="update C_LeadInfo set processed='Y' where C_Lead_ID="+saleOpp.getC_Lead_ID();
			int no = DB.executeUpdate(sql, get_TrxName());
			if (no <= 0)
				log.warning("(1) #" + no);
			
			lead.setStatus(MLead.STATUS_Closed);
			lead.setProcessed(true);
			if(lead.save(get_TrxName()))
				DB.commit(true, get_TrxName());
			
		}
		
		/// update process of Line 
		String sql="update C_OpportunityLine set processed='Y' where C_SalesOpportunity_ID="+v_C_SalesOpportunity_ID;
		int no = DB.executeUpdate(sql, get_TrxName());
		if (no <= 0)
			log.warning("(1) #" + no);
		
		saleOpp.setStatus(MSalesOpportunity.STATUS_Closed);
		saleOpp.setProcessed(true);
		if(saleOpp.save(get_TrxName()))
			DB.commit(true, get_TrxName());
			
		return returnVal;
	}

}
