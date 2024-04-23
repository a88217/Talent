package com.circule.talent.service;

import com.circule.talent.dto.packages.PackageCreateDTO;
import com.circule.talent.dto.packages.PackageDTO;
import com.circule.talent.dto.packages.PackageUpdateDTO;
import com.circule.talent.exception.ResourceNotFoundException;
import com.circule.talent.mapper.PackageMapper;
import com.circule.talent.repository.PackageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PackageService {

    private final PackageRepository packageRepository;

    private final PackageMapper packageMapper;

    public List<PackageDTO> index() {
        return packageMapper.map(packageRepository.findAll());
    }

    public PackageDTO show(Long id) {
        var pack = packageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Package with id " + id + " not found"));
        return packageMapper.map(pack);
    }

    public PackageDTO create(PackageCreateDTO packageData) {
        var pack = packageMapper.map(packageData);
        packageRepository.save(pack);
        return packageMapper.map(pack);
    }

    public PackageDTO update(PackageUpdateDTO packageData, Long id) {
        var pack =  packageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Package with id " + id + " not found"));
        packageMapper.update(packageData, pack);
        packageRepository.save(pack);
        return packageMapper.map(pack);
    }

    public void delete(Long id) {
        packageRepository.deleteById(id);
    }
}