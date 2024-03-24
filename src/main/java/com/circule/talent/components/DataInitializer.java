package com.circule.talent.components;

import com.circule.talent.dto.talents.TalentCreateDTO;
import com.circule.talent.mapper.TalentMapper;
import com.circule.talent.model.Profession;
import com.circule.talent.model.Project;
import com.circule.talent.repository.ProfessionRepository;
import com.circule.talent.repository.ProjectRepository;
import com.circule.talent.repository.TalentRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private static final List<Profession> PROFESSIONS = List.of(
            getProfession("Продюсер", ""),
            getProfession("Режиссер", ""),
            getProfession("Фотограф", ""),
            getProfession("Оператор", ""),
            getProfession("Видеограф", ""),
            getProfession("Маркетолог", ""),
            getProfession("Бренд-стратег", ""),
            getProfession("Контент-стратег", ""),
            getProfession("Контентмейкер", ""),
            getProfession("Стилист/художник по костюмам", ""),
            getProfession("Стилист по волосам", ""),
            getProfession("Визажист", ""),
            getProfession("Дизайнер", ""),
            getProfession("Копирайтер", ""),
            getProfession("Сценарист", ""),
            getProfession("Монтажер", ""),
            getProfession("Художник", ""),
            getProfession("Моушен дизайнер", ""),
            getProfession("PR менеджер", ""),
            getProfession("Комьюнити менеджер", ""),
            getProfession("Редактор", ""),
            getProfession("Food-стилист", ""),
            getProfession("Художник-постановщик", ""));

    private static final List<Project> PROJECTS = List.of(
            getProject("Одри", "Воспитание самой лучшей и послушной собаки"));

    private final TalentRepository talentRepository;

    private final ProjectRepository projectRepository;

    private final ProfessionRepository professionRepository;

    private final TalentMapper talentMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        for (var profession : PROFESSIONS) {
            if (professionRepository.findByTitle(profession.getTitle()).isEmpty()) {
                professionRepository.save(profession);
            }
        }
        for (var project : PROJECTS) {
            if (projectRepository.findByTitle(project.getTitle()).isEmpty()) {
                projectRepository.save(project);
            }
        }
        if (talentRepository.findByEmail("elenaiarygina@gmail.com").isEmpty()) {
            var talentData = new TalentCreateDTO();
            talentData.setFirstName("Елена");
            talentData.setLastName("Ярыгина");
            talentData.setAbout("Сладкий котик");
            talentData.setEmail("elenaiarygina@gmail.com");
            talentData.setPassword("qwerty");
            talentData.setProfessionIds(Set.of(professionRepository.findByTitle("Продюсер").get().getId(),
                    professionRepository.findByTitle("Контент-стратег").get().getId()));
            talentData.setProjectIds(Set.of(projectRepository.findByTitle("Одри").get().getId()));
            var talent = talentMapper.map(talentData);

            System.out.println("After talent mapper");
            System.out.println("Email: " + talent.getEmail());

            talentRepository.save(talent);

            System.out.println("Talent: " + talentRepository.findByEmail("elenaiarygina@gmail.com"));
        }
    }

    public static Profession getProfession(String title, String description) {
        var profession = new Profession();
        profession.setTitle(title);
        profession.setDescription(description);
        return profession;
    }

    public static Project getProject(String title, String description) {
        var project = new Project();
        project.setTitle(title);
        project.setDescription(description);
        return project;
    }
}
