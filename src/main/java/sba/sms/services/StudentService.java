package sba.sms.services;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import sba.sms.dao.StudentI;
import sba.sms.models.Course;
import sba.sms.models.Student;
import sba.sms.utils.HibernateUtil;

public class StudentService implements StudentI {
    
    @Override
    public void createStudent(Student student) {
        Transaction tx = null;
        // try with resource to auto-close session
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(student);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public Student getStudentByEmail(String email) {
        // Create instance of Student
        Student student = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            student = session.get(Student.class, email);
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return student;
    }

    @Override
    public List<Student> getAllStudents() {
        // Handling commit / rollback doesn't make sense for this function, so added with comments
        // Transaction tx = null;
        List<Student> allStudentsList= new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // could change to just createQuery
            Query<Student> query = session.createNamedQuery("Student.getAllStudents", Student.class);
            allStudentsList = query.getResultList();
            // tx.commit();
        } catch (HibernateException e) {
            // if(tx != null) tx.rollback();
            e.printStackTrace();
        }
        return allStudentsList;
    }

    @Override
    public void registerStudentToCourse(String email, int courseId) {
        Transaction tx = null;

        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            // Slight workaround
            // Student student = getStudentByEmail(email);
            
            Student studentToRegister = session.get(Student.class, email);
            Course newCourse = session.get(Course.class, courseId);
            
            // Nullcheck
            if (newCourse != null) {
                // Check if courseId param matches 
                if (studentToRegister.getCourses().contains(newCourse)) {
                    throw new RuntimeException("Already registered to this course");
                }
            }
            // Use helper method of Student model
            studentToRegister.addCourse(newCourse);

            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }
    @Override
    public List<Course> getStudentCourses(String email) {
        Transaction tx = null;

        // Create student object from model
        Student student = null;

        // Store getCourses results in list to return
        List<Course> studentCoursesList = new ArrayList<>();

        // Query pulls the student which matches the email in params
        final String qfindStudentCourses ="SELECT * FROM Student s WHERE email = :email";

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Query<Student> query = session.createNativeQuery(qfindStudentCourses, Student.class).setParameter("email", email);

            // Adds found user to the student object
            student = query.getSingleResult();

            // Helper method to get courses
            studentCoursesList.addAll(student.getCourses());
            tx.commit();
        } catch (HibernateException | NullPointerException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
        return studentCoursesList;
    }

    @Override
    public boolean validateStudent(String email, String password) {

        Transaction tx = null;

        boolean validationCheck = false;
        boolean inSystem = false;
        boolean pwMatch = false;
        //boolean validEmail = false;

        email.trim();
        password.trim();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            if(!email.isEmpty() | email != null) {

                // Check if student is in system
                // inSystem = !getStudentByEmail(email).equals(null);

                // pwMatch = getStudentByEmail(email).getPassword().equals(password);

                // Decouple with session below
                Student student = session.get(Student.class, email);

                inSystem = !student.getEmail().equals(null);

                pwMatch = student.getPassword().equals(password);

                // if(email.substring(1, email.lastIndexOf(email) - 1).contains("@")) {
                //     validEmail = true;
                // } else {
                //     throw new StringIndexOutOfBoundsException(email);
                // }
            }

            if (inSystem && pwMatch /*&& validEmail*/) {
                validationCheck = true;
            }

            tx.commit();     
        } catch (HibernateException | NullPointerException e) {
            if(tx != null) tx.rollback();
            e.printStackTrace();
        }
        return validationCheck;
    } 
}
