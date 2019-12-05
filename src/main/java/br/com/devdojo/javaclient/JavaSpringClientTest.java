package br.com.devdojo.javaclient;

import br.com.devdojo.model.Student;

public class JavaSpringClientTest {
    public static void main(String[] args) {
        Student student = new Student();
        student.setName("John Wick");
        student.setEmail("john@teste.com");

        JavaClientDAO dao = new JavaClientDAO();
        dao.listAll();
        dao.findById(1);
        dao.save(student);
        dao.update(student);
        dao.delete(29L);
    }
}
