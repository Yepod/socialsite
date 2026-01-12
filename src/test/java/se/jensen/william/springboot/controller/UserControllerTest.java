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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {

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
        user.setRole("USER");
        user.setUsername("SimpleUser");
        user.setPassword(passwordEncoder.encode("SimpleUser"));
        user.setEmail("SimpleUser@example.com");
        user.setDisplayName("User");
        user.setBio("I am but a mere simple user.");
        user.setProfileImagePath("null");
        userRepository.save(user);
    }

    @Disabled
    @Test
    void shouldGetMyProfile() throws Exception{
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/users/my-profile")
                .with(httpBasic("SimpleUser", "SimpleUser")))
                .andExpect(status().isOk()).andReturn();

        String response = result.getResponse().getContentAsString();
        UserResponseDTO user = objectMapper.readValue(
                response, new TypeReference<UserResponseDTO>() {

                }
        );

        assertEquals("SimpleUser", user.username());
        assertEquals("SimpleUser@example.com", user.email());
        assertTrue(user.role().contains("USER"));

    }
}
