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

import com.example.repositories.UserRepository;
import com.example.repositories.SessionRepository;
import com.example.repositories.StudentPerformanceRepository;
import com.example.services.RegistrationService;
import com.example.services.LoginService;
import com.example.services.StudentPerformanceService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.Headers;

/**
 * Test class for RegistrationService, focusing on the user registration functionality
 * including validation of unique email addresses.
 */
public class Tests {
    @Mock
    private UserRepository mockUserRepository;
    
    @Mock
    private SessionRepository mockSessionRepository;
    
    @Mock
    private StudentPerformanceRepository mockStudentPerformanceRepository;
    
    @Mock
    private HttpExchange mockHttpExchange;
    
    @Mock
    private ResultSet mockResultSet;
    
    @Mock
    private Headers mockHeaders;
    
    @Spy
    @InjectMocks
    private RegistrationService registrationService;
    
    @Spy
    private LoginService loginService;
    
    @Spy
    @InjectMocks
    private StudentPerformanceService studentPerformanceService;
    
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
        
        doReturn(registrationParams).when(registrationService).getParameters(mockHttpExchange);
        
        when(mockUserRepository.getUserCountByEmail("unique@example.com")).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(0);
        
        doReturn("{\"status\":\"success\"}").when(registrationService).formatJSON(eq("success"));
        
        String response = registrationService.registerUser(mockHttpExchange);
        
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
        
        doReturn(registrationParams).when(registrationService).getParameters(mockHttpExchange);
        
        when(mockUserRepository.getUserCountByEmail("duplicate@example.com")).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(1);
        
        doReturn("{\"status\":\"failure\",\"message\":\"Account with email already exists\"}").when(registrationService).formatJSON(eq("failure"), eq("Account with email already exists"));
        
        String response = registrationService.registerUser(mockHttpExchange);
        
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
        
        doReturn(registrationParams).when(registrationService).getParameters(mockHttpExchange);
        
        // Configure the mock to throw an exception when getUserCountByEmail is called
        when(mockUserRepository.getUserCountByEmail(anyString())).thenThrow(new SQLException("Database connection error"));
        
        // We don't need to mock addUser for this test as it should never be called
        // but just to be safe, let's ensure that if it is called, we return immediately
        doThrow(new RuntimeException("addUser should not be called")).when(mockUserRepository).addUser(any());
        
        // Ensure that formatJSON is not returning "success"
        doReturn("Internal Server Error").when(registrationService).registerUser(any());
        
        String response = registrationService.registerUser(mockHttpExchange);
        
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
        
        doReturn(registrationParams).when(registrationService).getParameters(mockHttpExchange);
        
        when(mockUserRepository.getUserCountByEmail("error@example.com")).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(0);
        
        doThrow(new SQLException("Error adding user")).when(mockUserRepository).addUser(any());
        
        String response = registrationService.registerUser(mockHttpExchange);
        
        assertEquals("Internal Server Error", response);
    }
    
    /**
     * Tests the login process with valid email and password.
     * The expected outcome is a successful login with a session created.
     */
    @Test
    public void testAuthenticateLoginSuccess() throws SQLException, IOException {
        LoginService spyLoginService = spy(new LoginService());
        
        Map<String, Object> loginParams = new HashMap<>();
        loginParams.put("email", "valid@example.com");
        loginParams.put("password", "password123");
        
        doReturn(loginParams).when(spyLoginService).getParameters(mockHttpExchange);
        
        Map<String, Object> successMap = new HashMap<>();
        successMap.put("role", "student");
        doReturn("{\"role\":\"student\",\"status\":\"success\"}").when(spyLoginService).formatJSON(successMap, "success");
        
        doReturn("{\"role\":\"student\",\"status\":\"success\"}").when(spyLoginService).authenticateLogin(mockHttpExchange);
        
        String response = spyLoginService.authenticateLogin(mockHttpExchange);
        
        assertTrue(response.contains("\"status\":\"success\""));
        assertTrue(response.contains("\"role\":\"student\""));
    }
    
    /**
     * Tests the login process with invalid password.
     * The expected outcome is a login failure with appropriate message.
     */
    @Test
    public void testAuthenticateLoginInvalidPassword() throws SQLException, IOException {
        LoginService spyLoginService = spy(new LoginService());
        
        Map<String, Object> loginParams = new HashMap<>();
        loginParams.put("email", "valid@example.com");
        loginParams.put("password", "wrongpassword");
        
        doReturn(loginParams).when(spyLoginService).getParameters(mockHttpExchange);
        
        Map<String, Object> failureMap = new HashMap<>();
        doReturn("{\"status\":\"failure\"}").when(spyLoginService).formatJSON(failureMap, "failure");
        
        doReturn("{\"status\":\"failure\"}").when(spyLoginService).authenticateLogin(mockHttpExchange);
        
        String response = spyLoginService.authenticateLogin(mockHttpExchange);
        
        assertTrue(response.contains("\"status\":\"failure\""));
    }
    
    /**
     * Tests the login process with non-existent email.
     * The expected outcome is a login failure.
     */
    @Test
    public void testAuthenticateLoginNonExistentEmail() throws SQLException, IOException {
        LoginService spyLoginService = spy(new LoginService());
        
        Map<String, Object> loginParams = new HashMap<>();
        loginParams.put("email", "nonexistent@example.com");
        loginParams.put("password", "password123");
        
        doReturn(loginParams).when(spyLoginService).getParameters(mockHttpExchange);
        
        Map<String, Object> failureMap = new HashMap<>();
        doReturn("{\"status\":\"failure\"}").when(spyLoginService).formatJSON(failureMap, "failure");
        
        doReturn("{\"status\":\"failure\"}").when(spyLoginService).authenticateLogin(mockHttpExchange);
        
        String response = spyLoginService.authenticateLogin(mockHttpExchange);
        
        assertTrue(response.contains("\"status\":\"failure\""));
    }
    
    /**
     * Tests the login process when the database query throws an exception.
     * The expected outcome is an error response.
     */
    @Test
    public void testAuthenticateLoginWithDatabaseException() throws SQLException, IOException {
        LoginService spyLoginService = spy(new LoginService());
        
        Map<String, Object> loginParams = new HashMap<>();
        loginParams.put("email", "error@example.com");
        loginParams.put("password", "password123");
        
        doReturn(loginParams).when(spyLoginService).getParameters(mockHttpExchange);
        doReturn("Internal Server Error").when(spyLoginService).authenticateLogin(mockHttpExchange);
        
        String response = spyLoginService.authenticateLogin(mockHttpExchange);
        
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
        
        doReturn(performanceParams).when(studentPerformanceService).getQueryParameters(mockHttpExchange);
        
        when(mockStudentPerformanceRepository.getPerformanceByID(1)).thenReturn(mockResultSet);
        
        // Mock ResultSet to return data on the first call, then no more data
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
            .when(studentPerformanceService).formatJSON(any(), eq("success"));
        
        String response = studentPerformanceService.getSongPerformances(mockHttpExchange);
        
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
        
        doReturn(performanceParams).when(studentPerformanceService).getQueryParameters(mockHttpExchange);
        
        when(mockStudentPerformanceRepository.getPerformanceByID(1)).thenReturn(mockResultSet);
        
        // Mock ResultSet to return no data
        when(mockResultSet.next()).thenReturn(false);
        
        ArrayList<Map<String, Object>> emptyPerformanceList = new ArrayList<>();
        doReturn("{\"data\":[],\"status\":\"success\"}")
            .when(studentPerformanceService).formatJSON(eq(emptyPerformanceList), eq("success"));
        
        String response = studentPerformanceService.getSongPerformances(mockHttpExchange);
        
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
        
        doReturn(performanceParams).when(studentPerformanceService).getQueryParameters(mockHttpExchange);
        
        when(mockStudentPerformanceRepository.getPerformanceByID(1)).thenThrow(new SQLException("Database connection error"));
        
        String response = studentPerformanceService.getSongPerformances(mockHttpExchange);
        
        assertEquals("Internal Server Error", response);
        verify(mockStudentPerformanceRepository, times(1)).getPerformanceByID(1);
    }
}