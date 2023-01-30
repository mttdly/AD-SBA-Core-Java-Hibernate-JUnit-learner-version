package sba.sms.services;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
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
        Student student = new Student();
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            student = session.get(Student.class, email);
            tx.commit();
        } catch (HibernateException e) {
            if(tx != null) tx.rollback();
            e.printStackTrace();
        }
        return student;
    }

    @Override
    public List<Student> getAllStudents() {
        Transaction tx = null;
        List<Student> allStudentsList = new ArrayList<>();

        //final String qGetAllStudents = ;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Query<Student> query = session.createQuery("FROM Student", Student.class);
            allStudentsList.addAll(query.getResultList());
            tx.commit();
        } catch (HibernateException e) {
            if(tx != null) tx.rollback();
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
                    //throw new RuntimeException("Already registered to this course");
                    System.out.println("Already registered");
                } else {
                    studentToRegister.addCourse(newCourse);
                }
            }
            
            // Use helper method of Student model
            
            session.merge(studentToRegister);
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
            NativeQuery<Student> query = session.createNativeQuery(qfindStudentCourses, Student.class).setParameter("email", email);

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
        boolean pwMatch = false;



        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            if(!email.isEmpty() | email != null) {

                // pwMatch = getStudentByEmail(email).getPassword().equals(password);

                // Decouple with session below
                Student student = session.get(Student.class, email);

 

                pwMatch = student.getPassword().equals(password);
                // if(email.substring(1, email.lastIndexOf(email) - 1).contains("@")) {
                //     validEmail = true;
                // } else {
                //     throw new StringIndexOutOfBoundsException(email);
                // }
                if (pwMatch) {
                    validationCheck = true;
                }
            }

            tx.commit();     
        } catch (HibernateException | NullPointerException e) {
            //if(tx != null) tx.rollback();
            e.printStackTrace();
        }
        return validationCheck;
    } 
}
