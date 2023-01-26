package sba.sms.services;

import java.util.List;

import sba.sms.models.Course;
import sba.sms.models.Student;

public class StudentService  {
    
    public void createStudent(Student student) {
    }

    public List<Student> getAllStudents() {
       return null;
    }

    public Student getStudentByEmail(String email) {
        return null;
    }

    public boolean validateStudent(String email, String password) {
        return false;
    }

    public void registerStudentToCourse(String email, int courseId) {
    }

    public List<Course> getStudentCourses(String email) {
        return null;
    }
}
