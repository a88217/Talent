package com.circule.talent.utils;

import com.circule.talent.model.Talent;
import com.circule.talent.model.Project;
import com.circule.talent.model.Profession;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ModelGenerator {

    private Model<Talent> talentModel;
    private Model<Project> projectModel;
    private Model<Profession> professionModel;

    @Autowired
    private Faker faker;

    @PostConstruct
    private void init() {
        talentModel = Instancio.of(Talent.class)
                .ignore(Select.field(Talent::getId))
                .ignore(Select.field(Talent::getCreatedAt))
                .ignore(Select.field(Talent::getUpdatedAt))
                .supply(Select.field(Talent::getEmail), () -> faker.internet().emailAddress())
                .toModel();

        professionModel = Instancio.of(Profession.class)
                .ignore(Select.field(Profession::getId))
                .supply(Select.field(Profession::getTitle), () -> faker.text().text(3, 255))
                .supply(Select.field(Profession::getDescription), () -> faker.text().text(3, 255))
                .ignore(Select.field(Profession::getCreatedAt))
                .toModel();

        projectModel = Instancio.of(Project.class)
                .ignore(Select.field(Project::getId))
                .supply(Select.field(Project::getTitle), () -> faker.text().text(3, 255))
                .supply(Select.field(Project::getDescription), () -> faker.text().text(3, 255))
                .ignore(Select.field(Project::getCreatedAt))
                .toModel();
    }


}
