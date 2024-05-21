package ldpd.suso.service;

import ldpd.suso.entity.Result;
import ldpd.suso.repository.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResultService {
    @Autowired
    private ResultRepository resultRepository;

    public void save(Result result) {
        resultRepository.save(result);
    }
}