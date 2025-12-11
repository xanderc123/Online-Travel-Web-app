<template>
    <el-row style="background-color: #FFFFFF;padding: 5px 0;border-radius: 5px;">
        <el-row style="padding: 10px 5px;margin: 0 5px;">
            <el-row>
                <el-date-picker size="small" style="width: 220px;margin-left: 5px;" v-model="searchTime"
                    type="daterange" range-separator="to" start-placeholder="Start Date" end-placeholder="End Date">
                </el-date-picker>
                <el-input size="small" style="width: 188px;margin-left: 5px;margin-right: 6px;"
                    v-model="hotelQueryDto.name" placeholder="Hotel Name" clearable @clear="handleFilterClear">
                    <el-button slot="append" @click="handleFilter" icon="el-icon-search"></el-button>
                </el-input>
                <span style="float: right;">
                    <el-button size="small"
                        style="background-color: rgb(96, 98, 102);color: rgb(247,248,249);border: none;"
                        class="customer" type="info" @click="add()"><i class="el-icon-plus"></i>Add hotel</el-button>
                </span>
            </el-row>
        </el-row>
        <el-row style="margin: 0 15px;border-top: 1px solid rgb(245,245,245);">
            <el-table :data="tableData" style="width: 100%">
                <el-table-column prop="cover" label="Hotel Image" width="120px">
                    <template slot-scope="scope">
                        <img :src="scope.row.cover" style="width: 88px;height: 55px;border-radius: 5px;" />
                    </template>
                </el-table-column>
                <el-table-column prop="name" label="Hotel Name"></el-table-column>
                <el-table-column prop="address" label="Location" width="200px"></el-table-column>
                <el-table-column prop="contactPhone" label="Contact Phone" width="150px"></el-table-column>
                <el-table-column prop="supplierId" sortable label="Supplier ID" width="120px"></el-table-column>
                <el-table-column prop="supplierName" label="Supplier Name" width="150px"></el-table-column>
                <el-table-column prop="createTime" sortable width="168" label="Creation Time"></el-table-column>
                <el-table-column label="Operations" width="150">
                    <template slot-scope="scope">
                        <span class="text-button" @click="handleEdit(scope.row)">Edit</span>
                        <span class="text-button" @click="handleDelete(scope.row)">Delete</span>
                    </template>
                </el-table-column>
            </el-table>
            <el-pagination style="margin:10px 0;" @size-change="handleSizeChange" @current-change="handleCurrentChange"
                :current-page="currentPage" :page-sizes="[10, 20]" :page-size="pageSize"
                layout="total, sizes, prev, pager, next, jumper" :total="totalItems"></el-pagination>
        </el-row>
        <!-- 操作面板 -->
        <el-dialog :show-close="false" :visible.sync="dialogOperation" width="25%">
            <div slot="title">
                <p class="dialog-title">{{ !isOperation ? 'Add Hotel' : 'Modify Hotel Information' }}</p>
            </div>
            <div style="padding:0 20px;">
                <el-row>
                    <div>
                        <span class="dialog-hover">Hotel Cover Image</span>
                    </div>
                    <el-upload class="avatar-uploader" action="/api/online-travel-sys/v1.0/file/upload"
                        :show-file-list="false" :on-success="handleCoverSuccess">
                        <img v-if="cover" :src="cover" style="width: 218px;height: 130px;">
                        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>
                </el-row>
                <el-row>
                    <span class="dialog-hover">Hotel Name</span>
                    <input style="line-height: 45px;" class="dialog-input" v-model="data.name"
                        placeholder="Hotel Name" />
                    <span class="dialog-hover">Address</span>
                    <input style="line-height: 45px;" class="dialog-input" v-model="data.address"
                        placeholder="Please enter" />
                    <span class="dialog-hover">Contact Phone</span>
                    <input style="line-height: 45px;" class="dialog-input" v-model="data.contactNum"
                        placeholder="Please enter" />
                </el-row>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button size="small" v-if="!isOperation"
                    style="background-color: rgb(96, 98, 102);color: rgb(247,248,249);border: none;" class="customer"
                    type="info" @click="addOperation()">Add</el-button>
                <el-button size="small" v-else
                    style="background-color: rgb(96, 98, 102);color: rgb(247,248,249);border: none;" class="customer"
                    type="info" @click="updateOperation()">Modify</el-button>
                <el-button class="customer" size="small" style="background-color: rgb(211, 241, 241);border: none;"
                    @click="cannel()">Cancel</el-button>
            </span>
        </el-dialog>
    </el-row>
</template>

<script>
export default {
    data() {
        return {
            cover: '',
            data: {},
            filterText: '',
            cover: '',
            currentPage: 1,
            pageSize: 10,
            totalItems: 0,
            tableData: [],
            selectedRows: [],
            hotelQueryDto: {}, // 搜索条件
            searchTime: [],
            dialogOperation: false, // 控制弹窗酒店的开关
            isOperation: false
        };
    },
    created() {
        this.fetchFreshData();
    },
    methods: {
        handleCoverSuccess(res, file) {
            this.$notify({
                duration: 1500,
                title: '封面上传',
                message: res.code === 200 ? '上传成功' : '上传失败',
                type: res.code === 200 ? 'success' : 'error'
            });
            // 上传成功则更新用户头像
            if (res.code === 200) {
                this.cover = res.data;
            }
        },
        // 新增酒店信息
        async addOperation() {
            this.data.cover = this.cover;
            const { data } = await this.$axios.post('/hotel/save', this.data);
            if (data.code === 200) {
                // 通知结果
                this.$notify({
                    duration: 1500,
                    title: 'Add hotel',
                    message: 'Success',
                    type: 'success'
                });
                this.cannel();
                this.fetchFreshData();
            }
        },
        cannel() {
            this.dialogOperation = false;
            this.isOperation = false;
            this.data = {};
            this.cover = '';
        },
        // 修改酒店信息
        async updateOperation() {
            this.data.cover = this.cover;
            const { data } = await this.$axios.put('/hotel/update', this.data);
            if (data.code === 200) {
                // 通知结果
                this.$notify({
                    duration: 1500,
                    title: 'Hotel Modification',
                    message: 'Modification successful',
                    type: 'success'
                });
                this.cannel();
                this.fetchFreshData();
            }
        },
        add() {
            this.dialogOperation = true;
        },
        // 批量删除数据
        async batchDelete() {
            if (!this.selectedRows.length) {
                this.$message(`No data selected.`);
                return;
            }
            const confirmed = await this.$swalConfirm({
                title: 'Delete Hotel Data',
                text: `This action cannot be undone. Do you want to continue?`,
                icon: 'warning',
            });
            if (confirmed) {
                try {
                    let ids = this.selectedRows.map(entity => entity.id);
                    const response = await this.$axios.post(`/hotel/batchDelete`, ids);
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
                    console.error(`酒店信息删除异常：`, e);
                }
            }
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
                    ...this.hotelQueryDto
                };
                const response = await this.$axios.post('/hotel/queryHotelSupplier', params);
                const { data } = response;
                this.tableData = data.data;
                this.totalItems = data.total;
            } catch (error) {
                console.error('查询酒店信息异常:', error);
            }
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
            this.cover = row.cover;
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