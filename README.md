# Book_Awra_2024_25


 Name             |      ID
----------------- | -------------
Selamawit Shimeles| UGR/8982/15
Tsion Shimelis    | UGR/0654/15
Yadeni Lemma      | UGR/0174/15
Yedi Worku        | UGR/1035/15
Yordanos Abay     | UGR/0919/15

## About BookAura

📖 BookAura is a Kotlin-based Wattpad clone that allows users to write, read, and interact with stories. It features user authentication, story management, and interactive elements like likes and comments. The app runs locally, using a Node.js/Express.js backend with MongoDB as the database.
🛠️ Tech Stack
Frontend: Kotlin (Jetpack Compose/Android)Backend: Node.js, Express.js
Database: MongoDBAuthentication: JWT (JSON Web Token)
✨ Features
1. Authentication & AuthorizationUser sign-up & login with JWT authentication.
Role-based access (Regular Users & Admins).
Password hashing using bcrypt.
2. Story Management (CRUD)Users can create, read, update, and delete their own stories.
Each story contains:
Title
Description
Genre
Content
Author Info
Creation Date
3. User Interactions
Like & Comment on stories.
Users can edit and delete their own comments.
Display the number of likes per story.
4. User Profiles
Users can create and edit their profile.
View all published stories by a specific author.
🏗️ Project Setup
📌 Backend Setup (Node.js & MongoDB)Clone the repository:
git clone https://github.com/yourusername/bookaura.git
cd bookaura/backendInstall dependencies:
npm install
Create a .env file with the following:
PORT=5000MONGO_URI=mongodb://localhost:27017/bookaura
JWT_SECRET=your_secret_keyStart the server:
npm start
📌 Frontend Setup (Kotlin - Android Studio)
Open Android Studio.
Clone the repository and open the frontend folder.
Build and run the project on an emulator or real device.
🔍 API Endpoints
AuthenticationMethod Endpoint Description
POST /api/auth/register User RegistrationPOST /api/auth/login User Login
StoriesMethod Endpoint Description
GET /api/stories Get all stories
POST /api/stories Create a new storyPUT /api/stories/:id Update a story
DELETE /api/stories/:id Delete a story
🧪 TestingBackend: Unit & Integration tests using Jest.
Frontend: Widget Testing for UI components.
🚀 Future EnhancementsImplement offline reading mode.
Add a story recommendation system.
Implement push notifications for new story uploads.
