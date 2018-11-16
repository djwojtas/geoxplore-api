package pl.edu.agh.geoxplore;

import com.google.gson.Gson;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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
import pl.edu.agh.geoxplore.entity.ApplicationUser;
import pl.edu.agh.geoxplore.repository.ApplicationUserRepository;
import pl.edu.agh.geoxplore.rest.Geolocation;
import pl.edu.agh.geoxplore.security.SecurityConstants;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AppTestConfig.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ApplicationUserRepository applicationUserRepository;

    private String authorizationHeader;

    @Getter
    @Builder
    private static class LoginRequestStub {
        private Double longitude;
        private Double latitude;
    }

    @Before
    public void addUser() {
        applicationUserRepository.deleteAll();
        applicationUserRepository.save(ApplicationUser.builder()
                .email("test@test.com")
                .experience(0L)
                .level(0L)
                .password(passwordEncoder.encode("password123"))
                .username("test1234")
                .build());

        authorizationHeader = SecurityConstants.TOKEN_PREFIX + Jwts.builder().setSubject("test1234")
                .setExpiration(Date.from(ZonedDateTime.now(ZoneOffset.UTC).plus(SecurityConstants.EXPIRATION_TIME, ChronoUnit.MILLIS).toInstant()))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET)
                .compact();
    }

    @Test
    public void shouldGetAddedHomeLocation() throws Exception {
        mockMvc.perform(post("/user/home")
                .header("Authorization", authorizationHeader)
                .contentType(MediaType.APPLICATION_JSON).content(
                        new Gson().toJson(LoginRequestStub.builder()
                                .longitude(1500.0)
                                .latitude(1000.0).build())))
                .andExpect(status().isOk());
        MvcResult result = mockMvc.perform(get("/user/home")
                .header("Authorization", authorizationHeader)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Geolocation geolocation = new Gson().fromJson(result.getResponse().getContentAsString(), Geolocation.class);
        assertEquals((double) geolocation.getLatitude(), 1000.0);
        assertEquals((double) geolocation.getLongitude(), 1500.0);
    }
}
