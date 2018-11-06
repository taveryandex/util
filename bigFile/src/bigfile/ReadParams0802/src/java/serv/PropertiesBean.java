package serv;
/*
import common.GTContexts;
import common.IDocTransf.eDocType;
import common.IDocTransf.eSyncMode;*/
import common.IProperties;
/*
import common.RCException;
import common.Utilities;
import common.XLogger;
import common.XLoggers;
*/
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.Local;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

@Singleton(name="PropertiesBean")
@LocalBean
@Startup
@Local({IProperties.class})
public class PropertiesBean
  implements IProperties
{
  @Resource(lookup="java:app/AppName")
  String appName;
  @Resource(lookup="java:module/ModuleName")
  String modName;
  Properties props;
  static boolean shuttingDown = false;
  public static final String syncTimeOutDefault = "10000";
  public static final String asyncTimeOutDefault = "10000";
  private String jmsChannelRCASYNCOUT0;
  private String jmsChannelRCASYNCIN0;
  private String jmsChannelRCSYNCOUT0;
  private String jmsChannelRCSYNCINRS0;
  public static final String chanRCOUTSUFFIX = "RCOut";
  public static final String chanRCINSUFFIX = "RCIn";
  
  public static boolean GetShuttingDown()
  {
    return shuttingDown;
  }
  
  public static void SetShuttingDown()
  {
    shuttingDown = true;
  }
  
  private static Logger logger = null;
  
  public static Logger getLogger()
  {
    return logger;
  }
 /* 
 static XLoggers xloggers = null;
  
  public XLogger getXLogger(String loggerNamee)
  {
    return xloggers.getLogger(loggerNamee);
  }
 
  public void SetXLoggers()
  {
    try
    {
      InputStream log4jStream = null;
      if (this.props.getProperty("useCfgRelativePath", "Y").startsWith("Y")) {
        log4jStream = PropertiesBean.class.getResourceAsStream("/META-INF/log4j.properties");
      } else {
        log4jStream = new FileInputStream(new File(this.props.getProperty("appMetaInf") + "/log4j.properties"));
      }
      if (log4jStream == null)
      {
        xloggers = new XLoggers(this.appName);
      }
      else
      {
        Properties loggerProps = new Properties();
        loggerProps.load(log4jStream);
        log4jStream.close();
        
        xloggers = new XLoggers(loggerProps, this.appName);
      }
    }
    catch (Exception e)
    {
      xloggers = new XLoggers(this.appName);
    }
  }
  
  static GTContexts gtContexts = null;
   */
  public static String jmsPrefix;
  public static String RCAppName;

  @PostConstruct
  void init()    
    throws EJBException
  {
    try
    {
      logger = Logger.getLogger(this.appName);
        
      //RCException.setLogger(logger);
      
      logger.log(Level.INFO, "{0} Initialization!", getClass().getName());
      System.out.println("this.appName ="+this.appName);
      System.out.println("this.modName ="+this.modName);
         
      this.props = new Properties();

        System.out.println("getResource1 = "+this.getClass().getClassLoader().getResource("/app.properties").getPath().toString());
        //InputStream propsStream = PropertiesBean.class.getResourceAsStream("/app.properties");  
        InputStream propsStream = this.getClass().getClassLoader().getResourceAsStream("app.properties");  
   
      this.props.load(propsStream);
      propsStream.close();
      
      int ipos = this.appName.indexOf("-ejb");
      if (ipos != -1) {
        RCAppName = this.appName.substring(0, ipos);
      }
      setProperty("appName", this.appName);
      setProperty("modName", this.modName);
      setProperty("RCAppName", RCAppName);

      logger.log(Level.INFO, "\nappName : {0}, modName : {1}, RCAppName : {2}", new String[] { this.appName, this.modName, RCAppName });
      
      jmsPrefix = getProperty("JMS.Prefix");
    /*     
      this.jmsChannelRCASYNCOUT0 = (jmsPrefix + RCAppName + "AsyncOut0");
      this.jmsChannelRCASYNCIN0 = (jmsPrefix + RCAppName + "AsyncIn0");
      this.jmsChannelRCSYNCOUT0 = (jmsPrefix + RCAppName + "SyncOut0");
      this.jmsChannelRCSYNCINRS0 = (jmsPrefix + RCAppName + "SyncInRs0");

      if (getProperty("Sync.Timeout") == null) {
        setProperty("Sync.Timeout", "10000");
      }
      if (getProperty("Async.Timeout") == null) {
        setProperty("Async.Timeout", "10000");
      }
      setProperty("JMS.TopicConnectionFactory.Name", jmsPrefix + getProperty("TopicConnectionFactory.Name"));
      setProperty("JMS.QueueConnectionFactory.Name", jmsPrefix + getProperty("QueueConnectionFactory.Name"));
      
      logger.log(Level.INFO, "\nWSIMPL : {0} ", getProperty("WSIMPL"));
       
      SetXLoggers();
      
      gtContexts = new GTContexts(this.appName);
      
      logger.setLevel(Level.parse(getProperty(this.appName + ".LogLevel", "INFO")));
      
      logger.log(Level.INFO, "{0} \nPropertiesBean WelCome!", getClass().getName());
        */
    }
    catch (Exception e)
    {
      //RCException rce = new RCException(e, getClass().getName());rce.log();
      throw new EJBException("PropertiesBean initialization error", e);
    }
          
  }
  
  public String getProperty(String name)
  {
    return this.props.getProperty(name);
  }
  
  public String getProperty(String name, String defVal)
  {
    return this.props.getProperty(name, defVal);
  }
  
  public void setProperty(String name, String value)
  {
    this.props.setProperty(name, value);
  }
  
  public void setLogLevelInt(String sysn, String level)
  {
    int levelInt = Level.parse(level).intValue();
    if (levelInt == 0) {
      levelInt = 800;
    }
    this.props.setProperty(sysn + ".LogLevel", String.valueOf(levelInt));
  }
  
  public boolean hasProperty(String name)
  {
    return this.props.containsKey(name);
  }
  
  public Properties getProperties()
  {
    return this.props;
  }
  
  public void setRCChannels(String gateNamee, String gateRCChannelss)
  {
    String[] RCChannels = gateRCChannelss.split(",");
    if (RCChannels.length > 0) {
      setProperty(gateNamee + "RCOut", RCChannels[0]);
    }
    if (RCChannels.length > 1) {
      setProperty(gateNamee + "RCIn", RCChannels[1]);
    }
  }
  /*
  public String getJMSChanIn(String gateNamee, IDocTransf.eDocType edocTypee, IDocTransf.eSyncMode esyncModee)
  {
      
    String jmsChanIn = getProperty(gateNamee + "RCIn");
    if ((jmsChanIn == null) || (jmsChanIn.isEmpty())) {
      jmsChanIn = this.jmsChannelRCASYNCIN0;
    }
    return jmsChanIn;
  }
  
  public String getJMSChanOut(String gateNamee, IDocTransf.eDocType edocTypee, IDocTransf.eSyncMode esyncModee)
  {
    String jmsChanOut = getProperty(gateNamee + "RCOut");
    if ((jmsChanOut == null) || (jmsChanOut.isEmpty())) {
      if (esyncModee == IDocTransf.eSyncMode.EASYNC) {
        jmsChanOut = this.jmsChannelRCASYNCOUT0;
      } else {
        switch (edocTypee)
        {
        case IN_REQUEST: 
        case IN_RESPONSE: 
          jmsChanOut = this.jmsChannelRCSYNCINRS0; break;
        case OUT_RESPONSE: 
        case OUT_REQUEST: 
          jmsChanOut = this.jmsChannelRCSYNCOUT0; break;
        default: 
          jmsChanOut = "";
        }
      }
    }
    return jmsChanOut;
  }
  
  public String getXsltStr(String xsltName)
  {
    String result = "";
    try
    {
      InputStream xStream = PropertiesBean.class.getResourceAsStream("/xslt/" + getProperty(xsltName));
      result = Utilities.convertStreamToString(xStream);
      xStream.close();
    }
    catch (IOException ioe)
    {
      RCException rce = new RCException(ioe, getClass().getName());rce.log();
    }
    return result;
  }
  
  public Source getXsltSource(String xsltName)
  {
    Source xsltSource = null;
    try
    {
      String fName = PropertiesBean.class.getResource("/xslt/" + getProperty(xsltName)).getFile();
      xsltSource = new StreamSource(new File(fName));
    }
    catch (Exception e)
    {
      RCException rce = new RCException(e, getClass().getName());rce.log();
    }
    return xsltSource;
  }
  
  public Logger getULogger()
  {
    return getLogger();
  }
  */
  @PreDestroy
  void shutdown()
  {
   // GTContexts.clear(this);
  }
}
