package serv;
///*
//import common.GTContext;
//import common.GTContexts;
//import common.IDocTransf.eDocType;
//*/
//import common.OraConnectionFab;
///*
//import common.RCException;
//import common.RQContext;
//import common.Utilities;
//import gpdoc.GPDocTransf;
//import gpdoc.GPDocTransf.eDocLevel;
// */
//import java.io.Reader;
//import java.sql.Clob;
//import java.sql.ResultSet;
//import java.util.Properties;
//import java.util.concurrent.Future;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javax.ejb.AsyncResult;
//import javax.ejb.Asynchronous;
//import javax.ejb.EJB;
//import javax.ejb.LocalBean;
//import javax.ejb.Stateless;
//import oracle.jdbc.OracleCallableStatement;
//import oracle.jdbc.OracleConnection;
//import oracle.sql.CLOB;
//
//@Stateless(name="DBListenerBean")
//@LocalBean
public class DBListenerBean
{}
//  @EJB
//  PropertiesBean propertiesBean;
//  /*
//  @EJB
//  DispatchBean dispatchBean;
//  */
//  String jdbcName;
//  int ListenerTimeout;
//  String ListenerSignal;
//  String appOrg;
//  String appName;
//  String appVersion;
//  
//  void init()
//  {
//    try
//    {
//      //this.jdbcName = this.propertiesBean.getProperty("jdbc.Name");
//      this.jdbcName = "jdbc/3CRPool";
//      //this.ListenerTimeout = Integer.parseInt(this.propertiesBean.getProperty("Listener.Timeout"));
//      this.ListenerTimeout = 120;
//      //this.ListenerSignal = this.propertiesBean.getProperty("Listener.Signal");
//      this.ListenerSignal = "RetCom_GP_DOCUMENT_QUEUE";
//      //this.appOrg = this.propertiesBean.getProperty("Client_App.Org");
//      this.appOrg = "SomeOrg";
//      //this.appName = this.propertiesBean.getProperty("Client_App.Name");
//      this.appName = "SomeName";
//      //this.appVersion = this.propertiesBean.getProperty("Client_App.Version");
//      this.appVersion = "1.0";
//      PropertiesBean.getLogger().log(Level.INFO, "{0} \nDBListenerBean WelCome!", getClass().getName());
//    }
//    catch (Exception e)
//    {
//     // RCException rce = new RCException(e, getClass().getName());rce.log();
//    }
//  }
//  
//  public static String getMessage(CLOB clobb)
//    throws Exception
//  {
//    String message = "";
//    
//    Reader instream = clobb.getCharacterStream();
//    int size = clobb.getBufferSize();
//    char[] cbuffer = new char[size];
//    int length = -1;
//    while ((length = instream.read(cbuffer)) != -1) {
//      if (length < size) {
//        message = message + String.valueOf(cbuffer, 0, length);
//      } else {
//        message = message + String.valueOf(cbuffer);
//      }
//    }
//    instream.close();
//    
//    return message;
//  }
//  
//  public static String getMessage(Clob clobb)
//    throws Exception
//  {
//    String message = "";
//    
//    Reader instream = clobb.getCharacterStream();Throwable localThrowable3 = null;
//    try
//    {
//      int size = (int)clobb.length();
//      char[] cbuffer = new char[size];
//      int length = -1;
//      while ((length = instream.read(cbuffer)) != -1) {
//        if (length < size) {
//          message = message + String.valueOf(cbuffer, 0, length);
//        } else {
//          message = message + String.valueOf(cbuffer);
//        }
//      }
//    }
//    catch (Throwable localThrowable1)
//    {
//      localThrowable3 = localThrowable1;throw localThrowable1;
//    }
//    finally
//    {
//      if (instream != null) {
//        if (localThrowable3 != null) {
//          try
//          {
//            instream.close();
//          }
//          catch (Throwable localThrowable2)
//          {
//            localThrowable3.addSuppressed(localThrowable2);
//          }
//        } else {
//          instream.close();
//        }
//      }
//    }
//    return message;
//  }
//  
//  @Asynchronous
//  public Future<String> ListenCRS()
//  {
//      System.out.println("Start DBListenerBean.ListenCRS()");
//    init();
//    
//  //  RQContext rqCtx = null;
//    String status = "";
//    
//    OracleConnection conn = null;
//    try
//    {
//         
//        
//      
//      //PropertiesBean.getLogger().log(Level.INFO, "{0} \nDBListenerBean ListenCRS Start", this.jdbcName + ":" + this.ListenerSignal);
//    
//      //Properties userParam = new Properties();
//    /*  
//      userParam.put("appOrg", this.appOrg);
//      userParam.put("appName", this.appName);
//      userParam.put("appVersion", this.appVersion);
//      */
//      
//        System.out.println("DBListenerBean.ListenCRS() this.jdbcName ="+this.jdbcName);
//      conn = OraConnectionFab.captConnection(this.jdbcName);
//      
//          
//      OracleCallableStatement ocs = (OracleCallableStatement)conn.prepareCall("begin :crs := rram_handler.listenerCRS(:nWait, :sSignal); end;");
//      //OracleCallableStatement ocs = (OracleCallableStatement)conn.prepareCall("begin   :crs := tibco_su.get_acclist(:b_num); end;");
//      //OracleCallableStatement ocs = (OracleCallableStatement)conn.prepareCall("begin :crs :=RBR.ListenerCRStest(:nWait, :sSignal); end;");
//    
//      
//        ocs.setIntAtName("nWait", this.ListenerTimeout);
//        ocs.setStringAtName("sSignal", this.ListenerSignal);
//    /*
//        ocs.setStringAtName("b_num", "34495");
//    */
//        ocs.registerOutParameter(1, -10);
//      while (!PropertiesBean.GetShuttingDown())
//      {
//        ocs.execute();
//        conn.commit();
//        ResultSet cursor = ocs.getCursor(1);
//          System.out.println("cursor"+cursor);
//        while (cursor.next())
//        {
//             
//          String ssid = cursor.getString(4);
//            System.out.println("ssid ="+ssid);
//        /*  if ((ssid != null) && (!ssid.isEmpty()))
//          { */
//            //userParam.put("SSID", ssid);
//            
//            String gateName = cursor.getString(9);
//             
//            /*
//            GTContext gtCtx = GTContexts.getGTCtx(gateName);
//            if (gtCtx == null)
//            {
//              PropertiesBean.getLogger().log(Level.INFO, "\n��������!!! ����������� GATE: {0}\n��������� SSID: {1}", new Object[] { gateName, ssid });
//            }
//            else
//            {
//              gtCtx.log(Level.INFO, "{0} \nDBListenerBean Got Message", ssid);
//              */
//              Clob clob = cursor.getClob(1);
//              String qxml = getMessage(clob);
//              
//              int istrf = cursor.getInt(2);
//              String rqid = cursor.getString(3);
//              String rqtp = cursor.getString(5);
//
//               
//              //userParam.put("RQTP", rqtp);
//             
//              String mbName = cursor.getString(6);
//              //userParam.put("MBName", mbName);
//              String sysn = cursor.getString(7);
//              
//              String cuid = cursor.getString(8);
//              //userParam.put("AddInf", cuid);
//              int ipmod = cursor.getInt(10);
//              //String pmode = String.valueOf(ipmod);
//              //userParam.put("ProcMode", pmode);
//              String stat =cursor.getString(11);//String stat = Utilities.nvl(cursor.getString(11), "");
//              //userParam.put("Status", stat);
//              String errDesc = cursor.getString(12);//String errDesc = Utilities.nvl(cursor.getString(12), "");
//              System.out.println("gateName="+gateName);
//              System.out.println("qxml="+qxml);
//              System.out.println("istrf="+istrf);
//              System.out.println("rqid="+rqid);
//              System.out.println("rqtp="+rqtp);
//              System.out.println("mbName="+mbName);
//              System.out.println("sysn="+sysn);
//              System.out.println("cuid="+cuid);
//              System.out.println("ipmod="+ipmod);
//              System.out.println("errDesc="+errDesc);
//              
//             /*
//              String routeTable = ":";userParam.put("RouteTable", ":");
//              IDocTransf.eDocType edocType;
//              String rstp;
//              String spName;
//              if (stat.isEmpty())
//              {
//                IDocTransf.eDocType edocType = IDocTransf.eDocType.OUT_REQUEST;String rstp = rqtp;
//                String spName = sysn;userParam.put("SPName", spName);
//                sysn = mbName;userParam.put("CSName", sysn);
//              }
//              else
//              {
//                edocType = IDocTransf.eDocType.IN_RESPONSE;rstp = rqtp.substring(0, rqtp.length() - 1) + "s";
//                spName = mbName;userParam.put("SPName", spName);
//                userParam.put("CSName", sysn);
//              }
//              rqCtx = new RQContext(gtCtx, spName, sysn, cuid, ssid, rqtp, GPDocTransf.SDocType(edocType), "", pmode, routeTable);
//              
//              String sfrom = "<" + rstp + ">";String sto = "<" + rstp + " xmlns=\"urn:schemas-psit-ru:gp\">";
//              int ipos = qxml.indexOf(sfrom);
//              if (ipos != -1) {
//                qxml = qxml.substring(0, ipos) + sto + qxml.substring(ipos + sfrom.length());
//              }
//              GPDocTransf gp_dtr = new GPDocTransf();
//              gp_dtr.Init(qxml, edocType, GPDocTransf.eDocLevel.EMSG, userParam);
//              
//              ssid = gp_dtr.GetSSID();rqCtx.setSsid(ssid);
//              String atyp = gp_dtr.GetATYP();rqCtx.setAtyp(atyp);
//              
//              rqCtx.log(Level.INFO, "\nListenCRS:gateName:mbName:sysn:cuid {0} \nSend Message to Dispatch", gateName + " : " + mbName + " : " + sysn + " : " + cuid);
//              
//              this.dispatchBean.Dispatch(rqCtx, gp_dtr.GetDocumentStr(GPDocTransf.eDocLevel.EGP));
//            } */
//           
//         /* } // if  */
//        }
//          System.out.println("cursor.finish DBListenerBean.ListenCRS()");
//        cursor.close();
//      }
//      OraConnectionFab.releaseConnection(conn);
//       
//    }
//    catch (Exception e)
//    {
//      if (conn != null) {
//        try
//        {
//          OraConnectionFab.releaseConnection(conn);
//        }
//        catch (Exception localException1) {}
//      }
//     // RCException rce = new RCException(e, getClass().getName());rce.log(rqCtx);
//    }
//    //PropertiesBean.getLogger().log(Level.INFO, "{0} \nDBListenerBean ListenCRS Stop", this.jdbcName + ":" + this.ListenerSignal);
//    
//    return new AsyncResult(status);
//  }
//}
//