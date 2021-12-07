package com.example.demoapi11.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/student")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService){
        this.studentService = studentService;
    }
    @GetMapping
    public List<Student> getStudents( @RequestParam(required = false) String name) {
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
}

