package com.example.demoapi11.service;

import com.example.demoapi11.mapper.StudentMapper;
import com.example.demoapi11.student.Student;
import com.example.demoapi11.repository.StudentRepository;
import com.example.demoapi11.student.StudentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private StudentRepository studentRepository;
    @Autowired
    public StudentService(StudentRepository studentRepository){
        this.studentRepository = studentRepository;
    }

    public List<StudentDTO> getStudents(String name) {
        if(name != null && name.length() > 0){
            return studentRepository.findStudentByName(name)
                                                .stream()
                                                .map(student -> StudentMapper.getInstance().toDTO(student))
                                                .collect(Collectors.toList());
        }
        return studentRepository.findAll()
                                .stream()
                                .map(student -> StudentMapper.getInstance().toDTO(student))
                                .collect(Collectors.toList());
    }

    public void addNewStudent(Student student){
        Optional<Student> studentOptional = studentRepository.findStudentByEmail(student.getEmail());
        if(studentOptional.isPresent()){
            throw new IllegalStateException("Email taken");
        }
        studentRepository.save(student);
    }

    public void deleteStudent(Long studentId){
        boolean isExisted = studentRepository.existsById(studentId);
        if(!isExisted){
            throw  new IllegalStateException("Student with id " + studentId + " does not exist");
        }
        studentRepository.deleteById(studentId);
    }

    //Update name or email
    @Transactional
    public void updateStudent(Long studentId, String name, String email) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new IllegalStateException(
                "Student with id " + studentId + " does not existed"
        ));

        if(name != null && name.length() > 0 && !Objects.equals(student.getName(),name)){
            student.setName(name);
        }

        if(email != null && email.length() > 0 && !Objects.equals(student.getEmail(),email)){
           Optional<Student> studentOptional = studentRepository.findStudentByEmail(email);
           if(studentOptional.isPresent()){
               throw  new IllegalStateException("email taken");
           }

           student.setEmail(email);
        }
    }

    public List<StudentDTO> getStudentsWithDOB() {

        List<StudentDTO> dtos = studentRepository.findAll().stream().map(student -> StudentMapper.getInstance().toDTO(student)).collect(Collectors.toList());
        List<StudentDTO> result = new ArrayList<>();
        for (StudentDTO dto: dtos
             ) {
            StudentDTO tmp = new StudentDTO();
            tmp.setId(dto.getId());
            tmp.setDob(dto.getDob());
            tmp.setEmail(dto.getEmail());
            result.add(tmp);
        }
        return result;
    }

    public List<StudentDTO> getStudentsWithoutName() {
        List<StudentDTO> dtos = studentRepository.findAll()
                                                 .stream()
                                                 .map(student -> StudentMapper.getInstance().toDTO(student))
                                                 .collect(Collectors.toList());
        List<StudentDTO> result = new ArrayList<>();
        for (StudentDTO dto: dtos
        ) {
            StudentDTO tmp = new StudentDTO();
            tmp.setId(dto.getId());
            tmp.setDob(dto.getDob());
            tmp.setAge(dto.getAge());
            result.add(tmp);
        }
        return result;
    }
}