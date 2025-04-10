=
JavaChatApp - UniversityOfGreenwichCoursework 
=
📁 Project Folder Name: COMP1549_Group51_CW 
👨‍💻 Language: Java 
📌 Description: 
- This is a Java-based group chat system implemented as part of our COMP1549 coursework.
- The system uses a client-server architecture and supports private messaging, broadcast messaging, coordinator management, and fault tolerance.
- The project includes:
  - Server.java → Handles connections, coordinator logic, and message routing.
  - Client.java → Connects users to the server and allows interaction.
  - Singleton.java → Shared logger using the Singleton Design Pattern.
  - ServerTest.java → JUnit to test the correct behavior of the Singleton pattern by ensuring only one instance is created.
🛠️ How to Run: 
1. Open Command Prompt. 
2. Navigate to the folder: 
   cd COMP1549_Group51_CW 
   cd src 
3. Compile all files: 
   javac *.java 
4. Start the server: 
   java Server 
5. Open a new command prompt for each client: 
   java Client 
📎 Coursework Report (PDF): The PDF report includes our implementation details, screenshots, AI usage, and contribution table. 
🔗 View the PDF here: [Insert your Google Drive share link here]

🧪 JUnit Testing: Run the test to validate Singleton: 
  - javac -cp .;junit-4.13.2.jar;hamcrest-core-1.3.jar ServerTest.java 
  - java -cp .;junit-4.13.2.jar;hamcrest-core-1.3.jar org.junit.runner.JUnitCore ServerTest

✅ Features Implemented: 
- Broadcast and private messaging 
- Unique client ID and name registration 
- Dynamic coordinator assignment and reassignment (fault tolerance) 
- Singleton design pattern for logging 
- AI-assisted enhancements (instructions display, usability) 
- JUnit test verification
 
👥 Group 51 Members: 
- Bryan Hernandez Upegui 
- Sushil Singh 
- Ben Peverall
- Thank you for reviewing our work!
