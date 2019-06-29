
<template>
<el-dialog :title="formAction == 0 ? '新增产品类型' : '修改产品类型'" :visible.sync="visible" :append-to-body="true">
  <el-form :model="headerEntity" ref="ruleForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="名称" prop="name" verify :maxLength="50" class="is-required">
      <el-input v-model="headerEntity.name"></el-input>
    </el-form-item>
    <el-form-item label="备注" prop="remark" verify can-be-empty :maxLength="255">
      <el-input type="textarea" v-model="headerEntity.remark"></el-input>
    </el-form-item>
  </el-form>
  <div>
    <h5>产品名称</h5>
    <hr/>
    <el-table :data="availDetailEntityList" rowKey="id" style="margin-bottom:18px">
      <el-table-column fixed="right" label="操作" width="110">
        <template slot-scope="scope">
          <el-button
            @click.native.prevent="newEntityDetail"
            type="text"
            size="small">
            新增
          </el-button>
          <el-button
            @click.native.prevent="deleteEntityDetail(scope.row)"
            type="text"
            size="small">
            删除
          </el-button>
      </template>
      </el-table-column>
      <el-table-column width="50">
        <template slot-scope="scope">
            <el-tooltip class="item" effect="dark" :content="scope.row.errorMessage" placement="top-start" v-if="scope.row.hasError">
              <el-button type="danger" icon="el-icon-error" circle size="mini" style="padding:4px"></el-button>
            </el-tooltip>
          </template>
      </el-table-column>
      <el-table-column label="名称" width="160">
        <template slot-scope="scope">
          <el-input v-model="scope.row.name"></el-input>
        </template>
      </el-table-column>
      <el-table-column label="还款算法" width="160">
        <template slot-scope="scope">
      <t-dic-dropdown-select dicType="pl_returnmoneymethod" v-model="scope.row.returnMoneyMethodId"></t-dic-dropdown-select>
     </template>
      </el-table-column>
      <el-table-column label="表单模板" width="160">
        <template slot-scope="scope">
      <t-dic-dropdown-select dicType="pl_formtemplatetype" v-model="scope.row.formTemplateTypeId"></t-dic-dropdown-select>
     </template>
      </el-table-column>

      <el-table-column label="备注">
        <template slot-scope="scope">
<el-input v-model="scope.row.remark"></el-input>
</template>
    </el-table-column>

  </el-table>
  </div>
  <span slot="footer" class="dialog-footer">
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
          <el-button @click="cancel()">取消</el-button>
    </span>
</el-dialog>
</template>

<script>
export default {
  data() {
    return {
      visible: false,
      formAction: 0,
      headerEntity: {},
      detailEntityList: [],
    }
  },
  created() {

  },
  activated() {

  },
  computed: {
    availDetailEntityList: {
      get: function() {
        return this.detailEntityList.filter(function(item) {
          return item.entity != null
        }).map(function(item, index) {
          return item.entity;
        });
      }
    },
  },
  methods: {
    // 初始化
    init(id) {

      this.visible = true;
      this.$nextTick(() => {
        this.$refs.ruleForm.resetFields();
      });

      let self = this;
      self.detailEntityList.length = 0;
      if (id) {
        tapp.services.pL_LoanProductType.get(id).then(function(result) {
          self.headerEntity = result.headerEntity;
          self.detailEntityList = result.detailEntityList.map((item, index) => {
            item.entityStatus = 1;
            let newItem = {
              id: item.id,
              entity: item
            };
            return newItem;
          })
          self.formAction = 1;
        });
      } else {
        self.headerEntity = {};

        self.detailEntityList.push({
          id: '0',
          entity: {
            id: '',
            name: null,
            returnMoneyMethodId: null,
            remark: null,
            hasError: false,
            errorMessage: null,
            entityStatus: 0,
          }
        });

        self.formAction = 0;
      }
    },
    newEntityDetail() {
      let tempId = this.detailEntityList.length;
      this.detailEntityList.push({
        id: tempId,
        entity: {
          id: tempId,
          name: null,
          returnMoneyMethodId: null,
          remark: null,
          hasError: false,
          errorMessage: null,
          entityStatus: 0,
        }
      });
    },
    validateEntityDetail(innerEntity) {
      let errorArrays = [];
      if (innerEntity.name == null || innerEntity.name.length == 0) {
        errorArrays.push('名称输入不能为空');
      } else if (innerEntity.name.length > 200) {
        errorArrays.push('名称输入应该在200个字符内');
      }
      if (innerEntity.formTemplateTypeId == null || innerEntity.formTemplateTypeId.length == 0) {
        errorArrays.push('表单模板选择不能为空');
      }
      if (innerEntity.returnMoneyMethodId == null || innerEntity.returnMoneyMethodId.length == 0) {
        errorArrays.push('还款算法选择不能为空');
      }

      if (innerEntity.remark != null && innerEntity.remark.length > 400) {
        errorArrays.push('备注输入应该在200个字符内');
      }
      return errorArrays;
    },
    deleteEntityDetail(item) {
      let itemIndex = this.detailEntityList.findIndex((innerItem) => {
        return innerItem.id == item.id;
      });

      if (item.entityStatus == 0) {
        this.detailEntityList.splice(itemIndex, 1);
      } else {
        this.detailEntityList[itemIndex].entity = null
      }
    },
    dataFormSubmit(formName) {
      let self = this;
      self.$refs['ruleForm'].validate((valid) => {
        let headerEntity = self.headerEntity;
        let detailEntityList = self.detailEntityList.filter(p => p.entity != null).map(p => {
          let innerEntity = p.entity;
          if (innerEntity.entityStatus == 0) {
            innerEntity.id = null;
          }
          let detailEntityErrorArrays = self.validateEntityDetail(innerEntity);
          if (detailEntityErrorArrays.length > 0) {
            innerEntity.hasError = true;
            innerEntity.errorMessage = detailEntityErrorArrays.join(",");
            valid = false;
          } else {
            innerEntity.hasError = false;
            innerEntity.errorMessage = null; 
          }
          return innerEntity;
        });
        if (valid) {
          let deletedDetailIdList = self.detailEntityList.filter(p => p.entity == null && p.id != null).map(p => p.id);

          let model = {
            headerEntity: headerEntity,
            detailEntityList: detailEntityList,
            deletedDetailIdList: deletedDetailIdList
          };
          tapp.services.pL_LoanProductType.save(model).then(function(result) {
            self.headerEntity = result.headerEntity;
            self.detailEntityList = result.detailEntityList.map((item, index) => {
              item.entityStatus = 1;
              return {
                id: item.id,
                entity: item
              };
            })
            self.formAction = 1;
            self.$notify.success({
              title: '操作成功！',
              message: '修改产品类型成功！',
              duration: 1500,
              onClose: () => {
                self.visible = false;
                self.$emit('change');
              }
            });
          });
        } else {
          this.$notify.error({
            title: '错误',
            message: '系统输入验证失败！'
          });
          return false;
        }
      });
    },
    cancel() {
      this.visible = false;
      this.$emit('change');
    }

  }
}
</script>
