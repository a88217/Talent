package com.circule.talent.controllers.api;

import com.circule.talent.dto.professions.ProfessionCreateDTO;
import com.circule.talent.dto.professions.ProfessionDTO;
import com.circule.talent.dto.professions.ProfessionUpdateDTO;
import com.circule.talent.service.ProfessionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/professions")
@AllArgsConstructor
public class ProfessionController {

    private final ProfessionService professionService;

    @GetMapping(path = "")
    public ResponseEntity<List<ProfessionDTO>> index() {
        var result = professionService.index();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(result.size()))
                .body(result);
    }

    @GetMapping(path = "/{id}")
    public ProfessionDTO show(@PathVariable long id) {
        return professionService.show(id);
    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public ProfessionDTO create(@Valid @RequestBody ProfessionCreateDTO professionData) {
        return professionService.create(professionData);
    }

    @PutMapping(path = "/{id}")
    public ProfessionDTO update(@Valid @RequestBody ProfessionUpdateDTO professionData, @PathVariable Long id) {
        return professionService.update(professionData, id);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        professionService.delete(id);
    }

}
