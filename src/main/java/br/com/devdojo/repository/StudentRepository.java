package br.com.devdojo.repository;

import br.com.devdojo.model.Student;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StudentRepository extends CrudRepository<Student, Long> {
    List<Student> findByNameIgnoreCaseContaining(String name);

    @Query("select id, name from Student s where s.id = :id")
    public Student findOne(Long id);
}
