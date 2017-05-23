package com.SharedModules;

public class AppointmentBean {

	private String APPOINTMENTID;
	private String  SLOTDATE;
	private String SLOTSTARTTIME;
	private String  SLOTENDTIME;
	
	public String getAPPOINTMENTID() {
		return APPOINTMENTID;
	}
	public String getSLOTDATE() {
		return SLOTDATE;
	}
	public String getSLOTSTARTTIME() {
		return SLOTSTARTTIME;
	}
	public String getSLOTENDTIME() {
		return SLOTENDTIME;
	}
	public void setAPPOINTMENTID(String aPPOINTMENTID) {
		APPOINTMENTID = aPPOINTMENTID;
	}
	public void setSLOTDATE(String sLOTDATE) {
		SLOTDATE = sLOTDATE;
	}
	public void setSLOTSTARTTIME(String sLOTSTARTTIME) {
		SLOTSTARTTIME = sLOTSTARTTIME;
	}
	public void setSLOTENDTIME(String sLOTENDTIME) {
		SLOTENDTIME = sLOTENDTIME;
	} 
}
