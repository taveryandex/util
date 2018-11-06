package bigfile;
/*
Nujno vernut' k jizni etot parser

Ochischaem ot musora fail schetchikami monitoringa loga GlassFish sobrannii classom Getcounts6
Sozdast fail v toizhe dirrectorii s prefixom clean
*/

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

 
public class CleanGlassFishMonitoringEventLog03 implements Closeable {
    static String  glassFishMonitoringFileName="Getcounts43_4.txt";
    //static String glassFishEventLogPath="U:/Results/3CardRetCom/";
    static String GlassFishMonitoringFileDir=""; 
    static String glassFishServerLog="";//glassFishEventLogPath+glassFishEventLogName;
	static int lengthChar=10000; // Сколько символов напечатать
	static int position=0; // С какого символа начать просмотр
    private final FileChannel fileChannel=null;
    private final MappedByteBuffer fileBuffer=null;
    private final String encoding=null;
    
    private final int[] linePositions=null;
    private final int[] lineSizes=null;
    Properties settingsAll = new Properties();
            
    public static void main(String[] args){
        CleanGlassFishMonitoringEventLog03 main= new CleanGlassFishMonitoringEventLog03();
        InputStream input=null;
        try {
            input = new FileInputStream("src/main/resources/settingsAll.properties");
            main.settingsAll.load(input);
            GlassFishMonitoringFileDir=main.settingsAll.getProperty("GlassFishMonitoringFileDir");
            glassFishMonitoringFileName=main.settingsAll.getProperty("glassFishMonitoringFileName");
            glassFishServerLog=GlassFishMonitoringFileDir+glassFishMonitoringFileName;
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CleanGlassFishMonitoringEventLog03.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CleanGlassFishMonitoringEventLog03.class.getName()).log(Level.SEVERE, null, ex);
        }

        byte[] massbyte;
         massbyte=new  byte[1];
         String readSimbol;
    	System.out.println("**************************");
    	System.out.println(glassFishServerLog);
    	System.out.println("Начальное время доступа к файлу лога : "+new Date());

        String path =main.glassFishServerLog;
 
        RandomAccessFile aFile;
        StringBuilder line= new StringBuilder();
        LinkedHashSet<String> operName=new LinkedHashSet();
        boolean isOperName=false;
		try {
                aFile = new RandomAccessFile(path,"r");
                FileChannel inChannel = aFile.getChannel();
                MappedByteBuffer fileBuffer;
                main.writeToFile("count, timestamp, dateTime, operation");
                

			try {
			fileBuffer = inChannel.map(MapMode.READ_ONLY, 0, inChannel.size());
			System.out.println("Размер файла в символах  : "+inChannel.size());
			double res=(position/inChannel.size())*100;
                        lengthChar=(int) inChannel.size();  // Целиком файл отпечатаем
		    	System.out.println("С какого символа начать просмотр : "+position+ "  : "+ res+"% ");
		    	System.out.println("Сколько символов напечатать : "+lengthChar );
		    	System.out.println(" ");
		    	System.out.println("**************************");
		    	System.out.println(" ");
			fileBuffer.position(position);
                        //Пробежимся по всему файлу
	            for (int count=1; count <=lengthChar-1; count ++){
	            	//int currentPosition = fileBuffer.position();
		        massbyte[0] = fileBuffer.get();
                        readSimbol=new String(massbyte,"cp1251");
                        line.append(readSimbol);
                        position++;
 
                            if (readSimbol.equals("\n")){
                                position++;
                                //System.out.println("line :"+line);
                                
                                //Тут мы должны разделить строку на элементы
                                
                                Pattern pattern = Pattern.compile("entity(.*?)childResources");  
                                Matcher matcher =pattern.matcher(line);
                                while (matcher.find()){
                                String x,gr1,gr2,gr3;
                                x=matcher.group();
                                gr1=matcher.group(1);
                                //gr2=matcher.group(2);
                                //gr3=matcher.group(3);
                                //System.out.println("gr1  "+gr1);
                                //System.out.println("gr2  "+gr2);
                                //System.out.println("gr3  "+gr3);
                                //Разбиваем блок на строки
                                
                                //Разбиваем блок на строки
                                String[] splLineArr=gr1.split("},");
                                for(String splLine:splLineArr){
                                    splLine=splLine.replace("\"", "'");
                                    //System.out.println("splLine :"+splLine);
                                    
                                    Pattern patternDate = Pattern.compile("^.*?\'count\':(.*?),\'lastsampletime\':(.*?),\'description\':(.*?),\'.*?$");  
                                    Matcher matcherDate =patternDate.matcher(splLine);
                                    while (matcherDate.find()){
                                        String xDate,gr1DateCount,gr2DateLastsampletime,gr3DateOperName;
                                        xDate=matcherDate.group();
                                        gr1DateCount=matcherDate.group(1); 
                                        gr2DateLastsampletime=matcherDate.group(2);
                                        gr3DateOperName=matcherDate.group(3);
                                        
                                        //Выполняется запись новой таблицы
                                        Iterator<String> iop=operName.iterator();
                                        int index=0;
                                        while (iop.hasNext()) {
                                            String oprNameElement = iop.next();
                                            if (gr3DateOperName.equals(oprNameElement)){
                                                isOperName=true;
                                                //System.out.println(index+" : "+gr3DateOperName+":");
                                                break;          }
                                            index=index+1;
                                            
                                        } //while
                                        if(!isOperName){ operName.add(gr3DateOperName); index=index+1;
                                        //System.out.println(index+" * "+gr3DateOperName+"*");
                                        
                                        }
                                        //System.out.println(index+" $ "+gr2DateLastsampletime+"$");
                                        Float fl=Float.parseFloat(gr2DateLastsampletime)/(1000*60*60*24);
                                        //System.out.println("*"+fl.toString()+"*");
                                        Double dateTime=25569.00+Double.parseDouble(gr2DateLastsampletime)/(1000*60*60*24)+3/24;
                                        
                                        main.writeToFile(gr1DateCount+", "+gr2DateLastsampletime+", "+dateTime+", "+index);
                                        isOperName=false;
                                    }
                                    
                                    patternDate = Pattern.compile("^.*?lastsampletime\':(\\d{1,}).*?description\':(\'.*?\').*?current\':(.*?),.*?$");  
                                    matcherDate =patternDate.matcher(splLine);
                                    while (matcherDate.find()){
                                        String xDate,gr1DateLastsampletime,gr2DateDescription,gr3Date;
                                        xDate=matcherDate.group();
                                        gr1DateLastsampletime=matcherDate.group(1); 
                                        gr2DateDescription=matcherDate.group(2);
                                        gr3Date=matcherDate.group(3);
                                        
                                        
                                        
                                        Iterator<String> iop=operName.iterator();
                                        int index=0;
                                        while (iop.hasNext()) {
                                            String oprNameElement = iop.next();
                                            if (gr2DateDescription.equals(oprNameElement)){
                                                isOperName=true;
                                                //System.out.println(index+" : "+gr3DateOperName+":");
                                                break;          }
                                            index=index+1;
                                            
                                        } //while
                                        if(!isOperName){ operName.add(gr2DateDescription); index=index+1;
                                        //System.out.println(index+" * "+gr2DateDescription+"*");
                                        }
                                        //System.out.println(index+" $ "+gr2DateDescription+"$");
                                        Float fl=Float.parseFloat(gr1DateLastsampletime)/(1000*60*60*24);
                                        //System.out.println("*"+fl.toString()+"*");
                                        Double dateTime=25569.00+Double.parseDouble(gr1DateLastsampletime)/(1000*60*60*24)+3/24;
                                        main.writeToFile(gr3Date+", "+gr1DateLastsampletime+", "+dateTime+", "+index);
                                        isOperName=false;                                        
                                    }  
                                    
                                }
                                }   
                                
                                line.setLength(0);
                            }
                            
                            
		        } //Конец обработки файла
                    
                    
                    Iterator<String> iop=operName.iterator();
                    int index=0;
                    while (iop.hasNext()) {
                        String oprNameElement = iop.next();
                        System.out.println(index+" : "+oprNameElement+"");
                        main.writeOperToFile(index+" : "+oprNameElement);
                        index=index+1;

                    } //while
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
 
    	System.out.println("Конечное время доступа к файлу лога : "+new Date());    	
    }
    
 
        private void writeToFile(String data){

        try(FileWriter writer = new FileWriter(GlassFishMonitoringFileDir +"csv"+glassFishMonitoringFileName, true))
        {
           // запись всей строки
            writer.write(data);
            // запись по символам
            writer.append('\r');writer.append('\n');
 
             
            writer.flush();
        }
        catch(IOException ex){
             
            System.out.println(ex.getMessage());
        }         
        } 
        private void writeOperToFile(String data){

        try(FileWriter writer = new FileWriter(GlassFishMonitoringFileDir +"oper"+glassFishMonitoringFileName, true))
        {
           // запись всей строки
            writer.write(data);
            // запись по символам
            writer.append('\r');writer.append('\n');
 
             
            writer.flush();
        }
        catch(IOException ex){
             
            System.out.println(ex.getMessage());
        }         
        }    

	public int lineCount() {
        return linePositions.length;
    }

 public void repalceAll(StringBuilder sb,Pattern p,String replacement){

    Matcher m=p.matcher(sb);
    int start=0;
    while (m.find(start)){
        sb.replace(m.start(),m.end(),replacement);
        start=m.start()+replacement.length();
    }


};       
        
        
    public String line(int lineNumber) throws IOException {
        fileBuffer.position(linePositions[lineNumber]);
        byte[] bytes = new byte[lineSizes[lineNumber]];
        fileBuffer.get(bytes);
        return new String(bytes, encoding);
    }
    
    @Override public void close() throws IOException {
        fileChannel.close();
    }
    
    
    
     
}