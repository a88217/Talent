package com.circule.talent.components;

import com.circule.talent.dto.clients.ClientCreateDTO;
import com.circule.talent.dto.talents.TalentCreateDTO;
import com.circule.talent.dto.teams.TeamCreateDTO;
import com.circule.talent.dto.users.UserCreateDTO;
import com.circule.talent.mapper.ClientMapper;
import com.circule.talent.mapper.TalentMapper;
import com.circule.talent.mapper.TeamMapper;
import com.circule.talent.mapper.UserMapper;
import com.circule.talent.model.*;
import com.circule.talent.model.Package;
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

    private static final List<Package> PACKAGES = List.of(
            getPackage(
            "Контент-стратегия",
            "<p>Создаём вам карту, которая поможет:</p>" +
                    "<ul>" +
                    "<li>Создавать контент, который будет интересен вашей аудитории.</li>" +
                    "<li>Публиковать его в нужных местах и в нужное время.</li>" +
                    "<li>Распространять его, чтобы о нём узнало как можно больше людей.</li>" +
                    "<li>Достигать своих бизнес целей</li></ul>",
            "14 рабочих дней",
            "от 150 тыс. р."),
            getPackage(
            "Бренд-платформа для новых компаний",
            "<p>Платформа бренда - это как документ, в котором подробно описаны все идеи," +
                    " принципы и ценности вашей компании. Её главная цель - сформировать четкую" +
                    " идентичность бренда, выделить вашу компанию среди других и подчеркнуть вашу уникальность" +
                    " перед потребителями, помогая вам отличаться от конкурентов</p>",
            "14 рабочих дней",
            "290 тыс. р."));

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

    private final TeamRepository teamRepository;

    private final PackageRepository packageRepository;

    private final TalentMapper talentMapper;

    private final ClientMapper clientMapper;

    private final TeamMapper teamMapper;

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
            userData.setFirstName("Александр");
            userData.setLastName("Музалёв");
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

        for (var pack : PACKAGES) {
            if (packageRepository.findByTitle(pack.getTitle()).isEmpty()) {
                packageRepository.save(pack);
            }
        }

        if (talentRepository.findByEmail("elenaiarygina@gmail.com").isEmpty()) {
            var talentData = new TalentCreateDTO();
            talentData.setFirstName("Елена");
            talentData.setLastName("Ярыгина");
            talentData.setAbout("Более 12 лет опыта в продюсировании контентных проектов, рекламы, фильмов, более 5 лет в предпринимательстве.");
            talentData.setEmail("elenaiarygina@gmail.com");
            talentData.setMobilePhone("+79152378222");
            talentData.setPhotoName("лена.png");
            talentData.setPassword("qwerty");
            talentData.setProfessionIds(Set.of(professionRepository.findByTitle("Продюсер").get().getId(),
                    professionRepository.findByTitle("Контент-стратег").get().getId()));

            var talent = talentMapper.map(talentData);
            Role talentRole = roleRepository.findByName("ROLE_TALENT");
            talent.setRoles(Arrays.asList(talentRole));

            talentRepository.save(talent);
            var project = getProject("Одри", "Воспитание самой лучшей и послушной собаки", talent);
            talent.addProject(project);
            projectRepository.save(project);
        }

        if (talentRepository.findByEmail("marina_lomteva@gmail.com").isEmpty()) {
            var talentData = new TalentCreateDTO();
            talentData.setFirstName("Марина");
            talentData.setLastName("Ломтева");
            talentData.setAbout("В креативном предпринимательстве более 20 лет. Руководство проектами в сфере маркетинга, рекламы, игрового и документального кино, внутренних коммуникаций, ивент-маркетинга и спецпроектов.");
            talentData.setEmail("marina_lomteva@gmail.com");
            talentData.setMobilePhone("+1111111111");
            talentData.setPhotoName("марина.png");
            talentData.setPassword("qwerty");
            talentData.setProfessionIds(Set.of(professionRepository.findByTitle("Продюсер").get().getId(),
                    professionRepository.findByTitle("Сценарист").get().getId(), professionRepository.findByTitle("Копирайтер").get().getId()));

            var talent = talentMapper.map(talentData);
            Role talentRole = roleRepository.findByName("ROLE_TALENT");
            talent.setRoles(Arrays.asList(talentRole));

            talentRepository.save(talent);
        }

        if (talentRepository.findByEmail("test_talent2@gmail.com").isEmpty()) {
            var talentData = new TalentCreateDTO();
            talentData.setFirstName("Исполнитель");
            talentData.setLastName("Второй");
            talentData.setAbout("Тестовый исполнитель 2");
            talentData.setEmail("test_talent2@gmail.com");
            talentData.setMobilePhone("+2222222222");
            talentData.setPassword("qwerty");
            talentData.setProfessionIds(Set.of(professionRepository.findByTitle("Маркетолог").get().getId(),
                    professionRepository.findByTitle("Контент-стратег").get().getId()));

            var talent = talentMapper.map(talentData);
            Role talentRole = roleRepository.findByName("ROLE_TALENT");
            talent.setRoles(Arrays.asList(talentRole));

            talentRepository.save(talent);
        }

        if (talentRepository.findByEmail("test_talent3@gmail.com").isEmpty()) {
            var talentData = new TalentCreateDTO();
            talentData.setFirstName("Исполнитель");
            talentData.setLastName("Третий");
            talentData.setAbout("Тестовый исполнитель 3");
            talentData.setEmail("test_talent3@gmail.com");
            talentData.setMobilePhone("+33333333333");
            talentData.setPassword("qwerty");
            talentData.setProfessionIds(Set.of(professionRepository.findByTitle("Художник").get().getId(),
                    professionRepository.findByTitle("Моушен дизайнер").get().getId(),
                    professionRepository.findByTitle("Редактор").get().getId()));

            var talent = talentMapper.map(talentData);
            Role talentRole = roleRepository.findByName("ROLE_TALENT");
            talent.setRoles(Arrays.asList(talentRole));

            talentRepository.save(talent);
        }

        if (talentRepository.findByEmail("test_talent4@gmail.com").isEmpty()) {
            var talentData = new TalentCreateDTO();
            talentData.setFirstName("Исполнитель");
            talentData.setLastName("Четвертый");
            talentData.setAbout("Тестовый исполнитель 4");
            talentData.setEmail("test_talent4@gmail.com");
            talentData.setPhotoName("Operator.jpg");
            talentData.setMobilePhone("+44444444444");
            talentData.setPassword("qwerty");
            talentData.setProfessionIds(Set.of(professionRepository.findByTitle("Режиссер").get().getId(),
                    professionRepository.findByTitle("Сценарист").get().getId(),
                    professionRepository.findByTitle("Оператор").get().getId()));

            var talent = talentMapper.map(talentData);
            Role talentRole = roleRepository.findByName("ROLE_TALENT");
            talent.setRoles(Arrays.asList(talentRole));

            talentRepository.save(talent);
        }

        if (talentRepository.findByEmail("test_talent5@gmail.com").isEmpty()) {
            var talentData = new TalentCreateDTO();
            talentData.setFirstName("Исполнитель");
            talentData.setLastName("Пятый");
            talentData.setAbout("Тестовый исполнитель 5");
            talentData.setEmail("test_talent5@gmail.com");
            talentData.setMobilePhone("+5555555555");
            talentData.setPassword("qwerty");
            talentData.setProfessionIds(Set.of(professionRepository.findByTitle("PR менеджер").get().getId()));

            var talent = talentMapper.map(talentData);
            Role talentRole = roleRepository.findByName("ROLE_TALENT");
            talent.setRoles(Arrays.asList(talentRole));

            talentRepository.save(talent);
        }

        if (teamRepository.findByTitle("Контент-стратег/креативный копирайтер").isEmpty()) {
            var teamData = new TeamCreateDTO();
            teamData.setTitle("Контент-стратег/креативный копирайтер");
            teamData.setDescription("Команда разработает контент-стратегию вашего бренда");

            var team = teamMapper.map(teamData);
            team.addTalent(talentRepository.findByEmail("marina_lomteva@gmail.com").get());
            team.addTalent(talentRepository.findByEmail("test_talent2@gmail.com").get());

            teamRepository.save(team);

            var pack = packageRepository.findByTitle("Контент-стратегия").get();
            pack.addTeam(team);
            packageRepository.save(pack);
        }

        if (teamRepository.findByTitle("Тестовая команда").isEmpty()) {
            var teamData = new TeamCreateDTO();
            teamData.setTitle("Тестовая команда");
            teamData.setDescription("Команда разработает что угодно");

            var team = teamMapper.map(teamData);
            team.addTalent(talentRepository.findByEmail("test_talent2@gmail.com").get());
            team.addTalent(talentRepository.findByEmail("test_talent3@gmail.com").get());
            team.addTalent(talentRepository.findByEmail("test_talent4@gmail.com").get());
            team.addTalent(talentRepository.findByEmail("test_talent5@gmail.com").get());
            team.getTalents().stream().forEach(t -> System.out.println(t.getEmail()));

            teamRepository.save(team);

            var pack = packageRepository.findByTitle("Бренд-платформа для новых компаний").get();
            pack.addTeam(team);
            packageRepository.save(pack);
        }



        if (clientRepository.findByEmail("test_client@gmail.com").isEmpty()) {
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

    public static Project getProject(String title, String description, Team performer) {
        var project = new Project();
        project.setTitle(title);
        project.setDescription(description);
        project.setPerformer(performer);
        return project;
    }

    public static Team getTeam(String title, String description) {
        var team = new Team();
        team.setTitle(title);
        team.setDescription(description);
        return team;
    }

    public static Package getPackage(String title, String description, String term, String price) {
        var pack = new Package();
        pack.setTitle(title);
        pack.setDescription(description);
        pack.setTerm(term);
        pack.setPrice(price);
        return pack;
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
