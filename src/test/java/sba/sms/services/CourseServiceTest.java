package sba.sms.services;

import org.hibernate.Session;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import sba.sms.models.Course;
import sba.sms.utils.CommandLine;
import sba.sms.utils.HibernateUtil;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CourseServiceTest {

    static CourseService courseService;


    @BeforeAll
    static void beforeAll() {
        courseService = new CourseService();
        CommandLine.addData();
    }


    @Test
    public void testCreateCourse() {
        Course course = new Course();
        // set course properties, e.g.
        course.setName("Painting 101");
        course.setId(0);
        course.setInstructor("Bob Ross");

        
        courseService.createCourse(course);

        // retrieve the course from database and verify its properties
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            Course savedCourse = session.get(Course.class, course.getId());
            assertNotNull(savedCourse);
            assertEquals("Painting 101", savedCourse.getName());
            assertEquals("Bob Ross", savedCourse.getInstructor());
        }
    }
}
