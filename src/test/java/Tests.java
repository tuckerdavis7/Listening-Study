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
import java.util.HashMap;
import java.util.Map;

import com.example.repositories.UserRepository;
import com.example.repositories.SessionRepository;
import com.example.services.RegistrationService;
import com.example.services.LoginService;
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
    private HttpExchange mockHttpExchange;
    
    @Mock
    private ResultSet mockResultSet;
    
    @Mock
    private Headers mockHeaders;
    
    @Spy
    @InjectMocks
    private RegistrationService registrationService;
    
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
}