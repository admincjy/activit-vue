package com.wl.wlp2ploansystem.publicsubsystem.services;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.wl.wlp2ploansystem.infrastructures.common.dto.TreeNodeDropInputDto;
import com.wl.wlp2ploansystem.infrastructures.common.support.TreeObject;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_Permission;

import java.util.Collection;
import java.util.List;

public interface Base_PermissionService {
    Collection<Base_Permission> getAll();
    Collection<TreeObject<Base_Permission>> getPermissions();

    /***
     * 更新父节点和序号
     * @param inputDto 输入Dto
     */
    void updateParentIdAndSortIndex(TreeNodeDropInputDto inputDto);

    /**
     * <p>
     * 根据单据id ，查询一条记录
     * </p>
     *
     * @param id 单据id
     * @return 实体
     */
    Base_Permission get(String id);

    /**
     * <p>
     * 根据 ew 条件，查询总记录数
     * </p>
     *
     * @param ew 实体对象
     * @return 满足条件记录数
     */
    Integer count(EntityWrapper<Base_Permission> ew);

    /**
     * 是否存在记录
     * @param id 单据id
     * @return true:存在 false:不存在
     */
    boolean exists(String id);

    /**
     * <p>
     * 插入一条记录
     * </p>
     *
     * @param entity 实体对象
     * @return 单据主键
     */
    String insert(Base_Permission entity);

    /**
     * <p>
     * 批量插入记录
     * </p>
     *
     */
    void insertList(List<Base_Permission> list);

    /**
     * <p>
     * 更新一条记录
     * </p>
     *
     * @param input 实体对象
     */
    void update(Base_Permission input);

    /**
     * <p>
     * 插入或更新一条记录(id:null 插入；id:not null 更新)
     * </p>
     *
     * @param input 实体对象
     * @return 单据主键
     */
    String  save(Base_Permission input);

    /**
     * <p>
     * 根据单据id ，删除一条记录
     * </p>
     *
     * @param id 单据id
     */
    Integer delete(String id);
    /**
     * <p>
     * 根据单据id集合 ，批量删除记录
     * </p>
     *
     * @param ids 单据id集合
     */
    Integer batchDelete(List<String> ids);
}
