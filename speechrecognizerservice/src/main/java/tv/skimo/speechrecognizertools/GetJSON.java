package tv.skimo.speechrecognizertools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray; 
import org.json.simple.JSONObject; 
import org.json.simple.parser.*; 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GetJSON {
	private File jsonFile; 
	
	public GetJSON(File jsonFile) {
		this.jsonFile = jsonFile; 
	}
	
	public JSONObject summarizedOutput() throws FileNotFoundException, IOException, ParseException {
		JSONObject retSum = new JSONObject(); 
		
		Object obj = new JSONParser().parse(new FileReader(jsonFile.getAbsolutePath()));
		JSONObject tj = (JSONObject) obj;
		
		JSONArray words = (JSONArray) tj.get("transcripts"); 
		retSum.put("Transcript", words); 
		return retSum; 
	}
	
}
