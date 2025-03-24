-- Clear existing data (if any)
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE playlistSongs;
TRUNCATE TABLE playlist;
TRUNCATE TABLE songPerformance;
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
('admin1', 'admin1@example.com', 'John', 'Smith', 0, 'administrator', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('admin2', 'admin2@example.com', 'Emma', 'Johnson', 0, 'administrator', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('admin3', 'admin3@example.com', 'Michael', 'Williams', 0, 'administrator', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('admin4', 'admin4@example.com', 'Olivia', 'Brown', 0, 'administrator', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('admin5', 'admin5@example.com', 'William', 'Jones', 0, 'administrator', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('admin6', 'admin6@example.com', 'Sophia', 'Miller', 0, 'administrator', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('admin7', 'admin7@example.com', 'James', 'Davis', 0, 'administrator', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('admin8', 'admin8@example.com', 'Charlotte', 'Garcia', 0, 'administrator', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('admin9', 'admin9@example.com', 'Benjamin', 'Rodriguez', 0, 'administrator', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('admin10', 'admin10@example.com', 'Amelia', 'Wilson', 0, 'administrator', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('admin11', 'admin11@example.com', 'Lucas', 'Martinez', 0, 'administrator', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK');

INSERT INTO users (username, email, first_name, last_name, deleted, role, password) VALUES
('mod1', 'mod1@example.com', 'Harper', 'Anderson', 0, 'moderator', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('mod2', 'mod2@example.com', 'Ethan', 'Thomas', 0, 'moderator', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('mod3', 'mod3@example.com', 'Ava', 'Jackson', 0, 'moderator', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('mod4', 'mod4@example.com', 'Noah', 'White', 0, 'moderator', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('mod5', 'mod5@example.com', 'Isabella', 'Harris', 0, 'moderator', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('mod6', 'mod6@example.com', 'Mason', 'Martin', 0, 'moderator', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('mod7', 'mod7@example.com', 'Mia', 'Thompson', 0, 'moderator', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('mod8', 'mod8@example.com', 'Jacob', 'Garcia', 0, 'moderator', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('mod9', 'mod9@example.com', 'Abigail', 'Martinez', 0, 'moderator', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('mod10', 'mod10@example.com', 'Alexander', 'Robinson', 0, 'moderator', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK'),
('mod11', 'mod11@example.com', 'Emily', 'Clark', 0, 'moderator', '$2a$12$69rOPERjWxQ5fnzy4CbRZ.i.0w0MARpuH5hUf37.007eWx44i5bAK');

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
SELECT email, first_name, last_name, 1, user_id FROM users WHERE role = 'admin';

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

INSERT INTO reportTime (timeOfReport, description, status) VALUES
('2025-03-01 09:30:00', 'Weekly performance report', 'Completed'),
('2025-03-02 14:15:00', 'Student progress assessment', 'Pending'),
('2025-03-05 10:00:00', 'Pop quiz results', 'In Progress'),
('2025-03-08 13:45:00', 'Monthly evaluation', 'Completed'),
('2025-03-10 11:30:00', 'Semester midpoint check', 'Completed'),
('2025-03-12 15:20:00', 'Theory test results', 'Pending'),
('2025-03-15 09:00:00', 'Composition project evaluation', 'In Progress'),
('2025-03-18 14:30:00', 'Performance recital feedback', 'Completed'),
('2025-03-20 10:45:00', 'Practical skills assessment', 'Pending'),
('2025-03-22 13:15:00', 'Ear training test results', 'In Progress'),
('2025-03-24 11:00:00', 'Year-end progress summary', 'Completed');

INSERT INTO song (songName, songComposer, songYear, youtubeLink) VALUES
('Moonlight Sonata', 'Ludwig van Beethoven', 1801, 'https://www.youtube.com/watch?v=4Tr0otuiQuU'),
('Für Elise', 'Ludwig van Beethoven', 1810, 'https://www.youtube.com/watch?v=_mVW8tgGY_w'),
('Claire de Lune', 'Claude Debussy', 1905, 'https://www.youtube.com/watch?v=CvFH_6DNRCY'),
('Nocturne Op. 9 No. 2', 'Frédéric Chopin', 1832, 'https://www.youtube.com/watch?v=9E6b3swbnWg'),
('The Four Seasons - Spring', 'Antonio Vivaldi', 1723, 'https://www.youtube.com/watch?v=mFWQgxXM_b8'),
('Canon in D', 'Johann Pachelbel', 1680, 'https://www.youtube.com/watch?v=8Af372EQLck'),
('Symphony No. 5', 'Ludwig van Beethoven', 1808, 'https://www.youtube.com/watch?v=fOk8Tm815lE'),
('The Blue Danube', 'Johann Strauss II', 1866, 'https://www.youtube.com/watch?v=cKkDMiGUbUw'),
('Requiem in D Minor', 'Wolfgang Amadeus Mozart', 1791, 'https://www.youtube.com/watch?v=Zi8vJ_lMxQI'),
('Gymnopédie No. 1', 'Erik Satie', 1888, 'https://www.youtube.com/watch?v=S-Xm7s9eGxU'),
('Prelude in C Major', 'Johann Sebastian Bach', 1722, 'https://www.youtube.com/watch?v=frxT2qB1POQ');

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

INSERT INTO playlistSongs (playlistID, songID) VALUES
(1, 1),
(1, 2),
(1, 3),
(2, 2),
(2, 4),
(3, 5),
(3, 6),
(4, 7),
(4, 8),
(5, 9),
(5, 10),
(6, 11),
(6, 1),
(7, 2),
(7, 3),
(8, 4),
(8, 5),
(9, 6),
(9, 7),
(10, 8),
(10, 9),
(11, 10),
(11, 11);

INSERT INTO songPerformance (StudentID, ClassID, SongID, TimesCorrect, TimesQuizzed) VALUES
(1, 1, 1, 8, 10),
(2, 2, 2, 7, 10),
(3, 3, 3, 9, 12),
(4, 4, 4, 5, 8),
(5, 5, 5, 10, 10),
(6, 6, 6, 6, 10),
(7, 7, 7, 8, 10),
(8, 8, 8, 7, 9),
(9, 9, 9, 4, 10),
(10, 10, 10, 9, 11),
(11, 11, 11, 10, 10);