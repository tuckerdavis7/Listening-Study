package com.example.services;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.repositories.ClassRepository;
import com.example.repositories.StudentClassRepository;
import com.example.repositories.StudentRepository;
import com.example.repositories.TeacherRepository;
import com.example.repositories.UserRepository;
import com.sun.net.httpserver.HttpExchange;

public class ModeratorService extends BaseService {
    private static final Logger logger = LoggerFactory.getLogger(ModeratorService.class);
    ClassRepository classRepository = new ClassRepository();
    UserRepository userRepository = new UserRepository();
    StudentRepository studentRepository = new StudentRepository();
    StudentClassRepository studentClassRepository = new StudentClassRepository();
    TeacherRepository teacherRepository = new TeacherRepository();

     /**
     * Gathers the classes from the DB
     *
     * @param exchange The data from the API request
     * @throws IOException If data processing fails
     * @return String JSON formatted string of data for frontend
     */
    public String getAllClasses(HttpExchange exchange) throws IOException {
        //logger.info("at getAllClasses in ModeratorService");
        String responseString = "";
        try {
            ResultSet result = classRepository.getAllClasses();
            ArrayList<Map<String, Object>> classList = new ArrayList<>();
            
            while (result.next()) {
                Map<String, Object> classMap = new HashMap<>();
                classMap.put("classID", result.getInt("class_id"));
                classMap.put("className", result.getString("classname"));
                classMap.put("teacherID", result.getInt("teacher_id"));
                classMap.put("studentCount", result.getInt("students_count"));
                classMap.put("playlistCount", result.getInt("playlist_count"));
                
                classList.add(classMap);
            }
            responseString = super.formatJSON(classList, "success");
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in getAllClasses of ModeratorService:");
            e.printStackTrace();
        }
        return responseString;
    }

     /**
     * Updates the designation of a teacher or moderator
     *
     * @param exchange The data from the API request
     * @throws IOException If data processing fails
     * @return String JSON formatted string of success or error message
     */
    public String updateDesignation(HttpExchange exchange) throws IOException {
        String responseString = "";
        Map<String, Object> parameters = super.getParameters(exchange);

        try {
            userRepository.updateModeratorOrTeacherDesignation(parameters);
            responseString = super.formatJSON("success");
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in updateDesignation of AdministratorService:");
            e.printStackTrace();
        }

        return responseString;
    }

    //ModeratorService
    public String addClass(HttpExchange exchange) throws IOException {
        logger.info("at addClass in ModeratorService");
        Map<String, Object> addClassData = super.getParameters(exchange);

        String className = (String)addClassData.get("className");
        String teacherEmail = (String)addClassData.get("findTeacher");
        int teacherID = -1;
        
        String responseString = "";

        try {
            ResultSet result = teacherRepository.getTeacherByEmail(teacherEmail);

            if(result.next()) {
                teacherID = result.getInt("ID");
            }
            else {
                logger.error("Teacher not found for email: " + teacherEmail);
                responseString = "Teacher not found!";
                return responseString;
            }
        }
        catch (Exception e){
            responseString = "Internal Server Error";
            logger.error("Error in addClass1 of ModeratorService");
            e.printStackTrace();
            return responseString;
        }

        try {
            classRepository.addClass(className, teacherID);
            responseString = super.formatJSON("success");
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in addClass2 of ModeratorService:");
            e.printStackTrace();
        }
        return responseString;
    }

    //classlist
    public String getClassStudents(HttpExchange exchange) throws IOException {
        logger.info("at getClassStudents in ModeratorRosterService");
        Map<String, Object> classData = super.getQueryParameters(exchange);
        int classID = ((Number)classData.get("classID")).intValue();
        String responseString = "";
        try {
            ResultSet result = classRepository.getStudentsByClassID(classID);
            ArrayList<Map<String, Object>> classStudents = new ArrayList<>();
            
            while (result.next()) {
                Map<String, Object> classStudentsMap = new HashMap<>();
                classStudentsMap.put("studentID", result.getInt("studentID"));
                classStudentsMap.put("firstName", result.getString("studentFirstname"));
                classStudentsMap.put("lastName", result.getString("studentLastname"));
                classStudentsMap.put("email", result.getString("studentEmail"));
                
                classStudents.add(classStudentsMap);
            }
            responseString = super.formatJSON(classStudents, "success");
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in getAllClasses of ModeratorRosterService:");
            e.printStackTrace();
        }
        return responseString;
    }

    //classlist
    public String addStudent(HttpExchange exchange) throws IOException {
        logger.info("at addStudent in ModeratorService");
        Map<String, Object> parameters = super.getParameters(exchange);
        String studentEmail = (String)parameters.get("email");
        System.out.println("studentEmail: " + studentEmail);
        String tempID = ((String)parameters.get("classID"));
        int classID = Integer.parseInt(tempID);
        System.out.println("classsID: " + classID);
        int studentID = -1;
        String responseString = "";

        try {
            ResultSet rs = studentRepository.getStudentByEmail(studentEmail);
            if (rs.next()) {
                studentID = rs.getInt("ID");
                System.out.println("StudentID: " + studentID);
            }
            responseString = super.formatJSON("success");
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in addStudent1 of ModeratorService:");
            e.printStackTrace();
        }

        try {
            studentClassRepository.addStudentToClass(studentID, classID);
            responseString = super.formatJSON("success");
        } 
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in addStudent2 of ModeratorService:");
            e.printStackTrace();
        }

        return responseString;
    }

    //teacherlist
    public String getClassTeachers(HttpExchange exchange) throws IOException {
        logger.info("at getClassTeachers in ModeratorTeacherlistService");
        Map<String, Object> classData = super.getQueryParameters(exchange);
        int classID = ((Number)classData.get("classID")).intValue();
        String responseString = "";
        try {
            ResultSet result = classRepository.getTeachersByClassID(classID);
            ArrayList<Map<String, Object>> classTeachers = new ArrayList<>();
            
            while (result.next()) {
                Map<String, Object> classTeachersMap = new HashMap<>();
                classTeachersMap.put("teacherID", result.getInt("teacherID"));
                classTeachersMap.put("firstName", result.getString("FirstName"));
                classTeachersMap.put("lastName", result.getString("LastName"));
                classTeachersMap.put("email", result.getString("Email"));
                
                classTeachers.add(classTeachersMap);
            }
            responseString = super.formatJSON(classTeachers, "success");
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in getAllClasses of ModeratorService:");
            e.printStackTrace();
        }
        return responseString;
    }

    //teacherlist
    public String removeTeacher(HttpExchange exchange) throws IOException {
        logger.info("at removeTeacher in ModeratorService");
        Map<String, Object> parameters = super.getParameters(exchange);
        String tempID = (String)parameters.get("classID");
        int classID = Integer.parseInt(tempID);

        String responseString = "";

        try {
            classRepository.removeTeacherFromClass(classID);
            responseString = super.formatJSON("success");
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in removeTeacher of ModeratorService:");
            e.printStackTrace();
        }

        return responseString;
    }

    //teacherlist
    public String addTeacher(HttpExchange exchange) throws IOException {
        logger.info("at addTeacher in ModeratorService");
        Map<String, Object> parameters = super.getParameters(exchange);
        String teacherEmail = (String)parameters.get("email");
        String tempID = ((String)parameters.get("classID"));
        int classID = Integer.parseInt(tempID);
        String responseString = "";

        try {
            classRepository.addTeacherToClass(classID, teacherEmail);
            responseString = super.formatJSON("success");
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in addTeacher of ModeratorService:");
            e.printStackTrace();
        }

        return responseString;
    }

    //roster
    public String removeStudent(HttpExchange exchange) throws IOException {
        logger.info("at removeStudent in ModeratorService");
        Map<String, Object> parameters = super.getParameters(exchange);
        int studentID = ((Number)parameters.get("studentID")).intValue();
        String tempID = (String)parameters.get("classID");
        int classID = Integer.parseInt(tempID);

        String responseString = "";

        try {
            logger.info("at try of removeStudent in ModeratorService");
            studentClassRepository.removeStudentFromClass(studentID, classID);
            responseString = super.formatJSON("success");
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in removeTeacher of ModeratorService:");
            e.printStackTrace();
        }

        return responseString;
    }

    public String deleteClass(HttpExchange exchange) throws IOException {
        logger.info("at deleteClass in ModeratorService");
        Map<String, Object> parameters = super.getParameters(exchange);
        int classID = ((Number)parameters.get("classID")).intValue();
        String responseString = "";

        try {
            studentClassRepository.removeAllStudentsFromClass(classID);
            responseString = super.formatJSON("success");
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in deleteClass1 of ModeratorService:");
            e.printStackTrace();
        }
        try {
            classRepository.deleteClass(classID);
            responseString = super.formatJSON("success");
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in deleteClass2 of ModeratorService:");
            e.printStackTrace();
        }

        return responseString;
    }
      
}
