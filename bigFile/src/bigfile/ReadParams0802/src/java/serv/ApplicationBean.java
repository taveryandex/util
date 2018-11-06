package serv;
/*
import common.OraConnectionFab;
import common.RCException;
import common.RetCom;
import common.transport.JMSSessionFabs;
import common.transport.RouteMap;
import common.transport.WSSessionFabImpl;
import common.transport.WSSessionFabLocator;
import gpdoc.GPDocTransf;
*/
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.DependsOn;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;

@Singleton(name="ApplicationBean")
@LocalBean
@Startup
@DependsOn({"DBListenerPoolBean"})
public class ApplicationBean
{
  @EJB
  PropertiesBean propertiesBean;
  @EJB
  DBListenerPoolBean listenerPoolBean;
  
  @PostConstruct
  void init()
    throws EJBException
  {
    try
    {
        System.out.println("Start public class ApplicationBean.init() ");
        /*
      this.propertiesBean.setProperty("RetComVersion", RetCom.getVersion());
      PropertiesBean.getLogger().log(Level.INFO, "\nApplication: RetCom Version: {0} OK!", RetCom.getVersion());
      
      String checkVersion = RetCom.checkVersion(this.propertiesBean);
      if (!checkVersion.isEmpty()) {
        throw new Exception(checkVersion);
      }
      PropertiesBean.getLogger().log(Level.INFO, "\nApplication: RetCom Version: {0} OK!", RetCom.getVersion());
      
      String jdbcPoolName = this.propertiesBean.getProperty("jdbc.Name");
      initJdbcPool(jdbcPoolName);
      
      logLevelCfg(jdbcPoolName);
     
      RouteMap rMap = RouteMap.getInstance();
      rMap.init(this.propertiesBean);
       
      JMSSessionFabs.init(this.propertiesBean);
      */
      this.listenerPoolBean.StartListenerPool();
      /*
      registerWSImpl(this.propertiesBean.getProperty("WSIMPL"));
      
      GPDocTransf.SetValidator(this.propertiesBean);
      
      PropertiesBean.getLogger().log(Level.INFO, "{0} \nApplicationBean WelCome!", getClass().getName());
              */
    }
    catch (Exception e)
    {
        /*
      RCException rce = new RCException(e, getClass().getName());rce.log();
      throw new EJBException("PropertiesBean initialization error", e);
                */
    }
  }
  
  void initJdbcPool(String jdbcPoolNamee)
    throws InterruptedException, Exception
  {
    PropertiesBean.getLogger().log(Level.INFO, "\nApplicationBean initJdbcPool: {0}", jdbcPoolNamee);
    
    Properties props = new Properties();
    props.put("MinLimit", this.propertiesBean.getProperty(jdbcPoolNamee + ".MinLimit", "8"));
    props.put("MaxLimit", this.propertiesBean.getProperty(jdbcPoolNamee + ".MaxLimit", "60"));
    props.put("InitialLimit", this.propertiesBean.getProperty(jdbcPoolNamee + ".InitialLimit", "4"));
    props.put("ValidateConnection", this.propertiesBean.getProperty(jdbcPoolNamee + ".ValidateConnection", "true"));
   // OraConnectionFab.initConnectionPool(jdbcPoolNamee, props);
    
    PropertiesBean.getLogger().log(Level.FINE, "\nApplicationBean initJdbcPool Post: {0}", jdbcPoolNamee);
  }
  
  void registerWSImpl(String gatePointt)
  {
      /*
    PropertiesBean.getLogger().log(Level.INFO, "\nApplicationBean registerWSImpl: {0}", gatePointt);
    try
    {
      String className = this.propertiesBean.getProperty(gatePointt + ".Class");
      Class<?> c = Class.forName(className);
      Object o = c.newInstance();
      
      WSSessionFabImpl wsSessFabImpl = (WSSessionFabImpl)o;
      WSSessionFabLocator.getInstance().Register(gatePointt, wsSessFabImpl);
      
      PropertiesBean.getLogger().log(Level.FINE, "\nApplicationBean registerWSImpl Post: {0}", gatePointt);
    }
    catch (Exception e)
    {
      RCException rce = new RCException(e, getClass().getName());rce.log();
      throw new EJBException("PropertiesBean initialization error", e);
    }
              */
  }
  
  public void logLevelCfg(String jdbcPoolNamee)
  {
      /*
    PropertiesBean.getLogger().log(Level.INFO, "\nApplicationBean logLevelCfg");
    
    OracleConnection conn = null;
    try
    {
      conn = OraConnectionFab.captConnection(jdbcPoolNamee);
      OracleCallableStatement ocs = (OracleCallableStatement)conn.prepareCall("begin :crs := rram_handler.RetComLogLevels; end;");
      ocs.registerOutParameter(1, -10);
      ocs.execute();
      
      ResultSet cursor = ocs.getCursor(1);
      while (cursor.next())
      {
        String key = cursor.getString(1);
        String value = cursor.getString(2);
        
        this.propertiesBean.setLogLevelInt(key, value);
        
        PropertiesBean.getLogger().log(Level.FINE, "\nLogLevel {0}:{1}", new Object[] { key, value });
      }
      OraConnectionFab.releaseConnection(conn);
      
      PropertiesBean.getLogger().log(Level.FINE, "\nApplicationBean logLevelCfg Post");
    }
    catch (Exception e)
    {
      if (conn != null) {
        try
        {
          OraConnectionFab.releaseConnection(conn);
        }
        catch (Exception localException1) {}
      }
      RCException rce = new RCException(e, getClass().getName());rce.log();
    }
              */
  }
  
  @PreDestroy
  void shutdown()
  {
    PropertiesBean.SetShuttingDown();
    PropertiesBean.getLogger().log(Level.INFO, "{0} \nApplicationBean Bye!", getClass().getName());
  }
}
