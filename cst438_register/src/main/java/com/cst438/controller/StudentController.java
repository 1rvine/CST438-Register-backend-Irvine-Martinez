package com.cst438.controller;

import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.cst438.domain.StudentDTO;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;



@RestController
public class StudentController {
		
    final StudentRepository studentRepository;
	
    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }
    
    @PostMapping("/student")
    @Transactional
    public StudentDTO addStudent(@RequestBody StudentDTO studentDTO) {
        System.out.println("/student called.");

        if (studentRepository.findByEmail(studentDTO.email) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Student with email '%s' already exists.", studentDTO.email));
        }
        if (studentRepository.findById(studentDTO.student_id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Student with id '%d' already exists.", studentDTO.student_id));
        }

        var student = new Student();
        student.setName(studentDTO.name);
        student.setEmail(studentDTO.email);
        student.setStatus(studentDTO.status);
        student.setStatusCode(studentDTO.statusCode);

        studentRepository.save(student);
        studentDTO.student_id = student.getStudent_id();
        return studentDTO;
    }
    
    @PutMapping("/student/{student_id}")
    public StudentDTO updateStatus(@PathVariable int student_id, @RequestBody StudentDTO studentDTO) {
        var student = studentRepository.findById(student_id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Student with id '%d' not found.", student_id)));

        student.setStatusCode(studentDTO.statusCode);
        student.setStatus(studentDTO.status);
        studentRepository.save(student);

        studentDTO.student_id = student.getStudent_id();
        studentDTO.name = student.getName();
        studentDTO.email = student.getEmail();

        return studentDTO;
    }
    	
}