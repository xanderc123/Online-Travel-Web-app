<template>
    <el-row style="width: 700px;margin: 0 auto;background-color: #FFFFFF;padding: 5px 0;border-radius: 5px;">
        <el-row style="padding: 10px;margin: 0 5px;">
            <el-row>
                <el-date-picker size="small" style="width: 220px;margin-left: 5px;" v-model="searchTime"
                    type="daterange" range-separator="to" start-placeholder="Start Date" end-placeholder="End Date">
                </el-date-picker>
                <el-input size="small" style="width: 188px;margin-left: 5px;margin-right: 6px;"
                    v-model="supplierQueryDto.title" placeholder="Title" clearable @clear="handleFilterClear">
                    <el-button slot="append" @click="handleFilter" icon="el-icon-search"></el-button>
                </el-input>
            </el-row>
        </el-row>
        <el-row style="margin: 0 15px;border-top: 1px solid rgb(245,245,245);">
            <el-row style="margin-block: 20px;">
                <div class="notice-item" @click="noticeSelected(notice)" v-for="(notice, index) in tableData"
                    :key="index">
                    <div class="title">{{ notice.title }}</div>
                    <div class="time">Created At: {{ notice.createTime }}</div>
                </div>
            </el-row>
            <el-pagination style="margin:10px 0;" @size-change="handleSizeChange" @current-change="handleCurrentChange"
                :current-page="currentPage" :page-sizes="[10, 20]" :page-size="pageSize"
                layout="total, sizes, prev, pager, next, jumper" :total="totalItems"></el-pagination>
        </el-row>
        <!-- Operation Panel -->
        <el-dialog :show-close="false" :visible.sync="dialogOperation" width="32%">
            <div slot="title">
                <p class="dialog-title">{{ !isOperation ? 'Add Announcement' : 'Edit Announcement Information' }}</p>
            </div>
            <div style="padding:0 20px;">
                <el-row style="margin-bottom: 20px;">
                    <span class="dialog-hover">Title</span>
                    <input class="dialog-input" v-model="data.title" placeholder="Please enter" />
                    <div>
                        <Editor height="300px" :receiveContent="data.content" @on-receive="onReceiveContent" />
                    </div>
                </el-row>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button size="small" v-if="!isOperation"
                    style="background-color: rgb(96, 98, 102);color: rgb(247,248,249);border: none;" class="customer"
                    type="info" @click="addOperation()">Add</el-button>
                <el-button size="small" v-else
                    style="background-color: rgb(96, 98, 102);color: rgb(247,248,249);border: none;" class="customer"
                    type="info" @click="updateOperation()">Edit</el-button>
                <el-button class="customer" size="small" style="background-color: rgb(211, 241, 241);border: none;"
                    @click="cannel()">Cancel</el-button>
            </span>
        </el-dialog>
    </el-row>
</template>

<script>
import Editor from "@/components/Editor"
export default {
    components: { Editor },
    data() {
        return {
            data: {},
            filterText: '',
            currentPage: 1,
            pageSize: 10,
            totalItems: 0,
            dialogOperation: false, // 开关
            isOperation: false, // 开关-标识新增或修改
            tableData: [],
            selectedRows: [],
            supplierQueryDto: {}, // 搜索条件
            users: [],
            searchTime: [],
            isAuditList: [{ value: null, label: 'All' }, { value: 0, label: '未审核' }, { value: 1, label: '已审核' }],
            statusList: [{ value: null, label: 'All' }, { value: 0, label: '不可用' }, { value: 1, label: '可用' }],
        };
    },
    created() {
        this.fetchFreshData();
        this.fetchUsers();
    },
    methods: {
        noticeSelected(notice) {
            // 存储公告
            sessionStorage.setItem('noticeInfo', JSON.stringify(notice));
            this.$router.push('/noticeDetail');
        },
        onReceiveContent(content) {
            this.data.content = content;
        },
        // 获取法人用户信息
        fetchUsers() {
            this.$axios.post('/user/query', {}).then(res => {
                if (res.data.code === 200) {
                    this.users = res.data.data;
                }
            }).catch(error => {
                console.error("查询法人信息异常：", error);
            });
        },
        // 置位
        cannel() {
            this.data = {};
            this.dialogOperation = false;
            this.isOperation = false;
        },
        // 批量删除数据
        async batchDelete() {
            if (!this.selectedRows.length) {
                this.$message(`No data selected.`);
                return;
            }
            const confirmed = await this.$swalConfirm({
                title: 'Delete Announcement Data',
                text: `This action cannot be undone. Do you want to continue?`,
                icon: 'warning',
            });
            if (confirmed) {
                try {
                    let ids = this.selectedRows.map(entity => entity.id);
                    const response = await this.$axios.post(`/notice/batchDelete`, ids);
                    if (response.data.code === 200) {
                        this.$swal.fire({
                            title: 'Delete notification',
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
                        title: 'Error notification',
                        text: e,
                        icon: 'error',
                        showConfirmButton: false,
                        timer: 2000,
                    });
                    console.error(`公告信息删除异常：`, e);
                }
            }
        },
        // 修改信息
        async updateOperation() {
            try {
                const response = await this.$axios.put('/notice/update', this.data);
                this.clearFormData();
                this.$swal.fire({
                    title: 'Announcement Information Modification',
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
            try {
                const response = await this.$axios.post('/notice/save', this.data);
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
        clearFormData() {
            this.data = {};
        },
        async fetchFreshData() {
            try {
                this.tableData = [];
                let startTime = null;
                let endTime = null;

                if (this.searchTime != null && this.searchTime.length === 2) {
                    const [startDate, endDate] = this.searchTime;

                    // 设置开始时间为当天的 00:00:00
                    startTime = new Date(startDate);
                    startTime.setHours(0, 0, 0, 0);

                    // 调整时区偏移
                    const timeZoneOffset = startTime.getTimezoneOffset();
                    startTime = new Date(startTime.getTime() - timeZoneOffset * 60 * 1000);

                    // 设置结束时间为当天的 23:59:59
                    endTime = new Date(endDate);
                    endTime.setHours(23, 59, 59, 999);

                    // 调整时区偏移
                    endTime = new Date(endTime.getTime() - timeZoneOffset * 60 * 1000);

                    // 将时间转换为 ISO 格式（如果需要）
                    startTime = startTime.toISOString();
                    endTime = endTime.toISOString();

                }
                // 请求参数
                const params = {
                    current: this.currentPage,
                    size: this.pageSize,
                    key: this.filterText,
                    startTime: startTime,
                    endTime: endTime,
                    ...this.supplierQueryDto
                };
                const response = await this.$axios.post('/notice/query', params);
                const { data } = response;
                this.tableData = data.data;
                this.totalItems = data.total;
            } catch (error) {
                console.error('查询公告信息异常:', error);
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
            this.data.content = row.content;
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
.notice-item:hover {
    background-color: rgb(246, 246, 246);
}

.notice-item {
    padding: 16px 6px;
    border-bottom: 1px solid rgb(246, 246, 246);
    border-radius: 4px;
    cursor: pointer;

    .title {
        font-size: 26px;
        font-weight: 800;
        margin-bottom: 10px;
    }

    .time {
        font-size: 14px;
    }
}
</style>