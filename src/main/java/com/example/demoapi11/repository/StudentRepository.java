package com.example.demoapi11.repository;
import com.example.demoapi11.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query("SELECT s FROM Student s WHERE s.email = ?1")
    Optional<Student> findStudentByEmail(String email);

    @Query("SELECT s FROM Student s WHERE s.name like %?1%")
    List<Student> findStudentByName(String name);

    @Query("select s from Student s " +
            "where day(s.dob) = ?1 " +
            "and month(s.dob) = ?2 ")
    List<Student> findStudentsByBirthday(int day, int month);

    @Query("SELECT s FROM Student s WHERE s.id = ?1")
    Optional<Student> findStudentById(Long id);

}
