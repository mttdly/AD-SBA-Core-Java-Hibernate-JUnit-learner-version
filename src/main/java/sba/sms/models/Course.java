package sba.sms.models;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
@ToString

@Table(name = "course")
@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //PK
    @Column(name = "id")
    int id;

    @NonNull
    @Column(name = "name", length = 50) 
    String name;

    @NonNull
    @Column(name = "instructor", length = 50)
    String instructor;

    @ToString.Exclude // Exclude collections to avoid infinite loop
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, mappedBy = "courses")
    List<Student> students;

    @Override
    public int hashCode() {
        return Objects.hash(id, instructor, name, students);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Course other = (Course) obj;
        return id == other.id && Objects.equals(instructor, other.instructor) && Objects.equals(name, other.name)
                && Objects.equals(students, other.students);
    }
}
