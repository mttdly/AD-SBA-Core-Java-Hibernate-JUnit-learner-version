package sba.sms.services;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import sba.sms.dao.CourseI;
import sba.sms.models.Course;
import sba.sms.utils.HibernateUtil;

public class CourseService implements CourseI {

    @Override
    public void createCourse(Course course) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.persist(course);
            tx.commit(); // Commit Handle
        } catch (HibernateException e) { // Base type for Hibernate exceptions
            if (tx != null) tx.rollback(); // Rollback Handle
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    @Override
    public List<Course> getAllCourses() {
        return null;
    }

    @Override
    public Course getCourseById(int courseId) {
        return null;
    }
}
