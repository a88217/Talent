package com.circule.talent.service;

import com.circule.talent.dto.talents.TalentCreateDTO;
import com.circule.talent.mapper.TalentMapper;
import com.circule.talent.model.Provider;
import com.circule.talent.repository.PrivilegeRepository;
import com.circule.talent.repository.RoleRepository;
import com.circule.talent.repository.TalentRepository;
import com.circule.talent.repository.UserRepository;
import com.circule.talent.utils.CustomOAuth2User;
import com.circule.talent.utils.CustomUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.Objects;

@AllArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsManager {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PrivilegeRepository privilegeRepository;

    private final PasswordEncoder passwordEncoder;

    private final TalentRepository talentRepository;

    private final TalentService talentService;

    private final TalentMapper talentMapper;


    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        System.out.println("Start customUserDetailsService loadUserByUsername");

        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (user == null) {
            return new org.springframework.security.core.userdetails.User(
                    " ", " ", true, true, true, true,
                    null);
        }

        return new CustomUserDetails.Builder().withId(user.getId())
                .withFirstName(user.getFirstName())
                .withLastName(user.getLastName())
                .withEmail(user.getEmail())
                .withUsername(user.getUsername())
                .withPassword(user.getPassword())
                .withAuthorities(user.getAuthorities(user.getRoles()))
                .build();
    }

    @Override
    public void createUser(UserDetails userData) {
        throw new UnsupportedOperationException("Unimplemented method 'updateUser'");
    }

    @Override
    public void updateUser(UserDetails user) {
        throw new UnsupportedOperationException("Unimplemented method 'updateUser'");
    }

    @Override
    public void deleteUser(String username) {
        throw new UnsupportedOperationException("Unimplemented method 'deleteUser'");
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        throw new UnsupportedOperationException("Unimplemented method 'changePassword'");
    }

    @Override
    public boolean userExists(String username) {
        throw new UnsupportedOperationException("Unimplemented method 'userExists'");
    }

    public void processOAuthPostLogin(CustomOAuth2User oAuth2User) {

        var existUser = userRepository.findByEmail(oAuth2User.getEmail()).orElse(null);

        if (Objects.isNull(existUser)) {
            var newUserDTO = new TalentCreateDTO();
            newUserDTO.setEmail(oAuth2User.getEmail());
            newUserDTO.setFirstName(oAuth2User.getAttribute("given_name"));
            newUserDTO.setLastName(oAuth2User.getAttribute("family_name"));
            newUserDTO.setPassword("Talents1278");

            talentService.create(newUserDTO);

            var newUser = talentRepository.findByEmail(oAuth2User.getEmail())
                                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));


            newUser.setProvider(Provider.GOOGLE);

            talentRepository.save(newUser);

            System.out.println("Created new user: " + oAuth2User.getEmail());
        } else {
            System.out.println("User with email: " + oAuth2User.getEmail() + " already exist");
        }

    }

}

