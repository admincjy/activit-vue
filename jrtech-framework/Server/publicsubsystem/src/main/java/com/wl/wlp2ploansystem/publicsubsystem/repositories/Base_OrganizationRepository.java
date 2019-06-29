package com.wl.wlp2ploansystem.publicsubsystem.repositories;

import com.wl.wlp2ploansystem.publicsubsystem.entities.BaseRepository;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_Organization;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_User;

import java.util.Collection;

public interface Base_OrganizationRepository extends BaseRepository<Base_Organization> {

    Collection<Base_Organization> getAll();
}
