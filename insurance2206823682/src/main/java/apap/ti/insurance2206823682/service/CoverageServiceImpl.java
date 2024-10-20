package apap.ti.insurance2206823682.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import apap.ti.insurance2206823682.model.Coverage;
import apap.ti.insurance2206823682.repository.CoverageDb;
import jakarta.annotation.PostConstruct;

@Service
public class CoverageServiceImpl implements CoverageService {

    @Autowired
    private CoverageDb coverageDb;

    @Override
    @PostConstruct
    public void initData() {
        // Check if there are existing records to avoid duplication
        if (coverageDb.count() == 0) {
            coverageDb.insertCoverages();  // Insert the coverage data using the native query
        }
    }

    @Override
    public long countCoverages() {
        return coverageDb.count();
    }

    @Override
    public List<Coverage> getAllCoverage(){
        return coverageDb.findAll();   
    }

    public boolean hasDuplicateCoverages(List<Coverage> coverages) {
        List<Long> coverageIds = coverages.stream()
                .map(Coverage::getId)
                .collect(Collectors.toList());

        Set<Long> uniqueCoverageIds = new HashSet<>(coverageIds);
        return uniqueCoverageIds.size() < coverageIds.size(); // returns true if duplicates exist
    }

}
