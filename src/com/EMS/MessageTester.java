package com.EMS;


import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TemporaryQueue;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.NamingException;

public class MessageTester
{
  private Context jndiContext;
  private QueueConnectionFactory qconFactory;
  private String queueName;
  private String messageBody;
  private String fileName;
  private long timeOut;
  private String sourceId;
  private String businessEvent;
  private String schemaVersion;
  private String userId;
  public static  String responseMsg;
  
  public MessageTester(String pFileName)
  {
    this.fileName = pFileName;
  } 
  
  public static void MessageTester_test(String sFileName) throws Exception {
    MessageTester lTester = new MessageTester(sFileName);
    //System.out.println("in EMS sender");
    try
    {
      int iterations = 1;
      long delay = 60000L;
      Boolean waitForResponse = Boolean.FALSE;

      lTester.setUp();
      lTester.dispatchSynchronousMessage(waitForResponse);
/*       do
      {
        System.out.println("\n\n");
        Thread.sleep(delay);
        lTester.dispatchSynchronousMessage(waitForResponse);
		iterations--; } while (iterations > 0);*/
    } 
    catch (NumberFormatException e)
    {
      System.out.println("iterations and delay args must be numeric");
      e.printStackTrace();
	  throw e;
      //System.exit(0);
    } catch (Exception e) {
      e.printStackTrace();
	  throw e;
      //System.exit(0);
    } 
  } 
  
  @SuppressWarnings({ "unchecked", "resource" })
protected void setUp() throws Exception {
    if (this.qconFactory == null) {
      String lContextFactoryName = null;
      String lProviderUrl = null;
      try
      {
        BufferedReader in = new BufferedReader(new java.io.FileReader(this.fileName));
        
        StringBuffer buffer = new StringBuffer();
        String str;
        if (((str = in.readLine()) != null) && (!str.trim().equals("")))
        {
          lContextFactoryName = str;
        } else {
          throw new Exception("Config file incorrect format");
        } 
        
        if (((str = in.readLine()) != null) && (!str.trim().equals("")))
        {
          lProviderUrl = str;
        } else {
          throw new Exception("Config file incorrect format");
        } 
        
        if (((str = in.readLine()) != null) && (!str.trim().equals("")))
        {
          this.queueName = str;
        } else {
          throw new Exception("Config file incorrect format");
        } 
        
        if (((str = in.readLine()) != null) && (!str.trim().equals("")))
        {
          this.timeOut = Long.valueOf(str).longValue();
        } else {
          throw new Exception("Config file incorrect format");
        } 
        if (((str = in.readLine()) != null) && (!str.trim().equals("")))
        {
          this.sourceId = str;
        } else {
          throw new Exception("Config file incorrect format");
        } 
        if (((str = in.readLine()) != null) && (!str.trim().equals("")))
        {
          this.businessEvent = str;
        } else {
          throw new Exception("Config file incorrect format");
        } 
        if (((str = in.readLine()) != null) && (!str.trim().equals("")))
        {
          this.schemaVersion = str;
        } else {
          throw new Exception("Config file incorrect format");
        } 
        if (((str = in.readLine()) != null) && (!str.trim().equals("")))
        {
          this.userId = str;
        } else {
          throw new Exception("Config file incorrect format");
        } 
        
        while ((str = in.readLine()) != null)
        {
          buffer.append(str);
        } 
        
        this.messageBody = buffer.toString();
        
        in.close();
      } catch (IOException e) {
        System.out.print("Failure reading from config file");
        throw e;
      } catch (NumberFormatException e) {
        throw new Exception(
          "Config file incorrect format. Timeout value not a number");
      } 
      
      @SuppressWarnings("rawtypes")
	Hashtable env = new Hashtable();
      env.put("java.naming.factory.initial", lContextFactoryName);
      env.put("java.naming.provider.url", lProviderUrl);
      
      this.jndiContext = new javax.naming.InitialContext(env);
      Object obj = this.jndiContext.lookup("QueueConnectionFactory");
      this.qconFactory = ((QueueConnectionFactory)obj);
    } 
  } 
  
  private void dispatchSynchronousMessage(Boolean pWaitForResponse) throws Exception {
    TemporaryQueue temporaryQueue = null;
    QueueConnection qcon = null;
    QueueSession qsession = null;
    
    try
    {
      qcon = this.qconFactory.createQueueConnection();
     //System.out.println(qcon);
      qsession = qcon.createQueueSession(false, 1);
      qcon.start();
      temporaryQueue = qsession.createTemporaryQueue();
      
      long startTime = sendTextMessage(qsession, temporaryQueue);
    
      if ((pWaitForResponse != null) && (pWaitForResponse.booleanValue())) {
    	 
        long endTime = recieveMessage(qsession, temporaryQueue);
        
        
      } 
      
    }
    catch (Exception e) {
      throw e;
    } finally {
      if (qsession != null) {
        qsession.close();
      } 
      
      if (qcon != null) {
        qcon.close();
      }
	
    } 
  } 
  
  private long recieveMessage(QueueSession qsession, TemporaryQueue temporaryQueue) throws Exception
  {
    long endTime = 0L;
    
    javax.jms.QueueReceiver receiver = qsession.createReceiver(temporaryQueue);
    
    Message message = receiver.receive(this.timeOut);
    
    endTime = System.currentTimeMillis();
  //  System.out.println("Recieved message @ " + endTime);
    
    if (message != null) {
      if ((message instanceof TextMessage)) {
        TextMessage responceTxtMsg = (TextMessage)message;
        String response = responceTxtMsg.getText();
       responseMsg = response;
   
              
      } else {
        throw new Exception("Message Received in Unknown Format.");
      } 
    } else {
      throw new Exception("RequestTimeout");
    } 
    
    return endTime;
  } 
  
  private long sendTextMessage(QueueSession qsession, TemporaryQueue temporaryQueue) throws Exception
  {
    TextMessage txtMsg = null;
    QueueSender qsender = null;
    Queue queue = null;
    long startTime = 0L;
    try
    {
      queue = (Queue)this.jndiContext.lookup(this.queueName);
      
      txtMsg = qsession.createTextMessage();
      
      txtMsg.setJMSReplyTo(temporaryQueue);
      txtMsg.setText(this.messageBody);
      
      txtMsg = setMessageHeaderProperties(txtMsg, this.timeOut);
      
      qsender = qsession.createSender(queue);
      qsender.setTimeToLive(this.timeOut);
      qsender.setDeliveryMode(1);
      
      
      startTime = System.currentTimeMillis();
      qsender.send(txtMsg);
      
     
    } catch (NamingException e) {
    
      throw e;
    } catch (JMSException e) {
    
      throw e;
    } 
    
    return startTime;
  } 
  
  private TextMessage setMessageHeaderProperties(TextMessage txtMsg, long timeOut)
    throws Exception
  {
    try
    {
      if (timeOut != 0L) {
        txtMsg.setJMSExpiration(timeOut);
      } 
      
      txtMsg.setJMSType("TextMessage");
      txtMsg.setJMSCorrelationID(new Date().getTime() + "");
      txtMsg.setStringProperty("SourceId", this.sourceId);
      txtMsg.setStringProperty("BusinessEvent", this.businessEvent);
      txtMsg.setStringProperty("SchemaVersion", this.schemaVersion);
      txtMsg.setStringProperty("UserId", this.userId);
      txtMsg.setBooleanProperty("JMS_TIBCO_PRESERVE_UNDELIVERED", true);
      txtMsg.setStringProperty("SLATimeStamp", new Date().toString());
    } catch (JMSException e) {
      throw new Exception("Exception in setMessageHeaderProperties. ", e);
    } 
    return txtMsg;
  } 

  public static String  MessageTester_test_Synchronous(String sFileName) throws Exception {
	    MessageTester lTester = new MessageTester(sFileName);
	    //System.out.println("in EMS sender");
	    try
	    {
	      int iterations = 1;
	      long delay = 60000L;
	      Boolean waitForResponse = Boolean.TRUE;

	      lTester.setUp();
	      lTester.dispatchSynchronousMessage(waitForResponse);
	/*       do
	      {
	        System.out.println("\n\n");
	        Thread.sleep(delay);
	        lTester.dispatchSynchronousMessage(waitForResponse);
			iterations--; } while (iterations > 0);*/
	    } 
	    catch (NumberFormatException e)
	    {
	      System.out.println("iterations and delay args must be numeric");
	      e.printStackTrace();
		  throw e;
	      //System.exit(0);
	    } catch (Exception e) {
	      e.printStackTrace();
		  throw e;
	      //System.exit(0);
	    }finally{
	    	return responseMsg;
	    }
	  } 

} 