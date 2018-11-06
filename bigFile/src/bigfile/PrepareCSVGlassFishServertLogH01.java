package bigfile;
/*

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
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

 
public class PrepareCSVGlassFishServertLogH01 implements Closeable {
    static String  glassFishServertLogName="Getcounts43_4.txt";
    //static String glassFishEventLogPath="U:/Results/3CardRetCom/";
    static String GlassFishServertLogDir=""; 
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
        
        PrepareCSVGlassFishServertLogH01 main= new PrepareCSVGlassFishServertLogH01();
        InputStream input=null;
        try {
            input = new FileInputStream("src/main/resources/settingsAll.properties");
            main.settingsAll.load(input);
            GlassFishServertLogDir=main.settingsAll.getProperty("GlassFishServertLogDirH");
            glassFishServertLogName=main.settingsAll.getProperty("glassFishServertLogNameH");
            glassFishServerLog=GlassFishServertLogDir+glassFishServertLogName;
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PrepareCSVGlassFishServertLogH01.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PrepareCSVGlassFishServertLogH01.class.getName()).log(Level.SEVERE, null, ex);
        }

        byte[] massbyte;
         massbyte=new  byte[1];
         String readSimbol;
    	System.out.println("**************************");
    	System.out.println(" ");
    	System.out.println("Начальное время доступа к файлу лога : "+new Date());

        String path =main.glassFishServerLog;
    	//String path ="D:\\temp\\logs\\IB\\Card.Get.Statement_2017-03-03-00-00.log";
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
	            for (int count=1; count <=lengthChar-2; count ++){
		        //while (fileBuffer.remaining() > 0) {
	            	//int currentPosition = fileBuffer.position();
		        massbyte[0] = fileBuffer.get();
                        readSimbol=new String(massbyte,"cp1251");
                        line.append(readSimbol);
                        position++;
            		//System.out.println("currentPosition : "+currentPosition+"  int  : "+character+"  character  : "+ (char)character+"   i "+(i++));
                        //System.out.println("length "+readSimbol.length());
		        //System.out.print(readSimbol);
                            if (readSimbol.equals("]")){
                                position++;
                                count ++;
                                massbyte[0] = fileBuffer.get();
                                readSimbol=new String(massbyte,"cp1251");
                                line.append(readSimbol);
                            if (readSimbol.equals("]")){

                                position++;

                              main.repalceAll(line,Pattern.compile(",")," ");
                              main.repalceAll(line,Pattern.compile("\""),"'");   
                              main.repalceAll(line,Pattern.compile("[^\\r]\\n")," "); 
                              main.repalceAll(line,Pattern.compile("\r\n\r\n"),"\r\n"); 
                              System.out.println("line :"+line);                                                               
                                line.setLength(0);
                            }
                            }
                            
		        } //Конец обработки файла
                    
 
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
 
    	System.out.println("Конечное время доступа к файлу лога : "+new Date());    	
    }
    
 
        private void writeToFile(String data){

        try(FileWriter writer = new FileWriter(GlassFishServertLogDir +"clean3"+glassFishServertLogName, true))
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

        try(FileWriter writer = new FileWriter(GlassFishServertLogDir +"oper"+glassFishServertLogName, true))
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