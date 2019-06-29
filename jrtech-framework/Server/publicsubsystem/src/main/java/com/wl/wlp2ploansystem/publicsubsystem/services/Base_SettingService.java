package com.wl.wlp2ploansystem.publicsubsystem.services;

import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_Setting;

import java.util.Collection;

public interface Base_SettingService {
    Collection<Base_Setting> getAll();

    void save(Base_Setting input);
    void saveList(Collection<Base_Setting> input);
    String getStringValue(String id);

    Boolean getBooleanValue(String id);

    Base_Setting get(String id);

    Integer getIntegerValue(String id);
}
