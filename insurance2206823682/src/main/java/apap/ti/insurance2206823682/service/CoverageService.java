package apap.ti.insurance2206823682.service;

import java.util.List;

import apap.ti.insurance2206823682.model.Coverage;

public interface CoverageService {
    void initData();
    long countCoverages();
    List<Coverage> getAllCoverage();
    boolean hasDuplicateCoverages(List<Coverage> listCoverage);

}
