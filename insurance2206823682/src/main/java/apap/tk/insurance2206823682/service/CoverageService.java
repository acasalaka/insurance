package apap.tk.insurance2206823682.service;

import apap.tk.insurance2206823682.model.Coverage;

public interface CoverageService {
    void initData();
    long countCoverages();
    Coverage getById(Long id);
}
