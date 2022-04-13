package org.cyprus.webui.apps.graph;

import org.cyprus.webui.component.Grid;
import org.cyprus.webui.component.Panel;
import org.cyprus.webui.component.Row;
import org.cyprus.webui.component.Rows;
import org.cyprusbrs.model.MGoal;
import org.cyprusbrs.util.CLogger;
import org.cyprusbrs.util.Env;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;

public class WPAPanel extends Panel implements EventListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6491684272848160726L;

	/**
	 * 	Get Panel if User has Performance Goals
	 *	@return panel pr null
	 */
	public static WPAPanel get()
	{
		int AD_User_ID = Env.getAD_User_ID(Env.getCtx());
		MGoal[] goals = MGoal.getUserGoals(Env.getCtx(), AD_User_ID);
		if (goals.length == 0)
			return null;
		return new WPAPanel(goals);
	}
	
	/**************************************************************************
	 * 	Constructor
	 *	@param goals
	 */
	private WPAPanel (MGoal[] goals)
	{
		super ();
		m_goals = goals;
		init();
	}
	
	/** Goals			*/
	private MGoal[] 	m_goals = null;
	
	/**	Logger	*/
	private static CLogger log = CLogger.getCLogger (WPAPanel.class);
	
	/**
	 * 	Static/Dynamic Init
	 */
	private void init()
	{
		Grid grid = new Grid();
		appendChild(grid);
		grid.setWidth("100%");
		grid.setStyle("margin:0; padding:0; position: absolute;");
		grid.makeNoStrip();
		grid.setOddRowSclass("even");

		Rows rows = new Rows();
		grid.appendChild(rows);

		for (int i = 0; i < m_goals.length; i++)
		{
			Row row = new Row();
			rows.appendChild(row);
			row.setWidth("100%");
			
			WPerformanceIndicator pi = new WPerformanceIndicator(m_goals[i]);
			row.appendChild(pi);
			pi.addEventListener(Events.ON_CLICK, this);			
		}	
	}	//	init

	/**
	 * 	Action Listener for Drill Down
	 *	@param e event
	 */
	public void onEvent(Event e) throws Exception 
	{
		if (e.getTarget() instanceof WPerformanceIndicator)
		{
			WPerformanceIndicator pi = (WPerformanceIndicator) e.getTarget();
			log.info(pi.toString());
			MGoal goal = pi.getGoal();
			if (goal.getMeasure() != null)
				new WPerformanceDetail(goal);
		}
	}
}
