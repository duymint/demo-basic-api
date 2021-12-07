package com.example.demoapi11.mapper;

import com.example.demoapi11.student.Student;
import com.example.demoapi11.student.StudentDTO;

public class StudentMapper {
    private static StudentMapper INSTANCE;

    public static  StudentMapper getInstance(){
        if (INSTANCE == null){
            INSTANCE = new StudentMapper();
        }
        return INSTANCE;
    }

    public Student toEntity(StudentDTO dto){
        Student student = new Student();
        student.setId(dto.getId());
        student.setName(dto.getName());
        student.setEmail(dto.getEmail());
        student.setDob(dto.getDob());
        student.setAge(dto.getAge());
        return student;
    }

    public StudentDTO toDTO(Student student){
        StudentDTO dto = new StudentDTO();
        dto.setAge(student.getAge());
        dto.setDob(student.getDob());
        dto.setEmail(student.getEmail());
        dto.setId(student.getId());
        dto.setName(student.getName());

        return dto;
    }

}
