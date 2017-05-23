package com.BusinessModules;

import io.appium.java_client.android.AndroidDriver;
import com.SharedModules.*;
import com.Engine.Reporter;

public class Provisioning {

	public Provisioning(AndroidDriver Driver, Reporter report) {
		super();
		// TODO Auto-generated constructor stub
	}

	public void CeasePackage_SelectPackage(String ChangePackage_Type,String InstallationMethod) throws Exception{

		if (ChangePackage_Type.equalsIgnoreCase("MPF_TV")){
			switch(InstallationMethod){

			}
		}

	}
}