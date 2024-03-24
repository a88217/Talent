package com.circule.talent.api;

import com.circule.talent.dto.talents.TalentCreateDTO;
import com.circule.talent.dto.talents.TalentUpdateDTO;
import com.circule.talent.model.Profession;
import com.circule.talent.model.Project;
import com.circule.talent.model.Talent;
import com.circule.talent.repository.ProfessionRepository;
import com.circule.talent.repository.ProjectRepository;
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
import org.springframework.test.web.servlet.MockMvc;
import com.circule.talent.utils.ModelGenerator;

import java.time.format.DateTimeFormatter;
import java.util.Set;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TalentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProfessionRepository professionRepository;

    @Autowired
    private TalentRepository talentRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ModelGenerator modelGenerator;

    private Talent testTalent;

    private Project testProject;

    private Profession testProfession;

    @BeforeEach
    public void setUp() {
        testProfession = Instancio.of(modelGenerator.getProfessionModel()).create();
        professionRepository.save(testProfession);
        testProject = Instancio.of(modelGenerator.getProjectModel()).create();
        projectRepository.save(testProject);
        testTalent = Instancio.of(modelGenerator.getTalentModel()).create();
        testTalent.setProfessions(Set.of(testProfession));
        testTalent.setProjects(Set.of(testProject));
        talentRepository.save(testTalent);
    }

    @AfterEach
    public void clean() {
        talentRepository.deleteAll();
        professionRepository.deleteAll();
        projectRepository.deleteAll();

    }



    @Test
    public void testShow() throws Exception {
        var request = get("/api/talents/" + testTalent.getId());
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                a -> a.node("id").isEqualTo(testTalent.getId()),
                a -> a.node("firstName").isEqualTo(testTalent.getFirstName()),
                a -> a.node("lastName").isEqualTo(testTalent.getLastName()),
                a -> a.node("email").isEqualTo(testTalent.getEmail()),
                a -> a.node("about").isEqualTo(testTalent.getAbout()),
                a -> a.node("createdAt").isEqualTo(testTalent.getCreatedAt()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))),
                a -> a.node("projectIds").isEqualTo(Set.of(testProject.getId())),
                a -> a.node("professionIds").isEqualTo(Set.of(testProfession.getId()))
        );
    }

    @Test
    public void testIndex() throws Exception {
        var request = get("/api/talents");
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        System.out.println(talentRepository.findByEmail("elenaiarygina@gmail.com").get().getAbout());
        assertThatJson(body).isArray().hasSize(2);
    }

    @Test
    public void testCreate() throws Exception {
        var newProject = Instancio.of(modelGenerator.getProjectModel()).create();
        projectRepository.save(newProject);
        var newProfession = Instancio.of(modelGenerator.getProfessionModel()).create();
        professionRepository.save(newProfession);
        var newTalent = Instancio.of(modelGenerator.getTalentModel()).create();
        var data = new TalentCreateDTO();
        data.setFirstName(newTalent.getFirstName());
        data.setLastName(newTalent.getLastName());
        data.setAbout(newTalent.getAbout());
        data.setEmail(newTalent.getEmail());
        data.setProjectIds(Set.of(newProject.getId()));
        data.setProfessionIds(Set.of(newProfession.getId()));
        var request = post("/api/talents")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));
        mockMvc.perform(request)
                .andExpect(status().isCreated());
        var talent = talentRepository.findByEmail(newTalent.getEmail()).get();
        assertNotNull(talent);
        assertThat(talent.getFirstName()).isEqualTo(newTalent.getFirstName());
        assertThat(talent.getLastName()).isEqualTo(newTalent.getLastName());
        assertThat(talent.getAbout()).isEqualTo(newTalent.getAbout());
        assertThat(talent.getEmail()).isEqualTo(newTalent.getEmail());
        assertThat(talent.getProjects()).isEqualTo(Set.of(newProject));
        assertThat(talent.getProfessions()).isEqualTo(Set.of(newProfession));
    }

    @Test
    public void testUpdate() throws Exception {
        var data = new TalentUpdateDTO();
        data.setLastName(JsonNullable.of("New lastname"));
        data.setAbout(JsonNullable.of("12345"));
        var request = put("/api/talents/" + testTalent.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));
        mockMvc.perform(request)
                .andExpect(status().isOk());
        var talent = talentRepository.findById(testTalent.getId()).get();
        assertThat(talent.getLastName()).isEqualTo("New lastname");
        assertThat(talent.getAbout()).isEqualTo("12345");
    }

    @Test
    public void testDelete() throws Exception {
        var request = delete("/api/talents/" + testTalent.getId());
        mockMvc.perform(request)
                .andExpect(status().isNoContent());
        assertThat(talentRepository.existsById(testTalent.getId())).isFalse();
    }


}
