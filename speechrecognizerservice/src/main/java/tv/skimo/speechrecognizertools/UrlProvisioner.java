package tv.skimo.speechrecognizertools;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.CRC32;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UrlProvisioner {
	private String url;
	private String sourceFileName;
	private String wavFileName;
	private final String TOO_BIG = "The file size is too big to be proccessed (exceeds 1GB)"; 
	
	public UrlProvisioner(String url, String sourcesourceFileName, String wavFileName) {
		this.url = url; 
		this.sourceFileName = sourcesourceFileName; 
		this.wavFileName  = wavFileName; 
	}
    
    public String createUUID(String audioFileLoc) throws IOException {
    	/* Calls downloadUsingNIO and crc32 to generate the UUID and then moves the audio file 
    	   to a newly created directory named after the UUID */
    	
    	if(audioFileLoc.equals(TOO_BIG)) {
    		return TOO_BIG; 
    	}
    	
    	String uuid = "" + crc32(audioFileLoc);
    	String uuidFolderName = sourceFileName + "/" + uuid;
  	    File uuidFile = new File(uuidFolderName);
  	    if(!uuidFile.exists()) {
  	  	    uuidFile.mkdir(); 
  	  	    String dest = uuidFolderName + "/" + wavFileName; 
  	    	Path destPath = Paths.get(dest);  
  	    	Path audioPath = Paths.get(audioFileLoc);
  	    	Files.move(audioPath, destPath); 
  	    	return uuid;
  	    }
  	    else
  	    	return "File already exists"; 
    }
    
    private static final Logger log = LoggerFactory.getLogger(UrlProvisioner.class);
    public String downloadUsingNIO() throws IOException {
    	//Helper method used to download the .wav file from a URL source using the Java NIO package 
    	
        URL urlDL = new URL(url);
        
        final int GIGABYTE_SIZE = 1073741824; 
        BigInteger sizeOfAudioFile = new BigInteger("1");
        HttpURLConnection conn; 
        try
        {    
            conn = (HttpURLConnection)urlDL.openConnection(); 
            conn.setRequestMethod("HEAD");   
            conn.getInputStream();           
            sizeOfAudioFile = BigInteger.valueOf(conn.getContentLength()); 
            conn.getInputStream().close(); 
        } 
        catch (Exception e) 
        { 
            log.info("Connection to source audio file failed"); 
        }
        
        if(sizeOfAudioFile.compareTo(BigInteger.valueOf(GIGABYTE_SIZE)) == -1) {
            ReadableByteChannel rbc = Channels.newChannel(urlDL.openStream());
            String audioFileLoc = sourceFileName + "/" + wavFileName;
            FileOutputStream fos = new FileOutputStream(audioFileLoc);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            rbc.close();
            return audioFileLoc;	
        }
        else
        	return TOO_BIG; 
    }
    
	private static long crc32(String sourcesourceFileName) throws IOException {
	   //Helper method used to generate a UUID through the CRC-32 algorithm
		
	   InputStream inputStream = new BufferedInputStream(new FileInputStream(sourcesourceFileName));
	   CRC32 crc = new CRC32();
	   int cnt;
	   while ((cnt = inputStream.read()) != -1) {
	     crc.update(cnt);
	   }
	   inputStream.close();
	   return crc.getValue();
	}
}

