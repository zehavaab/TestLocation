package ibasis.net.portlet.test;

//import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import com.ibasis.portal.dal.entity.FraudAlertDetail;
import com.ibasis.portal.dal.entity.FraudCodeDef;
import com.ibasis.portal.dal.entity.FraudCustomRule;
import com.ibasis.portal.dal.entity.FraudCustomRuleDetail;
import com.ibasis.portal.dal.entity.FraudDefCustomRule;
import com.ibasis.portal.dal.entity.FraudDefCustomRuleDetail;
import com.ibasis.portal.dal.entity.FraudFilter;
import com.ibasis.portal.dal.entity.FraudSeverityConfiguration;
import com.ibasis.portal.dal.entity.FraudThresholdConfiguration;
import com.ibasis.portal.logic.FraudLogic;
import com.ibasis.portal.spring.SpringUtils;
import com.ibasis.portal.utils.FraudUtils;
import com.ibasis.portal.utils.enumurator.FraudAlertStatusEnum;
import com.ibasis.portal.utils.enumurator.FraudAlertTypeEnum;
import com.ibasis.portal.utils.enumurator.FraudCodeDefTypeEnum;
import com.ibasis.portal.utils.enumurator.FraudFilterNameEnum;
import com.ibasis.portal.utils.enumurator.FraudSeverityEnum;
import com.ibasis.portal.utils.enumurator.FraudSeverityTypeEnum;
import com.liferay.portal.kernel.util.DateUtil;


public class FraudTest {

	private static FraudLogic fraudLogic; 
	private static String existCustomerId ;
	private static String existRuleId ;
	
	@BeforeClass
	public static void init() { 
		fraudLogic = (FraudLogic) SpringUtils.locateBeanTest("fraudLogic");
		existCustomerId = "";
		existRuleId = "";
	}

	@Test
	public void initCodeDefMap(){
		List<FraudCodeDef> codeDefs = fraudLogic.getCodeDefList();
		System.out.println("Code Def List : ");
		for (FraudCodeDef fraudCodeDef : codeDefs) {
			System.out.println("Code Def - " + fraudCodeDef);
		}
		Assert.assertTrue(true);
	}
	
	@Test
	public void testGetAlertsDetails(){
		List<FraudFilter> fraudFilters = new ArrayList<FraudFilter>();
		//----------My Alerts or Community Alerts filter
		fraudFilters.add(new FraudFilter(FraudFilterNameEnum.ALERT_TYPE, Arrays.asList(FraudAlertTypeEnum.MY_FRAUD_ALERTS.getName())));
		
		//----------ALert Status filter
		fraudFilters.add(new FraudFilter(FraudFilterNameEnum.ALERT_STATUS , Arrays.asList(FraudAlertStatusEnum.ACKNOWLEDGE_BLOCKED.getName())));				
		
		//----------country filter
		fraudFilters.add(new FraudFilter(FraudFilterNameEnum.COUNTRY , null));
		
		//----------Threshold filter
		//fraudFilters.add(new FraudFilter(FraudFilterNameEnum.THRESHOLD_TYPE , Arrays.asList( FraudUtils.getCodeDefListByType(FraudCodeDefTypeEnum.FRAUDSVRT_CODE_TYPE.getName()).get(0).getDescription())));
		
		/*
		 * ---------Date filter
		 * fraudFilters.add(new FraudFilter(FraudFilterNameEnum.DATE_RANGE_FROM , Arrays.asList(DateUtil.newDate().toString())));
		 * fraudFilters.add(new FraudFilter(FraudFilterNameEnum.DATE_RANGE_TO , Arrays.asList(DateUtil.newDate().toString())));
		*/
		List<FraudAlertDetail> alertDetailsList = fraudLogic.getAlertsDetails(new BigDecimal(101301), 0, 15, fraudFilters, null, null);
		for (FraudAlertDetail fraudAlertDetail : alertDetailsList) {
			System.out.println(fraudAlertDetail.getAlertDetails());
		}
	} 
	
	@Test
	public void testGetAlertsDetailsSize(){
		long result = fraudLogic.getAlertsDetailsSize(new BigDecimal(101301),null, null, null);
		System.out.println("Alert details size: "+result);
	} 
	   
	@Test
	public void getCustomersCustomRuleList(){  
		try   
		{
			BigDecimal customerId = new BigDecimal(existCustomerId);
			List<FraudCustomRule> rulesList = fraudLogic.getCustomRuleList(customerId);
			Assert.assertNotNull(rulesList);
			
			System.out.println("Custom Rule List : ");
			for (FraudCustomRule customRule : rulesList) {
				Assert.assertNotNull(customRule);
				Assert.assertEquals(customerId, customRule.getCustomerId());
				System.out.println("Custom Rule - "+customRule);
				System.out.println("Custom Rule "+customRule.getRuleName()+" Details : ");
				 for (FraudCustomRuleDetail customAlertsDetail : customRule.getFraudCustomRuleDetails()){
					System.out.println("Alert Detail - "+customAlertsDetail);
				}
			}
			Assert.assertTrue(true);
		}
		catch (Exception e) {
			Assert.assertFalse(true);
		}
	}
	
	
	
	@Test
	public void getStandartThresholds() {
		try
		{	
			Map<String, Map<String , BigDecimal>> map = new HashMap<String, Map<String,BigDecimal>>();
			map = fraudLogic.getStandartConfigurations();
			Assert.assertNotNull(map);
			Assert.assertEquals(map.size(), 2);
	
			Assert.assertTrue(map.containsKey(FraudSeverityTypeEnum.MINUTES));
			System.out.println(FraudSeverityTypeEnum.MINUTES+" - "+map.get(FraudSeverityTypeEnum.MINUTES));
			
			Assert.assertTrue(map.get(FraudSeverityTypeEnum.MINUTES).containsKey(FraudSeverityEnum.INFO));
			Assert.assertTrue(map.get(FraudSeverityTypeEnum.MINUTES).containsKey(FraudSeverityEnum.WARNING));
			Assert.assertTrue(map.get(FraudSeverityTypeEnum.MINUTES).containsKey(FraudSeverityEnum.CRITICAL));
			
			Assert.assertTrue(map.containsKey(FraudSeverityTypeEnum.REVENUE));
			System.out.println(FraudSeverityTypeEnum.REVENUE+" - "+map.get(FraudSeverityTypeEnum.REVENUE));
			
			Assert.assertTrue(map.get(FraudSeverityTypeEnum.REVENUE).containsKey(FraudSeverityEnum.INFO));
			Assert.assertTrue(map.get(FraudSeverityTypeEnum.REVENUE).containsKey(FraudSeverityEnum.WARNING));
			Assert.assertTrue(map.get(FraudSeverityTypeEnum.REVENUE).containsKey(FraudSeverityEnum.CRITICAL));
			
			
			System.out.println("Standart Thresholds Map : ");
			for (Map<String, BigDecimal> standartThresholdsMap : map.values()) {
				for (BigDecimal standartThresholdsValue : standartThresholdsMap.values()) {
					System.out.println("Standart Threshold - "+standartThresholdsValue);
				}
			}
			Assert.assertTrue(true);
			
		}
		catch (Exception e) {
			Assert.assertFalse(true);
		}
	}
	
	@Test
	public void getThresholdTypes(){
		try
		{
			List<FraudCodeDef> codeDefList = FraudUtils.getCodeDefListByType(FraudCodeDefTypeEnum.FRAUDSVRTTYPE_CODE_TYPE.getName());
			Assert.assertNotNull(codeDefList);
			Assert.assertFalse(codeDefList.isEmpty());
			Assert.assertTrue(codeDefList.size()<3);
			
			System.out.println("Threshold's Types : ");
			for (FraudCodeDef currCodeDef : codeDefList) {
				Assert.assertTrue(currCodeDef.getOperator().equals(FraudUtils.getCodeDef(FraudCodeDefTypeEnum.FRAUDSVRTTYPE_CODE_TYPE.getName(),FraudSeverityTypeEnum.REVENUE.getName())) || currCodeDef.getOperator().equals(FraudUtils.getCodeDef(FraudCodeDefTypeEnum.FRAUDSVRTTYPE_CODE_TYPE.getName(),FraudSeverityTypeEnum.MINUTES.getName())));
				System.out.println("Threshold Type - "+ currCodeDef);
			}
			Assert.assertTrue(true);
		}
		catch (Exception e) {
			Assert.assertFalse(true);
		}
	}
	
	
	@Test
	public void getThresholdConfiguration(){
		try
		{
			BigDecimal customerId = null ;
			FraudThresholdConfiguration thresholdConfiguration = fraudLogic.getThresholdConfiguration(customerId);
			Assert.assertNull(thresholdConfiguration);
			
			customerId = new BigDecimal(existCustomerId);			
			thresholdConfiguration = fraudLogic.getThresholdConfiguration(customerId);
			Assert.assertNotNull(thresholdConfiguration);

			Assert.assertTrue(thresholdConfiguration.getSeverityType().equals(FraudUtils.getCodeDef(FraudCodeDefTypeEnum.FRAUDSVRTTYPE_CODE_TYPE.getName(),FraudSeverityTypeEnum.REVENUE.getName())) || thresholdConfiguration.getSeverityType().equals(FraudUtils.getCodeDef(FraudCodeDefTypeEnum.FRAUDSVRTTYPE_CODE_TYPE.getName(),FraudSeverityTypeEnum.MINUTES.getName())));
			System.out.println("Threshold Configuration : "+ thresholdConfiguration);
			Assert.assertTrue(true);
		
		}
		catch (Exception e) {
			Assert.assertFalse(true);
		}
	}
	
	@Test
	public void getSeverityConfigurations(){
		try
		{
			BigDecimal customerId = null;
			List<FraudSeverityConfiguration> severitiesList = fraudLogic.getSeverityConfigurations(customerId);
			Assert.assertNull(severitiesList);
			
			customerId = new BigDecimal(existCustomerId);
			severitiesList = fraudLogic.getSeverityConfigurations(customerId);
			Assert.assertNotNull(severitiesList);
			Assert.assertFalse(severitiesList.isEmpty());
			Assert.assertEquals(severitiesList.size(), 3);
			
			System.out.println("Severity Configurations : ");
			for (FraudSeverityConfiguration currSeverityConf : severitiesList) {
				Assert.assertTrue(currSeverityConf.getId().getSeverity().equals(FraudUtils.getCodeDef(FraudCodeDefTypeEnum.FRAUDSVRT_CODE_TYPE.getName(),FraudSeverityEnum.INFO.getName())) || currSeverityConf.getId().getSeverity().equals(FraudUtils.getCodeDef(FraudCodeDefTypeEnum.FRAUDSVRT_CODE_TYPE.getName(),FraudSeverityEnum.WARNING.getName())) || currSeverityConf.getId().getSeverity().equals(FraudUtils.getCodeDef(FraudCodeDefTypeEnum.FRAUDSVRT_CODE_TYPE.getName(),FraudSeverityEnum.CRITICAL.getName())));
				System.out.println("Severity Configuration - "+currSeverityConf);
			}
			Assert.assertTrue(true);
		}
		catch (Exception e) {
			Assert.assertFalse(true);
		}
	}
	//f

	
    @Test
    public void getDefaultCustomRuleList(){	 
		 try{		
			 List<FraudDefCustomRule> defaultRuleAlertsList = fraudLogic.getDefaultCustomRuleList();
			 System.out.println("Default Custom Rule Alerts : ");
			 for (FraudDefCustomRule fraudDefCustomRuleAlert : defaultRuleAlertsList) {
				 // need to print row by row the alert's details??
				 System.out.println("Default Custom Rule Alert - "+fraudDefCustomRuleAlert);
				 System.out.println("Alert "+fraudDefCustomRuleAlert.getRuleName()+" Details : ");
				 for (FraudDefCustomRuleDetail defCustomAlertsDetail : fraudDefCustomRuleAlert.getFraudDefCustomRuleDetails()){
					System.out.println("Alert Detail - "+defCustomAlertsDetail);
				}
				 Assert.assertTrue(true);
			}
			}catch(Exception exception) {
				Assert.assertFalse(true);	
			}
	 }


    @Test
    public void getDefaultRuleAlertDetailsList(){	 
		 try{
			 Long ruleId = new Long(existRuleId);
			 List<FraudDefCustomRuleDetail> defaultRuleAlertDetailsList = fraudLogic.getDefaultRuleAlertDetailsList(ruleId);
			 System.out.println("Default Rule Alert Details : ");
			 for (FraudDefCustomRuleDetail defCustomAlertsDetail : defaultRuleAlertDetailsList) {
				 System.out.println("Default Rule Alert Detail - "+defCustomAlertsDetail);
			}
			 Assert.assertTrue(true);
			 
		 }catch(Exception exception) {
			 Assert.assertFalse(true);	
		}
	 }
    

    /**
     * This method save or update custom rule alert.
     * @param customRuleAlerts
     */
    @Test
    public void saveCustomRulesDetails(){
    	try{
    		List<FraudCustomRule> customRuleAlerts = new ArrayList<FraudCustomRule>();
    		for(int i =0 ; i<3;i++)
    		{
	    		Set<FraudCustomRuleDetail> custAlertsDetailSet = new HashSet<FraudCustomRuleDetail>();
	    		FraudCustomRule customRuleAlert = new FraudCustomRule();//new Date(), new BigDecimal(existCustomerId), "billing", "ruleDescritption", "ruleName", new Date(),custAlertsDetailSet );
	    		for(int j =0 ;j<2;j++){
	    			FraudCustomRuleDetail custAlertsDetail = new FraudCustomRuleDetail();//"GNE", "10", "var", i, "billing", new Date(), new Date(), customRuleAlert);
	    			custAlertsDetailSet.add(custAlertsDetail);
	    		}
	    		customRuleAlerts.add(customRuleAlert);
    		}
    		fraudLogic.saveCustomRulesDetails(customRuleAlerts);	
    		   		
    		List<FraudCustomRule> customRuleAlertsList = fraudLogic.getCustomRuleList(new BigDecimal(existCustomerId));
    		for (FraudCustomRule customRuleAlert : customRuleAlerts) {
    			Assert.assertTrue(customRuleAlertsList.contains(customRuleAlert)); 
			}
    		Assert.assertTrue(true);
		}catch(Exception exception) {
			Assert.assertFalse(true);	
		}
    }
    

    /**
     * This method save or update custom rule alert.
     * @param customRuleAlerts
     */
    @Test
    public void saveCustomRuleDetails(){
    	try{
    		Set<FraudCustomRuleDetail> custAlertsDetailSet = new HashSet<FraudCustomRuleDetail>();
    		FraudCustomRule customRuleAlert = new FraudCustomRule();// new Date(), new BigDecimal(existCustomerId), "billing", "ruleDescritption", "ruleName", new Date(),custAlertsDetailSet );
    		for(int j =0 ;j<2;j++){
    			FraudCustomRuleDetail custAlertsDetail = new FraudCustomRuleDetail();//"GNE", "10", "var", new Long(existRuleId), "billing", new Date(), new Date(), customRuleAlert);
    			custAlertsDetailSet.add(custAlertsDetail);
    		}
    		
    		fraudLogic.saveCustomRuleDetails(customRuleAlert);	
    		  		   		
    		List<FraudCustomRule> customRuleAlertsList = fraudLogic.getCustomRuleList(new BigDecimal(existCustomerId));
    		Assert.assertTrue(customRuleAlertsList.contains(customRuleAlert)); 
		}catch(Exception exception) {
			Assert.assertFalse(true);	
		}
    }
    
    /**
     * This method delete custom rule alert by rule id.
     * @param ruleId
     */
    @Test
    public void deleteCustomRule() {
    	try{	
    		Long ruleIdToDelete = new Long("12121");
    		
    		Set<FraudCustomRuleDetail> custAlertsDetailSet = new HashSet<FraudCustomRuleDetail>();
    		FraudCustomRule customRuleAlert = new FraudCustomRule();// new Date(), new BigDecimal(existCustomerId), "billing", "ruleDescritption", "ruleName", new Date(),custAlertsDetailSet );
    		for(int j =0 ;j<2;j++){
    			FraudCustomRuleDetail custAlertsDetail = new FraudCustomRuleDetail();//"GNE", "10", "var", ruleIdToDelete, "billing", new Date(), new Date(), customRuleAlert);
    			custAlertsDetailSet.add(custAlertsDetail);
    		}
    		  		
			fraudLogic.deleteCustomRule(ruleIdToDelete);	
			
			List<FraudCustomRule> customRuleAlertsList = fraudLogic.getCustomRuleList(new BigDecimal(existCustomerId));
    		Assert.assertFalse(customRuleAlertsList.contains(customRuleAlert)); 
		
		}catch(Exception exception) {
			Assert.assertFalse(true);	
		}
    }
    
    
	
	
	@Test
	public void getALertsDetails() {
//		BigDecimal customerId = new BigDecimal(existCustomerId);
//		int startIndex = 0;
//		int count = 5 ;
//		Object[] filter = null;
//		boolean[] ascendingStates = null;
//		Object[] sortPropertyIds = null;
//		try{
//			List<FraudAlertDetail> fraudAlertDetails = fraudLogic.getALertsDetails(customerId, startIndex, count, filter, ascendingStates, sortPropertyIds);
//			System.out.println("Alerts Details : ");
//			for (FraudAlertDetail fraudAlertDetail : fraudAlertDetails) {
//				System.out.println("Alert Details - "+fraudAlertDetail);
//			}
//			Assert.assertTrue(true);
//		}
//		catch (Exception e) {
//			Assert.assertFalse(true);
//		}
		
	
	}
	
//	@Test
//	public void getALertsDetailsSize() {
//		try{
//			BigDecimal newCustomerId = new BigDecimal("1212");
//			int sizeOfAlertDetails = 3;
//			Set<CustomAlertsDetail> custAlertsDetailSet = new HashSet<CustomAlertsDetail>();
//    		CustomRuleAlert customRuleAlert = new CustomRuleAlert(new Long(existRuleId), new Date(), newCustomerId, "billing", "ruleDescritption", "ruleName", new Date(),custAlertsDetailSet );
//    		for(int j =0 ;j<sizeOfAlertDetails;j++){
//    			CustomAlertsDetail custAlertsDetail = new CustomAlertsDetail("GNE", "10", "var", new Long(existRuleId), "billing", new Date(), new Date(), customRuleAlert);
//    			custAlertsDetailSet.add(custAlertsDetail);
//    		}
//    		
//    		fraudLogic.saveCustomRuleDetails(customRuleAlert);
//    		
//			int alertsDetailsSize = fraudLogic.getALertsDetailsSize(newCustomerId, null, null, null);
//			Assert.assertEquals(sizeOfAlertDetails,alertsDetailsSize);
//		}
//		catch (Exception e) {
//			Assert.assertFalse(true);
//		}
//		
//	}

	
	@Test
	public  void saveSeverityConfiguration(){
		try{
			int size = 3;
			List<FraudSeverityConfiguration> severityConfigurations = new ArrayList<FraudSeverityConfiguration>();
			for(int i =0; i<size;i++){
			//	severityConfigurations.add(new FraudSeverityConfiguration(new FraudSeverityConfigurationPK(new BigDecimal(existCustomerId), FraudSeverityEnum.fromInt(i).getName()),new BigDecimal("1000"), false, "email@notif.com;email2@notifi2.com", "0202457821,0213456789,010245658912"));
			}
			
			fraudLogic.saveSeverityConfiguration(severityConfigurations);
			
			List<FraudSeverityConfiguration> actualSeverityConfigurations  = fraudLogic.getSeverityConfigurations(new BigDecimal(existCustomerId));
			for (FraudSeverityConfiguration severityConf : severityConfigurations) {
				Assert.assertTrue(actualSeverityConfigurations.contains(severityConf));
			}
			Assert.assertTrue(true);
		}
		catch (Exception e) {
			Assert.assertFalse(true);
		}
	}
	@Test
	public  void saveThresholdConfiguration( ){
		 try{
			// FraudThresholdConfiguration thresholdConfiguration = new FraudThresholdConfiguration(new FraudThresholdConfigurationPK(new BigDecimal(existCustomerId), FraudSeverityTypeEnum.MINUTES.getName()), new Date(), "user", true);
			// fraudLogic.saveThresholdConfiguration(thresholdConfiguration);
			 
			// FraudThresholdConfiguration thresholdConf =  fraudLogic.getThresholdConfiguration(new BigDecimal(existCustomerId));
			 
			// Assert.assertEquals(thresholdConfiguration, thresholdConf);
		 }
			catch (Exception e) {
				Assert.assertFalse(true);
			}
		}
	
//	@Test
//  public void getVariableList(){
//      try
//      {
//          List<CustomRuleVariable> ruleVariableList = fraudLogic.getVariableList();
//          Assert.assertNull(ruleVariableList);
//          System.out.println("Variable's List : ");
//          for (CustomRuleVariable ruleVariable : ruleVariableList) {             
//              System.out.println("Variable : "+ ruleVariable);
//          }
//      }
//      catch (Exception e) {
//          Assert.assertFalse(true);
//      }
//  }

}
	
