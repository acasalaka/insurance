package apap.tk.insurance2206823682.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import apap.tk.insurance2206823682.model.Company;
import apap.tk.insurance2206823682.model.Coverage;
import apap.tk.insurance2206823682.repository.CoverageDb;
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
    public Coverage getById(Long id){
        return coverageDb.findById(id).orElse(null);
    }


}
