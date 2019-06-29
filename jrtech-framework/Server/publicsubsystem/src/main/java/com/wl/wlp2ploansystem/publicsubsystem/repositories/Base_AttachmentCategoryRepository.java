package com.wl.wlp2ploansystem.publicsubsystem.repositories;

import com.wl.wlp2ploansystem.publicsubsystem.entities.BaseRepository;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_AttachmentCategory;

import java.util.Collection;

public interface Base_AttachmentCategoryRepository  extends BaseRepository<Base_AttachmentCategory> {
    Collection<Base_AttachmentCategory> getAllCategoryies();
}

