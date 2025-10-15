#!/bin/bash

# Chạy Ollama server ở nền
ollama serve &

# Đợi server chạy (vì cần thời gian khởi động)
sleep 5

# Tự động pull model bạn cần
ollama pull qwen2.5:7b-instruct

# Giữ container chạy
wait
