package com.circule.talent.controllers.api;

import com.circule.talent.dto.packages.PackageCreateDTO;
import com.circule.talent.dto.packages.PackageDTO;
import com.circule.talent.dto.packages.PackageUpdateDTO;
import com.circule.talent.service.PackageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/packages")
@RequiredArgsConstructor
public class PackageController {

    private final PackageService packageService;

    @GetMapping(path = "")
    public ResponseEntity<List<PackageDTO>> index() {
        var result = packageService.index();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(result.size()))
                .body(result);
    }

    @GetMapping(path = "/{id}")
    public PackageDTO show(@PathVariable long id) {
        return packageService.show(id);
    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public PackageDTO create(@Valid @RequestBody PackageCreateDTO packageData) {
        return packageService.create(packageData);
    }

    @PutMapping(path = "/{id}")
    public PackageDTO update(@Valid @RequestBody PackageUpdateDTO packageData, @PathVariable Long id) {
        return packageService.update(packageData, id);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        packageService.delete(id);
    }

}