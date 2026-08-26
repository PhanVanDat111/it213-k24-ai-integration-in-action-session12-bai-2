# Báo cáo Bài tập 2: Dò Lỗi & Tối Ưu Cấu Hình — Xử Lý Bẫy Ô Nhiễm Standard Output (Stdio Pollution)

## Phần 1: Tiêu đề bài tập và tóm tắt yêu cầu
- **Tiêu đề**: Giải quyết lỗi ô nhiễm Stdio (Stdio Pollution) trên MCP Server viết bằng Spring Boot khi tích hợp với Claude Desktop.
- **Tóm tắt yêu cầu**:
  - Phân tích nguyên nhân kỹ thuật khiến việc in log/banner ra `System.out` làm hỏng giao thức JSON-RPC của MCP.
  - Thực hiện tắt hoàn toàn Banner khởi động của Spring Boot trong mã nguồn Java.
  - Cấu hình Logback (`logback-spring.xml`) chuyển hướng toàn bộ luồng log từ `System.out` sang `System.err`.
  - Giải thích cơ chế vì sao sử dụng `System.err` giải quyết triệt để vấn đề mà vẫn giữ nguyên khả năng giám sát hệ thống.

## Phần 2: Giả lập cuộc trò chuyện thực tế với AI

### Prompt gửi cho AI:
> "Tôi đang phát triển một MCP Server bằng Spring Boot kết nối với Claude Desktop qua giao thức Stdio. Tuy nhiên khi khởi chạy, Claude Desktop báo lỗi không thể parse JSON-RPC do có sự xuất hiện của các ký tự lạ dạng Spring Boot Banner (ví dụ: `.   ____          _            __ _ _`) và log hệ thống. Hãy giúp tôi cấu hình lại mã nguồn Spring Boot để tắt banner khởi động, đồng thời viết cấu hình logback-spring.xml chuyển toàn bộ log sang System.err thay vì System.out. Giải thích rõ cơ chế vận hành này giúp tôi."

### Tóm tắt phản hồi của AI:
- **Phân tích nguyên nhân**: Giao thức Stdio MCP (Model Context Protocol) sử dụng luồng đầu ra tiêu chuẩn (`stdout` / `System.out`) để vận chuyển các thông điệp JSON-RPC thuần khiết. Bất kỳ ký tự dư thừa nào khác JSON (như banner ASCII, log logback...) xuất hiện trên luồng này đều làm hỏng bộ phân tích cú pháp (parser) của MCP Client.
- **Giải pháp lập trình**: Sử dụng đối tượng `SpringApplication` để cấu hình `setBannerMode(Banner.Mode.OFF)` trước khi gọi hàm `.run()`.
- **Giải pháp cấu hình**: Tạo file `logback-spring.xml` với target appender trỏ thẳng tới `System.err` thay vì mặc định `System.out`.
- **Lợi ích**: `System.err` (stderr) được các IDE, Docker console, và các MCP Client thu thập như một kênh riêng biệt dành cho log gỡ lỗi (diagnostic logs), không can thiệp hay trộn lẫn vào kênh truyền tin dữ liệu chính của `System.out` (stdout).