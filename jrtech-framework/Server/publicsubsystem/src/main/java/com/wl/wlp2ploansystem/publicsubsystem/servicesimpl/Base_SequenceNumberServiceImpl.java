package com.wl.wlp2ploansystem.publicsubsystem.servicesimpl;

import com.wl.wlp2ploansystem.publicsubsystem.repositories.Base_SequenceNumberRepository;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_SequenceNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Base_SequenceNumberServiceImpl extends BaseServiceImpl implements Base_SequenceNumberService {

    @Autowired
    private Base_SequenceNumberRepository repository;

    @Override
    public List<String> getSequenceNumberList(String key, Integer count) {
        //return Arrays.asList(IdGenerator.get());
        return  repository.getSequenceNumbers(key,count);
    }

    @Override
    public String getSequenceNumber(String key) {
        List<String> list = this.getSequenceNumberList(key,1);

        return list.get(0);
    }
}
