package pl.edu.agh.geoxplore;

import com.google.gson.Gson;
import lombok.Builder;
import lombok.Getter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.edu.agh.geoxplore.config.AppConfig;
import pl.edu.agh.geoxplore.entity.ApplicationUser;
import pl.edu.agh.geoxplore.repository.ApplicationUserRepository;
import pl.edu.agh.geoxplore.security.SecurityConstants;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AppConfig.class)
@AutoConfigureMockMvc
public class UserLoginTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Getter
    @Builder
    private static class LoginRequestStub {
        private String username;
        private String password;
    }

    @Before
    public void addUser() {
        applicationUserRepository.save(ApplicationUser.builder()
                .email("test@test.com")
                .experience(0L)
                .level(0L)
                .password(passwordEncoder.encode("password123"))
                .username("test1234")
                .build());
    }

    @Test
    public void shouldLoginUserForCorrectCredentials() throws Exception {
        MvcResult result = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON).content(
                        new Gson().toJson(LoginRequestStub.builder()
                                .username("test1234")
                                .password("password123").build())))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().startsWith("{\"token\":\"" + SecurityConstants.TOKEN_PREFIX));
    }

    @Test
    public void shouldReturn401ForIncorrectCredentials() throws Exception {
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON).content(
                        new Gson().toJson(LoginRequestStub.builder()
                                .username("test1234")
                                .password("password1234").build())))
                .andExpect(status().isUnauthorized());
    }
}
