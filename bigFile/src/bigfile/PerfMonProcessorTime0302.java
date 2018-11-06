package bigfile;
/*
copy * output.txt /b - kak skleit faili
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
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

 
public class PerfMonProcessorTime0302 implements Closeable {
    private boolean isDebug = false;
    static String  GroupByLogName="Getcounts43_4.txt";
    //static String glassFishEventLogPath="U:/Results/3CardRetCom/";
    static String GroupByLogDir=""; 
    static Integer Interval=1000;
    static int lengthChar=10000; // Сколько символов напечатать
    static int position=0; // С какого символа начать просмотр
    private final FileChannel fileChannel=null;
    private final MappedByteBuffer fileBuffer=null;
    private final String encoding=null;
    
    private final int[] linePositions=null;
    private final int[] lineSizes=null;
    Properties settingsAll = new Properties();
            
    public static void main(String[] args){
        PerfMonProcessorTime0302 main= new PerfMonProcessorTime0302();
        InputStream input=null;
        Integer lineCount=0;
        Double value;
        TreeMap<Long,ArrayList<Double>> groupValuses = new TreeMap<>();
        Date date = null;       
                
        try {
            input = new FileInputStream("src/main/resources/settingsAll.properties");
            main.settingsAll.load(input);
            GroupByLogDir=main.settingsAll.getProperty("GroupByLogDir");
            GroupByLogName=main.settingsAll.getProperty("GroupByLogName");
            Interval=Integer.parseInt(main.settingsAll.getProperty("Interval"));
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PerfMonProcessorTime0302.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PerfMonProcessorTime0302.class.getName()).log(Level.SEVERE, null, ex);
        }

        byte[] massbyte;
         massbyte=new  byte[1];
         String readSimbol;
    	System.out.println("**************************");
    	System.out.println(" "+GroupByLogDir+"  "+GroupByLogName);
    	System.out.println("Начальное время доступа к файлу лога : "+new Date());

        String path =GroupByLogDir+GroupByLogName;
 
        RandomAccessFile aFile;
        StringBuilder line= new StringBuilder();
        LinkedHashSet<String> operName=new LinkedHashSet();
        boolean isOperName=false;
		try {
                aFile = new RandomAccessFile(path,"r");
                FileChannel inChannel = aFile.getChannel();
                MappedByteBuffer fileBuffer;
                //main.writeToFile("count, timestamp, dateTime, operation");
                

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
	            for (int count=1; count <=lengthChar-1; count ++){
		        //while (fileBuffer.remaining() > 0) {
	            	//int currentPosition = fileBuffer.position();
		        massbyte[0] = fileBuffer.get();
                        readSimbol=new String(massbyte,"cp1251");
                        line.append(readSimbol);
                            if (readSimbol.equals("\r")){
                                count ++; // obiazatelno/ prichna oshibok
                                massbyte[0] = fileBuffer.get();
                                readSimbol=new String(massbyte,"cp1251");
                                line.append(readSimbol);
                            if (readSimbol.equals("\n")){
                               if (main.isDebug) { System.out.println("line :"+line); }
      if (lineCount>0){
                        
        String[] splLineArr=line.toString().split("\",");


            
            
            // Столбец значений
        String stralue=splLineArr[4].replace("\"", "");
        if (stralue.equals(" ")){ stralue="0"; }
        if (main.isDebug) { System.out.println("splLineArr[4] :  ***"+stralue+"***"); }
            value=Double.parseDouble(stralue);
            
            
            
            if (main.isDebug) {System.out.println("value :"+value); }
             DateFormat format= new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS",Locale.ENGLISH);
             date=format.parse(splLineArr[0].replace("\"", ""));
            if (main.isDebug) {System.out.println("date.getTime() : "+date.getTime());}
            Long time=date.getTime()/Interval;
            
            if (main.isDebug) {System.out.println("Long Time : "+time);}
 
    if(groupValuses.get(time)==null){
            System.out.println("Новый объект : "+groupValuses.get(time));
            groupValuses.put(time, new ArrayList<>());

    }
       ArrayList<Double> fff=groupValuses.get(time);
       fff.add(value);
  
          
            
            
      }
       // }
                                    //                          
 /*
                              main.repalceAll(line,Pattern.compile(",")," ");
                              main.repalceAll(line,Pattern.compile("\""),"'");   
                              main.repalceAll(line,Pattern.compile("[^\r]\n")," "); 
                              main.repalceAll(line,Pattern.compile("\r\n\r\n"),"\r\n");
                              main.repalceAll(line,Pattern.compile("\r\n[^\\[]")," "); 
*/
                                //
                             // System.out.println("line :"+line);                                
                                
                                
                                //Тут мы должны разделить строку на элементы
                                //
                              
   /*
    Pattern p=Pattern.compile("glassfish");
    Matcher m=p.matcher(line);
    int start=0;

    
    while (m.find(start)){
         
        start=m.start()+"glassfish".length();
        System.out.println(m.start());    
    }
     */                   
                              
                              
                              
    // 
    //Pattern pattern = Pattern.compile("\\[([\\s\\S]*?)\\]\\s\\[([\\s\\S]*?)\\]\\s\\[([\\s\\S]*?)\\]\\s\\[([\\s\\S]*?)\\]\\s\\[([\\s\\S]*?)\\]\\s\\[([\\s\\S]*?)\\]\\s\\[([\\s\\S]*?)\\]\\s\\[([\\s\\S]*?)\\]\\s\\[([\\s\\S]*?)\\]\\s\\[");  
    //Pattern pattern = Pattern.compile("\\[([\\s\\S]*?)\\]\\s\\[([\\s\\S]*?)\\]\\s\\[([\\s\\S]*?)\\]\\s\\[([\\s\\S]*?)\\]\\s\\[([\\s\\S]*?)\\]\\s\\[([\\s\\S]*?)\\]\\s\\[([\\s\\S]*?)\\]\\s\\[([\\s\\S]*?)\\][\\s\\S]*?\\[\\[([\\s\\S]*?)\\]\\]");  
    /*
    ^(\[[\s\S]*?)\]\s\[([\s\S]*?)\]\s\[([\s\S]*?)\]\s\[([\s\S]*?)\]\s\[([\s\S]*?)\]\s\[([\s\S]*?)\]\s\[([\s\S]*?)\]\s\[([\s\S]*?)\]([\s\S]*?)$ на "\1","\2","\3","\4","\5","\6","\7","\8","\9"
*/ 
                                /*
    Pattern pattern = Pattern.compile("\\[([\\s\\S]*?)\\]\\s\\[([\\s\\S]*?)\\]\\s\\[([\\s\\S]*?)\\]\\s\\[([\\s\\S]*?)\\]\\s\\[([\\s\\S]*?)\\]\\s\\[([\\s\\S]*?)\\]\\s\\[([\\s\\S]*?)\\]\\s\\[([\\s\\S]*?)\\]\\s\\[([\\s\\S]*?)\\]\\]");  
                              
                                Matcher matcher =pattern.matcher(line.toString());
                                while (matcher.find()){
                                String x,gr1,gr2,gr3,gr4,gr5,gr6,gr7,gr8,gr9;
                                x=matcher.group();
                                gr1=matcher.group(1);
                                gr2=matcher.group(2);
                                gr3=matcher.group(3);
                                gr4=matcher.group(4);
                                gr5=matcher.group(5);
                                gr6=matcher.group(6);
                                gr7=matcher.group(7);
                                gr8=matcher.group(8);
                                gr9=matcher.group(9);
                                main.writeToFile(gr1+", "+gr2+", "+gr3+", "+gr4+", "+gr5+", "+gr6+", "+gr7+", "+gr8+", "+gr9);
                                }   
                                 */
                                lineCount++;
                                if (main.isDebug) {System.out.println("lineCount  : "+lineCount);}
                                
                                line.setLength(0);
                            }
                            }
                            
		        } //Конец обработки файла
 
            SimpleDateFormat formater= new SimpleDateFormat("dd.MM.yyyy HH:mm:ss",Locale.ENGLISH);
            String output;
        main.writeToFile("date; ProcessorTime" );                    
                    
        Set<Long> keys= groupValuses.keySet();     
        for (Long key:keys){
            System.out.println("");
            System.out.print("key :"+key);
            ArrayList<Double> ggg=groupValuses.get(key);
            Double sum=0d;
            for (int i=0; i<ggg.size();i++){
                System.out.print(ggg.get(i)+"; ");
                sum+=ggg.get(i);
            }      
            date.setTime(key*main.Interval);

            output=formater.format(date);
            main.writeToFile(output+" ; "+ ( sum/ggg.size() ));
        }
        
                   
                    
                    
                    
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
			} catch (ParseException ex) {
                Logger.getLogger(PerfMonProcessorTime0302.class.getName()).log(Level.SEVERE, null, ex);
            }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
 
    	System.out.println("Конечное время доступа к файлу лога : "+new Date());    	
    }
    
 
        private void writeToFile(String data){
        //System.out.println(".");    
        try(FileWriter writer = new FileWriter(GroupByLogDir +"ProcessorTime"+Interval+GroupByLogName, true))
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

        try(FileWriter writer = new FileWriter(GroupByLogDir +"oper"+GroupByLogName, true))
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