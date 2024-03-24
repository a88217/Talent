package com.circule.talent.service;

import com.circule.talent.dto.professions.ProfessionCreateDTO;
import com.circule.talent.dto.professions.ProfessionDTO;
import com.circule.talent.dto.professions.ProfessionUpdateDTO;
import com.circule.talent.exception.ResourceNotFoundException;
import com.circule.talent.mapper.ProfessionMapper;
import com.circule.talent.repository.ProfessionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProfessionService {

    private final ProfessionRepository professionRepository;

    private final ProfessionMapper professionMapper;

    public List<ProfessionDTO> index() {
        var professions = professionRepository.findAll();
        return professions.stream()
                .map(p -> professionMapper.map(p))
                .toList();
    }

    public ProfessionDTO show(Long id) {
        var profession =  professionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profession with id " + id + " not found"));
        return professionMapper.map(profession);
    }

    public ProfessionDTO create(ProfessionCreateDTO professionData) {
        var profession = professionMapper.map(professionData);
        professionRepository.save(profession);
        return professionMapper.map(profession);
    }

    public ProfessionDTO update(ProfessionUpdateDTO professionData, Long id) {
        var profession =  professionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profession with id " + id + " not found"));
        professionMapper.update(professionData, profession);
        professionRepository.save(profession);
        return professionMapper.map(profession);
    }

    public void delete(Long id) {
        professionRepository.deleteById(id);
    }
}
