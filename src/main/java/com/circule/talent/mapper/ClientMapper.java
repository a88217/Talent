package com.circule.talent.mapper;

import com.circule.talent.dto.clients.ClientCreateDTO;
import com.circule.talent.dto.clients.ClientDTO;
import com.circule.talent.dto.clients.ClientUpdateDTO;
import com.circule.talent.model.Client;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;


@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class ClientMapper {
    @Autowired
    private PasswordEncoder encoder;

    @Mapping(target = "passwordDigest", source = "password")
    public abstract Client map(ClientCreateDTO dto);

    @Mapping(target = "passwordDigest", ignore = true)
    public abstract void update(ClientUpdateDTO dto, @MappingTarget Client model);

    public abstract ClientDTO map(Client model);

    public abstract List<ClientDTO> map(List<Client> models);

    @BeforeMapping
    public void encryptCreatePassword(ClientCreateDTO dto) {
        var password = dto.getPassword();
        dto.setPassword(encoder.encode(password));
    }

    @BeforeMapping
    public void encryptUpdatePassword(ClientUpdateDTO dto, @MappingTarget Client model) {
        var password = dto.getPassword();
        if (password != null && password.isPresent()) {
            model.setPasswordDigest(encoder.encode(password.get()));
        }
    }
}
