package com.circule.talent.controllers.api;

import com.circule.talent.dto.talents.TalentCreateDTO;
import com.circule.talent.dto.talents.TalentDTO;
import com.circule.talent.dto.talents.TalentParamsDTO;
import com.circule.talent.dto.talents.TalentUpdateDTO;
import com.circule.talent.service.TalentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/talents")
@AllArgsConstructor
public class TalentController {

    private final TalentService talentService;

    @GetMapping(path = "")
    public ResponseEntity<List<TalentDTO>> index(TalentParamsDTO params) {
        var result = talentService.index(params);
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(result.size()))
                .body(result);
    }

    @GetMapping(path = "/{id}")
    public TalentDTO show(@PathVariable long id) {
        return talentService.show(id);
    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public TalentDTO create(@Valid @RequestBody TalentCreateDTO talentData) {
        return talentService.create(talentData);
    }

    @PutMapping(path = "/{id}")
    public TalentDTO update(@Valid @RequestBody TalentUpdateDTO talentData, @PathVariable Long id) {
        return talentService.update(talentData, id);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        talentService.delete(id);
    }
}