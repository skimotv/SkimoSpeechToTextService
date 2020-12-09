package tv.skimo.speechrecognizertools;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component 
public class DeepSpeechRecognizer {
	
	// All values obtained from application properties located in src/main/resources 
	@Value("${assets}")
    private String ASSETS;
	
	@Value("${dsrScript}")
	private String DSR_SCRIPT; 
	
	@Value("${venvPath}")
	private String VENV_PATH; 
	
	private static final Logger log = LoggerFactory.getLogger(DeepSpeechRecognizer.class);
	@Scheduled(fixedDelay = 20000) //runs once a minute
	public void executeSpeechToText() throws IOException {
		// Iterates through the assets directory and calls the runDeepSpeech method
		// Note: assets directory located in public/resources 
		
		log.info("executeSpeechToText invoked");
		File[] assetURLFiles = new File(ASSETS).listFiles();
		for (File uuidFile : assetURLFiles) {
			if(uuidFile.isDirectory()) {
				File[] filesInUUID = new File(uuidFile.getAbsolutePath()).listFiles(); 
				for(int k = 0; k < filesInUUID.length; k++) {
					if(filesInUUID[k].getName().contains(".wav") && filesInUUID.length == 1) {
						runDeepSpeech(filesInUUID[k].getAbsolutePath(), uuidFile.getAbsolutePath());
					}
				}
			}
		} 
	}
	
	private void runDeepSpeech(String audioFileLoc, String uuidDirLoc) {
		/* Executes the shell script containing the DeepSpeech speech to text command using 
		   the ProcessBuilder class */
		
		String whereToPutTranscript = uuidDirLoc + "/" + "out.json"; 
		ArrayList<String> pbList = new ArrayList<String>();
		
		//Shell script containing DeepSpeech command
		pbList.add(DSR_SCRIPT);
		//audio file passed into DeepSpeech engine as parameter
		pbList.add(audioFileLoc);
		//transcript created by inference
		pbList.add(whereToPutTranscript);  
		
		ProcessBuilder pb = new ProcessBuilder(pbList);
		Map<String, String> env = pb.environment();
		//Add PATH so process builder can utilize Python 
		env.put("PATH", VENV_PATH);
				
		try {
			pb.redirectError(new File(uuidDirLoc + "/" + "RunOutput.txt")); //Redirect errors to text file  
			Process p = pb.start();
			p.waitFor(); 
		}
		catch(Exception e){
			System.out.println("Error" + e);
		}
	}
}

