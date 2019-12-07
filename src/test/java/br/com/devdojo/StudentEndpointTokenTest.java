package br.com.devdojo;

import br.com.devdojo.model.Student;
import br.com.devdojo.repository.StudentRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class StudentEndpointTokenTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @MockBean
    private StudentRepository studentRepository;

    @Autowired
    private MockMvc mockMvc;
    private HttpEntity<Void> protectedHeader;
    private HttpEntity<Void> adminHeader;
    private HttpEntity<Void> wrongHeader;

    @BeforeEach
    public void configProtectedHeaders() {
        System.out.printf("before user");
        String str = "{\"username\": \"user\",\"password\":\"1234\"}";
        HttpHeaders headers = restTemplate.postForEntity("/login", str, String.class).getHeaders();
        this.protectedHeader = new HttpEntity<>(headers);
    }

    @BeforeEach
    public void configAdminHeaders() {
        System.out.printf("before admin");
        String str = "{\"username\": \"admin\",\"password\":\"1234\"}";
        HttpHeaders headers = restTemplate.postForEntity("/login", str, String.class).getHeaders();
        this.adminHeader = new HttpEntity<>(headers);
    }

    @BeforeEach
    public void configWrongHeaders() {
        System.out.printf("before wrong");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "wrongAuthorization");
        this.wrongHeader = new HttpEntity<>(headers);
    }

    @BeforeEach
    public void Setup() {
        Student student = new Student(1L, "Legolas", "legolas@lotr.com");
        BDDMockito.when(studentRepository.findOne(student.getId())).thenReturn(student);
    }

    @Test
    public void listStudentsWhenTokenIsIncorrectShouldReturnStatusCode403() {
        ResponseEntity<String> response = restTemplate.exchange(
            "/v1/protected/students",
            HttpMethod.GET,
            wrongHeader,
            String.class
        );
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void getStudentsByIdWhenTokenIsIncorrectShouldReturnStatusCode403() {
        ResponseEntity<String> response = restTemplate.exchange(
                "/v1/protected/students/1",
                HttpMethod.GET,
                wrongHeader,
                String.class
        );
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void listStudentsWhenTokenIsCorrectShouldReturnStatusCode200() {
        ResponseEntity<String> response = restTemplate.exchange(
                "/v1/protected/students",
                HttpMethod.GET,
                protectedHeader,
                String.class
        );
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void getStudentsByIdWhenTokenIsCorrectShouldReturnStatusCode200() {
        ResponseEntity<String> response = restTemplate.exchange(
                "/v1/protected/students/{id}",
                HttpMethod.GET,
                protectedHeader,
                String.class,
                1
        );
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void getStudentsByIdWhenTokenIsCorrectAndStudentDoesNotExistShouldReturnStatusCode404() {
        ResponseEntity<String> response = restTemplate.exchange(
                "/v1/protected/students/{id}",
                HttpMethod.GET,
                protectedHeader,
                String.class,
                -1
        );
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    public void deleteWhenUserHasRoleAdminAndStudentExistsShouldReturnStatusCode200() {
        BDDMockito.doNothing().when(studentRepository).deleteById(1L);
        ResponseEntity<String> response = restTemplate.exchange(
                "/v1/admin/students/{id}",
                HttpMethod.DELETE,
                adminHeader,
                String.class,
                1
        );
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void deleteWhenUserHasRoleAdminAndStudentDoesNotExistsShouldReturnStatusCode404() throws Exception {
        adminHeader.getHeaders();
        String token = adminHeader.getHeaders().get("Authorization").get(0);
        BDDMockito.doNothing().when(studentRepository).deleteById(1L);
        mockMvc.perform(
            MockMvcRequestBuilders.delete("/v1/admin/students/{id}", -1L)
                .header("Authorization", token)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void deleteWhenUserDoesNotHaveRoleAdminShouldReturnStatusCode403() throws Exception {
        String token = protectedHeader.getHeaders().get("Authorization").get(0);
        BDDMockito.doNothing().when(studentRepository).deleteById(1L);
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/v1/admin/students/{id}", 1L)
                        .header("Authorization", token)
        ).andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void createWhenNameIsNullShouldReturnStatusCode400BadRequest() throws Exception {
        Student student = new Student(3L, null, "sam@lotr.com");
        BDDMockito.when(studentRepository.save(student)).thenReturn(student);
        ResponseEntity<String> response = restTemplate.exchange(
                "/v1/admin/students",
                HttpMethod.POST,
                new HttpEntity<>(student, adminHeader.getHeaders()),
                String.class
        );
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(400);
        //Assertions.assertThat(response.getBody()).contains("fieldMessage", "Nome é obrigatório");
    }

    @Test
    public void createShouldPersistDataAndReturnStatusCode201() throws Exception {
        Student student = new Student(3L, "Sam", "sam@lotr.com");
        BDDMockito.when(studentRepository.save(student)).thenReturn(student);
        ResponseEntity<Student> response = restTemplate.exchange(
                "/v1/admin/students",
                HttpMethod.POST,
                new HttpEntity<>(student, adminHeader.getHeaders()),
                Student.class
        );
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(201);
        Assertions.assertThat(response.getBody().getId()).isNotNull();
    }
}
