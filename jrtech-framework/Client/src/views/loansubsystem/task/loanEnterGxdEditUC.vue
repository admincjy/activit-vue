
<template>
<el-form :model="headerEntity" ref="ruleForm" label-width="160px" :disabled="disabled">
  <el-row :gutter="20">
    <el-col :span="8">
      <el-form-item label="客户名称" prop="customerName" verify :maxLength="50" class="is-required">
        <el-input v-model="headerEntity.customerName"></el-input>
      </el-form-item>
    </el-col>
    <el-col :span="8">
      <el-form-item label="身份证号" prop="customerCardNO" verify idcard class="is-required">
        <el-input v-model="headerEntity.customerCardNO"></el-input>
      </el-form-item>
    </el-col>
    <el-col :span="8">
      <el-form-item label="年龄" prop="age" verify class="is-required">
        <t-int-input v-model="headerEntity.age"></t-int-input>
      </el-form-item>
    </el-col>
  </el-row>
  <el-row :gutter="20">
    <el-col :span="8">
      <el-form-item label="联系方式" prop="mobile" verify :maxLength="40" class="is-required">
        <el-input v-model="headerEntity.mobile"></el-input>
      </el-form-item>
    </el-col>
    <el-col :span="8">
      <el-form-item label="性别" prop="sexId" verify class="is-required">
        <t-dic-dropdown-select dicType="public_sex" v-model="headerEntity.sexId"></t-dic-dropdown-select>
      </el-form-item>
    </el-col>
    <el-col :span="8">
      <el-form-item label="婚姻状况" prop="maritalStatusId" verify class="is-required">
        <t-dic-dropdown-select dicType="public_maritalstatus" v-model="headerEntity.maritalStatusId"></t-dic-dropdown-select>
      </el-form-item>
    </el-col>
  </el-row>
  <el-row :gutter="20">
    <el-col :span="8">
      <el-form-item label="学历" prop="educationalLevel" verify class="is-required">
        <t-dic-dropdown-select dicType="public_educationallevel" v-model="headerEntity.educationalLevel"></t-dic-dropdown-select>
      </el-form-item>
    </el-col>
    <el-col :span="8">
      <el-form-item label="工作单位" prop="companyName" verify can-be-empty :maxLength="200">
        <el-input v-model="headerEntity.companyName"></el-input>
      </el-form-item>
    </el-col>
    <el-col :span="8">
      <el-form-item label="职务" prop="jobId" verify class="is-required">
        <t-dic-dropdown-select dicType="pl_loanenter_job" v-model="headerEntity.jobId"></t-dic-dropdown-select>
      </el-form-item>
    </el-col>
  </el-row>
  <el-row :gutter="20">
    <el-col :span="8">
      <el-form-item label="从业年限" prop="companyWorkTerm" verify :maxLength="50" class="is-required">
        <el-input v-model="headerEntity.companyWorkTerm"></el-input>
      </el-form-item>
    </el-col>
    <el-col :span="8">
      <el-form-item label="社保编号" prop="socialSecurityCode" verify can-be-empty :maxLength="50">
        <el-input v-model="headerEntity.socialSecurityCode"></el-input>
      </el-form-item>
    </el-col>
    <el-col :span="8">
      <el-form-item label="居住情况" prop="residentialStatusId" verify class="is-required">
        <t-dic-dropdown-select dicType="pl_loanenter_residentialstatus" v-model="headerEntity.residentialStatusId"></t-dic-dropdown-select>
      </el-form-item>
    </el-col>
  </el-row>
  <el-row :gutter="20">
    <el-col :span="8">
      <el-form-item label="联系地址" prop="residentialAddress" verify :maxLength="100" class="is-required">
        <el-input v-model="headerEntity.residentialAddress"></el-input>
      </el-form-item>
    </el-col>
    <el-col :span="8">
      <el-form-item label="户籍地址" prop="placeOfHouseholdRegistration" verify :maxLength="100" class="is-required">
        <el-input v-model="headerEntity.placeOfHouseholdRegistration"></el-input>
      </el-form-item>
    </el-col>
    <el-col :span="8">
      <el-form-item label="业务来源" prop="businessSourceId" verify class="is-required">
        <t-dic-dropdown-select dicType="pl_loanenter_businesssource" v-model="headerEntity.businessSourceId"></t-dic-dropdown-select>
      </el-form-item>
    </el-col>
  </el-row>
  <el-row :gutter="20">
    <el-col :span="8">
      <el-form-item label="紧急联系人" prop="emergencyContactor" verify :maxLength="50" class="is-required">
        <el-input v-model="headerEntity.emergencyContactor"></el-input>
      </el-form-item>
    </el-col>
    <el-col :span="8">
      <el-form-item label="紧急联系人关系" prop="emergencyContactorRelation" verify :maxLength="50" class="is-required">
        <el-input v-model="headerEntity.emergencyContactorRelation"></el-input>
      </el-form-item>
    </el-col>
    <el-col :span="8">
      <el-form-item label="紧急联系人关系电话" prop="emergencyContactorMobile" verify :maxLength="50" class="is-required">
        <el-input v-model="headerEntity.emergencyContactorMobile"></el-input>
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

        <el-table-column width="50" class-name="is-required">
          <template slot-scope="scope">
               <el-tooltip class="item" effect="dark" :content="scope.row.errorMessage" placement="top-start" v-if="scope.row.hasError">
                 <el-button type="danger" icon="el-icon-error" circle size="mini" style="padding:4px"></el-button>
               </el-tooltip>
             </template>
        </el-table-column>
        <el-table-column label="资产类型" width="120" class-name="is-required">
          <template slot-scope="scope">
                            <t-dic-dropdown-select dicType="pl_loanenter_assetinformation_category" v-model="scope.row.categoryId"></t-dic-dropdown-select>
                            </template>
        </el-table-column>
        <el-table-column label="所有权人" width="100" class-name="is-required">
          <template slot-scope="scope">
          <el-input v-model="scope.row.owner"></el-input>

                      </template>
        </el-table-column>
        <el-table-column label="权属号" width="200" class-name="is-required">
          <template slot-scope="scope">
                                 <el-input v-model="scope.row.code"></el-input>
                      </template>
        </el-table-column>
        <el-table-column label="价值(万)" width="150" class-name="is-required">
          <template slot-scope="scope">
                                   <t-currentcy-input v-model="scope.row.value" :unit-value="10000"></t-currentcy-input>
                      </template>
        </el-table-column>
        <el-table-column label="资产现状" width="150">
          <template slot-scope="scope">
                                 <el-input v-model="scope.row.nowStatus"></el-input>
                      </template>
        </el-table-column>
        <el-table-column label="购买时间" min-width="160">
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
      <el-form-item label="家庭月收入(元)" prop="financeFamilyMonthIncome" verify class="is-required">
        <t-currentcy-input v-model="headerEntity.financeFamilyMonthIncome"></t-currentcy-input>
      </el-form-item>
    </el-col>
    <el-col :span="8">
      <el-form-item label="家庭月支出(元)" prop="financeFamilyMonthPay" verify class="is-required">
        <t-currentcy-input v-model="headerEntity.financeFamilyMonthPay"></t-currentcy-input>
      </el-form-item>
    </el-col>
    <el-col :span="8">
      <el-form-item label="家庭总资产(万)" prop="financeFamilyTotalAsset" verify class="is-required">
        <t-currentcy-input v-model="headerEntity.financeFamilyTotalAsset" :unit-value="10000"></t-currentcy-input>
      </el-form-item>
    </el-col>
  </el-row>
  <el-row :gutter="20">
    <el-col :span="8">
      <el-form-item label="房产套数" prop="financeHouseCount" verify class="is-required">
        <t-int-input v-model="headerEntity.financeHouseCount"></t-int-input>
      </el-form-item>
    </el-col>
    <el-col :span="8">
      <el-form-item label="还款负担率" prop="financeReturnMoneyLoanRate" verify class="is-required">
        <t-percent-input v-model="headerEntity.financeReturnMoneyLoanRate"></t-percent-input>
      </el-form-item>
    </el-col>
    <el-col :span="8">
      <el-form-item label="资产负债率" prop="financeAssetDebtRate" verify class="is-required">
        <t-percent-input v-model="headerEntity.financeAssetDebtRate"></t-percent-input>
      </el-form-item>
    </el-col>
  </el-row>

</el-form>
</template>

<script>
import util from '@/util'
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
          categoryId: [
          {
            required: true,
            message: '请输入资产类型'
          }],
          owner: [
          {
            required: true,
            message: '请输入所有权人,50内个字符',
            max: 50,
          }],
          code: [
          {
            required: true,
            message: '请输入权属号,50内个字符',
            max: 50,
          }],
          value: [
          {
            required: true,
            message: '请输入价值',
          }],
          nowStatus: [
          {
            required: true,
            message: '请输入资产现状,100个字符内',
            max: 100,
          }],
          buyDate: [
          {
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
  watch: {
    'headerEntity.customerCardNO': {　　　　
      handler(newValue, oldValue) {　
        if(newValue){
          let cardNoInfo = util.parseIdCard(newValue);
          if(cardNoInfo && cardNoInfo.valid){
            this.headerEntity.age = cardNoInfo.age;
            this.headerEntity.sexId = cardNoInfo.gender == 'M'?'public_sex_m':'public_sex_f';
            this.headerEntity.placeOfHouseholdRegistration = cardNoInfo.address;
          }
        }

      },
      deep: true,
    },
  },
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
      if (this.headerEntity.customerName == null || this.headerEntity.customerName.length == 0) {
        validResult.push('请补充客户名称输入');
      }
      if (this.headerEntity.customerCardNO == null || this.headerEntity.customerCardNO.length == 0) {
        validResult.push('请补充身份证号输入');
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
