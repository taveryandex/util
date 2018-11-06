package serv;
/*
import common.RCException;
*/
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.DependsOn;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.naming.InitialContext;
import javax.naming.NamingException;

@Singleton
@LocalBean
@Startup
@DependsOn({"PropertiesBean"})
public class DBListenerPoolBean
{
  @EJB
  PropertiesBean propertiesBean;
  int DBListenerNumber;
  String DBListenerName;
  HashMap<String, Future<String>> futStrPool;
  
  @PostConstruct
  void init()
    throws EJBException
  {
    try
    {
        /*
      String appName = this.propertiesBean.getProperty("appName");
      String modName = this.propertiesBean.getProperty("modName");
        */
      String modName = "ResieveFromDB0702";
      //this.DBListenerNumber = Integer.parseInt(this.propertiesBean.getProperty("Listener.Number"));
      this.DBListenerNumber = 3;
      this.DBListenerName = ("java:global/" + modName + "/DBListenerBean");
      this.futStrPool = new HashMap();
      
      PropertiesBean.getLogger().log(Level.INFO, "{0} \nDBListenerPoolBean DBListenerName: ", this.DBListenerName);
      PropertiesBean.getLogger().log(Level.INFO, "{0} \nDBListenerPoolBean WelCome!", getClass().getName());
    }
    catch (Exception e)
    {
/*
        RCException rce = new RCException(e, getClass().getName());rce.log();
      throw new EJBException("PropertiesBean initialization error", e);
        */
    }
  }
  
  Future<String> activateListener()
    throws NamingException, InterruptedException
  {
      System.out.println("Start activateListener()");
      
    InitialContext ctx = new InitialContext();
    DBListenerBean dbListener = (DBListenerBean)ctx.lookup(this.DBListenerName);
     
    
       
    while (dbListener == null)
    {
      Thread.sleep(3000L);
      dbListener = (DBListenerBean)ctx.lookup(this.DBListenerName);
    }
//    Future<String> futStr = dbListener.ListenCRS();
//    return futStr;
               
     return null;
  }
  
  @Asynchronous
  public Future<String> StartListenerPool()
  {
      System.out.println(" public Future<String> StartListenerPool() ");
    String status = "";
    try
    {
      for (int ii = 0; ii < this.DBListenerNumber; ii++)
      {

        //PropertiesBean.getLogger().log(Level.INFO, "{0} \nDBListenerPoolBean activateListener!", getClass().getName());
        System.out.println("DBListenerPoolBean.StartListenerPool()  for (int ii = 0; ii < this.DBListenerNumber; ii++)");
        System.out.println("Start HashMap<String, Future<String>> futStrPool;");
        this.futStrPool.put(String.valueOf(ii), activateListener());
      }
      while (!PropertiesBean.GetShuttingDown()) {
        try
        {
          Thread.sleep(60000L);
          
          Iterator it = this.futStrPool.keySet().iterator();
          while (it.hasNext())
          {
            String si = it.next().toString();
            Future<String> futStr = (Future)this.futStrPool.get(si);
            if (futStr.isDone()) {
              this.futStrPool.put(si, activateListener());
            }
          }
        }
        catch (Exception e)
        {
//          RCException rce = new RCException(e, getClass().getName());rce.log();
        }
      }
    }
    catch (Exception e)
    {
    //  RCException rce = new RCException(e, getClass().getName());rce.log();
    }
    return new AsyncResult(status);
  }
}
