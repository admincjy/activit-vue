
<template>
<el-dialog :title="formAction == 0 ? '新增企业关联人信息' : '修改企业关联人信息'" :close-on-click-modal="false" :visible.sync="visible" width="80%">
  <el-form :model="model" ref="ruleForm" @keyup.enter.native="dataFormSubmit()" label-width="120px">
    <el-row :gutter="20">
      <el-col :span="8">
        <el-form-item label="关联人名称" prop="name" verify  :maxLength="50" class="is-required">
          <el-input v-model="model.name"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="8">
        <el-form-item label="联系住址" prop="houseAddress" verify  :maxLength="200" class="is-required">
          <el-input v-model="model.houseAddress"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="8">
        <el-form-item label="营业执照" prop="companyBusinessLicence" verify :maxLength="50" class="is-required">
          <el-input v-model="model.companyBusinessLicence"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="8">
        <el-form-item label="注册资金(万)" prop="companyRegistrationCapital" verify class="is-required">
          <t-currentcy-input v-model="model.companyRegistrationCapital" :unit-value="10000"></t-currentcy-input>
        </el-form-item>
      </el-col>
      <el-col :span="8">
        <el-form-item label="注册时间" prop="companyRegistrationDate" verify class="is-required">
          <t-datetime-picker v-model="model.companyRegistrationDate" type="date">
          </t-datetime-picker>
        </el-form-item>
      </el-col>
      <el-col :span="8">
        <el-form-item label="所属行业" prop="companyBusinessType" verify :maxLength="50" class="is-required">
          <el-input v-model="model.companyBusinessType"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="8">
        <el-form-item label="法人姓名" prop="companyLegalPersonName" verify :maxLength="50" class="is-required">
          <el-input v-model="model.companyLegalPersonName"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="8">
        <el-form-item label="联系电话" prop="tel" verify  :maxLength="50" class="is-required">
          <el-input v-model="model.tel"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="8">
        <el-form-item label="关联关系" prop="relationCategoryId" verify class="is-required">
          <t-dic-dropdown-select dicType="pl_loanenter_customerrelation_relationcategory" v-model="model.relationCategoryId"></t-dic-dropdown-select>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col>
        <el-form-item label="股东情况" prop="companyStockholderInfo" verify :maxLength="200" class="is-required">
          <el-input type="textarea" v-model="model.companyStockholderInfo" :rows="8"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
  <span slot="footer" class="dialog-footer">
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
      <el-button @click="visible = false">取消</el-button>
    </span>
</el-dialog>
</template>

<script>
import moment from 'moment';
export default {
  data() {
    return {
      formAction: 0, //0 add,//1,edit
      visible: false,
      model: {
      },
    };
  },
  created() {
  },
  activated() {},
  methods: {
  init(loanDocId,id) {
    let self = this;
    self.visible = true;
    if (id) {
      tapp.services.pL_LoanEnterCustomerRelation.get(id).then(function(result) {
        self.$refs.ruleForm.resetFields();
        self.model = result;
        self.formAction = 1;
      });
    }else{
      self.model = {
        "loanDocId":loanDocId
      };
      self.model.categoryId = 'pl_loanenter_customerrelation_category_company';

      self.formAction = 0;
      self.$nextTick(() => {
        self.$refs.ruleForm.resetFields();
        self.formAction = 0;
      })
    }
  },
    dataFormSubmit() {
      let self = this;
      self.$refs['ruleForm'].validate((valid) => {
        if (valid) {
          let model = self.model;
          tapp.services.pL_LoanEnterCustomerRelation.save(model).then(function(result) {
            self.model = Object.assign({}, self.model, result);
            self.formAction = 1;
            self.$notify.success({
              title: '操作成功',
              message: '保存成功！',
              duration: 1500,
              onClose: () => {
                self.visible = false;
                self.$emit('change');
              }
            });
          });

        } else {
          self.$notify.error({
            title: '错误',
            message: '系统输入验证失败！'
          });
          return false;
        }
      });
    },

  }
  }
  </script>
