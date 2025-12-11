<template>
    <el-row style="background-color: #FFFFFF;padding: 5px 0;border-radius: 5px;">
        <el-row style="padding: 10px;margin:0 10px;">
            <el-row>
                <el-select @change="fetchFreshData" size="small" v-model="SupplierQueryDto.isAudit"
                    placeholder="Audit status">
                    <el-option v-for="item in isAuditList" :key="item.value" :label="item.label" :value="item.value">
                    </el-option>
                </el-select>
                <el-select @change="fetchFreshData" size="small" v-model="SupplierQueryDto.status"
                    style="margin-left: 5px;" placeholder="Status">
                    <el-option v-for="item in statusList" :key="item.value" :label="item.label" :value="item.value">
                    </el-option>
                </el-select>
                <el-date-picker size="small" style="width: 240px;margin-left: 5px;" v-model="searchTime"
                    type="daterange" range-separator="to" start-placeholder="sign up begin "
                    end-placeholder="sign up end">
                </el-date-picker>
                <el-input size="small" style="width: 188px;margin-left: 5px;margin-right: 6px;"
                    v-model="SupplierQueryDto.name" placeholder="SupplierName" clearable @clear="handleFilterClear">
                    <el-button slot="append" @click="handleFilter" icon="el-icon-search"></el-button>
                </el-input>
                <span style="float: right;">
                    <el-button size="small"
                        style="background-color: rgb(96, 98, 102);color: rgb(247,248,249);border: none;"
                        class="customer" type="info" @click="add()"><i class="el-icon-plus"></i>add supplier</el-button>
                </span>
            </el-row>
        </el-row>
        <el-row style="margin: 0 25px;border-top: 1px solid rgb(245,245,245);">
            <el-table :data="tableData" style="width: 100%">

                <el-table-column prop="name" label="Supplier name"></el-table-column>
                <el-table-column prop="userId" sortable width="100" label="Responsible Person ID"></el-table-column>
                <el-table-column prop="userName" width="100" label="Responsible Person"></el-table-column>
                <el-table-column prop="contactPerson" width="120" label="contactPerson"></el-table-column>
                <el-table-column prop="contactNumber" width="120" label="contactNumber"></el-table-column>
                <el-table-column prop="email" width="100" label="email"></el-table-column>
                <el-table-column prop="createTime" sortable width="168" label="createTime"></el-table-column>
                <el-table-column prop="location" width="110" label="location"></el-table-column>
                <el-table-column prop="isWord" width="108" label="Audit Status">
                    <template slot-scope="scope">
                        <i v-if="scope.row.isAudit" style="margin-right: 5px;" class="el-icon-success"></i>
                        <i v-else style="margin-right: 5px;color: rgb(253, 199, 50);" class="el-icon-warning"></i>
                        <el-tooltip v-if="scope.row.isAudit" class="item" effect="dark"
                            content="After approval, suppliers can publish services or products."
                            placement="bottom-end">
                            <span style="text-decoration: underline;text-decoration-style: dashed;">Approved</span>
                        </el-tooltip>
                        <span v-else>pending</span>
                    </template>
                </el-table-column>
                <el-table-column prop="isWord" width="108" label="Status">
                    <template slot-scope="scope">
                        <i v-if="scope.row.status" style="margin-right: 5px;" class="el-icon-success"></i>
                        <i v-else style="margin-right: 5px;color: rgb(253, 199, 50);" class="el-icon-warning"></i>
                        <el-tooltip v-if="scope.row.status" class="item" effect="dark"
                            content="unavailable status, stop supplier service." placement="bottom-end">
                            <span style="text-decoration: underline;text-decoration-style: dashed;">available</span>
                        </el-tooltip>
                        <span v-else>unavailable</span>
                    </template>
                </el-table-column>
                <el-table-column label="operate" width="120">
                    <template slot-scope="scope">
                        <span class="text-button" @click="handleEdit(scope.row)">edit</span>
                        <span class="text-button" @click="handleDelete(scope.row)">delete</span>
                    </template>
                </el-table-column>
            </el-table>
            <el-pagination style="margin:10px 0;" @size-change="handleSizeChange" @current-change="handleCurrentChange"
                :current-page="currentPage" :page-sizes="[10, 20]" :page-size="pageSize"
                layout="total, sizes, prev, pager, next, jumper" :total="totalItems"></el-pagination>
        </el-row>
        <!-- 操作面板 -->
         
        <el-dialog :w-close="false" :visible.sync="dialogOperation" width="38%">
            <div slot="title">
                <p class="dialog-title">{{ !isOperation ? 'Add supplier' : 'Modify supplier info' }}</p>
            </div>
            <div style="padding:0 20px;">
                
                <el-row style="margin-top: 20px;">
                    <span class="dialog-hover">Supplier name</span>
                    <input class="dialog-input" v-model="data.name" placeholder="Please Enter" />
                    <span class="dialog-hover">contact person</span>
                    <input class="dialog-input" v-model="data.contactPerson" placeholder="Contact Person" />
                    <span class="dialog-hover">contact number</span>
                    <input class="dialog-input" v-model="data.contactNumber" placeholder="Contact Number" />
                    <span class="dialog-hover">email</span>
                    <input class="dialog-input" v-model="data.email" placeholder="Email" />
                    <span class="dialog-hover">product type</span>
                    <input class="dialog-input" v-model="data.productType" placeholder="Product Type" />
                    <span class="dialog-hover">location</span>
                    <input class="dialog-input" v-model="data.location" placeholder="Location" />
                    <div style="margin: 10px;">
                        <span class="dialog-hover">Audit status</span>
                        <el-switch v-model="data.isAudit" active-text="Approved" inactive-text="Pending">
                        </el-switch>
                    </div>
                    <div>
                        <span class="dialog-hover">Status</span>
                        <el-switch v-model="data.status" active-text="normal" inactive-text="abnormal">
                        </el-switch>
                    </div>

                    <div style="margin: 10px 0;">
                        <span class="dialog-hover">legal user</span>
                        <el-select v-model="data.userId" placeholder="please choose legal user">
                            <el-option v-for="user in users" :key="user.id" :label="user.userName" :value="user.id">
                            </el-option>
                        </el-select>
                    </div>

                </el-row>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button size="small" v-if="!isOperation"
                    style="background-color: rgb(96, 98, 102);color: rgb(247,248,249);border: none;" class="customer"
                    type="info" @click="addOperation()">add</el-button>
                <el-button size="small" v-else
                    style="background-color: rgb(96, 98, 102);color: rgb(247,248,249);border: none;" class="customer"
                    type="info" @click="updateOperation()">modify</el-button>
                <el-button class="customer" size="small" style="background-color: rgb(211, 241, 241);border: none;"
                    @click="cancel()">cancel</el-button>
            </span>
        </el-dialog>
    </el-row>
</template>

<script>
export default {
    data() {
        return {
            data: {},
            filterText: '',
            currentPage: 1,
            pageSize: 10,
            totalItems: 0,
            dialogStatusOperation: false,
            dialogOperation: false, // 开关
            isOperation: false, // 开关-标识新增或修改
            tableData: [],
            searchTime: [],
            selectedRows: [],
            status: null,
            SupplierQueryDto: {}, // 搜索条件
            users: [],
            searchTime: [],
            isAuditList: [{ value: null, label: 'all' }, { value: 0, label: 'Pending' }, { value: 1, label: 'Approved' }],
            statusList: [{ value: null, label: 'all' }, { value: 0, label: 'unavailable' }, { value: 1, label: 'available' }],
        };
    },
    created() {
        this.fetchFreshData();
        this.fetchUsers();
    },
    methods: {
        //get userId
        fetchUsers() {
            const queryParams = {
                pageNum: this.currentPage || 1,      // 当前页码
                pageSize: this.pageSize || 10,       // 每页大小

            };

            this.$axios.post('/user/query', queryParams).then(res => {
                if (res.data.code == 200) {
                    this.users = res.data.data;
                }
            }).catch(error => {
                console.log("Query user info error");
            });
        },
        cancel() {
            this.data = {};
            this.dialogOperation = false;
            this.isOperation = false;
        },

        // 批量删除数据
        async batchDelete() {
            if (!this.selectedRows.length) {
                this.$message(`have not selected any data`);
                return;
            }
            const confirmed = await this.$swalConfirm({
                title: ' delete supplier',
                text: `cannot recover after deletion,continue?`,
                icon: 'warning',
            });
            if (confirmed) {
                try {
                    let ids = this.selectedRows.map(entity => entity.id);
                    const response = await this.$axios.post(`/supplier/batchDelete`, ids);
                    if (response.data.code === 200) {
                        this.$swal.fire({
                            title: 'delete notice',
                            text: response.data.msg,
                            icon: 'success',
                            showConfirmButton: false,
                            timer: 2000,
                        });
                        this.fetchFreshData();
                        return;
                    }
                } catch (e) {
                    this.$swal.fire({
                        title: 'error notice',
                        text: e,
                        icon: 'error',
                        showConfirmButton: false,
                        timer: 2000,
                    });
                    console.error(`supplier info delete error:`, e);
                }
            }
        },

        // 修改信息
        async updateOperation() {

            try {
                const response = await this.$axios.put('/supplier/update', this.data);
                this.clearFormData();
                this.$swal.fire({
                    title: 'supplier info modification',
                    text: response.data.msg,
                    icon: response.data.code === 200 ? 'success' : 'error',
                    showConfirmButton: false,
                    timer: 1000,
                });
                if (response.data.code === 200) {
                    this.cancel();
                    this.fetchFreshData();
                }
            } catch (error) {
                console.error('Error submitting form:', error);
                this.$message.error('Submission failed, please try again later!');
            }
        },
        // 信息新增
        async addOperation() {

            try {
                this.data.isAudit = true;
                const response = await this.$axios.post('/supplier/save', this.data);
                this.$message[response.data.code === 200 ? 'success' : 'error'](response.data.msg);
                if (response.data.code === 200) {
                    this.cancel();
                    this.fetchFreshData();
                }
            } catch (error) {
                console.error('Error submitting form:', error);
                this.$message.error('Submission failed, please try again later!');
            }
        },

        clearFormData() {
            this.data = {};
        },

        async fetchFreshData() {
            try {
                let startTime = null;
                let endTime = null;
                if (this.searchTime != null && this.searchTime.length === 2) {
                    const [startDate, endDate] = await Promise.all(this.searchTime.map(date => date.toISOString()));
                    startTime = `${startDate.split('T')[0]}T00:00:00`;
                    endTime = `${endDate.split('T')[0]}T23:59:59`;
                }
                // 请求参数
                const params = {
                    current: this.currentPage,
                    size: this.pageSize,
                    key: this.filterText,
                    startTime: startTime,
                    endTime: endTime,
                    ...this.SupplierQueryDto
                };
                const response = await this.$axios.post('/supplier/query', params);
                const { data } = response;
                this.tableData = data.data;
                this.totalItems = data.total;
            } catch (error) {
                console.error('Query supplier error:', error);
            }
        },
        add() {
            this.dialogOperation = true;
        },
        handleFilter() {
            this.currentPage = 1;
            this.fetchFreshData();
        },
        handleFilterClear() {
            this.filterText = '';
            this.handleFilter();
        },
        handleSizeChange(val) {
            this.pageSize = val;
            this.currentPage = 1;
            this.fetchFreshData();
        },
        handleCurrentChange(val) {
            this.currentPage = val;
            this.fetchFreshData();
        },
        handleEdit(row) {
            this.dialogOperation = true;
            this.isOperation = true;
            this.data = { ...row }
        },
        handleDelete(row) {
            this.selectedRows.push(row);
            this.batchDelete();
        }
    },
};
</script>
<style scoped lang="scss"></style>