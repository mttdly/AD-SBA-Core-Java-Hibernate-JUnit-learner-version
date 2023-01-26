package sba.sms.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
@ToString

@Table(name = "student")
@Entity
public class Student {
    @Id // Makes it PK
    @Column(length = 50, name = "email")
    String email;

    @Column(length = 50, nullable = false, name = "name")
    String name;

    @Column(length = 50, nullable = false, name = "password")
    String password;

    @ToString.Exclude

    // Student Database Attributes
    @JoinTable(name = "student_courses",
            pkColumn = @JoinColum(name = "student_email"),
            inversePK = @JoinColumn(name = "courses_id"))

    // A student can have many courses, and a course can have many students
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, fetch = FetchType.EAGER)




}
