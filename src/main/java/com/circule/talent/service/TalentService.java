package com.circule.talent.service;

import com.circule.talent.dto.talents.TalentDTO;
import com.circule.talent.dto.talents.TalentCreateDTO;
import com.circule.talent.dto.talents.TalentUpdateDTO;
import com.circule.talent.exception.ResourceNotFoundException;
import com.circule.talent.mapper.TalentMapper;
import com.circule.talent.model.Talent;
import com.circule.talent.repository.TalentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TalentService {

    private final TalentRepository talentRepository;

    private final TalentMapper talentMapper;

    public List<TalentDTO> index() {
        return talentMapper.map(talentRepository.findAll());
    }

    public TalentDTO show(Long id) {
        var talent =  talentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Talent with id " + id + " not found"));
        return talentMapper.map(talent);
    }

    public TalentDTO create(TalentCreateDTO talentData) {
        var talent = talentMapper.map(talentData);
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
