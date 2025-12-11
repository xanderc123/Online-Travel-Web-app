<template>
    <el-row style="background-color: #FFFFFF;padding: 5px 0;border-radius: 5px;">
        <el-row style="padding: 10px;margin-left: 10px;">
            <el-row>
                <el-select @change="fetchFreshData" size="small" v-model="userQueryDto.isLogin"
                    placeholder="login status">
                    <el-option v-for="item in loginStatuList" :key="item.value" :label="item.label" :value="item.value">
                    </el-option>
                </el-select>
                <el-select @change="fetchFreshData" size="small" v-model="userQueryDto.isWord" style="margin-left: 5px;"
                    placeholder="muted status">
                    <el-option v-for="item in wordStatuList" :key="item.value" :label="item.label" :value="item.value">
                    </el-option>
                </el-select>
                <el-select @change="fetchFreshData" size="small" v-model="userQueryDto.role" style="margin-left: 5px;"
                    placeholder="user role">
                    <el-option v-for="item in rolesList" :key="item.value" :label="item.label" :value="item.value">
                    </el-option>
                </el-select>
                <el-date-picker size="small" style="width: 240px;margin-left: 5px;" v-model="searchTime"
                    type="daterange" range-separator="to" start-placeholder="sign up begin"
                    end-placeholder="sign up end">
                </el-date-picker>
                <el-input size="small" style="width: 188px;margin-left: 5px;margin-right: 6px;"
                    v-model="userQueryDto.userName" placeholder="username" clearable @clear="handleFilterClear">
                    <el-button slot="append" @click="handleFilter" icon="el-icon-search"></el-button>
                </el-input>
                <span style="float: right;">
                    <el-button size="small"
                        style="background-color: rgb(96, 98, 102);color: rgb(247,248,249);border: none;"
                        class="customer" type="info" @click="add()"><i class="el-icon-plus"></i>add user</el-button>
                </span>
            </el-row>
        </el-row>
        <el-row style="margin: 0 20px;border-top: 1px solid rgb(245,245,245);">
            <el-table :data="tableData" style="width: 100%">
                <el-table-column prop="userAvatar" width="68" label="avator">
                    <template slot-scope="scope">
                        <el-avatar :size="25" :src="scope.row.userAvatar" style="margin-top: 10px;"></el-avatar>
                    </template>
                </el-table-column>
                <el-table-column prop="userName" label="name"></el-table-column>
                <el-table-column prop="userAccount" width="128" label="account"></el-table-column>
                <el-table-column prop="userEmail" width="168" label="email"></el-table-column>
                <el-table-column prop="userRole" width="68" label="role">
                    <template slot-scope="scope">
                        <span>{{ scope.row.userRole === 1 ? 'admin' : 'user' }}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="isLogin" width="108" label="ban account">
                    <template slot-scope="scope">
                        <i v-if="scope.row.isLogin" style="margin-right: 5px;" class="el-icon-warning"></i>
                        <i v-else style="margin-right: 5px;color: rgb(253, 199, 50);" class="el-icon-success"></i>
                        <el-tooltip v-if="scope.row.isLogin" class="item" effect="dark"
                            content="Banned accounts are restricted from logging in until an admin lifts the ban"
                            placement="bottom-end">
                            <span style="text-decoration: underline;text-decoration-style: dashed;">is banned</span>
                        </el-tooltip>
                        <span v-else>normal</span>
                    </template>
                </el-table-column>
                <el-table-column prop="isWord" width="108" label="mute">
                    <template slot-scope="scope">
                        <i v-if="scope.row.isWord" style="margin-right: 5px;" class="el-icon-warning"></i>
                        <i v-else style="margin-right: 5px;color: rgb(253, 199, 50);" class="el-icon-success"></i>
                        <el-tooltip v-if="scope.row.isWord" class="item" effect="dark"
                            content="Banned accounts are restricted from logging in until an admin lifts the ban"
                            placement="bottom-end">
                            <span style="text-decoration: underline;text-decoration-style: dashed;">is banned</span>
                        </el-tooltip>
                        <span v-else>normal</span>
                    </template>
                </el-table-column>
                <el-table-column :sortable="true" prop="createTime" width="168" label="register on"></el-table-column>
                <el-table-column label="operate" width="170">
                    <template slot-scope="scope">
                        <span class="text-button" @click="handleStatus(scope.row)">account status</span>
                        <span class="text-button" @click="handleEdit(scope.row)">edit</span>
                        <span class="text-button" @click="handleDelete(scope.row)">delete</span>
                    </template>
                </el-table-column>
            </el-table>
            <el-pagination style="margin:10px 0;" @size-change="handleSizeChange" @current-change="handleCurrentChange"
                :current-page="currentPage" :page-sizes="[5, 7]" :page-size="pageSize"
                layout="total, sizes, prev, pager, next, jumper" :total="totalItems"></el-pagination>
        </el-row>
        <!-- 操作面板 -->
        <el-dialog :show-close="false" :visible.sync="dialogUserOperaion" width="25%">
            <div slot="title">
                <p class="dialog-title">{{ !isOperation ? 'add user' : 'modify user information' }}</p>
            </div>
            <div style="padding:0 20px;">

                <el-row>
                    <span class="dialog-hover">username</span>
                    <input class="dialog-input" v-model="data.userName" placeholder="username" />
                    <span class="dialog-hover">account</span>
                    <input class="dialog-input" v-model="data.userAccount" placeholder="account" />
                    <span class="dialog-hover">email</span>
                    <input class="dialog-input" v-model="data.userEmail" placeholder="email" />
                    <span class="dialog-hover">password</span>
                    <input class="dialog-input" v-model="userPwd" type="password" placeholder="password" />
                </el-row>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button size="small" v-if="!isOperation"
                    style="background-color: rgb(96, 98, 102);color: rgb(247,248,249);border: none;" class="customer"
                    type="info" @click="addOperation()">add</el-button>
                <el-button size="small" v-else
                    style="background-color: rgb(96, 98, 102);color: rgb(247,248,249);border: none;" class="customer"
                    type="info" @click="updateOperation()">edit</el-button>
                <el-button class="customer" size="small" style="background-color: rgb(211, 241, 241);border: none;"
                    @click="cannel()">cancel</el-button>
            </span>
        </el-dialog>
        <el-dialog :show-close="false" :visible.sync="dialogStatusOperation" width="25%">
            <div slot="title">
                <p class="dialog-title">account status</p>
            </div>
            <div style="padding:0 20px;">
                <el-row>
                    <el-switch active-color="#13ce66" inactive-color="#ff4949" v-model="data.isLogin"
                        active-text="banned" inactive-text="normal">
                    </el-switch>
                </el-row>
                <el-row style="margin: 20px 0;">
                    <el-switch active-color="#13ce66" inactive-color="#ff4949" v-model="data.isWord" active-text="mute"
                        inactive-text="normal">
                    </el-switch>
                </el-row>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button size="small" style="background-color: rgb(96, 98, 102);color: rgb(247,248,249);border: none;"
                    class="customer" type="info" @click="comfirmStatus">confirm</el-button>
                <el-button class="customer" size="small" style="background-color: rgb(241, 241, 241);border: none;"
                    @click="dialogStatusOperation = false">cancel</el-button>
            </span>
        </el-dialog>
    </el-row>
</template>

<script>
export default {
    data() {
        return {
            userPwd: '',
            userAvatar: '',
            data: {},
            filterText: '',
            currentPage: 1,
            pageSize: 7,
            totalItems: 0,
            dialogStatusOperation: false,
            dialogUserOperaion: false, // 开关
            isOperation: false, // 开关-标识新增或修改
            tableData: [],
            searchTime: [],
            selectedRows: [],
            status: null,
            userQueryDto: {}, // 搜索条件
            loginStatuList: [{ value: null, label: 'all' }, { value: 1, label: 'banned' }, { value: 0, label: 'normal' }],
            wordStatuList: [{ value: null, label: 'all' }, { value: 0, label: 'normal' }, { value: 1, label: 'mute' }],
            rolesList: [{ value: null, label: 'all' }, { value: 2, label: 'user' }, { value: 1, label: 'admin' }]
        };
    },
    created() {
        this.fetchFreshData();
    },
    methods: {
        cannel() {
            this.data = {};
            this.userAvatar = '';
            this.userPwd = '';
            this.dialogUserOperaion = false;
            this.dialogStatusOperation = false;
            this.isOperation = false;
        },
        comfirmStatus() {
            const userUpdateDto = {
                id: this.data.id,
                isLogin: this.data.isLogin,
                isWord: this.data.isWord
            }
            this.$axios.put(`/user/backUpdate`, userUpdateDto).then(res => {
                if (res.data.code === 200) {
                    this.$notify({
                        duration: 1500,
                        title: ' Information modification',
                        message: '修改成功',
                        type: 'success'
                    });
                    this.dialogStatusOperation = false;
                }
            }).catch(error => {
                console.log("修改状态异常：" + error);
            })
        },
        handleStatus(data) {
            this.dialogStatusOperation = true;
            this.data = data;
        },
        // 头像上传回调函数
        handleAvatarSuccess(res, file) {
            this.$notify({
                duration: 1500,
                title: '头像上传',
                message: res.code === 200 ? 'Upload successful' : 'Upload failed',
                type: res.code === 200 ? 'success' : 'error'
            });
            // 上传成功则更新user头像
            if (res.code === 200) {
                this.userAvatar = res.data;
            }
        },
        switchChange() {
            this.fetchFreshData();
        },
        async handleSwitchChange(id, status, operation) {
            try {
                let param = { id: id };
                // Login status
                if (operation) {
                    param.isLogin = status;
                } else { // Comment status
                    param.isWord = status;
                }
                const response = await this.$axios.put(`/user/backUpdate`, param);
                if (response.data.code === 200) {
                    this.$notify({
                        duration: 1500,
                        title: operation ? 'Login Status' : 'Comment Status',
                        message: operation ? 'Login status operation successful' : 'Comment status operation successful',
                        type: operation ? 'success' : 'error'
                    });
                }
            } catch (e) {
                console.error(`Error updating user status: ${e}`);
            }
        },
        // Batch delete data
        async batchDelete() {
            if (!this.selectedRows.length) {
                this.$message(`No data selected`);
                return;
            }
            const confirmed = await this.$swalConfirm({
                title: 'Delete User Data',
                text: `Data cannot be recovered after deletion. Continue?`,
                icon: 'warning',
            });
            if (confirmed) {
                try {
                    let ids = this.selectedRows.map(entity => entity.id);
                    const response = await this.$axios.post(`/user/batchDelete`, ids);
                    if (response.data.code === 200) {
                        this.$swal.fire({
                            title: 'Delete Success',
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
                        title: 'Error',
                        text: e,
                        icon: 'error',
                        showConfirmButton: false,
                        timer: 2000,
                    });
                    console.error(`User data deletion error:`, e);
                }
            }
        },
        resetQueryCondition() {
            this.userQueryDto = {};
            this.searchTime = [];
            this.fetchFreshData();
        },
        // 修改信息
        async updateOperation() {
            if (this.userPwd !== '') {
                const pwd = this.$md5(this.$md5(this.userPwd));
                this.data.userPwd = pwd;
            } else {
                this.data.userPwd = null;
            }
            this.data.userAvatar = this.userAvatar;
            try {
                const response = await this.$axios.put('/user/backUpdate', this.data);
                this.clearFormData();
                this.closeDialog();
                this.$swal.fire({
                    title: 'user Info Modification',
                    text: response.data.msg,
                    icon: response.data.code === 200 ? 'success' : 'error',
                    showConfirmButton: false,
                    timer: 1000,
                });
                if (response.data.code === 200) {
                    this.cannel();
                    this.fetchFreshData();
                }
            } catch (error) {
                console.error('提交表单时出错:', error);
                this.$message.error('Submission failed. Please try again later.！');
            }
        },
        // 信息新增
        async addOperation() {
            if (this.userPwd !== '') {
                this.data.userPwd = this.$md5(this.$md5(this.userPwd));
            } else {
                this.data.userPwd = null;
            }
            this.data.userAvatar = this.userAvatar;
            try {
                const response = await this.$axios.post('/user/insert', this.data);
                this.$message[response.data.code === 200 ? 'success' : 'error'](response.data.msg);
                if (response.data.code === 200) {
                    this.cannel();
                    this.fetchFreshData();
                }
            } catch (error) {
                console.error('提交表单时出错:', error);
                this.$message.error('Submission failed. Please try again later.！');
            }
        },
        closeDialog() {
            this.dialogUserOperaion = false;
        },
        clearFormData() {
            this.data = {};
        },
        async fetchFreshData() {
            try {
                this.tableData = [];
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
                    ...this.userQueryDto
                };
                const response = await this.$axios.post('/user/query', params);
                const { data } = response;
                this.tableData = data.data;
                this.totalItems = data.total;
            } catch (error) {
                console.error('查询user信息异常:', error);
            }
        },
        add() {
            this.dialogUserOperaion = true;
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
            this.dialogUserOperaion = true;
            this.isOperation = true;
            row.userPwd = null;
            this.userAvatar = row.userAvatar;
            this.data = { ...row }
        },
        handleDelete(row) {
            this.selectedRows.push(row);
            this.batchDelete();
        }
    },
};
</script>
<style scoped lang="scss">
.tag-tip {
    display: inline-block;
    padding: 5px 10px;
    border-radius: 5px;
    background-color: rgb(245, 245, 245);
    color: rgb(104, 118, 130);
}

.input-def {
    height: 40px;
    line-height: 40px;
    outline: none;
    border: none;
    font-size: 20px;
    color: rgb(102, 102, 102);
    font-weight: 900;
    width: 100%;
}

.dialog-footer {
    /* 使按钮水平居中 */
    display: flex;
    justify-content: center;
    align-items: center;
}

/* 如果需要调整按钮之间的间距 */
.customer {
    margin: 0 8px;
    /* 根据需要调整间距 */
}
</style>