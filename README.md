# Skimo Speech-To-Text Service

## Overview
This is a Spring Boot REST microservice that uses the Speech-to-Text engine, [DeepSpeech](https://github.com/mozilla/DeepSpeech) to take a WAV audio file as an input and output a full transcript of the speech.

## Intsalling DeepSpeech
Be sure to have Python 3.6.5+ installed before proceeding

This service uses **DeepSpeech Version 0.6** (support for current releases in development)

Start by creating a virtual environment to install the necessary pakcages 

`$ python3 -m venv ./some/pyenv/dir/path/ds06`

Switch over to the directory containing the virutal environment, activate it, and install DeepSpeech. 

`$ source ./some/pyenv/dir/path/ds06/bin/activate`

`$ pip3 install deepspeech==0.6.0`

Create a separate directory to hold DeepSpeech and unzip the models (this may take some time)

`$ mkdir -p ./some/workspace/path/ds06`

`$ cd ./some/workspace/path/ds06`

`$ curl -LO https://github.com/mozilla/DeepSpeech/releases/download/v0.6.0/deepspeech-0.6.0-models.tar.gz`

`$ tar -xvzf deepspeech-0.6.0-models.tar.gz`

Download and unzip a few sample audio files to test

`$ curl -LO https://github.com/mozilla/DeepSpeech/releases/download/v0.6.0/audio-0.6.0.tar.gz`

`$ tar -xvzf audio-0.6.0.tar.gz`

Once this finishes, test DeepSpeech

`$ ls -l ./audio/`

`$ deepspeech --model deepspeech-0.6.0-models/output_graph.pb --lm deepspeech-0.6.0-models/lm.binary --trie ./deepspeech-0.6.0-models/trie --audio ./audio/2830-3980-0043.wav`

After the inference finishes running, the output should show **"experience proof less"**

**Before continuing, be sure to move the** `deepspeech-0.6.0-models` **directory along with the shell script** `DeepSpeechScript.sh` **and your virtual environment into** `/usr/local/bin`

## Using the Spring Boot Application 

This microservice utilizes two operations, a POST and GET, in order to download an audio file from a URL link and output the transcript line by line through a JSON.

The POST, GET, and Controller are located in the `src/main/java` directory.

The location of the DeepSpeech models, the virutal environment, and the `DeepSpeechScript.sh` shell script can be changed in `application.properties` located in the `src/main/resources` folder (default is `/usr/local/bin`)

### POST

The POST utilizes the `URLProvisioner` class to download a WAV file, generate a unique ID (UUID), and move the downloaded audio file for later use.

The Java NIO package is used to download the WAV file and the CRC-32 algorithm is used generate a UUID

Once the file has been downloaded and the UUID has been generated, a folder is created in the directory `public/assets` that is named after the UUID of the audio file. The WAV file is then moved to this directory.

### Background Process

The class `DeepSpeechRecognizer` contains a background process that runs every 60 seconds to scan through `public/assets` and find any WAV files which have not been transcribed. 

If one is found, the process runs the shell script `DeepSpeechScript.sh` using the `Process Builder` class in the Java API to generate a .txt file containing the transcripts  

### GET

The GET does not utilize a separate class. Rather, it is in the `SpeechRecognizerController` class itself. 

The GET loops through the `public/assets` directory to find any transcripts that have been generated. Once found, a JSON is returned containing the full transcript line by line. 


## Support and Contributing

We'd love that you contribute to the project. Any questions can be sent to **admanchi1@gmail.com**


















