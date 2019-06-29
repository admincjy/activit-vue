
<template>
<el-dialog :title="formAction == 0 ? '新增个人关联人信息' : '修改个人关联人信息'" :close-on-click-modal="false" :visible.sync="visible" width="80%">
  <el-form :model="model" ref="ruleForm" @keyup.enter.native="dataFormSubmit()" label-width="100px">
    <el-row :gutter="20">
      <el-col :span="8">
        <el-form-item label="关联人名称" prop="name" verify :maxLength="50" class="is-required">
          <el-input v-model="model.name"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="8">
        <el-form-item label="联系电话" prop="tel" verify :maxLength="50" class="is-required">
          <el-input v-model="model.tel"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="8">
        <el-form-item label="婚姻状况" prop="personalMaritalStatusId" verify class="is-required">
          <t-dic-dropdown-select dicType="public_maritalstatus" v-model="model.personalMaritalStatusId"></t-dic-dropdown-select>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="8">
        <el-form-item label="身份证号" prop="personalCardNO" verify idcard class="is-required">
          <el-input v-model="model.personalCardNO"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="8">
        <el-form-item label="年龄" prop="personalAge" verify class="is-required">
          <t-int-input v-model="model.personalAge"></t-int-input>
        </el-form-item>
      </el-col>
      <el-col :span="8">
        <el-form-item label="性别" prop="personalSexId" verify class="is-required">
          <t-dic-dropdown-select dicType="public_sex" v-model="model.personalSexId"></t-dic-dropdown-select>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="8">
        <el-form-item label="联系住址" prop="houseAddress" verify :maxLength="200" class="is-required">
          <el-input v-model="model.houseAddress"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="8">
        <el-form-item label="学历" prop="personalEducationalLevel" verify can-be-empty>
          <t-dic-dropdown-select dicType="public_educationallevel" v-model="model.personalEducationalLevel"></t-dic-dropdown-select>
        </el-form-item>
      </el-col>
      <el-col :span="8">
        <el-form-item label="工作单位" prop="personalCompanyName" verify :maxLength="200" class="is-required">
          <el-input v-model="model.personalCompanyName"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="8">
        <el-form-item label="职务" prop="personalJobId" verify can-be-empty>
          <t-dic-dropdown-select dicType="pl_loanenter_job" v-model="model.personalJobId"></t-dic-dropdown-select>
        </el-form-item>
      </el-col>
      <el-col :span="8">
        <el-form-item label="关联关系" prop="relationCategoryId" verify class="is-required">
          <t-dic-dropdown-select dicType="pl_loanenter_customerrelation_relationcategory" v-model="model.relationCategoryId"></t-dic-dropdown-select>
        </el-form-item>
      </el-col>
      <el-col :span="8"> 
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
import util from '@/util'
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
  watch: {
    'model.personalCardNO': {　　　　
      handler(newValue, oldValue) {
        if(newValue){
          let cardNoInfo = util.parseIdCard(newValue);
          if(cardNoInfo && cardNoInfo.valid){
            this.model.personalAge = cardNoInfo.age;
            this.model.personalSexId = cardNoInfo.gender == 'M'?'public_sex_m':'public_sex_f';
            this.model.houseAddress = cardNoInfo.address;
          }
        }
      },
      deep: true,
    },
  },
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
        let model = {
          "loanDocId":loanDocId,
          "categoryId":'pl_loanenter_customerrelation_category_personal',
          "personalCardNO":''
        };
        //VUE添加到对象上的新属性不会触发更新。在这种情况下可以创建一个新的对象，让它包含原对象的属性和新的属性
        self.model = model;
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
