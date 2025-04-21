-- Create database and select it
CREATE DATABASE listeningapp2;
USE listeningapp2;

-- Users Table - Central user management
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    deleted TINYINT(1) DEFAULT 0,
    role VARCHAR(255) NOT NULL,
    password VARCHAR(60) NOT NULL
);

CREATE TABLE class (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    className VARCHAR(255) NOT NULL
);

CREATE TABLE teacherMaster (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    Email VARCHAR(255) NOT NULL,
    Firstname VARCHAR(100) NOT NULL,
    LastName VARCHAR(100) NOT NULL,
    isActive BOOLEAN DEFAULT TRUE,
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE moderator (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    Email VARCHAR(255) NOT NULL,
    Firstname VARCHAR(100) NOT NULL,
    LastName VARCHAR(100) NOT NULL,
    isActive BOOLEAN DEFAULT TRUE,
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE administrator (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    Email VARCHAR(255) NOT NULL,
    Firstname VARCHAR(100) NOT NULL,
    LastName VARCHAR(100) NOT NULL,
    isActive BOOLEAN DEFAULT TRUE,
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE student (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    Email VARCHAR(255) NOT NULL,
    Firstname VARCHAR(100) NOT NULL,
    LastName VARCHAR(100) NOT NULL,
    isActive BOOLEAN DEFAULT TRUE,
    classID INT,
    user_id INT,
    FOREIGN KEY (classID) REFERENCES class(ID),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE studentClass (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    studentID INT NOT NULL,
    classID INT NOT NULL,
    FOREIGN KEY (studentID) REFERENCES student(ID),
    FOREIGN KEY (classID) REFERENCES class(ID)
);

CREATE TABLE reportTime (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    timeOfReport DATETIME NOT NULL,
    email VARCHAR(255),
    description TEXT,
    lastUpdatedTime DATETIME NOT NULL,
    lastUpdatedBy VARCHAR(255),
    status VARCHAR(50)
);

CREATE TABLE song (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    songName VARCHAR(255) NOT NULL,
    songComposer VARCHAR(255) NOT NULL,
    songYear INT(4),
    youtubeLink VARCHAR(255),
    mrTimestamp INT DEFAULT -1
);

CREATE TABLE studentPerformance (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    StudentID INT NOT NULL,
    ClassID INT NOT NULL,
    Weight DECIMAL(3,2),
    Score DECIMAL(5,2),
    SongID INT NOT NULL,
    PlaylistID INT NOT NULL,
    TimesCorrect INT DEFAULT 0,
    TimesQuizzed INT DEFAULT 0,
    FOREIGN KEY (StudentID) REFERENCES student(ID),
    FOREIGN KEY (ClassID) REFERENCES class(ID),
    FOREIGN KEY (SongID) REFERENCES song(ID)
);

CREATE TABLE playlist (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    playlistName VARCHAR(255) NOT NULL,
    teacherID INT,
    classID INT,
    FOREIGN KEY (teacherID) REFERENCES teacherMaster(ID),
    FOREIGN KEY (classID) REFERENCES class(ID)
);

CREATE TABLE playlistSongs (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    playlistID INT NOT NULL,
    songID INT NOT NULL,
    FOREIGN KEY (playlistID) REFERENCES playlist(ID),
    FOREIGN KEY (songID) REFERENCES song(ID),
    udTimestamp INT DEFAULT -1
);

CREATE TABLE metaData (
    dataID INT AUTO_INCREMENT PRIMARY KEY,
    appName VARCHAR(255) NOT NULL,
    version VARCHAR(20) NOT NULL,
    lastUpdate DATETIME DEFAULT CURRENT_TIMESTAMP,
    logo VARCHAR(255)
);

CREATE TABLE quizSettings (
    ID INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    playlistID INT,
    playbackMethod VARCHAR(255),
    playbackDuration INT,
    numQuestions INT,
    deleted TINYINT(1) default 0
);

CREATE TABLE quizResults (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    quizSettingsID INT NOT NULL,
    songID INT NOT NULL,
    songName VARCHAR(255) NOT NULL,
    songComposer VARCHAR(255),
    songYear INT,
    deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (quizSettingsID) REFERENCES quizSettings(ID) ON DELETE CASCADE
);

CREATE TABLE sessions (
    session_id VARCHAR(255) PRIMARY KEY,
    user_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP
);

-- Create View for Playlist
CREATE VIEW view_playlist AS
SELECT
    p.playlistName,
    s.songName,
    s.songComposer,
    s.songYear,
    s.youtubeLink,
    p.classID
FROM
    playlist p
JOIN
    playlistSongs ps ON p.ID = ps.playlistID
JOIN
    song s ON ps.songID = s.ID
ORDER BY
    p.playlistName;

-- Create View for Class
CREATE VIEW view_class AS
SELECT
    c.ID AS classID,
    c.className,
    t.ID AS teacherID,
    u_t.email AS teacherEmail,
    u_t.first_name AS teacherFirstname,
    u_t.last_name AS teacherLastname,
    s.ID AS studentID,
    u_s.email AS studentEmail,
    u_s.first_name AS studentFirstname,
    u_s.last_name AS studentLastname,
    s.isActive AS studentIsActive
FROM
    class c
JOIN
    teacherMaster t ON c.ID = t.ID
LEFT JOIN
    users u_t ON t.user_id = u_t.user_id
LEFT JOIN
    student s ON c.ID = s.classID
LEFT JOIN
    users u_s ON s.user_id = u_s.user_id;

-- Metadata view
CREATE VIEW view_metadata AS 
select
dataID, 
appName, 
version,
 count(user_id) userCount, 
 lastUpdate, 
 logo
 from metadata
 join users
 group by dataID