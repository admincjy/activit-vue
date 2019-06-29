package com.wl.wlp2ploansystem.publicsubsystem.servicesimpl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.wl.wlp2ploansystem.infrastructures.common.dto.TreeNodeDropInputDto;
import com.wl.wlp2ploansystem.infrastructures.common.errors.OptimisticConcurrencyException;
import com.wl.wlp2ploansystem.infrastructures.common.support.IdGenerator;
import com.wl.wlp2ploansystem.infrastructures.common.support.TreeObject;
import com.wl.wlp2ploansystem.infrastructures.common.support.TreeService;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_Navigation;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_Permission;
import com.wl.wlp2ploansystem.publicsubsystem.repositories.Base_PermissionRepository;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;

@Service
public class Base_PermissionServiceImpl  extends BaseServiceImpl implements Base_PermissionService {
    @Autowired
    private Base_PermissionRepository repository;

    @Override
    public Collection<Base_Permission> getAll() {

        EntityWrapper<Base_Permission> entityWrapper = new EntityWrapper<>();
        entityWrapper.orderBy("sortIndex",true);

        return repository.selectList(entityWrapper);
    }

    @Override
    public Collection<TreeObject<Base_Permission>> getPermissions() {
        Collection<Base_Permission> all = this.getAll();

        TreeService<Base_Permission> treeService = new TreeService<Base_Permission>(all);
        Collection<TreeObject<Base_Permission>> treeAll = treeService.getTreeRootList();

        return treeAll;
    }

    /***
     * 更新父节点和序号
     * @param inputDto 输入Dto
     */
    @Override
    @Transactional
    public void updateParentIdAndSortIndex(TreeNodeDropInputDto inputDto){

        Base_Permission entity = repository.selectById(inputDto.getId());
        entity.setParentId(inputDto.getNewParentId());
        entity.setSortIndex(inputDto.getNewSortIndex());

        repository.updateAllColumnById(entity);
    }

    /**
     * <p>
     * 根据单据id ，查询一条记录
     * </p>
     *
     * @param id 单据id
     * @return 实体
     */
    @Override
    @Transactional(readOnly = true)
    public Base_Permission get(String id) {
        Base_Permission entity = repository.selectById(id);


        return entity;
    }

    /**
     * <p>
     * 根据 ew 条件，查询总记录数
     * </p>
     *
     * @param ew 实体对象
     * @return 满足条件记录数
     */
    @Override
    @Transactional(readOnly = true)
    public Integer count(EntityWrapper<Base_Permission> ew) {
        return repository.selectCount(ew);
    }

    /**
     * 是否存在记录
     * @param id 单据id
     * @return true:存在 false:不存在
     */
    @Override
    @Transactional(readOnly = true)
    public boolean exists(String id){
        EntityWrapper<Base_Permission> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("id",id);

        return  this.count(entityWrapper)>0;
    }
    /**
     * <p>
     * 插入一条记录
     * </p>
     * @param input 实体对象
     * @return 单据id
     */
    @Override
    @Transactional
    public String insert(Base_Permission input) {
        if (StringUtils.isEmpty(input.getId())) {
            input.setId(IdGenerator.get());
        }
        repository.insert(input);
        return input.getId();
    }

    /**
     * <p>
     * 批量插入记录
     * </p>
     *
     */
    @Override
    @Transactional
    public void insertList(List<Base_Permission> list) {
        list.forEach(p -> {
            insert(p);
        });
    }

    /**
     * <p>
     * 更新一条记录
     * </p>
     * @param input 实体对象
     * @return 单据id
     */
    @Override
    @Transactional
    public void update(Base_Permission input) {

        Integer effectRecords = repository.updateAllColumnById(input);

        if (effectRecords == 0) {
            throw new OptimisticConcurrencyException();
        }
    }

    /**
     * <p>
     * 插入或更新一条记录(id:null 插入；id:not null 更新)
     * </p>
     *
     * @param input 实体对象
     * @return 单据id
     */
    @Override
    @Transactional
    public String save(Base_Permission input) {
        if (StringUtils.isEmpty(input.getId()) || !this.exists(input.getId())) {
            String id = this.insert(input);
            input.setId(id);
        } else {
            this.update(input);
        }
        return input.getId();
    }

    /**
     * <p>
     * 根据单据id ，删除一条记录
     * </p>
     *
     * @param id 单据id
     */
    @Override
    @Transactional
    public Integer delete(String id) {
        return repository.deleteById(id);
    }

    /**
     * <p>
     * 根据单据id集合 ，批量删除记录
     * </p>
     *
     * @param ids 单据id集合
     */
    @Override
    @Transactional
    public Integer batchDelete(List<String> ids) {

        return repository.deleteBatchIds(ids);
    }

}
