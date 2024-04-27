package com.circule.talent.service;

import com.circule.talent.dto.talents.TalentCreateDTO;
import com.circule.talent.dto.talents.TalentDTO;
import com.circule.talent.dto.talents.TalentParamsDTO;
import com.circule.talent.dto.talents.TalentUpdateDTO;
import com.circule.talent.exception.ResourceNotFoundException;
import com.circule.talent.mapper.TalentMapper;
import com.circule.talent.repository.RoleRepository;
import com.circule.talent.repository.TalentRepository;
import com.circule.talent.specification.TalentSpecification;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class TalentService {

    private final TalentRepository talentRepository;

    private final TalentMapper talentMapper;

    private final RoleRepository roleRepository;

    private final TalentSpecification talentSpecification;

    public List<TalentDTO> index(TalentParamsDTO params) {
        var spec = talentSpecification.build(params);
        return talentMapper.map(talentRepository.findAll(spec));
    }

    public TalentDTO show(Long id) {
        var talent =  talentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Talent with id " + id + " not found"));
        return talentMapper.map(talent);
    }

    public TalentDTO create(TalentCreateDTO talentData) {
        var talent = talentMapper.map(talentData);
        talent.setRoles(Arrays.asList(roleRepository.findByName("ROLE_TALENT")));
        talentRepository.save(talent);
        return talentMapper.map(talent);
    }

    public TalentDTO update(TalentUpdateDTO talentData, Long id) {
        var talent =  talentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Talent with id " + id + " not found"));
        talentMapper.update(talentData, talent);
        talentRepository.save(talent);
        return talentMapper.map(talent);
    }

    public void delete(Long id) {
        talentRepository.deleteById(id);
    }
}
