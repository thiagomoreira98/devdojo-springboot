package br.com.devdojo.repository;

import br.com.devdojo.model.Student;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface StudentRepository extends PagingAndSortingRepository<Student, Long> {
    List<Student> findByNameIgnoreCaseContaining(String name);

    @Query("SELECT s FROM Student s WHERE s.id = :id")
    Student findOne(Long id);

}
