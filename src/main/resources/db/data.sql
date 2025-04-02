-- Clear existing data (if any)
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE playlistSongs;
TRUNCATE TABLE playlist;
TRUNCATE TABLE studentPerformance;
TRUNCATE TABLE song;
TRUNCATE TABLE reportTime;
TRUNCATE TABLE studentClass;
TRUNCATE TABLE student;
TRUNCATE TABLE administrator;
TRUNCATE TABLE moderator;
TRUNCATE TABLE teacherMaster;
TRUNCATE TABLE class;
TRUNCATE TABLE users;
TRUNCATE TABLE metaData;
TRUNCATE TABLE quizSettings;
SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO metaData (appName, version, lastUpdate, logo) VALUES 
('Listening Study', '1.0', '2025-02-19', ''),
('Listening Study', '2.0', '2025-03-19', '');

INSERT INTO class (className) VALUES
('Music Theory 101'),
('Classical Piano'),
('Jazz Ensemble'),
('Vocal Training'),
('Music History'),
('Guitar Fundamentals'),
('Electronic Music Production'),
('Orchestral Studies'),
('Music Composition'),
('World Music'),
('Music Appreciation');

INSERT INTO users (username, email, first_name, last_name, deleted, role, password) VALUES
('administrator1', 'administrator1@example.com', 'John', 'Smith', 0, 'administrator', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('administrator2', 'administrator2@example.com', 'Emma', 'Johnson', 0, 'administrator', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('administrator3', 'administrator3@example.com', 'Michael', 'Williams', 0, 'administrator', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('administrator4', 'administrator4@example.com', 'Olivia', 'Brown', 0, 'administrator', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('administrator5', 'administrator5@example.com', 'William', 'Jones', 0, 'administrator', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('administrator6', 'administrator6@example.com', 'Sophia', 'Miller', 0, 'administrator', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('administrator7', 'administrator7@example.com', 'James', 'Davis', 0, 'administrator', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('administrator8', 'administrator8@example.com', 'Charlotte', 'Garcia', 0, 'administrator', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('administrator9', 'administrator9@example.com', 'Benjamin', 'Rodriguez', 0, 'administrator', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('administrator10', 'administrator10@example.com', 'Amelia', 'Wilson', 0, 'administrator', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('administrator11', 'administrator11@example.com', 'Lucas', 'Martinez', 0, 'administrator', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK');

INSERT INTO users (username, email, first_name, last_name, deleted, role, password) VALUES
('moderator1', 'moderator1@example.com', 'Harper', 'Anderson', 0, 'moderator', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('moderator2', 'moderator2@example.com', 'Ethan', 'Thomas', 0, 'moderator', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('moderator3', 'moderator3@example.com', 'Ava', 'Jackson', 0, 'moderator', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('moderator4', 'moderator4@example.com', 'Noah', 'White', 0, 'moderator', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('moderator5', 'moderator5@example.com', 'Isabella', 'Harris', 0, 'moderator', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('moderator6', 'moderator6@example.com', 'Mason', 'Martin', 0, 'moderator', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('moderator7', 'moderator7@example.com', 'Mia', 'Thompson', 0, 'moderator', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('moderator8', 'moderator8@example.com', 'Jacob', 'Garcia', 0, 'moderator', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('moderator9', 'moderator9@example.com', 'Abigail', 'Martinez', 0, 'moderator', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('moderator10', 'moderator10@example.com', 'Alexander', 'Robinson', 0, 'moderator', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('moderator11', 'moderator11@example.com', 'Emily', 'Clark', 0, 'moderator', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK');

INSERT INTO users (username, email, first_name, last_name, deleted, role, password) VALUES
('teacher1', 'teacher1@example.com', 'Daniel', 'Lewis', 0, 'teacher', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('teacher2', 'teacher2@example.com', 'Sofia', 'Lee', 0, 'teacher', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('teacher3', 'teacher3@example.com', 'Matthew', 'Walker', 0, 'teacher', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('teacher4', 'teacher4@example.com', 'Ella', 'Hall', 0, 'teacher', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('teacher5', 'teacher5@example.com', 'David', 'Allen', 0, 'teacher', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('teacher6', 'teacher6@example.com', 'Grace', 'Young', 0, 'teacher', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('teacher7', 'teacher7@example.com', 'Joseph', 'Hernandez', 0, 'teacher', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('teacher8', 'teacher8@example.com', 'Chloe', 'King', 0, 'teacher', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('teacher9', 'teacher9@example.com', 'Samuel', 'Wright', 0, 'teacher', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('teacher10', 'teacher10@example.com', 'Victoria', 'Lopez', 0, 'teacher', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('teacher11', 'teacher11@example.com', 'Henry', 'Hill', 0, 'teacher', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK');

INSERT INTO users (username, email, first_name, last_name, deleted, role, password) VALUES
('student1', 'student1@example.com', 'Liam', 'Scott', 0, 'student', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('student2', 'student2@example.com', 'Zoe', 'Green', 0, 'student', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('student3', 'student3@example.com', 'Jackson', 'Adams', 0, 'student', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('student4', 'student4@example.com', 'Lily', 'Baker', 0, 'student', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('student5', 'student5@example.com', 'Aiden', 'Gonzalez', 0, 'student', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('student6', 'student6@example.com', 'Madison', 'Nelson', 0, 'student', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('student7', 'student7@example.com', 'Owen', 'Carter', 0, 'student', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('student8', 'student8@example.com', 'Scarlett', 'Mitchell', 0, 'student', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('student9', 'student9@example.com', 'Gabriel', 'Perez', 0, 'student', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('student10', 'student10@example.com', 'Aubrey', 'Roberts', 0, 'student', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('student11', 'student11@example.com', 'Connor', 'Turner', 0, 'student', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK');

INSERT INTO administrator (Email, Firstname, LastName, isActive, user_id) 
SELECT email, first_name, last_name, 1, user_id FROM users WHERE role = 'administrator';

INSERT INTO moderator (Email, Firstname, LastName, isActive, user_id) 
SELECT email, first_name, last_name, 1, user_id FROM users WHERE role = 'moderator';

INSERT INTO teacherMaster (Email, Firstname, LastName, isActive, user_id) 
SELECT email, first_name, last_name, 1, user_id FROM users WHERE role = 'teacher';

INSERT INTO student (Email, Firstname, LastName, isActive, classID, user_id) 
SELECT 
    u.email, 
    u.first_name, 
    u.last_name, 
    1, 
    c.ID,
    u.user_id
FROM 
    users u
JOIN 
    class c ON c.ID = (u.user_id % 11) + 1
WHERE 
    u.role = 'student';

INSERT INTO studentClass (studentID, classID)
SELECT 
    s.ID, 
    (s.ID % 11) + 1
FROM 
    student s;

INSERT INTO reportTime (timeOfReport, username, email, description, lastUpdatedTime, lastUpdatedBy, status) VALUES
('2025-03-01 09:30:00', 'moderator1', 'moderator1@example.com', 'Weekly performance report', '2025-03-01 09:30:00', 'administrator1', 'Resolved'),
('2025-03-02 14:15:00', 'moderator1', 'moderator1@example.com', 'Student progress assessment', '2025-03-01 09:30:00', 'administrator1', 'Open'),
('2025-03-05 10:00:00', 'moderator1', 'moderator1@example.com', 'Pop quiz results', '2025-03-01 09:30:00', 'administrator1', 'Acknowledged'),
('2025-03-08 13:45:00', 'moderator1', 'moderator1@example.com', 'Monthly evaluation', '2025-03-01 09:30:00', 'administrator1', 'Resolved'),
('2025-03-10 11:30:00', 'moderator1', 'moderator1@example.com', 'Semester midpoint check', '2025-03-01 09:30:00', 'administrator1', 'Resolved'),
('2025-03-12 15:20:00', 'moderator1', 'moderator1@example.com', 'Theory test results', '2025-03-01 09:30:00', 'administrator1', 'Open'),
('2025-03-15 09:00:00', 'moderator1', 'moderator1@example.com', 'Composition project evaluation', '2025-03-01 09:30:00', 'administrator1', 'Acknowledged'),
('2025-03-18 14:30:00', 'moderator1', 'moderator1@example.com', 'Performance recital feedback', '2025-03-01 09:30:00', 'administrator1', 'Resolved'),
('2025-03-20 10:45:00', 'moderator1', 'moderator1@example.com', 'Practical skills assessment', '2025-03-01 09:30:00', 'administrator1', 'Open'),
('2025-03-22 13:15:00', 'moderator1', 'moderator1@example.com', 'Ear training test results', '2025-03-01 09:30:00', 'administrator1', 'Acknowledged'),
('2025-03-24 11:00:00', 'moderator1', 'moderator1@example.com', 'Year-end progress summary', '2025-03-01 09:30:00', 'administrator1', 'Resolved');

INSERT INTO song (songName, songComposer, songYear, youtubeLink, mrTimestamp) VALUES
('Moonlight Sonata', 'Ludwig van Beethoven', 1801, 'https://www.youtube.com/watch?v=4Tr0otuiQuU', 20),
('Für Elise', 'Ludwig van Beethoven', 1810, 'https://www.youtube.com/watch?v=_mVW8tgGY_w', 82),
('Claire de Lune', 'Claude Debussy', 1905, 'https://www.youtube.com/watch?v=CvFH_6DNRCY', -1),
('Nocturne Op. 9 No. 2', 'Frédéric Chopin', 1832, 'https://www.youtube.com/watch?v=9E6b3swbnWg', 44),
('The Four Seasons - Spring', 'Antonio Vivaldi', 1723, 'https://www.youtube.com/watch?v=mFWQgxXM_b8', 15),
('Canon in D', 'Johann Pachelbel', 1680, 'https://www.youtube.com/watch?v=8Af372EQLck', 30),
('Symphony No. 5', 'Ludwig van Beethoven', 1808, 'https://www.youtube.com/watch?v=fOk8Tm815lE', -1),
('The Blue Danube', 'Johann Strauss II', 1866, 'https://www.youtube.com/watch?v=cKkDMiGUbUw', 20),
('Requiem in D Minor', 'Wolfgang Amadeus Mozart', 1791, 'https://www.youtube.com/watch?v=Zi8vJ_lMxQI', 20),
('Gymnopédie No. 1', 'Erik Satie', 1888, 'https://www.youtube.com/watch?v=S-Xm7s9eGxU', 15),
('Prelude in C Major', 'Johann Sebastian Bach', 1722, 'https://www.youtube.com/watch?v=frxT2qB1POQ', 12);

INSERT INTO playlist (playlistName, teacherID, classID) VALUES
('Classical Masterpieces', 1, 1),
('Piano Fundamentals', 2, 2),
('Jazz Standards', 3, 3),
('Vocal Training Essentials', 4, 4),
('Historical Compositions', 5, 5),
('Guitar Classics', 6, 6),
('Electronic Music Pioneers', 7, 7),
('Orchestral Favorites', 8, 8),
('Composition Studies', 9, 9),
('World Music Exploration', 10, 10),
('Music Appreciation Selections', 11, 11);

INSERT INTO playlistSongs (playlistID, songID, udTimestamp) VALUES
(1, 1, 10),
(1, 2, 55),
(1, 3, -1),
(2, 2, -1),
(2, 4, -1),
(3, 5, 20),
(3, 6, 22),
(4, 7, -1),
(4, 8, -1),
(5, 9, -1),
(5, 10, -1),
(6, 11, 11),
(6, 1, 14),
(7, 2, 14),
(7, 3, -1),
(8, 4, 12),
(8, 5, 12),
(9, 6, -1),
(9, 7, -1),
(10, 8, -1),
(10, 9, -1),
(11, 10, -1),
(11, 11, -1);

INSERT INTO studentPerformance (StudentID, ClassID, SongID, PlaylistID, TimesCorrect, TimesQuizzed, Weight, Score) VALUES
(1, 1, 1, 1, 8, 10, 0.20, 80.00),
(2, 2, 2, 2, 7, 10, 0.30, 70.00),
(3, 3, 3, 3, 9, 12, 0.25, 75.00),
(4, 4, 4, 4, 5, 8, 0.38, 62.50),
(5, 5, 5, 5, 10, 10, 0.00, 100.00),
(6, 6, 6, 6, 6, 10, 0.40, 60.00),
(7, 7, 7, 7, 8, 10, 0.20, 80.00),
(8, 8, 8, 8, 7, 9, 0.22, 77.80),
(9, 9, 9, 9, 4, 10, 0.60, 40.00),
(10, 10, 10, 10, 9, 11, 0.18, 81.80),
(11, 11, 11, 11, 10, 10, 0.00, 100.00);

INSERT INTO quizSettings (playlistID, playbackMethod, playbackDuration, numQuestions, deleted) VALUES
(1, 'Random', 30, 10, 0),
(1, 'Random', 45, 15, 0),
(2, 'Random', 20, 8, 0),
(3, 'MostReplayed', 60, 20, 0),
(4, 'TeacherTimestamp', 30, 12, 0),
(5, 'TeacherTimestamp', 40, 15, 0),
(6, 'MostReplayed', 25, 10, 0),
(7, 'Random', 35, 12, 0),
(8, 'TeacherTimestamp', 45, 15, 0),
(9, 'MostReplayed', 30, 10, 0),
(10, 'Random', 60, 20, 0),
(11, 'TeacherTimestamp', 20, 8, 0);
