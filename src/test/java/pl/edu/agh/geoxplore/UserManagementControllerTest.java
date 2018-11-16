package pl.edu.agh.geoxplore;

import com.google.gson.Gson;
import lombok.Builder;
import lombok.Getter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import pl.edu.agh.geoxplore.entity.ApplicationUser;
import pl.edu.agh.geoxplore.repository.ApplicationUserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AppTestConfig.class)
@AutoConfigureMockMvc
public class UserManagementControllerTest { //todo test password change

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ApplicationUserRepository applicationUserRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Getter
    @Builder
    private static class CreateUserRequestStub {
        private String username;
        private String email;
        private String password;
    }

    @Test
    public void shouldCreateUserForGivenData() throws Exception {
        Gson gson = new Gson();
        String json = gson.toJson(CreateUserRequestStub.builder()
                .username("test1234")
                .email("test@test.com")
                .password("password123")
                .build());

        mockMvc.perform(post("/user-management/user/create")
                    .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());

        ApplicationUser user = applicationUserRepository.findByUsername("test1234");
        assertEquals(user.getEmail(), "test@test.com");
        assertTrue(passwordEncoder.matches("password123", user.getPassword()));
    }

}
