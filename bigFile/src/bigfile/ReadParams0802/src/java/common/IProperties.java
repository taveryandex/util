package common;

import java.util.Properties;
import java.util.logging.Logger;
import javax.xml.transform.Source;

public abstract interface IProperties
{
  public abstract String getProperty(String paramString);
  
  public abstract String getProperty(String paramString1, String paramString2);
  
  public abstract void setProperty(String paramString1, String paramString2);
  
  public abstract boolean hasProperty(String paramString);
  
  public abstract void setRCChannels(String paramString1, String paramString2);
  
  //public abstract String getJMSChanIn(String paramString, IDocTransf.eDocType parameDocType, IDocTransf.eSyncMode parameSyncMode);
  
  //public abstract String getJMSChanOut(String paramString, IDocTransf.eDocType parameDocType, IDocTransf.eSyncMode parameSyncMode);
  
 // public abstract String getXsltStr(String paramString);
  
 // public abstract Source getXsltSource(String paramString);
  
  //public abstract XLogger getXLogger(String paramString);
  
  //public abstract Logger getULogger();
  
  public abstract Properties getProperties();
}
