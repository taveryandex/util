package common;

//import com.sun.jna.Native;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.pool.OracleConnectionCacheManager;
import oracle.jdbc.pool.OracleDataSource;

public class OraConnectionFab
{
  private static HashMap<String, OracleDataSource> hmODS = new HashMap();
  
  public static void refreshOdsCache(String jdbcName, String refreshMode)
    throws Exception
  {
    try
    {
      OracleConnectionCacheManager occm = OracleConnectionCacheManager.getConnectionCacheManagerInstance();
      if (refreshMode.equals("REFRESH_ALL_CONNECTIONS")) {
        occm.refreshCache(jdbcName, 8192);
      } else {
        occm.refreshCache(jdbcName, 4096);
      }
    }
    catch (Exception e)
    {
      ///RCException rce = new RCException(e, "OraConnectionFab.refreshOdsCache");rce.log();
    }
  }
  
  public static OracleConnection captConnection(String jdbcName)
    throws Exception
  {
    System.out.println("Start OraConnectionFab.captConnection()");
    OracleDataSource ods = null;
    if (hmODS.containsKey(jdbcName))
    {
      ods = (OracleDataSource)hmODS.get(jdbcName);
    }
    else
    {
      Context context = new InitialContext();
      
      DataSource dataSource = (DataSource)context.lookup(jdbcName);
      ods = (OracleDataSource)dataSource.unwrap(OracleDataSource.class);
      ods.setConnectionCachingEnabled(true);
      ods.setConnectionCacheName(jdbcName);
      Properties prop = new Properties();
      prop.setProperty("MinLimit", "0");
      prop.setProperty("MaxLimit", "60");
      prop.setProperty("InitialLimit", "4");
      prop.setProperty("ValidateConnection", "true");
      
      ods.setConnectionCacheProperties(prop);
      
      hmODS.put(jdbcName, ods);
    }
    OracleConnection conn;
    try
    {
      conn = (OracleConnection)ods.getConnection();
    }
    catch (Exception e)
    {
        /*
      OracleConnection conn;
      RCException rce = new RCException(e, "OraConnectionFab.captConnection");rce.log();
      */
      refreshOdsCache(jdbcName, "REFRESH_INVALID_CONNECTIONS");
      conn = (OracleConnection)ods.getConnection();
    }
    return conn;
  }
  
  public static OracleConnection captConnectionUP(String jdbcName)
    throws Exception
  {
    OracleDataSource ods = null;
    if (hmODS.containsKey(jdbcName))
    {
      ods = (OracleDataSource)hmODS.get(jdbcName);
    }
    else
    {
      Context context = new InitialContext();
      
      DataSource dataSource = (DataSource)context.lookup(jdbcName);
      ods = (OracleDataSource)dataSource.unwrap(OracleDataSource.class);
      hmODS.put(jdbcName, ods);
    }
    OracleConnection conn = (OracleConnection)ods.getConnection();
    
    return conn;
  }
  
  public static OracleConnection captConnection(String jdbcName, int initilaze, String tessPath)
    throws Exception
  {
    OracleConnection conn = captConnection(jdbcName);
    String schm = conn.getCurrentSchema();
    
    int ipos = schm.indexOf("_");
    if (ipos != -1) {
      try
      {
        String bcrt = schm.substring(0, ipos);String logn = schm.substring(ipos + 1);
        
        String iLevlKey = "InitLevel";
        String iLevl = conn.getProperties().getProperty(iLevlKey, "0");
        if ((initilaze == 1) && (iLevl.equals("0")))
        {
          ///ITess iTess = (ITess)Native.loadLibrary(tessPath, ITess.class);
          String abcd = "abcd";
          byte[] dst = new byte['?'];
          //int iret = iTess.eval(abcd, bcrt, dst);
          //String eval = Utilities.convertByteToString(dst);
          String eval="AMDNQIHB";
          String strRole = "connect, resource, " + bcrt + "$$UC identified by \"" + eval + "\"";
          String strSQL = "begin dbms_session.set_role(:strRole); end;";
          OracleCallableStatement ocs = (OracleCallableStatement)conn.prepareCall(strSQL);
          ocs.setStringAtName("strRole", strRole);
          ocs.execute();
          conn.getProperties().setProperty(iLevlKey, "1");
        }
      }
      catch (Exception e)
      {
        //RCException rce = new RCException(e, "OraConnectionFab.OracleConnection");rce.log();
      }
    }
    return conn;
  }
  
  public static void initConnectionPool(String jdbcName, Properties props)
    throws Exception
  {
    OracleDataSource ods = null;
    if (hmODS.containsKey(jdbcName)) {
      return;
    }
    Context context = new InitialContext();
    
    DataSource dataSource = (DataSource)context.lookup(jdbcName);
    ods = (OracleDataSource)dataSource.unwrap(OracleDataSource.class);
    ods.setConnectionCachingEnabled(true);
    ods.setConnectionCacheName(jdbcName);
    Properties prop = new Properties();
    
    ods.setConnectionCacheProperties(props);
    
    hmODS.put(jdbcName, ods);
  }
  
  public static void releaseConnection(OracleConnection conn)
    throws Exception
  {
    conn.close();
  }
  
  public static void closePoolODS(String jdbcName)
    throws Exception
  {
    if (!hmODS.containsKey(jdbcName)) {
      return;
    }
    ((OracleDataSource)hmODS.get(jdbcName)).close();
  }
  
  public static void closeAll()
  {
    for (Map.Entry<String, OracleDataSource> entry : hmODS.entrySet())
    {
      try
      {
        ((OracleDataSource)entry.getValue()).close();
      }
      catch (Exception localException) {}
      hmODS.clear();
    }
  }
}
