package com.wl.wlp2ploansystem.publicsubsystem.repositories;
import com.wl.wlp2ploansystem.publicsubsystem.entities.BaseRepository;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_AttachmentCategoryOfClassification;
import org.apache.ibatis.annotations.Param;


public interface Base_AttachmentCategoryOfClassificationRepository extends BaseRepository<Base_AttachmentCategoryOfClassification> {
    void save(@Param("categoryId") String categoryId
            , @Param("categoryClassificationId") String categoryClassificationId
            , @Param("required") Boolean required);

    void deleteByCategoryClassificationId(@Param("categoryClassificationId") String categoryClassificationId);
}