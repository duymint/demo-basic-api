package com.example.demoapi11.service;

import com.example.demoapi11.mapper.StudentMapper;
import com.example.demoapi11.repository.StudentRepository;
import com.example.demoapi11.student.Student;
import com.example.demoapi11.student.StudentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class StudentService {


    private StudentRepository studentRepository;
    private final RedisTemplate<String, StudentDTO> redisTemplate;

    @Autowired
    public StudentService(StudentRepository studentRepository, RedisTemplate<String, StudentDTO> redisTemplate){
        this.studentRepository = studentRepository;
        this.redisTemplate = redisTemplate;
    }



    public List<StudentDTO> getStudents(String name) {
        if(name != null && name.length() > 0){
            return studentRepository.findStudentByName(name)
                                                .stream()
                                                .map(student -> StudentMapper.getInstance().toDTO(student))
                                                .collect(Collectors.toList());
        }
         List<StudentDTO> result =  studentRepository.findAll()
                                .stream()
                                .map(student -> StudentMapper.getInstance().toDTO(student))
                                .collect(Collectors.toList());
        Thread redisAddAllStudents = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        for (StudentDTO student: result
                        ) {
                            final String key = "student_" + student.getId();
                            final boolean hasKey = redisTemplate.hasKey(key);
                            if(!hasKey){
                                StudentDTO dto = new StudentDTO();
                                dto.setId(student.getId());
                                dto.setEmail(student.getEmail());
                                dto.setName(student.getName());
                                dto.setDob(student.getDob());
                                dto.setAge(student.getAge());
                                redisTemplate.opsForValue().set(key, dto, 60, TimeUnit.SECONDS);
                            }
                        }
                    }
                }
        );
        redisAddAllStudents.start();
        return result;
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
        final String key = "student_" + studentId;
        final boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey) {
            redisTemplate.delete(key);
        }
        studentRepository.deleteById(studentId);
    }

    //Update name or email
    @Transactional
    public void updateStudent(Long studentId, String name, String email) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new IllegalStateException(
                "Student with id " + studentId + " does not existed"
        ));

        final String key = "student_" + studentId;
        final boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey) {
            StudentDTO dto = new StudentDTO();
            dto.setId(studentId);
            dto.setEmail(email);
            dto.setName(name);
            dto.setDob(redisTemplate.opsForValue().get(key).getDob());
            dto.setAge(redisTemplate.opsForValue().get(key).getAge());
            redisTemplate.opsForValue().set(key, dto, 60, TimeUnit.SECONDS);
        }

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

    public List<StudentDTO> getStudentsWitName() {
        List<StudentDTO> dtos = studentRepository.findAll()
                                                 .stream()
                                                 .map(student -> StudentMapper.getInstance().toDTO(student))
                                                 .collect(Collectors.toList());
        List<StudentDTO> result = new ArrayList<>();
        for (StudentDTO dto: dtos
        ) {
            StudentDTO tmp = new StudentDTO();
            tmp.setId(dto.getId());
            tmp.setEmail(dto.getEmail());
            tmp.setName(dto.getName());
            result.add(tmp);
        }
        return result;
    }

    public void checkDOBWithQuartz() {

        int currentMonth = LocalDate.now().getMonthValue();
        int currentDay = LocalDate.now().getDayOfMonth();
        List<Student> students = studentRepository.findStudentsByBirthday(currentDay, currentMonth);
        if(!students.isEmpty()){
            System.out.println("------Students Have Birthday Today-----");

            for (Student student:students
            ) {
                System.out.print(student.getName());
                System.out.print("     Date of birth:   ");
                System.out.println(student.getDob());
            }

        }else {
            System.out.println("------No One Has Birthday Today-----");
        }
    }


    public StudentDTO findStudentById(Long id) {
        final  String key = "student_" + id;
        final ValueOperations<String, StudentDTO> operations = redisTemplate.opsForValue();
        final boolean hasKey = redisTemplate.hasKey(key);

        if (hasKey) {
            final StudentDTO student = operations.get(key);
            return student;
        }
        final Optional<Student> studentOptional = studentRepository.findStudentById(id);
        if(studentOptional.isPresent()) {
            StudentDTO result = new StudentDTO();
            result= StudentMapper.getInstance().toDTO(studentOptional.get());
            operations.set(key, result, 60, TimeUnit.SECONDS);
            System.out.println("Value of key: "+operations.get(key).getEmail());
            return result;
        } else {
            throw  new IllegalStateException("Student with id " + id + " does not exist");
        }
    }
}