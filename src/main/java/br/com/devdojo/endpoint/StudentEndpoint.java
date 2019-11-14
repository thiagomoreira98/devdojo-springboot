package br.com.devdojo.endpoint;

import br.com.devdojo.error.CustomErrorType;
import br.com.devdojo.error.ResourceNotFoundException;
import br.com.devdojo.model.Student;
import br.com.devdojo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("students")
public class StudentEndpoint {

    private final StudentRepository studentDao;
    @Autowired
    public StudentEndpoint(StudentRepository studentDao) {
        this.studentDao = studentDao;
    }

    @GetMapping
    public ResponseEntity<?> ListAll() {
        return new ResponseEntity<>(studentDao.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable("id") Long id) {
        verifyStudentExists(id);
        Student student = studentDao.findOne(id);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @GetMapping(path = "/findByName/{name}")
    public ResponseEntity<?> findByName(@PathVariable String name) {
        List<Student> students = studentDao.findByNameIgnoreCaseContaining(name);
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @PostMapping
    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<?> save(@Valid @RequestBody Student student) {
        Student result = studentDao.save(student);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        verifyStudentExists(id);
        studentDao.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody Student student) {
        verifyStudentExists(student.getId());
        studentDao.save(student);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void verifyStudentExists(Long id) {
        if(studentDao.findOne(id) == null)
            throw new ResourceNotFoundException("Student not found for ID: " + id);
    }
}