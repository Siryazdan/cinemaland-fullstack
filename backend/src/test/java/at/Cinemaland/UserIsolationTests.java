package at.Cinemaland;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** Every user has their own collection: nobody may see or change another user's movies. */
@SpringBootTest
@AutoConfigureMockMvc
class UserIsolationTests {

    @Autowired
    MockMvc mvc;

    private MockHttpSession signUp(String name) throws Exception {
        MockHttpSession session = new MockHttpSession();
        String email = name + "-" + UUID.randomUUID() + "@example.com";
        mvc.perform(post("/api/auth/signup").session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"displayName\":\"" + name + "\",\"email\":\"" + email + "\",\"password\":\"password123\"}"))
                .andExpect(status().isCreated());
        return session;
    }

    private String createMovie(MockHttpSession session, String title) throws Exception {
        MvcResult result = mvc.perform(post("/api/movies/").session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"" + title + "\"}"))
                .andExpect(status().isCreated())
                .andReturn();
        Matcher m = Pattern.compile("\"id\":\"([^\"]+)\"").matcher(result.getResponse().getContentAsString());
        assertThat(m.find()).isTrue();
        return m.group(1);
    }

    @Test
    void anonymousUsersCannotUseTheApi() throws Exception {
        mvc.perform(get("/api/movies/")).andExpect(status().isUnauthorized());
    }

    @Test
    void usersOnlySeeAndChangeTheirOwnMovies() throws Exception {
        MockHttpSession alice = signUp("alice");
        MockHttpSession bob = signUp("bob");
        String aliceMovie = createMovie(alice, "Alice Movie");

        // Bob's collection is empty and Alice's movie doesn't exist for him
        mvc.perform(get("/api/movies/").session(bob)).andExpect(content().json("[]"));
        mvc.perform(get("/api/movies/" + aliceMovie).session(bob)).andExpect(status().isNotFound());
        mvc.perform(put("/api/movies/" + aliceMovie).session(bob)
                        .contentType(MediaType.APPLICATION_JSON).content("{\"title\":\"hacked\"}"))
                .andExpect(status().isNotFound());
        mvc.perform(delete("/api/movies/" + aliceMovie).session(bob)).andExpect(status().isNotFound());

        // Alice's movie is unchanged
        mvc.perform(get("/api/movies/" + aliceMovie).session(alice))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Alice Movie"));
    }

    @Test
    void ownerCannotBeSetByTheClient() throws Exception {
        MockHttpSession alice = signUp("alice");
        String id = createMovie(alice, "Mine");
        // the owner field is never part of the JSON
        mvc.perform(get("/api/movies/" + id).session(alice))
                .andExpect(jsonPath("$.userId").doesNotExist());
    }

    @Test
    void duplicateEmailIsRejected() throws Exception {
        String body = "{\"displayName\":\"x\",\"email\":\"same@example.com\",\"password\":\"password123\"}";
        mvc.perform(post("/api/auth/signup").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated());
        mvc.perform(post("/api/auth/signup").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }
}
