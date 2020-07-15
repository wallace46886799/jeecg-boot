<template>
  <j-modal
    :title="title"
    :width="1200"
    :visible="visible"
    :maskClosable="false"
    :confirmLoading="confirmLoading"
    switchFullscreen
    @ok="handleOk"
    @cancel="handleCancel">
    <a-spin :spinning="confirmLoading">
      <!-- 主表单区域 -->
      <a-form :form="form">
        <a-row>

          <a-col :xs="24" :sm="12">
            <a-form-item label="账户名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['accountName']" placeholder="请输入账户名称"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xs="24" :sm="12">
            <a-form-item label="账户类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['accountType']" :trigger-change="true" dictCode="account_type" placeholder="请选择账户类型"/>
            </a-form-item>
          </a-col>
          <a-col :xs="24" :sm="12">
            <a-form-item label="账户号码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['accountNumber']" placeholder="请输入账户号码"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xs="24" :sm="12">
            <a-form-item label="所属用户" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['accountOwner']" :trigger-change="true" dictCode="dreamlabs_user,name,id" placeholder="请选择所属用户"/>
            </a-form-item>
          </a-col>
          <a-col :xs="24" :sm="12">
            <a-form-item label="所属机构" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['orgOwner']" :trigger-change="true" dictCode="dreamlabs_org,org_name,id" placeholder="请选择所属机构"/>
            </a-form-item>
          </a-col>
          <a-col :xs="24" :sm="12">
            <a-form-item label="状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['status']" :trigger-change="true" dictCode="dict_item_status" placeholder="请选择状态"/>
            </a-form-item>
          </a-col>
          <a-col :xs="24" :sm="12">
            <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['remark']" placeholder="请输入备注"></a-input>
            </a-form-item>
          </a-col>

        </a-row>
      </a-form>

      <!-- 子表单区域 -->
      <a-tabs v-model="activeKey" @change="handleChangeTabs">
        <a-tab-pane tab="账户参数表" :key="refKeys[0]" :forceRender="true">
          <j-editable-table
            :ref="refKeys[0]"
            :loading="dreamlabsAccountParamTable.loading"
            :columns="dreamlabsAccountParamTable.columns"
            :dataSource="dreamlabsAccountParamTable.dataSource"
            :maxHeight="300"
            :rowNumber="true"
            :rowSelection="true"
            :actionButton="true"/>
        </a-tab-pane>
        
      </a-tabs>

    </a-spin>
  </j-modal>
</template>

<script>

  import pick from 'lodash.pick'
  import { FormTypes,getRefPromise } from '@/utils/JEditableTableUtil'
  import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
  import { validateDuplicateValue } from '@/utils/util'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"

  export default {
    name: 'DreamlabsAccountModal',
    mixins: [JEditableTableMixin],
    components: {
      JDictSelectTag,
    },
    data() {
      return {
        labelCol: {
          xs: { span: 24 },
          sm: { span: 6 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        labelCol2: {
          xs: { span: 24 },
          sm: { span: 3 },
        },
        wrapperCol2: {
          xs: { span: 24 },
          sm: { span: 20 },
        },
        // 新增时子表默认添加几行空数据
        addDefaultRowNum: 1,
        validatorRules: {
        },
        refKeys: ['dreamlabsAccountParam', ],
        tableKeys:['dreamlabsAccountParam', ],
        activeKey: 'dreamlabsAccountParam',
        // 账户参数表
        dreamlabsAccountParamTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '参数组',
              key: 'paramGroup',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '参数键',
              key: 'paramKey',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '参数值',
              key: 'paramValue',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '状态',
              key: 'status',
              type: FormTypes.select,
              dictCode:"dict_item_status",
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '备注',
              key: 'remark',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
          ]
        },
        url: {
          add: "/account/dreamlabsAccount/add",
          edit: "/account/dreamlabsAccount/edit",
          dreamlabsAccountParam: {
            list: '/account/dreamlabsAccount/queryDreamlabsAccountParamByMainId'
          },
        }
      }
    },
    methods: {
      getAllTable() {
        let values = this.tableKeys.map(key => getRefPromise(this, key))
        return Promise.all(values)
      },
      /** 调用完edit()方法之后会自动调用此方法 */
      editAfter() {
        let fieldval = pick(this.model,'accountName','accountType','accountNumber','accountOwner','orgOwner','status','remark')
        this.$nextTick(() => {
          this.form.setFieldsValue(fieldval)
        })
        // 加载子表数据
        if (this.model.id) {
          let params = { id: this.model.id }
          this.requestSubTableData(this.url.dreamlabsAccountParam.list, params, this.dreamlabsAccountParamTable)
        }
      },
      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let main = Object.assign(this.model, allValues.formValue)
        return {
          ...main, // 展开
          dreamlabsAccountParamList: allValues.tablesValue[0].values,
        }
      },
      validateError(msg){
        this.$message.error(msg)
      },
     popupCallback(row){
       this.form.setFieldsValue(pick(row,'accountName','accountType','accountNumber','accountOwner','orgOwner','status','remark'))
     },

    }
  }
</script>

<style scoped>
</style>