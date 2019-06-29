
<template>
<el-form :model="headerEntity" ref="ruleForm" label-width="160px" :disabled="disabled"> 
  <el-row :gutter="20">
    <el-col :span="8">
      <el-form-item label="借款企业名称" prop="companyName" verify :maxLength="100" class="is-required">
        <el-input v-model="headerEntity.companyName"></el-input>
      </el-form-item>
    </el-col>
    <el-col :span="8">
      <el-form-item label="注册资金(万)" prop="companyRegistrationCapital" verify class="is-required">
        <t-currentcy-input v-model="headerEntity.companyRegistrationCapital"></t-currentcy-input>
      </el-form-item>
    </el-col>
    <el-col :span="8">
      <el-form-item label="所属行业" prop="companyBusinessType" verify :maxLength="50" class="is-required">
        <el-input v-model="headerEntity.companyBusinessType"></el-input>
      </el-form-item>
    </el-col>
  </el-row>
  <el-row :gutter="20">
    <el-col :span="8">
      <el-form-item label="成立年限(年)" prop="companyBuildYears" verify class="is-required">
        <t-int-input v-model="headerEntity.companyBuildYears"></t-int-input>
      </el-form-item>
    </el-col>
    <el-col :span="8">
      <el-form-item label="联系地址" prop="companyWorkAddress" verify :maxLength="200" class="is-required">
        <el-input v-model="headerEntity.companyWorkAddress"></el-input>
      </el-form-item>
    </el-col>
    <el-col :span="8">
      <el-form-item label="所在区域" prop="companyOfArea" verify :maxLength="50" class="is-required">
        <el-input v-model="headerEntity.companyOfArea"></el-input>
      </el-form-item>
    </el-col>
  </el-row>
  <el-row :gutter="20">
    <el-col :span="8">
      <el-form-item label="股东名称" prop="stockholderNames" verify :maxLength="50" class="is-required">
        <el-input v-model="headerEntity.stockholderNames"></el-input>
      </el-form-item>
    </el-col>
    <el-col :span="8">
      <el-form-item label="关联企业" prop="affiliatedEnterprise" verify :maxLength="200" class="is-required">
        <el-input v-model="headerEntity.affiliatedEnterprise"></el-input>
      </el-form-item>
    </el-col>
    <el-col :span="8">
      <el-form-item label="员工人数(个)" prop="companyEmpoyeeCount" verify class="is-required">
        <t-int-input v-model="headerEntity.companyEmpoyeeCount"></t-int-input>
      </el-form-item>
    </el-col>
  </el-row>
  <el-row :gutter="20">
    <el-col :span="8">
      <el-form-item label="联系人" prop="emergencyContactor" verify :maxLength="50" class="is-required">
        <el-input v-model="headerEntity.emergencyContactor"></el-input>
      </el-form-item>
    </el-col>
    <el-col :span="8">
      <el-form-item label="联系电话" prop="emergencyContactorMobile" verify :maxLength="50" class="is-required">
        <el-input v-model="headerEntity.emergencyContactorMobile"></el-input>
      </el-form-item>
    </el-col>
    <el-col :span="8">
      <el-form-item label="业务来源" prop="businessSourceId" verify class="is-required">
        <t-dic-dropdown-select dicType="pl_loanenter_businesssource" v-model="headerEntity.businessSourceId"></t-dic-dropdown-select>
      </el-form-item>
    </el-col>
  </el-row>
  <el-row :gutter="20">
    <el-col>
      <h4>资产信息</h4>
      <hr class="el-row-hr" />
    </el-col>
  </el-row>
  <el-row :gutter="20">
    <el-col>
      <t-edit-grid ref="assetInformationGrid" :options="assetInformationListGridOptions" :disabled="disabled">
        <template slot="columnDataHeader">

        <el-table-column width="50">
          <template slot-scope="scope">
               <el-tooltip class="item" effect="dark" :content="scope.row.errorMessage" placement="top-start" v-if="scope.row.hasError">
                 <el-button type="danger" icon="el-icon-error" circle size="mini" style="padding:4px"></el-button>
               </el-tooltip>
             </template>
        </el-table-column>
        <el-table-column label="资产类型" width="200">
          <template slot-scope="scope">
                            <t-dic-dropdown-select dicType="pl_loanenter_assetinformation_category" v-model="scope.row.categoryId"></t-dic-dropdown-select>
                            </template>
        </el-table-column>
        <el-table-column label="所有权人" width="200">
          <template slot-scope="scope">
          <el-input v-model="scope.row.owner"></el-input>

                      </template>
        </el-table-column>
        <el-table-column label="权属号" width="200">
          <template slot-scope="scope">
                                 <el-input v-model="scope.row.code"></el-input>
                      </template>
        </el-table-column>
        <el-table-column label="价值(万)" width="200">
          <template slot-scope="scope">
                                   <t-currentcy-input placeholder="万" v-model="scope.row.value"></t-currentcy-input>
                      </template>
        </el-table-column>
        <el-table-column label="资产现状">
          <template slot-scope="scope">
                                 <el-input v-model="scope.row.nowStatus"></el-input>
                      </template>
        </el-table-column>
        <el-table-column label="购买时间">
          <template slot-scope="scope">
                <t-datetime-picker v-model="scope.row.buyDate" type="date">
                </t-datetime-picker>
                      </template>
        </el-table-column>
</template>
</t-edit-grid>
</el-col>
</el-row>
<el-row :gutter="20" style="margin-top:20px;">
  <el-col :span="12">
    <el-form-item label="住房资产总套数" prop="assetInformationHouseCount" verify :maxLength="50" class="is-required">
      <el-input v-model="headerEntity.assetInformationHouseCount"></el-input>
    </el-form-item>
  </el-col>
    <el-col :span="8">
    </el-col>
  <el-col :span="8">
  </el-col>
</el-row>
  <el-row :gutter="20">
    <el-col>
      <h4>借款意向</h4>
      <hr class="el-row-hr" />
    </el-col>
  </el-row>
  <el-row :gutter="20">
    <el-col :span="8">
      <el-form-item label="申请金额" prop="loanMoneyAmount" verify  class="is-required">
        <t-currentcy-input v-model="headerEntity.loanMoneyAmount"></t-currentcy-input>
      </el-form-item>
    </el-col>
    <el-col :span="8">
      <el-form-item label="申请期限" prop="loanTermCount" verify  class="is-required">
        <t-int-input v-model="headerEntity.loanTermCount"></t-int-input>
      </el-form-item>
    </el-col>
    <el-col :span="8">
      <el-form-item label="还款方式" prop="returnMoneyMethodId" verify class="is-required">
        <t-dic-dropdown-select dicType="pl_returnmoneymethod" v-model="headerEntity.returnMoneyMethodId"></t-dic-dropdown-select>
      </el-form-item>
    </el-col>
  </el-row>
  <el-row :gutter="20">
    <el-col :span="8">
      <el-form-item label="借款用途" prop="loanRegPurpose" verify :maxLength="50" class="is-required">
        <el-input v-model="headerEntity.loanRegPurpose"></el-input>
      </el-form-item>
    </el-col>
    <el-col :span="8">
      <el-form-item label="还款来源" prop="returnMoneySource" verify :maxLength="50" class="is-required">
        <el-input v-model="headerEntity.returnMoneySource"></el-input>
      </el-form-item>
    </el-col>
    <el-col :span="8">
      <el-form-item label="资金来源" prop="fundsSourceId" verify class="is-required">
        <t-dic-dropdown-select dicType="pl_loanenter_fundssource" v-model="headerEntity.fundsSourceId"></t-dic-dropdown-select>
      </el-form-item>
    </el-col>
  </el-row>
  <el-row :gutter="20">
    <el-col :span="8">
      <el-form-item label="放款开户行" prop="bankOfOpen" verify :maxLength="100" class="is-required">
        <el-input v-model="headerEntity.bankOfOpen"></el-input>
      </el-form-item>
    </el-col>
    <el-col :span="8">
      <el-form-item label="放款账号" prop="bankAccountNo" verify :maxLength="50" class="is-required">
        <el-input v-model="headerEntity.bankAccountNo"></el-input>
      </el-form-item>
    </el-col>
    <el-col :span="8">
      <el-form-item label="户名" prop="bankAccounName" verify :maxLength="50" class="is-required">
        <el-input v-model="headerEntity.bankAccounName"></el-input>
      </el-form-item>
    </el-col>
  </el-row>
  <el-row :gutter="20">
    <el-col>
      <h4>财务信息</h4>
      <hr class="el-row-hr" />
    </el-col>
  </el-row>
  <el-row :gutter="20">
       <el-col :span="8">
                <el-form-item label="年营业额(万)" prop="financeYearSalesVolume" verify 				  class="is-required"   >
         <t-currentcy-input v-model="headerEntity.financeYearSalesVolume"></t-currentcy-input>
        </el-form-item>        </el-col>
       <el-col :span="8">


        <el-form-item label="总资产(万)" prop="financeTotalAsset" verify   class="is-required"  >
         <t-currentcy-input v-model="headerEntity.financeTotalAsset"></t-currentcy-input>
        </el-form-item>
                                     </el-col>


   <el-col :span="8">


        <el-form-item label="资产负债率" prop="financeTotalAssetDebtRate" verify   class="is-required"  >
         <t-percent-input v-model="headerEntity.financeTotalAssetDebtRate"></t-percent-input>
        </el-form-item>
                                     </el-col>

   </el-row>
   <el-row :gutter="20">
       <el-col :span="8">

                <el-form-item label="流动比" prop="financeFlowRate" verify 				  class="is-required"   >
         <t-percent-input v-model="headerEntity.financeFlowRate"></t-percent-input>
        </el-form-item>        </el-col>
       <el-col :span="8">


        <el-form-item label="速动比" prop="financeQuickRate" verify   class="is-required"  >
         <t-percent-input v-model="headerEntity.financeQuickRate"></t-percent-input>
        </el-form-item>
                                     </el-col>


   <el-col :span="8">


        <el-form-item label="上年度纳税申请额(万)" prop="financeTaxAmount" verify   class="is-required"  >
         <t-currentcy-input v-model="headerEntity.financeTaxAmount"></t-currentcy-input>
        </el-form-item>
                                     </el-col>

   </el-row>
   <el-row :gutter="20">
       <el-col :span="8">

                <el-form-item label="利润率" prop="financeProfitRate" verify 				  class="is-required"   >
         <t-percent-input v-model="headerEntity.financeProfitRate"></t-percent-input>
        </el-form-item>        </el-col>
       <el-col :span="8">
                   </el-col>

           <el-col :span="8">
       </el-col>
   </el-row>
   <el-row :gutter="20">
     <el-col>
       <el-form-item label="近半年银行流水情况" prop="financeBankStatement" verify :maxLength="200">
         <el-input type="textarea" v-model="headerEntity.financeBankStatement" :rows="3"></el-input>
       </el-form-item>
     </el-col>
   </el-row>
</el-form>
</template>

<script>
export default {
  props: {
    entity: {
      type: Object,
      default: null,
    },
    disabled: {
      type: Boolean,
      default: false,
      required: false
    },
  },
  data() {
    return {
      headerEntity: {},
      assetInformationListGridOptions: {
        dataSource: [],
        grid: {
          defaultSort: {
            prop: 'id',
            order: 'ascending'
          },
        },
        rowRule: {
          categoryId: [{
            required: true,
            message: '请输入资产类型'
          }],
          owner: [{
            required: true,
            message: '请输入所有权人,50内个字符',
            max: 50,
          }],
          code: [{
            required: true,
            message: '请输入权属号,50内个字符',
            max: 50,
          }],
          value: [{
            required: true,
            message: '请输入价值',
          }],
          nowStatus: [{
            required: true,
            message: '请输入资产现状,100个字符内',
            max: 100,
          }],
          buyDate: [{
            required: true,
            message: '请输入购买时间',
          }],
        },
      },
    }
  },

  created() {

  },
  activated() {

  },
  computed: {

  },
  watch: {},
  methods: {
    getEntity() {
      let assetInformationData = this.$refs.assetInformationGrid.getData();
      return {
        "headerEntity": this.headerEntity,
        "assetInformationList": assetInformationData.list,
        "deletedAssetInformationIdList": assetInformationData.deletedIdList,
      };
    },
    setEntity(entity) {
      this.headerEntity = { ...entity
      };
      delete this.headerEntity.assetInformationList;
      this.assetInformationListGridOptions.dataSource = entity.assetInformationList;
    },
    saveVaild() {
      let validResult = [];
      if (this.headerEntity.companyName == null || this.headerEntity.companyName.length == 0) {
        validResult.push('请补充借款企业名称输入');
      }
      return validResult;
    },
    submitVaild() {
      let self = this;
      let valid = this.$refs.assetInformationGrid.validate();
      if (valid) {
        return self.$refs['ruleForm'].validate;
      } else {
        return new Promise(function(resolve, reject) {
          reject(false);
        });
      }
    },

  }
}
</script>
