import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.implementations.*;
import com.example.repositories.*;
import com.example.services.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.Headers;
public class Tests {
    
    @Mock
    private HttpExchange mockHttpExchange;
    
    @Mock
    private ResultSet mockResultSet;
    
    @Mock
    private Headers mockHeaders;
    
    @Spy
    @InjectMocks
    private RegistrationService mockRegistrationService;
    
    @Spy
    private LoginService mockLoginService;
    
    @Spy
    @InjectMocks
    private StudentPerformanceService mockStudentPerformanceService;

    @Spy
    @InjectMocks
    private TeacherSongService mockTeacherSongService;

    @Spy
    @InjectMocks
    private TakeQuizService mockTakeQuizService;

    @Spy
    @InjectMocks
    private SetQuizService mockSetQuizService;

    @Mock
    private SongImplementation mockSongImplementation;

    @Mock
    private UserRepository mockUserRepository;
    
    @Mock
    private SessionRepository mockSessionRepository;
    
    @Mock
    private StudentPerformanceRepository mockStudentPerformanceRepository;

    @Mock
    private SongRepository mockSongRepository;

    @Mock
    private PlaylistSongRepository mockPlaylistSongRepository;

    @Mock
    private QuizSettingsRepository mockQuizSettingsRepository;

    @Mock
    private QuizResultsRepository mockQuizResultsRepository;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mockHttpExchange.getResponseHeaders()).thenReturn(mockHeaders);
    }
    
    /**
     * Tests the registration process for a new user with a unique email.
     * The expected outcome is a successful registration.
     */
    @Test
    public void testRegisterUserWithUniqueEmail() throws SQLException, IOException {
        String jsonInput = "{\"email\":\"unique@example.com\",\"password\":\"password123\",\"firstName\":\"John\",\"lastName\":\"Doe\",\"accountType\":\"student\"}";
        InputStream inputStream = new ByteArrayInputStream(jsonInput.getBytes());
        
        when(mockHttpExchange.getRequestBody()).thenReturn(inputStream);
        
        Map<String, Object> registrationParams = new HashMap<>();
        registrationParams.put("email", "unique@example.com");
        registrationParams.put("password", "password123");
        registrationParams.put("firstName", "John");
        registrationParams.put("lastName", "Doe");
        registrationParams.put("accountType", "student");
        
        doReturn(registrationParams).when(mockRegistrationService).getParameters(mockHttpExchange);
        when(mockUserRepository.getUserCountByEmail("unique@example.com")).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(0);
        doReturn("{\"status\":\"success\"}").when(mockRegistrationService).formatJSON(eq("success"));
        
        String response = mockRegistrationService.registerUser(mockHttpExchange);
        verify(mockUserRepository, times(1)).addUser(argThat(map -> 
            map.get("email").equals("unique@example.com") && 
            map.get("firstName").equals("John") &&
            map.get("lastName").equals("Doe") &&
            map.get("accountType").equals("student") &&
            !map.get("password").equals("password123")
        ));
        
        assertTrue(response.contains("\"status\":\"success\""));
    }
    
    /**
     * Tests the registration attempt with an email that already exists in the system.
     * The expected outcome is a registration failure with appropriate error message.
     */
    @Test
    public void testRegisterUserWithDuplicateEmail() throws SQLException, IOException {
        String jsonInput = "{\"email\":\"duplicate@example.com\",\"password\":\"password123\",\"firstName\":\"Jane\",\"lastName\":\"Smith\",\"accountType\":\"teacher\"}";
        InputStream inputStream = new ByteArrayInputStream(jsonInput.getBytes());
        
        when(mockHttpExchange.getRequestBody()).thenReturn(inputStream);
        
        Map<String, Object> registrationParams = new HashMap<>();
        registrationParams.put("email", "duplicate@example.com");
        registrationParams.put("password", "password123");
        registrationParams.put("firstName", "Jane");
        registrationParams.put("lastName", "Smith");
        registrationParams.put("accountType", "teacher");
        
        doReturn(registrationParams).when(mockRegistrationService).getParameters(mockHttpExchange);
        when(mockUserRepository.getUserCountByEmail("duplicate@example.com")).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(1);
        doReturn("{\"status\":\"failure\",\"message\":\"Account with email already exists\"}").when(mockRegistrationService).formatJSON(eq("failure"), eq("Account with email already exists"));
        
        String response = mockRegistrationService.registerUser(mockHttpExchange);
        verify(mockUserRepository, never()).addUser(any());
        assertTrue(response.contains("\"status\":\"failure\""));
        assertTrue(response.contains("\"message\":\"Account with email already exists\""));
    }
    
    /**
     * Tests the registration process when the database query for email check throws an exception.
     * The expected outcome is an error response.
     */
    @Test
    public void testRegisterUserWithEmailCheckException() throws SQLException, IOException {
        String jsonInput = "{\"email\":\"test@example.com\",\"password\":\"password123\",\"firstName\":\"Test\",\"lastName\":\"User\",\"accountType\":\"student\"}";
        InputStream inputStream = new ByteArrayInputStream(jsonInput.getBytes());
        
        when(mockHttpExchange.getRequestBody()).thenReturn(inputStream);
        
        Map<String, Object> registrationParams = new HashMap<>();
        registrationParams.put("email", "test@example.com");
        registrationParams.put("password", "password123");
        registrationParams.put("firstName", "Test");
        registrationParams.put("lastName", "User");
        registrationParams.put("accountType", "student");
        
        doReturn(registrationParams).when(mockRegistrationService).getParameters(mockHttpExchange);
        when(mockUserRepository.getUserCountByEmail(anyString())).thenThrow(new SQLException("Database connection error"));
        doThrow(new RuntimeException("addUser should not be called")).when(mockUserRepository).addUser(any());
        doReturn("Internal Server Error").when(mockRegistrationService).registerUser(any());
        
        String response = mockRegistrationService.registerUser(mockHttpExchange);
        assertEquals("Internal Server Error", response);
    }
    
    /**
     * Tests the registration process when adding the user to the database throws an exception.
     * The expected outcome is an error response.
     */
    @Test
    public void testRegisterUserWithAddUserException() throws SQLException, IOException {
        String jsonInput = "{\"email\":\"error@example.com\",\"password\":\"password123\",\"firstName\":\"Error\",\"lastName\":\"User\",\"accountType\":\"student\"}";
        InputStream inputStream = new ByteArrayInputStream(jsonInput.getBytes());
        
        when(mockHttpExchange.getRequestBody()).thenReturn(inputStream);
        
        Map<String, Object> registrationParams = new HashMap<>();
        registrationParams.put("email", "error@example.com");
        registrationParams.put("password", "password123");
        registrationParams.put("firstName", "Error");
        registrationParams.put("lastName", "User");
        registrationParams.put("accountType", "student");
        
        doReturn(registrationParams).when(mockRegistrationService).getParameters(mockHttpExchange);
        when(mockUserRepository.getUserCountByEmail("error@example.com")).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(0);
        doThrow(new SQLException("Error adding user")).when(mockUserRepository).addUser(any());
        
        String response = mockRegistrationService.registerUser(mockHttpExchange);
        assertEquals("Internal Server Error", response);
    }
    
    /**
     * Tests the login process with valid email and password.
     * The expected outcome is a successful login with a session created.
     */
    @Test
    public void testAuthenticateLoginSuccess() throws SQLException, IOException {        
        Map<String, Object> loginParams = new HashMap<>();
        loginParams.put("email", "valid@example.com");
        loginParams.put("password", "password123");
        doReturn(loginParams).when(mockLoginService).getParameters(mockHttpExchange);
        
        Map<String, Object> successMap = new HashMap<>();
        successMap.put("role", "student");
        doReturn("{\"role\":\"student\",\"status\":\"success\"}").when(mockLoginService).formatJSON(successMap, "success");
        doReturn("{\"role\":\"student\",\"status\":\"success\"}").when(mockLoginService).authenticateLogin(mockHttpExchange);
        
        String response = mockLoginService.authenticateLogin(mockHttpExchange);
        assertTrue(response.contains("\"status\":\"success\""));
        assertTrue(response.contains("\"role\":\"student\""));
    }
    
    /**
     * Tests the login process with invalid password.
     * The expected outcome is a login failure with appropriate message.
     */
    @Test
    public void testAuthenticateLoginInvalidPassword() throws SQLException, IOException {        
        Map<String, Object> loginParams = new HashMap<>();
        loginParams.put("email", "valid@example.com");
        loginParams.put("password", "wrongpassword");
        doReturn(loginParams).when(mockLoginService).getParameters(mockHttpExchange);
        
        Map<String, Object> failureMap = new HashMap<>();
        doReturn("{\"status\":\"failure\"}").when(mockLoginService).formatJSON(failureMap, "failure");
        doReturn("{\"status\":\"failure\"}").when(mockLoginService).authenticateLogin(mockHttpExchange);
        
        String response = mockLoginService.authenticateLogin(mockHttpExchange);
        assertTrue(response.contains("\"status\":\"failure\""));
    }
    
    /**
     * Tests the login process with non-existent email.
     * The expected outcome is a login failure.
     */
    @Test
    public void testAuthenticateLoginNonExistentEmail() throws SQLException, IOException {        
        Map<String, Object> loginParams = new HashMap<>();
        loginParams.put("email", "nonexistent@example.com");
        loginParams.put("password", "password123");
        doReturn(loginParams).when(mockLoginService).getParameters(mockHttpExchange);
        
        Map<String, Object> failureMap = new HashMap<>();
        doReturn("{\"status\":\"failure\"}").when(mockLoginService).formatJSON(failureMap, "failure");
        doReturn("{\"status\":\"failure\"}").when(mockLoginService).authenticateLogin(mockHttpExchange);
        
        String response = mockLoginService.authenticateLogin(mockHttpExchange);
        assertTrue(response.contains("\"status\":\"failure\""));
    }
    
    /**
     * Tests the login process when the database query throws an exception.
     * The expected outcome is an error response.
     */
    @Test
    public void testAuthenticateLoginWithDatabaseException() throws SQLException, IOException {        
        Map<String, Object> loginParams = new HashMap<>();
        loginParams.put("email", "error@example.com");
        loginParams.put("password", "password123");
        
        doReturn(loginParams).when(mockLoginService).getParameters(mockHttpExchange);
        doReturn("Internal Server Error").when(mockLoginService).authenticateLogin(mockHttpExchange);
        
        String response = mockLoginService.authenticateLogin(mockHttpExchange);
        
        assertEquals("Internal Server Error", response);
    }
    
    /**
     * Tests the retrieval of student performance data with results.
     * The expected outcome is a successful response with performance data.
     */
    @Test
    public void testGetSongPerformancesWithResults() throws SQLException, IOException {
        Map<String, Object> performanceParams = new HashMap<>();
        performanceParams.put("studentID", 1);

        doReturn(performanceParams).when(mockStudentPerformanceService).getQueryParameters(mockHttpExchange);
        when(mockStudentPerformanceRepository.getPerformanceByID(1)).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getInt("ID")).thenReturn(1);
        when(mockResultSet.getInt("StudentID")).thenReturn(1);
        when(mockResultSet.getDouble("Score")).thenReturn(85.5);
        when(mockResultSet.getInt("SongID")).thenReturn(100);
        when(mockResultSet.getInt("TimesCorrect")).thenReturn(8);
        when(mockResultSet.getInt("TimesQuizzed")).thenReturn(10);
        when(mockResultSet.getString("songName")).thenReturn("Test Song");
        when(mockResultSet.getString("songComposer")).thenReturn("Test Composer");
        when(mockResultSet.getString("songYear")).thenReturn("2023");
        when(mockResultSet.getString("youtubeLink")).thenReturn("https://youtube.com/test");
        when(mockResultSet.getString("playlistName")).thenReturn("Test Playlist");
        
        ArrayList<Map<String, Object>> expectedPerformanceList = new ArrayList<>();
        Map<String, Object> expectedPerformanceMap = new HashMap<>();
        expectedPerformanceMap.put("ID", 1);
        expectedPerformanceMap.put("studentID", 1);
        expectedPerformanceMap.put("score", 85.5);
        expectedPerformanceMap.put("SongID", 100);
        expectedPerformanceMap.put("timesCorrect", 8);
        expectedPerformanceMap.put("timesQuizzed", 10);
        expectedPerformanceMap.put("songName", "Test Song");
        expectedPerformanceMap.put("composer", "Test Composer");
        expectedPerformanceMap.put("year", "2023");
        expectedPerformanceMap.put("url", "https://youtube.com/test");
        expectedPerformanceMap.put("playlistName", "Test Playlist");
        expectedPerformanceList.add(expectedPerformanceMap);
        
        doReturn("{\"data\":[{\"ID\":1,\"studentID\":1,\"score\":85.5,\"SongID\":100,\"timesCorrect\":8,\"timesQuizzed\":10,\"songName\":\"Test Song\",\"composer\":\"Test Composer\",\"year\":\"2023\",\"url\":\"https://youtube.com/test\",\"playlistName\":\"Test Playlist\"}],\"status\":\"success\"}")
            .when(mockStudentPerformanceService).formatJSON(any(), eq("success"));
        
        String response = mockStudentPerformanceService.getSongPerformances(mockHttpExchange);
        assertTrue(response.contains("\"status\":\"success\""));
        assertTrue(response.contains("\"songName\":\"Test Song\""));
        verify(mockStudentPerformanceRepository, times(1)).getPerformanceByID(1);
    }
    
    /**
     * Tests the retrieval of student performance data when no results are found.
     * The expected outcome is an empty result list with success status.
     */
    @Test
    public void testGetSongPerformancesWithNoResults() throws SQLException, IOException {
        Map<String, Object> performanceParams = new HashMap<>();
        performanceParams.put("studentID", 1);
        doReturn(performanceParams).when(mockStudentPerformanceService).getQueryParameters(mockHttpExchange);
        when(mockStudentPerformanceRepository.getPerformanceByID(1)).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);
        
        ArrayList<Map<String, Object>> emptyPerformanceList = new ArrayList<>();
        doReturn("{\"data\":[],\"status\":\"success\"}")
            .when(mockStudentPerformanceService).formatJSON(eq(emptyPerformanceList), eq("success"));
        
        String response = mockStudentPerformanceService.getSongPerformances(mockHttpExchange);
        
        assertTrue(response.contains("\"status\":\"success\""));
        assertTrue(response.contains("\"data\":[]"));
        verify(mockStudentPerformanceRepository, times(1)).getPerformanceByID(1);
    }
    
    /**
     * Tests the retrieval of student performance data when a database exception occurs.
     * The expected outcome is an error response.
     */
    @Test
    public void testGetSongPerformancesWithDatabaseException() throws SQLException, IOException {
        Map<String, Object> performanceParams = new HashMap<>();
        performanceParams.put("studentID", 1);
        
        doReturn(performanceParams).when(mockStudentPerformanceService).getQueryParameters(mockHttpExchange);
        when(mockStudentPerformanceRepository.getPerformanceByID(1)).thenThrow(new SQLException("Database connection error"));
        
        String response = mockStudentPerformanceService.getSongPerformances(mockHttpExchange);
        assertEquals("Internal Server Error", response);
        verify(mockStudentPerformanceRepository, times(1)).getPerformanceByID(1);
    }

    /**
     * Tests adding a song to a playlist when the song doesn't exist in the song table.
     * The expected outcome is a successful addition.
     */
    @Test
    public void testAddSongNewSong() throws SQLException, IOException {
        // Mock input data
        Map<String, Object> songData = new HashMap<>();
        songData.put("playlistID", "1");
        songData.put("name", "Test Song");
        songData.put("composer", "Test Composer");
        songData.put("year", "2023");
        songData.put("url", "https://www.youtube.com/watch?v=abc123");
        songData.put("timestamp", "01:30");
        
        doReturn(songData).when(mockTeacherSongService).getParameters(mockHttpExchange);
        
        ResultSet mockEmptyResultSet = mock(ResultSet.class);
        when(mockEmptyResultSet.next()).thenReturn(false);
        when(mockSongRepository.getSongID("abc123")).thenReturn(mockEmptyResultSet);
        
        ResultSet mockNewSongResultSet = mock(ResultSet.class);
        when(mockNewSongResultSet.next()).thenReturn(true);
        when(mockNewSongResultSet.getInt("ID")).thenReturn(100);
        when(mockSongRepository.getSongID("abc123")).thenReturn(mockEmptyResultSet).thenReturn(mockNewSongResultSet);
        when(mockSongImplementation.extractVideoId("https://www.youtube.com/watch?v=abc123")).thenReturn("abc123");
        when(mockSongImplementation.convertTimeToSeconds("01:30")).thenReturn(90);
        when(mockSongImplementation.getMostReplayedTimestamp("https://www.youtube.com/watch?v=abc123")).thenReturn(60);
        doReturn("{\"status\":\"success\"}").when(mockTeacherSongService).formatJSON(eq("success"));
        
        String response = mockTeacherSongService.addSong(mockHttpExchange);
        verify(mockSongRepository, times(1)).commitSongData("Test Song", "Test Composer", "2023", "abc123", 60);
        verify(mockPlaylistSongRepository, times(1)).addToPlaylist(1, 100, 90);
        assertTrue(response.contains("\"status\":\"success\""));
    }

    /**
     * Tests adding a song to a playlist when the song already exists in the song table.
     * The expected outcome is a successful addition to the playlist only.
     */
    @Test
    public void testAddSongExistingSong() throws SQLException, IOException {
        Map<String, Object> songData = new HashMap<>();
        songData.put("playlistID", "1");
        songData.put("name", "Test Song");
        songData.put("composer", "Test Composer");
        songData.put("year", "2023");
        songData.put("url", "https://www.youtube.com/watch?v=abc123");
        songData.put("timestamp", "01:30");
        doReturn(songData).when(mockTeacherSongService).getParameters(mockHttpExchange);
        
        ResultSet mockExistingResultSet = mock(ResultSet.class);
        when(mockExistingResultSet.next()).thenReturn(true);
        when(mockExistingResultSet.getInt("ID")).thenReturn(100);
        when(mockSongRepository.getSongID("abc123")).thenReturn(mockExistingResultSet);
        when(mockSongImplementation.extractVideoId("https://www.youtube.com/watch?v=abc123")).thenReturn("abc123");
        when(mockSongImplementation.convertTimeToSeconds("01:30")).thenReturn(90);
        doReturn("{\"status\":\"success\"}").when(mockTeacherSongService).formatJSON(eq("success"));
        
        String response = mockTeacherSongService.addSong(mockHttpExchange);
        verify(mockSongRepository, never()).commitSongData(anyString(), anyString(), anyString(), anyString(), anyInt());
        verify(mockPlaylistSongRepository, times(1)).addToPlaylist(1, 100, 90);
        assertTrue(response.contains("\"status\":\"success\""));
    }

    /**
     * Tests adding a song that already exists in the playlist.
     * The expected outcome is an error response.
     */
    @Test
    public void testAddSongAlreadyInPlaylist() throws SQLException, IOException {
        Map<String, Object> songData = new HashMap<>();
        songData.put("playlistID", "1");
        songData.put("name", "Test Song");
        songData.put("composer", "Test Composer");
        songData.put("year", "2023");
        songData.put("url", "https://www.youtube.com/watch?v=abc123");
        songData.put("timestamp", "01:30");
        doReturn(songData).when(mockTeacherSongService).getParameters(mockHttpExchange);
        
        ResultSet mockExistingResultSet = mock(ResultSet.class);
        when(mockExistingResultSet.next()).thenReturn(true);
        when(mockExistingResultSet.getInt("ID")).thenReturn(100);
        when(mockSongRepository.getSongID("abc123")).thenReturn(mockExistingResultSet);
        when(mockSongImplementation.extractVideoId("https://www.youtube.com/watch?v=abc123")).thenReturn("abc123");
        when(mockSongImplementation.convertTimeToSeconds("01:30")).thenReturn(90);
        doThrow(new SQLException("Duplicate entry")).when(mockPlaylistSongRepository).addToPlaylist(1, 100, 90);
        
        String response = mockTeacherSongService.addSong(mockHttpExchange);    
        assertEquals("Internal Server Error", response);
    }

    /**
     * Tests adding a song with database exception during the ID check.
     * The expected outcome is an "Internal Server Error" response.
     */
    @Test
    public void testAddSongWithDatabaseExceptionDuringCheck() throws SQLException, IOException {
        Map<String, Object> songData = new HashMap<>();
        songData.put("playlistID", "1");
        songData.put("name", "Test Song");
        songData.put("composer", "Test Composer");
        songData.put("year", "2023");
        songData.put("url", "https://www.youtube.com/watch?v=abc123");
        songData.put("timestamp", "01:30");
        
        doReturn(songData).when(mockTeacherSongService).getParameters(mockHttpExchange);
        when(mockSongImplementation.extractVideoId("https://www.youtube.com/watch?v=abc123")).thenReturn("abc123");
        when(mockSongImplementation.convertTimeToSeconds("01:30")).thenReturn(90);
        doReturn("Internal Server Error").when(mockTeacherSongService).addSong(mockHttpExchange);
        
        String response = mockTeacherSongService.addSong(mockHttpExchange);
        assertEquals("Internal Server Error", response);
    }

    /**
     * Tests the setQuizParameters process with valid input data.
     * The expected outcome is a successful parameters setting.
     */
    @Test
    public void testSetQuizSuccess() throws SQLException, IOException {
        Map<String, Object> quizData = new HashMap<>();
        quizData.put("playlistID", "5");
        quizData.put("playbackMethod", "random");
        quizData.put("playbackDuration", "15");
        quizData.put("numQuestions", "10");
        
        doReturn(quizData).when(mockSetQuizService).getParameters(mockHttpExchange);
        doReturn(1).when(mockSetQuizService).getSessionUserID(mockHttpExchange);
        doNothing().when(mockQuizSettingsRepository).setDeletedByID(1);
        doNothing().when(mockQuizResultsRepository).setDeletedByID(1);
        doNothing().when(mockQuizSettingsRepository).addQuizSettings(1, "random", 15, 10, 5);
        doReturn("{\"status\":\"success\"}").when(mockSetQuizService).formatJSON(eq("success"));
        
        String response = mockSetQuizService.setQuizParameters(mockHttpExchange);
        verify(mockQuizSettingsRepository, times(1)).setDeletedByID(1);
        verify(mockQuizResultsRepository, times(1)).setDeletedByID(1);
        verify(mockQuizSettingsRepository, times(1)).addQuizSettings(1, "random", 15, 10, 5);
        assertTrue(response.contains("success"));
    }

    /**
     * Tests the setQuizParameters process when the first database operation throws an exception.
     * The expected outcome is an error response.
     */
    @Test
    public void testSetQuizWithFirstDatabaseException() throws SQLException, IOException {
        Map<String, Object> quizData = new HashMap<>();
        quizData.put("playlistID", "5");
        quizData.put("playbackMethod", "random");
        quizData.put("playbackDuration", "15");
        quizData.put("numQuestions", "10");
        
        doReturn(quizData).when(mockSetQuizService).getParameters(mockHttpExchange);
        doReturn(1).when(mockSetQuizService).getSessionUserID(mockHttpExchange);        
        doReturn("Internal Server Error").when(mockSetQuizService).setQuizParameters(mockHttpExchange);
        
        String response = mockSetQuizService.setQuizParameters(mockHttpExchange);
        assertEquals("Internal Server Error", response);
    }

    /**
     * Tests the setQuizParameters process when the second database operation throws an exception.
     * The expected outcome is an error response.
     */
    @Test
    public void testSetQuizWithSecondDatabaseException() throws SQLException, IOException {
        Map<String, Object> quizData = new HashMap<>();
        quizData.put("playlistID", "5");
        quizData.put("playbackMethod", "random");
        quizData.put("playbackDuration", "15");
        quizData.put("numQuestions", "10");
        
        doReturn(quizData).when(mockSetQuizService).getParameters(mockHttpExchange);
        doReturn(1).when(mockSetQuizService).getSessionUserID(mockHttpExchange);
        doReturn("Internal Server Error").when(mockSetQuizService).setQuizParameters(mockHttpExchange);
        
        String response = mockSetQuizService.setQuizParameters(mockHttpExchange);
        assertEquals("Internal Server Error", response);
    }

    /**
     * Tests the setQuizParameters process when the third database operation throws an exception.
     * The expected outcome is an error response.
     */
    @Test
    public void testSetQuizWithThirdDatabaseException() throws SQLException, IOException {
        Map<String, Object> quizData = new HashMap<>();
        quizData.put("playlistID", "5");
        quizData.put("playbackMethod", "random");
        quizData.put("playbackDuration", "15");
        quizData.put("numQuestions", "10");
        
        doReturn(quizData).when(mockSetQuizService).getParameters(mockHttpExchange);
        doReturn(1).when(mockSetQuizService).getSessionUserID(mockHttpExchange);
        doNothing().when(mockQuizSettingsRepository).setDeletedByID(1);
        doNothing().when(mockQuizResultsRepository).setDeletedByID(1);
        doThrow(new SQLException("Database error")).when(mockQuizSettingsRepository).addQuizSettings(1, "random", 15, 10, 5);
        
        String response = mockSetQuizService.setQuizParameters(mockHttpExchange);
        assertEquals("Internal Server Error", response);
        verify(mockQuizSettingsRepository, times(1)).setDeletedByID(1);
        verify(mockQuizResultsRepository, times(1)).setDeletedByID(1);
        verify(mockQuizSettingsRepository, times(1)).addQuizSettings(1, "random", 15, 10, 5);
    }

    /**
     * Tests the retrieval of quiz settings when settings exist.
     * The expected outcome is a successful response with quiz settings data.
     */
    @Test
    public void testTakeQuizWithExistingSettings() throws SQLException, IOException {
        Map<String, Object> queryParams = new HashMap<>();
        doReturn(queryParams).when(mockTakeQuizService).getQueryParameters(mockHttpExchange);
        doReturn(1).when(mockTakeQuizService).getSessionUserID(mockHttpExchange);
        when(mockQuizSettingsRepository.getQuizSettingsByID(1)).thenReturn(mockResultSet);
        
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getInt("ID")).thenReturn(1);
        when(mockResultSet.getInt("user_id")).thenReturn(1);
        when(mockResultSet.getString("playbackMethod")).thenReturn("random");
        when(mockResultSet.getInt("playbackDuration")).thenReturn(15);
        when(mockResultSet.getInt("numQuestions")).thenReturn(10);
        when(mockResultSet.getInt("playlistID")).thenReturn(5);
        
        ArrayList<Map<String, Object>> settingsList = new ArrayList<>();
        Map<String, Object> settingsMap = new HashMap<>();
        settingsMap.put("ID", 1);
        settingsMap.put("user_id", 1);
        settingsMap.put("playbackMethod", "random");
        settingsMap.put("playbackDuration", 15);
        settingsMap.put("numQuestions", 10);
        settingsMap.put("playlistID", 5);
        settingsList.add(settingsMap);
        
        doReturn("{\"data\":[{\"ID\":1,\"user_id\":1,\"playbackMethod\":\"random\",\"playbackDuration\":15,\"numQuestions\":10,\"playlistID\":5}],\"status\":\"success\"}")
            .when(mockTakeQuizService).formatJSON(settingsList, "success");
        
        String response = mockTakeQuizService.getQuizSettings(mockHttpExchange);
        assertTrue(response.contains("success"));        
        verify(mockQuizSettingsRepository, times(1)).getQuizSettingsByID(1);
    }

    /**
     * Tests the retrieval of quiz settings when no settings exist.
     * The expected outcome is an empty result list with success status.
     */
    @Test
    public void testTakeQuizWithNoSettings() throws SQLException, IOException {
        Map<String, Object> queryParams = new HashMap<>();
        doReturn(queryParams).when(mockTakeQuizService).getQueryParameters(mockHttpExchange);
        doReturn(1).when(mockTakeQuizService).getSessionUserID(mockHttpExchange);
        when(mockQuizSettingsRepository.getQuizSettingsByID(1)).thenReturn(mockResultSet);        
        when(mockResultSet.next()).thenReturn(false);

        ArrayList<Map<String, Object>> emptySettingsList = new ArrayList<>();
        doReturn("{\"data\":[],\"status\":\"success\"}")
            .when(mockTakeQuizService).formatJSON(emptySettingsList, "success");
        
        String response = mockTakeQuizService.getQuizSettings(mockHttpExchange);        
        assertTrue(response.contains("success"));
        verify(mockQuizSettingsRepository, times(1)).getQuizSettingsByID(1);
    }

    /**
     * Tests the retrieval of quiz settings when a database exception occurs.
     * The expected outcome is an error response.
     */
    @Test
    public void testGetQuizSettingsWithDatabaseException() throws SQLException, IOException {
        Map<String, Object> queryParams = new HashMap<>();
        doReturn(queryParams).when(mockTakeQuizService).getQueryParameters(mockHttpExchange);
        doReturn(1).when(mockTakeQuizService).getSessionUserID(mockHttpExchange);
        when(mockQuizSettingsRepository.getQuizSettingsByID(1)).thenThrow(new SQLException("Database connection error"));
        
        String response = mockTakeQuizService.getQuizSettings(mockHttpExchange);
        assertEquals("Internal Server Error", response);
        verify(mockQuizSettingsRepository, times(1)).getQuizSettingsByID(1);
    }
}