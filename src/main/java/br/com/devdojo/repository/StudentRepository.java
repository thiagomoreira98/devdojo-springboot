package br.com.devdojo.repository;

import br.com.devdojo.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.web.SortDefault;

import java.util.List;

public interface StudentRepository extends PagingAndSortingRepository<Student, Long> {
    List<Student> findByNameIgnoreCaseContaining(String name);

    @Query(value = "select id, name, email from Student s where s.id = :id", nativeQuery = true)
    public Student findOne(Long id);
}
