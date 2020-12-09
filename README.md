# Skimo Speech-To-Text Service

## Overview
This is a Spring Boot REST microservice that uses the Speech-to-Text engine, [DeepSpeech](https://github.com/mozilla/DeepSpeech), to take a speech sample (in WAV format) as an input and output a full transcript with timecodes.

## Intsalling DeepSpeech
**Be sure to have Python 3.7+ installed before proceeding**

This service uses **DeepSpeech Version 0.9.2** 

You can find instructions for downloading and installing DeepSpeech [here](https://deepspeech.readthedocs.io/en/v0.9.2/).  

## Using the Spring Boot Application 

This microservice utilizes two operations, a POST and GET, in order to download an audio file from a URL link and output the transcript line by line through a JSON.

The POST, GET, and Controller are located in the `src/main/java` directory.

The path to the location of the DeepSpeech models, the virutal environment, and the `DeepSpeechScript.sh` shell script can be changed in `application.properties` located in the `src/main/resources` folder.  

### POST
In the `SpeechRegonizer` class, a JSON request is taken in as an input containing the URL to a sample speech file. 

The POST utilizes the `URLProvisioner` class to: 
* download the WAV file using the Java NIO package 
* generate a unique ID (UUID) using the CRC-32 algorithm  
* move the downloaded audio file to a directory for later use.

Once the file has been downloaded and the UUID has been generated, a folder is created in the `assets` directory (located in `public/assets`) that is named after the UUID of the audio file. The WAV file is then moved to this directory. 

In the `SpeechRecognizer` class the POST then returns a JSON containing the UUID of the file. 

### Background Process

The class `DeepSpeechRecognizer` contains a background process that runs every 60 seconds to scan through `assets` and detect any WAV files which have not been transcribed yet. 

Once one without a transcription is found, the process runs the shell script using the `Process Builder` class in the Java API to generate a .json file containing the output.  

### GET

The GET does not utilize a separate class. Rather, it is in the `SpeechRecognizerController` class itself. 

The GET takes in the UUID of an audio file and loops through the `assets` directory to find any folders that match it. 

If found, it scans the UUID directory to see if any transcripts have been generated. If a transcript is detected, it is returned.  


## Support and Contributing

We'd love that you contribute to the project. Any questions can be sent to **admanchi1@gmail.com**.


















