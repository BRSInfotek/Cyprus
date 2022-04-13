package org.cyprus.pos.base;

import org.cyprus.exceptions.AdempiereException;
import org.cyprusbrs.util.Env;
import org.cyprusbrs.util.Msg;

/**
 * @author victor.perez@e-evolution.com , http://www.e-evolution.com
 */
public class AdempierePOSException extends AdempiereException {

	public AdempierePOSException(String message) {
		super(Msg.parseTranslation(Env.getCtx() ,message));
	}
}
