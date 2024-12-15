#!/bin/bash
/bin/ollama serve &
pid=$!
sleep 5
# The default Ollama Model in Spring Ai is mistral, but it can be changed in the applications property file. Make sure to download the same Model here
echo "🔴 Retrieve LLAMA3.1 model..."
ollama pull mistral
echo "🟢 Done!"
wait $pid