# Skimo Speech To Text Service

## Overview
This is a Spring Boot REST microservice that uses the Speech-to-Text engine, [DeepSpeech](https://github.com/mozilla/DeepSpeech) to take a WAV audio file as an input and output a full transcript of the speech.

## Intsalling DeepSpeech
Be sure to have Python 3.6.5+ installed before proceeding

This service uses **DeepSpeech Version 0.6** (support for current releases in development)

Start by creating a virtual environment to install the necessary pakcages 

`$ python3 -m venv ./some/pyenv/dir/path/ds06`

Switch over to the directory containing the virutal environment, activate it and install DeepSpeech. 

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
