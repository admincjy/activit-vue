package com.wl.wlp2ploansystem.infrastructures.common.eventbus;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.context.ApplicationEvent;

import java.util.Collection;

/***
 * 实体变更事件对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EntityChangedEvent extends ApplicationEvent
{

    /***
     * 单据类型
     */
    private  String docType;

    /***
     * 单据Id
     */
    private Collection<String> docIds;

    /***
     * 变更类型
     */
    private EntityChangedType entityChangedType;

    /**
     * 重写构造函数
     * @param source 发生事件的对象
     */
    public EntityChangedEvent(Object source,String docType,Collection<String> docIds,EntityChangedType entityChangedType) {
        super(source);
        this.docType = docType;
        this.docIds = docIds;
        this.entityChangedType = entityChangedType;
    }
}
