package br.com.devdojo.endpoint;

import br.com.devdojo.error.CustomErrorType;
import br.com.devdojo.error.ResourceNotFoundException;
import br.com.devdojo.model.Student;
import br.com.devdojo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("v1")
public class StudentEndpoint {

    private final StudentRepository studentDao;
    @Autowired
    public StudentEndpoint(StudentRepository studentDao) {
        this.studentDao = studentDao;
    }

    @GetMapping(path = "protected/students")
    public ResponseEntity<?> ListAll(@PageableDefault(size = 10) Pageable pageable) {
        return new ResponseEntity<>(studentDao.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping(path = "protected/students/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable("id") Long id) {
        verifyStudentExists(id);
        Student student = studentDao.findOne(id);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @GetMapping(path = "protected/students/findByName/{name}")
    public ResponseEntity<?> findByName(@PathVariable String name) {
        List<Student> students = studentDao.findByNameIgnoreCaseContaining(name);
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @PostMapping(path = "admin/students")
    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<?> save(@Valid @RequestBody Student student) {
        Student result = studentDao.save(student);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "admin/students/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        //verifyStudentExists(id);
        studentDao.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(path = "admin/students")
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