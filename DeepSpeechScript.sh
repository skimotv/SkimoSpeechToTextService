asset="$1"
output="$2"
echo $asset >> $output
deepspeech --model /usr/local/bin/deepspeech-0.6.0-models/output_graph.pb --lm /usr/local/bin/deepspeech-0.6.0-models/lm.binary --trie /usr/local/bin/deepspeech-0.6.0-models/trie --audio $asset > $output
