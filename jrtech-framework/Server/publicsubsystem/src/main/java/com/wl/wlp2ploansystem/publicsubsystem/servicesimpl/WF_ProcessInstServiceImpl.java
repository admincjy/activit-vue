package com.wl.wlp2ploansystem.publicsubsystem.servicesimpl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.wl.wlp2ploansystem.infrastructures.common.errors.OptimisticConcurrencyException;
import com.wl.wlp2ploansystem.infrastructures.common.errors.UserFriendlyException;
import com.wl.wlp2ploansystem.infrastructures.common.support.IdGenerator;
import com.wl.wlp2ploansystem.infrastructures.workflow.enums.EProcessInstStatus;
import com.wl.wlp2ploansystem.infrastructures.workflow.service.TaskPersistService;
import com.wl.wlp2ploansystem.infrastructures.workflow.vo.WF_AgentUserInfo;
import com.wl.wlp2ploansystem.infrastructures.workflow.vo.WF_BusinessKey;
import com.wl.wlp2ploansystem.publicsubsystem.entities.*;
import com.wl.wlp2ploansystem.publicsubsystem.repositories.WF_ProcessInstRepository;
import com.wl.wlp2ploansystem.publicsubsystem.repositories.WF_ProcessinstFullRepository;
import com.wl.wlp2ploansystem.publicsubsystem.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("wf_ProcessInstService")
public class WF_ProcessInstServiceImpl extends BaseServiceImpl implements WF_ProcessInstService  ,TaskPersistService {

    @Autowired
    private WF_ProcessInstRepository repository;

    @Autowired
    private WF_ProcessinstFullRepository fullRepository;

    @Autowired
    private WF_ProcessInstHistoryService historyService;

    @Autowired
    private Base_NotificationService notificationService;
    @Autowired
    private Base_RoleService roleService;

    @Autowired
    private Base_UserService userService;

    @Autowired
    private Base_OrganizationService organizationService;

    @Autowired
    private Base_DataDictionaryService dictionaryService;

    @Autowired
    private WF_TaskAgentService taskAgentService;

    @Override
    @Transactional
    public void createProcessInst(String instId,String businessId,String processDefination, String origiator,LocalDateTime saveDate, String folio, String remark) {
        WF_ProcessInst entity = new WF_ProcessInst();
        entity.setId(instId);
        entity.setProcessDefinationKey(processDefination);
        entity.setBusinessId(businessId);
        entity.setOrigiator(origiator);
        entity.setSaveDate(saveDate);
        entity.setStartDate(LocalDateTime.now());
        entity.setFolio(folio);
        entity.setStatus(EProcessInstStatus.Approving.name());
        entity.setRemark(remark);
        repository.insert(entity);

    }

    /**
     * 获取岗位的所属用户Id，getUserMode：all:所有用户：getUserMode：avg:当前审批任务最少的岗位用户
     * @param processDefinationKey 流程定义key，可空
     * @param actId 流程结点，可空
     * @param roleId 岗位
     * @param getUserMode 获取用户方式:all:所有、avg:当前审批任务最少的岗位用户
     * @return 用户Id列表
     */
    @Override
    public List<String> getRoleUsers(String processDefinationKey,String actId,String roleId, String getUserMode) {

        List<String> users = roleService.getRoleUserList(roleId).stream().map(p->p.getId()).distinct().collect(Collectors.toList());
        if(users == null || users.size() == 0){
            Base_Role roleEntity = roleService.get(roleId);

            String roleName = roleEntity == null?roleId:roleEntity.getName() + "(" + roleId + ")";
            throw  new UserFriendlyException("岗位"+roleName+"用户为空,参数无效！");
        }

        if("all".equals(getUserMode)){
            return  users;
        }
        else if("avg".equals(getUserMode)) {
            List<keyIntegerValuePair> taskActUserCount = repository.getTaskActUserCount(processDefinationKey, actId);

            List<keyIntegerValuePair> userTaskCountDic = new ArrayList<>();

            users.forEach(p->{
                KeyValuePair<String,Integer> foundTaskUser =  taskActUserCount.stream().filter(sp->p.equals(sp.getKey())).findFirst().orElse(null);
                if(foundTaskUser !=null ){
                    userTaskCountDic.add(new keyIntegerValuePair(foundTaskUser.getKey(),foundTaskUser.getValue()));
                }else {
                    userTaskCountDic.add(new keyIntegerValuePair(p,0));
                }
            });

            String foundUser =  userTaskCountDic.stream().sorted(Comparator.comparing(keyIntegerValuePair::getValue).thenComparing(keyIntegerValuePair::getKey)).map(p->p.getKey())
                    .findFirst().orElse(null);

            List<String> foundUsers  =  new ArrayList<String>();
            foundUsers.add(foundUser);
            return  foundUsers;

        }
        else{
            throw  new UserFriendlyException("getUserMode 参数无效！");
        }
    }

    /**
     * 获取部门下特定岗位类别的所属用户Id，getUserMode：all:所有用户：getUserMode：avg:当前审批任务最少的岗位用户
     * @param processDefinationKey 流程定义key，可空
     * @param actId 流程结点，可空
     * @param departmentId 部门Id
     * @param roleCategoryId 岗位类别
     * @param getUserMode 获取用户方式:all:所有、avg:当前审批任务最少的岗位用户
     * @return 用户Id列表
     */
    @Override
    public List<String> getRoleCategoryUsers(String processDefinationKey, String actId, String departmentId,String roleCategoryId, String getUserMode) {


        List<String> users = roleService.getRoleCategoryUsers(departmentId,roleCategoryId).stream().map(p->p.getId()).distinct().collect(Collectors.toList());
        if(users == null || users.size() == 0){

            Base_Organization organizationEntity = organizationService.get(departmentId);
            String departmentName = organizationEntity == null?departmentId:organizationEntity.getName() + "(" + departmentId + ")";

            Base_DataDictionary roleCategoryEntity = dictionaryService.getDataDictionay(roleCategoryId);
            String roleCategoryName = roleCategoryEntity == null?roleCategoryId:roleCategoryEntity.getName() + "(" + roleCategoryId + ")";

            throw  new UserFriendlyException("营业部:"+departmentName+"岗位类别:"+roleCategoryName+"用户为空,参数无效！");
        }

        if("all".equals(getUserMode)){
            return  users;
        }
        else if("avg".equals(getUserMode)) {
            List<keyIntegerValuePair> taskActUserCount = repository.getTaskActUserCount(processDefinationKey, actId);

            List<keyIntegerValuePair> userTaskCountDic = new ArrayList<>();

            users.forEach(p->{
                KeyValuePair<String,Integer> foundTaskUser =  taskActUserCount.stream().filter(sp->p.equals(sp.getKey())).findFirst().orElse(null);
                if(foundTaskUser !=null ){
                    userTaskCountDic.add(new keyIntegerValuePair(foundTaskUser.getKey(),foundTaskUser.getValue()));
                }else {
                    userTaskCountDic.add(new keyIntegerValuePair(p,0));
                }
            });

            String foundUser =  userTaskCountDic.stream().sorted(Comparator.comparing(keyIntegerValuePair::getValue).thenComparing(keyIntegerValuePair::getKey)).map(p->p.getKey())
                    .findFirst().orElse(null);

            List<String> foundUsers  =  new ArrayList<String>();
            foundUsers.add(foundUser);
            return  foundUsers;

        }
        else{
            throw  new UserFriendlyException("getUserMode 参数无效！");
        }
    }

    @Override
    @Transactional
    public void afterCreateTask(String id, String procInstId, String actId, String actName, String actUserId, String formUrl, String
            status, WF_BusinessKey businessKey, Map<String, Object> variables) {
        String taskFormUrl =  MessageFormat.format("{0}?taskId={1}&id={2}&procInstId={3}",
                formUrl,
                id,
                businessKey.getId(),
                procInstId);

        String folio = businessKey.getFolio();

        String title = "您有一个待办任务需" + status +"。评审内容:" +folio;
        String content = "您有一个待办任务需" + status +"。评审内容:" +folio;

        String targetTos = "";
        if(!actUserId.startsWith(",")){
            targetTos = "," + actUserId;
        }
        if(!actUserId.endsWith(",")){
            targetTos = actUserId + ",";
        }
        Base_Notification notification = new Base_Notification();
        notification.setSource(id);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setUrl(taskFormUrl);
        notification.setTargetType("");
        notification.setTargetTos(targetTos);
        notificationService.saveAndNotify(notification);
    }
    @Override
    @Transactional
    public void createProcessInstHistory(String taskId, String processInstId, String actId, String actName, String taskUserSource, String taskUserId, String doAction, String taskResult, String taskRemark, LocalDateTime actionDate)
    {
        WF_ProcessInstHistory historyEntity = new WF_ProcessInstHistory();
        historyEntity.setId(IdGenerator.get());
        historyEntity.setTaskId(taskId);
        historyEntity.setProcessInstId(processInstId);
        historyEntity.setActId(actId);
        historyEntity.setActName(actName);
        historyEntity.setActUserSource(taskUserSource);
        historyEntity.setActUserId(taskUserId);
        historyEntity.setDoAction(doAction);
        historyEntity.setResult(taskResult);
        historyEntity.setActionDate(actionDate);
        historyEntity.setRemark(taskRemark);

        historyService.insert(historyEntity);

        //任务处理后自动把待办通知标记为已读
        notificationService.readBySource(taskId);
    }

    @Override
    @Transactional
    public void setProcessInstStatus(String instId, String status)
    {
        WF_ProcessInst procInstObject = repository.selectById(instId);
        procInstObject.setId(instId);
        procInstObject.setOriginalStatus(procInstObject.getStatus());
        procInstObject.setStatus(status);
        Integer effectRecords= repository.updateAllColumnById(procInstObject);

        if(effectRecords == 0){
            throw new OptimisticConcurrencyException();
        }

    }
    @Override
    @Transactional
    public void normalEndProcess(String instId,String status)
    {
        WF_ProcessInst procInstObject = repository.selectById(instId);
        procInstObject.setId(instId);
        procInstObject.setStatus(status);
        procInstObject.setEndDate(LocalDateTime.now());

        Integer effectRecords= repository.updateAllColumnById(procInstObject);

        if(effectRecords == 0){
            throw new OptimisticConcurrencyException();
        }
    }


    @Transactional
    public  void delete(String id){
        repository.deleteById(id);
    }


    /**
     * <p>
     * 根据 entity 条件，查询全部记录（并翻页）
     * </p>
     *
     * @param page 分页查询条件（可以为 RowBounds.DEFAULT）
     * @param ew 实体对象封装操作类（可以为 null）
     * @return 实体分页对象
     */
    @Override
    @Transactional(readOnly = true)
    public Page<WF_ProcessInstFull> getPagedList(Page<WF_ProcessInstFull> page, EntityWrapper<WF_ProcessInstFull> ew) {
        return page.setRecords(fullRepository.selectPage(page, ew));
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
    public WF_ProcessInst get(String id) {
        WF_ProcessInst entity = repository.selectById(id);


        return entity;
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

    /***
     *获取用户特定流程可用的授权用户，可能为空
     * @param processDefinitionKey 流程定义
     * @param assignee 被授权人
     */
    @Override
    @Transactional(readOnly = true)
    public WF_AgentUserInfo getAssigneeEnabledAgents(String  processDefinitionKey, String assignee) {

        return taskAgentService.getAssigneeEnabledAgents(processDefinitionKey,assignee);
    }
}

