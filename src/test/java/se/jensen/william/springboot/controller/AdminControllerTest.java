package se.jensen.william.springboot.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import se.jensen.william.springboot.dto.UserResponseDTO;
import se.jensen.william.springboot.entities.User;
import se.jensen.william.springboot.repository.UserRepository;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup(){
        userRepository.deleteAll();

        User user = new User();
        user.setRole("ADMIN");
        user.setUsername("Admin");
        user.setPassword(passwordEncoder.encode("Admin"));
        user.setEmail("Admin@example.com");
        user.setDisplayName("Admin");
        user.setBio("I am the Admin");
        user.setProfileImagePath("null");
        userRepository.save(user);
    }

    @Disabled
    @Test
    void shouldGetAllUsers() throws Exception{
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/admin/all-users")
                        .with(httpBasic("Admin", "Admin")))
                .andExpect(status().isOk()).andReturn();

        String response = result.getResponse().getContentAsString();
        List<UserResponseDTO> users = objectMapper.readValue(
                response, new TypeReference<List<UserResponseDTO>>() {

                }
        );

        assertEquals(1, users.size());

    }
}
