package com.DataFeeder;
/**Class Name		:	XP_WelcomeCentre
 * Description		:	Interface for storing Element Locators in Welcome Centre Pages
 * @author Sneha Karri
 * Function Names	:	no functions
 * Creation Date	:	17 Aug 2016
 */
public interface XP_WelcomeCentre {

	public static String XP_GNews_Fname="//*[@id='welcome-message']//*[contains(text(),'Great news M_FName') and contains(@data-ng-show,'!maAuto')]";
	public static String XP_Wel_Fname="//*[@id='welcome-message']//*[contains(text(),'Welcome M_FName') and contains(@data-ng-show,'!maAuto')]";
	public static String XP_FirstName="//*[@id='welcome-message']//*[contains(text(),'Hi M_FName')  and contains(@data-ng-show,'!maAuto') ]";
	public static String XP_APPconfirmText="//*[@id='welcome-message']//*[contains(text(),'got your order and everything is running smoothly')  and contains(@data-ng-show,'!maAuto')]";
	public static String XP_GNews_Ordercom="//*[@id='welcome-message']//*[contains(text(),'forget to come back to make the most of your TalkTalk') and contains(@data-ng-show,'!maAuto') and contains(text(),'Your M_PACK is now up and running')] ";
	public static String XP_GNews_Ordercom_TV="//*[@id='welcome-message']//*[contains(text(),'forget to come back to make the most of your TalkTalk') and contains(@data-ng-show,'!maAuto') and contains(text(),'Your M_PACK with M_TV TV Box is now up and running')]";
	public static String XP_UpgradeUFO="//*[@id='welcome-message']//*[contains(text(),'Thanks for upgrading to UFO') and contains(@data-ng-show,'!maAuto')]";
	public static String XP_GNews_Order_BBComp="//*[@id='welcome-message']//*[contains(text(),'Your Phone and Broadband are up and running and your TV is on its way') and contains(@data-ng-show,'!maAuto')]";
	public static String XP_Cancelled_TV="//*[@id='welcome-message']//*[contains(text(),'Our records show that your M_PACK with M_TVBOX has been cancelled') and contains(@data-ng-show,'!maAuto')]";
	public static String XP_Cancelled="//*[@id='welcome-message']//*[contains(text(),'Our records show that your M_PACK has been cancelled') and contains(@data-ng-show,'!maAuto')]";
	public static String XP_Rejected="//*[@id='welcome-message']//*[contains(text(),'Sorry M_FName, there seems to be a problem with your M_PACK order') and contains(@data-ng-show,'!maAuto')]";
	public static String XP_Rejected_TV="//*[@id='welcome-message']//*[contains(text(),'Sorry M_FName, there seems to be a problem with your M_PACK with M_TVBOX order') and contains(@data-ng-show,'!maAuto')]";
	public static String XP_Wel_Warning="//*[@id='welcome-message' and contains(@class,'warning')]";
	public static String XP_PlaceOrder="//*[@id='welcome-message']//*[contains(text(),'Place another order')]";
	public static String XP_ToggeIcon="//*[@id='summary']//*[contains(@class,'toggleIcon')]";
	public static String XP_MissedApp="//*[@id='welcome-message']//*[contains(text(),'It looks like your appointment was missed. You can reschedule it from the relevant section below') and contains(@data-ng-show,'!maAuto')]";
//	public static String XP_ToggeIcon="//*[@id='summary']//*[contains(@class,'toggleIcon')]";
	public static String XP_BBAfterGolive=".//*[@id='welcome-message']//*[contains(text(),'a bit of a delay') and contains(text(),' working hard to get your services up and running as quickly as we can. Check back later for an update') and contains(@data-ng-show,'!maAuto')]";
	
	

	/****************************MYORDER XPATHS*************************************************************/
	public static String XP_ModName="//*[@id='summary']//*[contains(text(),'My M_PACK order')]";
	public static String XP_ModName_TV="//*[@id='summary']//*[contains(text(),'My M_PACK with M_TVBOX order')]";
	public static String XP_PackName="//*[@id='summary']//*[contains(text(),'M_PACK')]/..[contains(@class,'color_3')]";
	public static String XP_PackName_TV="//*[@id='summary']//*[contains(text(),'M_PACK with M_TVBOX')]/..[contains(@class,'color_3')]";
	public static String XP_SubPackName="//*[@id='summary']//*[contains(text(),'Broadband & calls')]";
	public static String XP_SubPackName_TV="//*[@id='summary']//*[contains(text(),'Broadband & calls & TV')]";
	public static String XP_PackagedetaislButton="//*[@id='summary']//*[contains(text(),'See all your package details')]";
	public static String XP_PackagedetaisText="//*[@id='fullpackage']//*[contains(text(),'Full package details')]";
	public static String XP_FP_Packname="//*[@id='fullpackage']//*[contains(text(),'M_PACK')]";
	public static String XP_FP_Packname_TV="//*[@id='fullpackage']//*[contains(text(),'M_PACK')]";
	public static String XP_FP_SubPackname_TV="//*[@id='fullpackage']//*[contains(text(),'(Broadband & calls & TV)')]";
	public static String XP_FP_SubPackname="//*[@id='fullpackage']//*[contains(text(),'(Broadband & calls)')]";
	public static String XP_FPButton="//*[@id='summary']//*[contains(text(),'See all your package details')]";
	
	
	
	/****************************APPOINTMENT CHANGE  XPATHS*************************************************************/
	public static String XP_ModName_EngApp="//*[@id='bbsetup']//*[contains(text(),'My engineer appointment')]";
	public static String XP_App_Reshedule="//*[@id='bbsetup']//*[contains(text(),'Reschedule appointment')]";
	public static String XP_date_Sel="//*[@id='date-picker-holder']//*[contains(@class,'special')]";
	public static String XP_date_Text="//*[@id='book-an-engineer']//*[contains(text(),'Choose a date for your engineer visit')]";
	public static String XP_Slot_Text="//*[@id='book-an-engineer-options']//label[contains(text(),'Choose a time for your engineer visit')]";
	public static String XP_Slot_selector="//*[@id='book-time-select']";
	public static String XP_ConfirmChanges="//*[@id='book-an-engineer-options']//*[contains(text(),'Confirm changes')]";
	public static String XP_ConfirmPage="//*[@id='book-an-engineer']//*[contains(text(),'Confirmation')]";
	public static String XP_Refresh="//*[@id='book-an-engineer']//*[contains(text(),'Refresh')]";
	public static String XP_ConfirmSlot="//*[@id='book-an-engineer']//*[contains(text(),'Between')]";
	public static String XP_ConfirmDate="//*[contains(@data-ng-show,'BB')]//*[contains(@class,'lrg-date moved color_3')]";
	public static String XP_WelcomeText="//*[@id='welcome-message']//*[contains(text(),'Your appointment has been changed')]";
	
	
	/****************************MY ROUTER DELIVERY*************************************************************/
	public static String XP_RouDel="//*[@id='bbdelivery']//*[contains(text(),'My Router delivery')]";
	public static String XP_ArriDueText="//*[@id='bbdelivery']//*[contains(text(),'This is due to arrive ')]";
	public static String XP_getDay="//*[@id='bbdelivery']//*[contains(@data-ng-bind,'dd')]";
	public static String XP_getMonth ="//*[@id='bbdelivery']//*[contains(@data-ng-bind,'MMMM')]";
	public static String XP_getDeliveryHPLink ="//*[@id='bbdelivery']//*[contains(text(),'Get delivery Support')]";
	public static String XP_checklatertext ="//*[@id='bbdelivery']//*[contains(text(),'too soon to send your router just yet. Check back later for tracking details')]";
	
	/****************************MY PHONE AND BB GOLIVE MODULE*************************************************************/
	public static String XP_PHONE_BB="//*[@id='bbsetup']//*[contains(text(),'My Phone & Broadband go live')]";
	public static String XP_GoliveText="//*[@id='bbsetup']//*[contains(@style,'display') and contains(@data-ng-show,'broadbandSetup')]//*[contains(text(),'Your Phone and Broadband service')]";
	public static String XP_CheckDay="//*[@id='bbsetup']//*[contains(@class,'color-green')]//*[contains(@data-ng-hide,'completed')]//*[contains(text(),'M_DAY')]";
	public static String XP_CheckMonth ="//*[@id='bbsetup']//*[contains(@class,'color-green')]//*[contains(@data-ng-hide,'completed')]//*[contains(text(),'M_Month')]";
	public static String XP_getSupportHPLink =".//*[@id='bbsetup']//*[contains(text(),'Get Phone and Broadband Support')]";
//	public static String XP_checklatertext ="//*[@id='bbdelivery']//*[contains(text(),'too soon to send your router just yet. Check back later for tracking details')]";
	
	
	
	
}