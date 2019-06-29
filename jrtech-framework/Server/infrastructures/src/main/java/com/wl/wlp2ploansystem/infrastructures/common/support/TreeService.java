package com.wl.wlp2ploansystem.infrastructures.common.support;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 树形对象处理服务
 */
public class TreeService<T extends ITreeObject> {

    private Collection<T> _inputList;
    private  Collection<TreeObject<T>> treeRootList = new ArrayList<>();

    /**
     * 转换后的树形结构
     */
    public Collection<TreeObject<T>> getTreeRootList() {
    	 return treeRootList;
     }
    public void setTreeRootList( Collection<TreeObject<T>> treeRootList) {
        this.treeRootList = treeRootList;
    }
    public TreeService()
    {
    }
    /**
     * 构造函数
     * @param inputList  要转换为树形的记录列表,根结点为null
     */
    public TreeService(Collection<T> inputList)
    {
        _inputList = inputList;

        this.buildTree(null,0,".",false);
    }

    /**
     * 构造函数
     * @param inputList  要转换为树形的记录列表
     * @param rootIds  根结点编码
     */
    public TreeService(Collection<T> inputList, Collection<String> rootIds,boolean containRoot) {
        _inputList = inputList;
        rootIds.forEach(p->{
            this.buildTree(p,0,".",containRoot);
        });

    }
    /**
     * 构建树
     * @param inputTreeList 树结构
     */
    public static<T extends ITreeObject> TreeService<T> of(Collection<TreeObject<T>> inputTreeList)  {
        TreeService<T> treeService = new TreeService<T>();
        treeService.setTreeRootList(inputTreeList);

        return  treeService;
    }

    /**
     * 遍历树控件
     * @param action 遍历时执行的函数
     */
    public void retriveTree(Consumer<TreeObject<T>> action) {
        for (TreeObject<T> cNode : treeRootList)
        {
            action.accept(cNode);

            retriveAllChildnodes(cNode, action);
        }
    }
    /***
     * 获取树的叶子节点列表
     * @return 叶子节点列表
     */
    public List<TreeObject<T>> getLeafNodes() {
        List<TreeObject<T>> leafNodes = new ArrayList<TreeObject<T>>();

        for (TreeObject<T> cNode : treeRootList)
        {
            if (cNode.getItems().size() == 0)
            {
                leafNodes.add(cNode);
            }

            retriveAllChildnodes(cNode, p-> {
                if (p.getItems().size() == 0) {
                    leafNodes.add(p);
                }

            });
        }
        return leafNodes;
    }

    private void retriveAllChildnodes(TreeObject<T> node, Consumer<TreeObject<T>> action) {
        if (node.getItems().size() == 0)
        {
            return;
        }
        for (TreeObject<T> cNode : node.getItems())
        {
            action.accept(cNode);

            retriveAllChildnodes(cNode, action);
        }
    }
    private void buildTree(String rootId,int level,String path,boolean containRoot) {
        int newLevel = level + 1;
        List<T> childEntityList;
        if (StringUtils.isEmpty(rootId)) {
            childEntityList = _inputList.stream().filter(p -> { return StringUtils.isEmpty(p.getParentId()); }).collect(Collectors.toList());
        }
        else
        {
            if(containRoot){
                childEntityList = _inputList.stream().filter(p -> {
                    return  p.getId().equals(rootId);
                }).collect(Collectors.toList());
            }
            else {

                childEntityList = _inputList.stream().filter(p -> {
                    return !StringUtils.isEmpty(p.getParentId()) && p.getParentId().equals(rootId);
                }).collect(Collectors.toList());
            }
        }

        childEntityList.stream().forEach(p->
        {
            TreeObject<T> t = new TreeObject<T>();
            t.setParentId(rootId);
            t.setParent(null);
            t.setLevel(newLevel);
            t.setId(p.getId());
            t.setName(p.getName());
            t.setPath(path+";" + t.getName());
            t.getIdPath().add(t.getId());
            treeRootList.add(t);
            t.setSelf(p);

            this.convertEntityToTreeStruct(t,t.getPath());
        });

    }
    private void convertEntityToTreeStruct(TreeObject<T> node,String path) {
         List<T> childEntityList = _inputList.stream().filter(p -> { return !StringUtils.isEmpty(p.getParentId()) && p.getParentId().equals(node.getSelf().getId()); })
        		 .collect(Collectors.toList());

         if (childEntityList.size() == 0)
         {
             return;
         }

         int newLevel = node.getLevel() + 1;
         childEntityList.stream().forEach(p ->
         {
             List<String> idPath = CommonHelper.deepCopyList(node.getIdPath());
             idPath.add(p.getId());

        	 TreeObject<T> t = new TreeObject<T>();
             t.setId(p.getId());
             t.setParentId(node.getId());
             t.setParent(node);
             t.setName(p.getName());
             t.setLevel(newLevel);
             t.setPath(path+";" + t.getName());
             t.setIdPath(idPath);
        	 t.setSelf(p);

        	 node.getItems().add(t);

             this.convertEntityToTreeStruct(t,t.getPath());
         });
     }
 }
