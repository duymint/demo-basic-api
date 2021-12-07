//package com.example.demoapi11.student;
//
//import com.sun.tools.javac.util.List;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.time.LocalDate;
//import java.time.Month;
//
//@Configuration
//public class StudentConfig {
//    @Bean
//    CommandLineRunner commandLineRunner(StudentRepository repository) {
//        return args -> {
//            Student mariam = new Student(
//                    "Mariam",
//                    LocalDate.of(2000, Month.APRIL, 5),
//                    "mariam@gmail.com"
//            );
//            Student alex = new Student(
//                    "Alex",
//                    LocalDate.of(1990, Month.AUGUST, 12),
//                    "alex@gmail.com"
//            );
//            repository.saveAll(List.of(mariam, alex));
//        };
//    }
//
//};
