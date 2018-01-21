package org.tp23.dep3rest;

/**
 * Created by teknopaul on 09/10/16.
 */
public class OK {

	private boolean ok = true;

	public boolean isOk() {
		return ok;
	}

	public void setOk(boolean ok) {
		this.ok = ok;
	}

	public boolean equals(Object ok) {
		// yeah yeah yeah
		return ok instanceof OK && ((OK)ok).ok == isOk();
	}
}
