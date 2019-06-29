package com.wl.wlp2ploansystem.infrastructures.common.dto;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import lombok.EqualsAndHashCode;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 通用查询数据传输对象，包括查询字段、排序字段、前端筛选列表
 * */
@EqualsAndHashCode()
public class CommonSearchInputDto  implements Validator {

    /**
     * 查询字段
     */
    @Size(max = 20)
    private String searchKey;

    /**
     * 排序字段
     */
    @Size(max = 30)
    private String sorting;

    /**
     * 前端筛选列表
     */
    private HashMap<String,ArrayList<String>> filters;

    public HashMap<String, ArrayList<String>> getFilters() {
        return filters;
    }

    public void setFilters(HashMap<String, ArrayList<String>> filters) {
        this.filters = filters;
    }


    public String getSorting() {
        return sorting;
    }

    public void setSorting(String sorting) {
        this.sorting = sorting;
    }


    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }



    @Override
    public boolean supports(Class clazz) {
        return CommonSearchInputDto.class.equals(clazz);
    }
    public SortDirection getSortDirection() {
        return SortDirections.getSortDirection(this.sorting);
    }

    public String getSortingFiled() {
      return  SortDirections.getSortingFiled(this.sorting);
    }

    @Override
    public void validate(Object obj, Errors e) {

        //排序字段输入验证
        String sortingValideResult = SortDirections.validate(this.sorting);

        if (!StringUtils.isEmpty(sortingValideResult)) {
            e.rejectValue("sorting", sortingValideResult);
        }
    }

    /**
     * 把前端筛选列表转换为数据库查询EntityWrapper
     */
    public static <T>  void resolveFilters(EntityWrapper<T> ew,HashMap<String, ArrayList<String>> filters){

        if (filters != null && filters.size() > 0) {
            filters.forEach((k, v) -> {
                String field = k;
                List<String> inValues = v;
                ew.in(field, v);

            });
        }
    }

}
