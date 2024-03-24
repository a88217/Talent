package com.circule.talent.api;

import com.circule.talent.dto.professions.ProfessionCreateDTO;
import com.circule.talent.dto.professions.ProfessionUpdateDTO;
import com.circule.talent.model.Profession;
import com.circule.talent.repository.ProfessionRepository;
import com.circule.talent.repository.TalentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import com.circule.talent.utils.ModelGenerator;

import java.time.format.DateTimeFormatter;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProfessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProfessionRepository professionRepository;

    @Autowired
    private TalentRepository talentRepository;

    @Autowired
    private ModelGenerator modelGenerator;

    private Profession testProfession;

    @BeforeEach
    public void setUp() {
        testProfession = Instancio.of(modelGenerator.getProfessionModel()).create();
        professionRepository.save(testProfession);
    }

    @AfterEach
    public void clean() {
        talentRepository.deleteAll();
        professionRepository.deleteAll();
    }

    @Test
    public void testIndex() throws Exception {
        var request = get("/api/professions");
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        System.out.println(professionRepository.findAll());
        assertThatJson(body).isArray();
    }

    @Test
    public void testShow() throws Exception {
        var request = get("/api/professions/" + testProfession.getId());
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                a -> a.node("id").isEqualTo(testProfession.getId()),
                a -> a.node("title").isEqualTo(testProfession.getTitle()),
                a -> a.node("description").isEqualTo(testProfession.getDescription()),
                a -> a.node("createdAt").isEqualTo(testProfession.getCreatedAt()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
        );
    }

    @Test
    public void testCreate() throws Exception {
        var newProfession = Instancio.of(modelGenerator.getProfessionModel()).create();
        var data = new ProfessionCreateDTO();
        data.setTitle(newProfession.getTitle());
        var request = post("/api/professions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));
        mockMvc.perform(request)
                .andExpect(status().isCreated());
        var profession = professionRepository.findByTitle(newProfession.getTitle()).get();
        assertNotNull(profession);
    }

    @Test
    public void testUpdate() throws Exception {
        var data = new ProfessionUpdateDTO();
        data.setTitle(JsonNullable.of("New title"));
        var request = put("/api/professions/" + testProfession.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var profession = professionRepository.findById(testProfession.getId()).get();
        assertThat(profession.getTitle()).isEqualTo("New title");
    }

    @Test
    public void testDelete() throws Exception {
        var request = delete("/api/professions/" + testProfession.getId());
        mockMvc.perform(request)
                .andExpect(status().isNoContent());
        assertThat(professionRepository.existsById(testProfession.getId())).isFalse();
    }

}
