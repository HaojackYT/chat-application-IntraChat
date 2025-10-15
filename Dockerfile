FROM ubuntu:22.04

# 1. Cài dependencies
RUN apt-get update && apt-get install -y curl && \
    rm -rf /var/lib/apt/lists/*

# 2. Cài Ollama
RUN curl -fsSL https://ollama.ai/install.sh | sh

# 3. Thiết lập thư mục làm việc (tùy chọn)
WORKDIR /app

# 4. Copy script khởi động vào container
COPY ollama_start.sh /app/ollama_start.sh
RUN chmod +x /app/ollama_start.sh

# 5. Expose cổng của Ollama
EXPOSE 11434

# 6. Chạy script khi container start
CMD ["/app/ollama_start.sh"]
