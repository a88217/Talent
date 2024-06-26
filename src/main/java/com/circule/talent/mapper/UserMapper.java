package com.circule.talent.mapper;

import com.circule.talent.dto.users.UserCreateDTO;
import com.circule.talent.dto.users.UserDTO;
import com.circule.talent.dto.users.UserUpdateDTO;
import com.circule.talent.model.User;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class UserMapper {
    @Autowired
    private PasswordEncoder encoder;

    @Mapping(target = "passwordDigest", source = "password")
    public abstract User map(UserCreateDTO dto);

    @Mapping(target = "passwordDigest", ignore = true)
    public abstract void update(UserUpdateDTO dto, @MappingTarget User model);

    public abstract UserDTO map(User model);

    @BeforeMapping
    public void encryptCreatePassword(UserCreateDTO dto) {
        var password = dto.getPassword();
        dto.setPassword(encoder.encode(password));
    }

    @BeforeMapping
    public void encryptUpdatePassword(UserUpdateDTO dto, @MappingTarget User model) {
        var password = dto.getPassword();
        if (password != null && password.isPresent()) {
            model.setPasswordDigest(encoder.encode(password.get()));
        }
    }
}
