package com.wl.wlp2ploansystem.publicsubsystem.services;

import java.util.List;

public interface Base_SequenceNumberService extends BaseService {
    List<String> getSequenceNumberList(String key, Integer count);
    String getSequenceNumber(String key);
}
