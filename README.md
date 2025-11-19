# IntraChat Client Application

<div align="center">
  <img src="src/main/resources/icon/icon.png" alt="IntraChat Logo" width="120"/>
  
  ![Java](https://img.shields.io/badge/Java-24-orange?style=flat-square&logo=openjdk)
  ![Maven](https://img.shields.io/badge/Maven-3.x-C71A36?style=flat-square&logo=apache-maven)
  ![Socket.IO](https://img.shields.io/badge/Socket.IO-2.1.2-010101?style=flat-square&logo=socket.io)
  ![License](https://img.shields.io/badge/License-MIT-green?style=flat-square)
  
  **A modern, real-time chat application client built with Java Swing and Socket.IO**
</div>

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Key Features](#-key-features)
- [Technology Stack](#-technology-stack)
- [Architecture](#-architecture)
- [Prerequisites](#-prerequisites)
- [Installation](#-installation)
- [Configuration](#-configuration)
- [Usage](#-usage)
- [Project Structure](#-project-structure)
- [Building from Source](#-building-from-source)
- [Troubleshooting](#-troubleshooting)
- [Contributing](#-contributing)
- [License](#-license)

---

## 🎯 Overview

**IntraChat Client** is a feature-rich, desktop chat application designed for real-time communication within organizations. Built with Java Swing for a native desktop experience and Socket.IO for reliable real-time messaging, this application provides a modern, intuitive interface for seamless team collaboration.

The client connects to the IntraChat Server and supports text messaging, file sharing (for image only), image viewing, emoji reactions, and user presence management.

---

## ✨ Key Features

### Core Functionality
- 🔐 **Secure Authentication**: User registration and login with password protection
- 💬 **Real-time Messaging**: Instant message delivery using WebSocket technology
- 👥 **User Presence**: Live status indicators showing who's online
- 📁 **File Transfer**: Seamless file sharing with progress tracking (in progress)
- 🖼️ **Image Support**: In-app image preview with BlurHash loading optimization
- 😊 **Emoji Support**: Rich emoji picker for expressive communication
- 📝 **Message History**: Persistent chat history with date separators (in progress)

### User Experience
- 🎨 **Modern UI**: Clean, responsive interface with FlatLaf theming
- 🌓 **Dark/Light Themes**: Customizable IntelliJ-inspired themes (in progress)
- 🔔 **Message Notifications**: Visual indicators for new messages (in progress)
- 👤 **User Profiles**: Avatar support with profile customization (in progress)
- 📱 **Responsive Layout**: Adaptive UI that works across different screen sizes

### Technical Excellence
- ⚡ **High Performance**: Optimized for low latency messaging
- 🔄 **Auto-reconnection**: Automatic connection recovery
- 🎭 **Event-driven Architecture**: Decoupled components for maintainability
- 📦 **Modular Design**: Clean separation of concerns (MVC pattern)

---

## 🛠 Technology Stack

### Core Technologies
| Technology | Version  | Purpose                                  |
|------------|----------|------------------------------------------|
| **Java**   | 24       | Core programming language                |
| **Maven**  | 3.x      | Dependency management & build automation |
| **Swing**  | Built-in | GUI framework                            |

### Key Dependencies
| Dependency                  | Version  | Description                           |
|-----------------------------|----------|---------------------------------------|
| **Socket.IO Client**        | 2.1.2    | Real-time bidirectional communication |
| **FlatLaf**                 | 3.6.2    | Modern Look and Feel for Swing        |
| **FlatLaf IntelliJ Themes** | 3.6.2    | Professional UI themes                |
| **MigLayout**               | 11.4.2   | Advanced layout manager               |
| **OkHttp**                  | 4.9.3    | HTTP client for Socket.IO             |
| **JSON**                    | 20250517 | JSON data parsing                     |

---

## 🏗 Architecture

```
┌─────────────────────────────────────────┐
│            Client Application           │
├─────────────────────────────────────────┤
│   Main UI (Login, Home, Chat, Profile)  │
├─────────────────────────────────────────┤
│        Event System (PublicEvent)       │
├─────────────────────────────────────────┤
│     Service Layer (Socket.IO Client)    │
├─────────────────────────────────────────┤
│        Model Layer (Data Objects)       │
└─────────────────────────────────────────┘
           ↕ WebSocket (Socket.IO)
┌─────────────────────────────────────────┐
│             IntraChat Server            │
└─────────────────────────────────────────┘
```

### Design Patterns
- **MVC (Model-View-Controller)**: Separation of data, presentation, and logic
- **Observer Pattern**: Event-driven communication between components
- **Singleton Pattern**: Service management (Service, PublicEvent)
- **Factory Pattern**: Component creation and initialization

---

## 📦 Prerequisites

Before running the application, ensure you have:

- ✅ **Java Development Kit (JDK) 24 or higher**
  ```bash
  java -version
  # Should output: java version "24" or higher
  ```

- ✅ **Apache Maven 3.x**
  ```bash
  mvn -version
  # Should output: Apache Maven 3.x.x
  ```

- ✅ **IntraChat Server** running on `localhost:9999` (or configured host)
  - See [server-IntraChat](../server-IntraChat/README.md) for setup

- ✅ **Network Access**: Port 9999 must be accessible to the server

---

## 🚀 Installation

### Option 1: Clone the Repository

```bash
# Clone the project
git clone <repository-url>
cd chat-application-IntraChat

# Install dependencies and build
mvn clean install

# Run the application
mvn exec:java
```

### Option 2: Download Release

1. Download the latest JAR from the [Releases](../../releases) page
2. Run the application:
   ```bash
   java -jar chat-application-IntraChat-1.0-SNAPSHOT.jar
   ```

---

## ⚙️ Configuration

### Server Connection

Edit the connection settings in `Service.java`:

```java
private final int PORT_NUMBER = 9999;  // Server port
private final String IP = "localhost";  // Server IP/hostname
```

### File Storage (for image only)

Client-side file storage is managed in the `client_data/` directory:
- Images received from other users are stored here

### UI Customization

The application uses FlatLaf themes. To change the theme, modify `Main.java`:

```java
// Current theme
FlatArcIJTheme.setup();

// Alternative themes:
// FlatDarkLaf.setup();
// FlatLightLaf.setup();
// FlatIntelliJLaf.setup();
```

---

## 📖 Usage

### First Time Setup

1. **Launch the Application**
   ```bash
   mvn exec:java
   ```

2. **Register a New Account**
   - Click "Register" on the login screen
   - Enter username and password
   - Choose gender and upload profile picture (in progress)
   - Submit to create account

3. **Login**
   - Enter your credentials
   - Click "Login" to access the chat

### Using the Application

#### Starting a Conversation
1. View the user list on the left panel
2. Click on a user to open chat
3. Type your message in the text box
4. Press CTRL + Enter or click Send icon

#### Sending Files (in progress)
1. Click the options icon (...)
2. Click the attachment icon (📎)
3. Select a file from your system
4. File will upload with progress indicator
4. Recipient can download the file

#### Sending Images
1. Click the options icon (...)
2. Click the image icon (🖼️)
3. Select an image file
4. Image preview appears in chat
5. Click image to view full-size

#### Using Emojis
1. Click the options icon (...)
2. Click the emoji button (😊)
3. Browse emoji categories
4. Click emoji to insert in message

#### Viewing User Status
- 🟢 Green dot: User is online
- ⚫ Gray dot: User is offline

---

## 📁 Project Structure

```
chat-application-IntraChat/
│
├── src/main/java/com/example/
│   ├── app/                    # Application-level classes
│   │   └── MessageType.java    # Message type constants
│   │
│   ├── component/              # Reusable UI components
│   │   ├── ChatBody.java       # Chat message display
│   │   ├── ChatBottom.java     # Message input area
│   │   ├── ChatItem.java       # Individual chat messages
│   │   ├── ChatLeft.java       # Received messages
│   │   ├── ChatRight.java      # Sent messages
│   │   ├── ImageItem.java      # Image message display
│   │   └── ItemPeople.java     # User list items
│   │
│   ├── event/                  # Event handling system
│   │   ├── EventChat.java      # Chat events interface
│   │   ├── EventLogin.java     # Login events interface
│   │   ├── EventMain.java      # Main window events
│   │   ├── EventMenuLeft.java  # User list events
│   │   └── PublicEvent.java    # Central event dispatcher
│   │
│   ├── form/                   # Main UI forms
│   │   ├── Login.java          # Login screen
│   │   ├── Home.java           # Main chat window
│   │   ├── Chat.java           # Chat conversation view
│   │   ├── Loading.java        # Loading overlay
│   │   └── ViewImage.java      # Image viewer modal
│   │
│   ├── icon/                   # Icon and emoji handling
│   │   ├── Emoji.java          # Emoji utilities
│   │   └── ModelEmoji.java     # Emoji data model
│   │
│   ├── main/                   # Application entry point
│   │   └── Main.java           # Main class
│   │
│   ├── model/                  # Data models
│   │   ├── ModelLogin.java     # Login credentials
│   │   ├── ModelMessage.java   # Chat messages
│   │   ├── ModelUserAccount.java # User profiles
│   │   ├── ModelFileSender.java  # File upload
│   │   └── ModelFileReceiver.java # File download
│   │
│   ├── service/                # Business logic layer
│   │   └── Service.java        # Socket.IO client service
│   │
│   └── swing/                  # Custom Swing components
│       └── blurHash/           # BlurHash image loading
│
├── src/main/resources/
│   ├── emoji/                  # Emoji image assets
│   └── icon/                   # Application icons
│
├── client_data/                # Client file storage
│
├── pom.xml                     # Maven configuration
└── README.md                   # This file
```

### Key Components Explained

#### Service Layer (`service/Service.java`)
- Manages Socket.IO connection to server
- Handles all WebSocket events (login, messages, file transfer)
- Implements singleton pattern for global access
- Manages file upload/download queues

#### Event System (`event/PublicEvent.java`)
- Central event bus for component communication
- Decouples UI components from business logic
- Implements observer pattern for event listeners

#### UI Components (`form/` and `component/`)
- Modular, reusable Swing components
- Follows MVC architecture
- Each component has `.form` (NetBeans GUI designer) and `.java` files

---

## 🔨 Building from Source

### Development Build

```bash
# Clean and compile
mvn clean compile

# Run in development mode
mvn exec:java
```

### Production Build

```bash
# Create executable JAR
mvn clean package

# JAR file location
# target/chat-application-IntraChat-1.0-SNAPSHOT.jar

# Run the JAR
java -jar target/chat-application-IntraChat-1.0-SNAPSHOT.jar
```

### Creating Distribution

```bash
# Build with dependencies included
mvn clean package assembly:single

# Creates a fat JAR with all dependencies
```

---

## 🐛 Troubleshooting

### Connection Issues

**Problem**: Cannot connect to server
```
Solution:
1. Verify server is running on localhost:9999
2. Check firewall settings
3. Verify IP and PORT in Service.java
4. Check server logs for errors
```

**Problem**: Connection drops frequently
```
Solution:
1. Check network stability
2. Increase Socket.IO timeout in server config
3. Verify no proxy interfering with WebSocket
```

### UI Issues

**Problem**: UI appears distorted or too small
```
Solution:
1. Update Java to latest version
2. Check display scaling settings
3. Try different FlatLaf theme
4. Verify minimum window size (800x500)
```

**Problem**: Images not loading
```
Solution:
1. Verify resources/icon/ directory exists
2. Check file permissions in client_data/
3. Ensure image formats are supported (PNG, JPG)
```

### File Transfer Issues

**Problem**: File upload fails
```
Solution:
1. Check file size limits (if any)
2. Verify write permissions in client_data/
3. Ensure stable connection to server
4. Check server file storage capacity
```

### Build Issues

**Problem**: Maven build fails
```
Solution:
1. Verify JDK 24 is installed and JAVA_HOME is set
2. Clear Maven cache: mvn dependency:purge-local-repository
3. Update Maven: mvn -U clean install
4. Check network for dependency downloads
```

---

## 🤝 Contributing

Contributions are welcome! Please follow these guidelines:

### Development Workflow

1. **Fork the Repository**
   ```bash
   git clone <your-fork-url>
   cd chat-application-IntraChat
   ```

2. **Create a Feature Branch**
   ```bash
   git checkout -b feature/your-feature-name
   ```

3. **Make Changes**
   - Follow Java naming conventions
   - Add comments for complex logic
   - Update documentation if needed

4. **Test Thoroughly**
   - Test with multiple users
   - Verify file transfers work
   - Check UI responsiveness

5. **Commit and Push**
   ```bash
   git add .
   git commit -m "Add: descriptive commit message"
   git push origin feature/your-feature-name
   ```

6. **Create Pull Request**
   - Describe your changes clearly
   - Reference any related issues

### Code Style Guidelines

- Use 4 spaces for indentation
- Follow standard Java naming conventions:
  - Classes: `PascalCase`
  - Methods: `camelCase`
  - Constants: `UPPER_SNAKE_CASE`
- Add JavaDoc comments for public methods
- Keep methods focused and under 50 lines when possible

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👥 Authors

- **Development Team** - *Initial work* - University of Transport Ho Chi Minh City

---

## 🙏 Acknowledgments

- **FlatLaf** - For the modern Look and Feel
- **Socket.IO** - For reliable real-time communication
- **MigLayout** - For flexible layout management
- **BlurHash** - For progressive image loading
- **Open Source Community** - For excellent libraries and tools

---

## 📞 Support

For issues, questions, or suggestions:

- 📧 Email: support@intrachat.example.com
- 🐛 Issues: [GitHub Issues](../../issues)
- 📚 Documentation: [Wiki](../../wiki)

---

<div align="center">
  Made with ❤️ by the IntraChat Team
  
  **[⬆ back to top](#intrachat-client-application)**
</div>
