package com.circule.talent.components;

import com.circule.talent.dto.clients.ClientCreateDTO;
import com.circule.talent.dto.talents.TalentCreateDTO;
import com.circule.talent.dto.users.UserCreateDTO;
import com.circule.talent.mapper.ClientMapper;
import com.circule.talent.mapper.TalentMapper;
import com.circule.talent.mapper.UserMapper;
import com.circule.talent.model.*;
import com.circule.talent.repository.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
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

    private final TalentRepository talentRepository;

    private final ClientRepository clientRepository;

    private final ProjectRepository projectRepository;

    private final ProfessionRepository professionRepository;

    private final TalentMapper talentMapper;

    private final ClientMapper clientMapper;

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final RoleRepository roleRepository;

    private final PrivilegeRepository privilegeRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if (userRepository.findByEmail("muzalev.as@gmail.com").isEmpty()) {

            Privilege readPrivilege
                    = createPrivilegeIfNotFound("READ_PRIVILEGE");
            Privilege writePrivilege
                    = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

            List<Privilege> adminPrivileges = Arrays.asList(
                    readPrivilege, writePrivilege);


            createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
            createRoleIfNotFound("ROLE_TALENT", Arrays.asList(readPrivilege));
            createRoleIfNotFound("ROLE_CLIENT", Arrays.asList(readPrivilege));

            Role adminRole = roleRepository.findByName("ROLE_ADMIN");

            var userData = new UserCreateDTO();
            userData.setEmail("muzalev.as@gmail.com");
            userData.setPassword("qwerty");
            var user = userMapper.map(userData);
            user.setRoles(Arrays.asList(adminRole));
            userRepository.save(user);

        }

        for (var profession : PROFESSIONS) {
            if (professionRepository.findByTitle(profession.getTitle()).isEmpty()) {
                professionRepository.save(profession);
            }
        }

        if (talentRepository.findByEmail("elenaiarygina@gmail.com").isEmpty()) {
            System.out.println("Start talent initialisation");
            var talentData = new TalentCreateDTO();
            talentData.setFirstName("Елена");
            talentData.setLastName("Ярыгина");
            talentData.setAbout("Сладкий котик");
            talentData.setEmail("elenaiarygina@gmail.com");
            talentData.setMobilePhone("+79152378222");
            talentData.setPassword("qwerty");
            talentData.setProfessionIds(Set.of(professionRepository.findByTitle("Продюсер").get().getId(),
                    professionRepository.findByTitle("Контент-стратег").get().getId()));
            System.out.println(talentData.getPassword());

            var talent = talentMapper.map(talentData);
            Role talentRole = roleRepository.findByName("ROLE_TALENT");
            talent.setRoles(Arrays.asList(talentRole));

            talentRepository.save(talent);
            var project = getProject("Одри", "Воспитание самой лучшей и послушной собаки", talent);
            talent.addProject(project);
            projectRepository.save(project);
        }

        if (clientRepository.findByEmail("test_client@gmail.com").isEmpty()) {
            System.out.println("Start client initialisation");
            var clientData = new ClientCreateDTO();
            clientData.setFirstName("Тест");
            clientData.setLastName("Тестович");
            clientData.setAbout("Тестовый клиент");
            clientData.setEmail("test_client@gmail.com");
            clientData.setCompanyName("Тестовая компания");
            clientData.setMobilePhone("+79262864405");
            clientData.setPassword("qwerty");

            var client = clientMapper.map(clientData);
            Role clientRole = roleRepository.findByName("ROLE_CLIENT");
            client.setRoles(Arrays.asList(clientRole));
            clientRepository.save(client);
            System.out.println("End client initialisation");
        }
    }

    public static Profession getProfession(String title, String description) {
        var profession = new Profession();
        profession.setTitle(title);
        profession.setDescription(description);
        return profession;
    }

    public static Project getProject(String title, String description, Talent creator) {
        var project = new Project();
        project.setTitle(title);
        project.setDescription(description);
        project.setCreator(creator);
        return project;
    }

    @Transactional
    Privilege createPrivilegeIfNotFound(String name) {

        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege();
            privilege.setName(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    Role createRoleIfNotFound(
            String name, Collection<Privilege> privileges) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role();
            role.setName(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }

}
