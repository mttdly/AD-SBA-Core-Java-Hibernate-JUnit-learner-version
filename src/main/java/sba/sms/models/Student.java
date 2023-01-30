package sba.sms.models;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
@ToString



@Entity
@Table(name = "student")
public class Student {
    @NonNull // need this NonNull or else HibernateUtil will throw errors
    @Id // Makes it PK
    @Column(length = 50, name = "email")
    String email;

    @NonNull
    @Column(length = 50, name = "name")
    String name;

    @NonNull
    @Column(length = 50, name = "password")
    String password;

    // Connect 'id' field from 'course' table to 'email' field in 'student' under new table 'student_courses'
    @JoinTable(name = "student_courses",
            joinColumns = @JoinColumn(name = "student_email"),
            inverseJoinColumns = @JoinColumn(name = "courses_id"))


    // A student can have many courses, and a course can have many students
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, fetch = FetchType.EAGER)
    @ToString.Exclude // prevent inf loop output of collections
    List<Course> courses;


    // Helper method
    public void addCourse(Course course){
        courses.add(course);
        course.getStudents().add(this);
    }


    @Override
    public int hashCode() {
        return Objects.hash(email, name, password);
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Student other = (Student) obj;
        return Objects.equals(email, other.email) && Objects.equals(name, other.name)
                && Objects.equals(password, other.password);
    }
}
