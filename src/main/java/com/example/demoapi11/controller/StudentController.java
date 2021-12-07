package com.example.demoapi11.controller;

import com.example.demoapi11.service.StudentService;
import com.example.demoapi11.student.Student;
import com.example.demoapi11.student.StudentDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/student")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService){
        this.studentService = studentService;
    }
    @GetMapping
    public List<StudentDTO> getStudents(@RequestParam(required = false) String name) {
        return studentService.getStudents(name);
    }


    @PostMapping
    public ResponseEntity registerNewStudent(@RequestBody Student student){
        studentService.addNewStudent(student);
        return ResponseEntity.ok().body(student);
    }

    @DeleteMapping(path = "{studentId}")
    public void deleteStudent(@PathVariable ("studentId") Long studentId){
        studentService.deleteStudent(studentId);
    }

    @PutMapping(path = "{studentId}")
    public void updateStudent(
            @PathVariable("studentId") Long studentId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email){
            studentService.updateStudent(studentId, name, email);
    }
    @GetMapping(path = "/no-name")
    public List<StudentDTO> getStudentsWithoutName(){
        return studentService.getStudentsWithoutName();
    }

    @GetMapping(path = "/dob-mail")
    public List<StudentDTO> getStudentsWithDOB(){
       return studentService.getStudentsWithDOB();
    }
}

