#!/bin/bash
# Start Ollama in the background.
/bin/ollama serve &
# Record Process ID.
pid=$!
# Pause for Ollama to start.
sleep 5
# The default Ollama Model in Spring Ai is mistral, but it can be changed in the applications property file. Make sure to download the same Model here
echo "🔴 Retrieve LLAMA3 model..."
ollama pull codellama
echo "🟢 Done!"
# Wait for the Ollama process to finish.
wait $pid