package sba.sms.services;

import java.util.ArrayList;
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
        Transaction tx = null;

        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(course);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public List<Course> getAllCourses() {
        List<Course> allCoursesList = new ArrayList<>();
        Transaction tx = null;

        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            Query<Course> query = session.createQuery("FROM Course", Course.class);
            allCoursesList = query.getResultList();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
        return allCoursesList;
    }

    @Override
    public Course getCourseById(int courseId) {
        Transaction tx = null;
        Course courseById = null;
        
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            // HQL query to return if exists
            Query<Course> query = session.createQuery("FROM Course c WHERE c.id = :courseId", Course.class) 
                .setParameter("courseId", courseId);
            System.out.println(query);

            if (query.getSingleResult() != null) {
                courseById = query.getSingleResult();
            } else {
                throw new HibernateException("Course does not exist");
            }
            tx.commit();
        } catch (HibernateException e) {  
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
        return courseById;
    }
}
