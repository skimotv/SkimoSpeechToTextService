asset="$1"
output="$2"
echo $asset >> $output
deepspeech --model /usr/local/bin/deepSpeech_models_scorer/deepspeech-0.9.2-models.pbmm --scorer /usr/local/bin/deepSpeech_models_scorer/deepspeech-0.9.2-models.scorer --audio $asset > $output --json
