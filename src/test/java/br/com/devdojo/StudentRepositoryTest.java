package br.com.devdojo;

import br.com.devdojo.model.Student;
import br.com.devdojo.repository.StudentRepository;
import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolationException;
import java.util.List;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // para executar os testes usando o banco de dados SQL
public class StudentRepositoryTest {
    @Autowired
    private StudentRepository studentRepository;
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void createShouldPersistData() {
        Student student = new Student("Thiago", "thiago@teste.com");
        this.studentRepository.save(student);
        Assertions.assertThat(student.getId()).isNotNull();
        Assertions.assertThat(student.getName()).isEqualTo("Thiago");
        Assertions.assertThat(student.getEmail()).isEqualTo("thiago@teste.com");
    }

    @Test
    public void deleteShouldRemoveData() {
        Student student = new Student("Thiago", "thiago@teste.com");
        this.studentRepository.save(student);
        this.studentRepository.delete(student);
        Assertions.assertThat(studentRepository.findOne(student.getId())).isNull();
    }

    @Test
    public void updateShouldChangeAndPersistData() {
        Student student = new Student("Thiago", "thiago@teste.com");
        this.studentRepository.save(student);
        student.setName("Thiago2");
        student.setEmail("thiago2@teste2.com");
        this.studentRepository.save(student);
        student = this.studentRepository.findOne(student.getId());
        Assertions.assertThat(student.getName()).isEqualTo("Thiago2");
        Assertions.assertThat(student.getEmail()).isEqualTo("thiago2@teste2.com");
    }

    @Test
    public void findByNameIgnoreCaseContainingShouldIgnoreCase() {
        Student student = new Student("Thiago", "thiago@teste.com");
        Student student2 = new Student("thiago", "thiago2@teste2.com");
        this.studentRepository.save(student);
        this.studentRepository.save(student2);
        List<Student> studentList = this.studentRepository.findByNameIgnoreCaseContaining("thiago");
        Assertions.assertThat(studentList.size()).isEqualTo(2);
    }

    @Test
    public void createWhenNameIsNullShouldThrowConstraintViolationException() {
        thrown.expect(ConstraintViolationException.class);
        thrown.expectMessage("O campo nome do estudante é obrigatório");
        this.studentRepository.save(new Student());
    }

    @Test
    public void createWhenEmailIsNullShouldThrowConstraintViolationException() {
        thrown.expect(ConstraintViolationException.class);
        Student student = new Student();
        student.setName("Thiago");
        this.studentRepository.save(student);
    }

    @Test
    public void createWhenEmailIsNotValidShouldThrowConstraintViolationException() {
        thrown.expect(ConstraintViolationException.class);
        thrown.expectMessage("Digite um email válido");
        Student student = new Student();
        student.setName("Thiago");
        student.setEmail("tt");
        this.studentRepository.save(student);
    }
}
