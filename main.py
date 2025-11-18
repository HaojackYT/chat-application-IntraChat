import http.server
import ssl
import json

class SimpleAPIHandler(http.server.BaseHTTPRequestHandler):
    def do_POST(self):
        if self.path == "/api/send_message":
            length = int(self.headers.get("Content-Length", 0))
            body = self.rfile.read(length)
            data = json.loads(body.decode("utf-8"))

            # Ví dụ: chỉ echo lại
            response = {
                "status": "ok",
                "message": data.get("message", ""),
            }

            resp_body = json.dumps(response).encode("utf-8")
            self.send_response(200)
            self.send_header("Content-Type", "application/json")
            self.send_header("Content-Length", str(len(resp_body)))
            self.end_headers()
            self.wfile.write(resp_body)
        else:
            self.send_response(404)
            self.end_headers()

def run():
        server_address = ("0.0.0.0", 8443)
        httpd = http.server.HTTPServer(server_address, SimpleAPIHandler)

        # Tạo SSLContext kiểu mới chuẩn nhất
        ssl_context = ssl.SSLContext(ssl.PROTOCOL_TLS_SERVER)
        ssl_context.load_cert_chain(certfile="cert.pem", keyfile="key.pem")

        # Bọc socket HTTP bằng SSL Context
        httpd.socket = ssl_context.wrap_socket(httpd.socket, server_side=True)

        print("🔥 HTTPS Server chạy tại: https://localhost:8443/api/hello")
        httpd.serve_forever()


if __name__ == "__main__":
    run()
