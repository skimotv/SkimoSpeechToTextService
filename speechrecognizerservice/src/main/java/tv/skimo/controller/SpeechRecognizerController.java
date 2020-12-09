package tv.skimo.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.skimo.speechrecognizertools.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpeechRecognizerController {
  
  // Value obtained from application properties located in src/main/resources 
  @Value("${assets}")
  private String ASSETS; 
  
  // Default GET Mapping 
  @GetMapping("/speechtotext")
  public String getHello() {
	    return "Hello";
  } 	
  
  @PostMapping("/speechtotext")
  JSONObject getMessage(@RequestParam Map<String,String> reqPar) throws IOException {
	  // POST method that utilizes the URLProvisioner class
	  
	  String url = reqPar.get("URL"); 
	  UrlProvisioner myAudio = new UrlProvisioner(url, 
			  ASSETS, "input-audio.wav");
	  JSONObject obj = new JSONObject();
	  String audioFileLoc = myAudio.downloadUsingNIO();
	  if(audioFileLoc.equals("The audio file size is too big")) {
		  obj.put("Error", "The audio file size is too big"); 
	  }
	  else {
		  String uuid = myAudio.createUUID(audioFileLoc); 
		  obj.put("UUID", uuid);   
	  }
	  return obj; 
  }
  
  private static final Logger log = LoggerFactory.getLogger(SpeechRecognizerController.class);
  @GetMapping("/speechtotext/{id}")
  JSONObject getTranscript(@PathVariable Long id) throws FileNotFoundException, IOException, ParseException {
	  /* GET method that iterates through the assets directory and finds the transcript 
	     for a desired .wav file */
	  
	  	ArrayList<String> linesList = new ArrayList<String>(); 
	  	JSONObject transcript = new JSONObject();
		File[] assetFiles = new File(ASSETS).listFiles();
		for (File uuidFile : assetFiles) {
			if(uuidFile.getName().equals(id.toString())) {
				File[] filesInUUID = new File(uuidFile.getAbsolutePath()).listFiles();
				for(int k = 0; k < filesInUUID.length; k++) {
					if(filesInUUID[k].getName().equals("out.json")) {
//						GetJSON get = new GetJSON(filesInUUID[k]); trying to isolate 1 of the 3 transcript predictions
//						transcript = get.summarizedOutput(); 
						Object obj = new JSONParser().parse(new FileReader(filesInUUID[k].getAbsolutePath()));
						JSONObject tj = (JSONObject) obj;
						transcript = tj; 
						} 
					}
				}
		}
		return transcript; 
  	}
}


