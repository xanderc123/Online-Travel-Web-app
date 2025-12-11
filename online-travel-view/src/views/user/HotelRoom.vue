<template>
    <el-row style="background-color: #FFFFFF;padding: 5px 0;border-radius: 5px;">
        <el-row style="padding: 10px;margin: 0 5px;">
            <el-row>
                <el-select @change="fetchFreshData" size="small" v-model="hotelRoomQueryDto.hotelId" placeholder="从属酒店">
                    <el-option v-for="item in hotels" :key="item.id" :label="item.name" :value="item.id">
                    </el-option>
                </el-select>
                <el-date-picker size="small" style="width: 220px;margin-left: 5px;" v-model="searchTime"
                    type="daterange" range-separator="To" start-placeholder="Creation Start"
                    end-placeholder="Creation End">
                </el-date-picker>
                <el-input size="small" style="width: 188px;margin-left: 5px;margin-right: 6px;"
                    v-model="hotelRoomQueryDto.name" placeholder="Hotel Room Name" clearable @clear="handleFilterClear">
                    <el-button slot="append" @click="handleFilter" icon="el-icon-search"></el-button>
                </el-input>
                <span style="float: right;">
                    <el-button size="small"
                        style="background-color: rgb(96, 98, 102);color: rgb(247,248,249);border: none;"
                        class="customer" type="info" @click="add()"><i class="el-icon-plus"></i>Add Hotel
                        Room</el-button>
                </span>
            </el-row>
        </el-row>
        <el-row style="margin: 0 15px;border-top: 1px solid rgb(245,245,245);">
            <el-table :data="tableData" style="width: 100%">
                <el-table-column prop="cover" label="Room Cover" width="120px">
                    <template slot-scope="scope">
                        <img :src="scope.row.cover" style="width: 88px;height: 55px;border-radius: 5px;" />
                    </template>
                </el-table-column>
                <el-table-column prop="name" label="Room Name"></el-table-column>
                <el-table-column prop="hotelId" sortable label="Hotel ID" width="120px"></el-table-column>
                <el-table-column prop="hotelName" sortable label="Hotel Name" width="120px"></el-table-column>
                <el-table-column prop="price" sortable label="Price" width="80px"></el-table-column>
                <el-table-column prop="discount" sortable label="Discount" width="120px"></el-table-column>
                <el-table-column prop="createTime" sortable width="168" label="Creation Time"></el-table-column>
                <el-table-column label="Operations" width="240">
                    <template slot-scope="scope">
                        <span class="text-button" @click="roomManage(scope.row)">Bed Management</span>
                        <span class="text-button" @click="handleEdit(scope.row)">Edit</span>
                        <span class="text-button" @click="handleDelete(scope.row)">Delete</span>
                    </template>
                </el-table-column>
            </el-table>
            <el-pagination style="margin:10px 0;" @size-change="handleSizeChange" @current-change="handleCurrentChange"
                :current-page="currentPage" :page-sizes="[10, 20]" :page-size="pageSize"
                layout="total, sizes, prev, pager, next, jumper" :total="totalItems"></el-pagination>
        </el-row>
        <!-- Operation Panel -->
        <el-dialog :show-close="false" :visible.sync="dialogOperation" width="28%">
            <div slot="title">
                <p class="dialog-title">{{ !isOperation ? 'Add Hotel Room' : 'Modify Hotel Room Information' }}</p>
            </div>
            <div style="padding:0 20px;">
                <span class="dialog-hover">Hotel Room Cover</span>
                <el-row>
                    <el-upload class="avatar-uploader" action="/api/online-travel-sys/v1.0/file/upload"
                        :show-file-list="false" :on-success="handleCoverSuccess">
                        <img v-if="cover" :src="cover" style="width: 200px;height: 120px;">
                        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>
                </el-row>
                <el-row style="margin-bottom: 20px;">
                    <span class="dialog-hover">Hotel Room Name</span>
                    <input class="dialog-input" v-model="data.name" placeholder="Please enter" />
                    <div>
                        <div>
                            <span class="dialog-hover">Description</span>
                        </div>
                        <el-input style="width: 100%;" type="textarea" :rows="2" placeholder="Please enter"
                            v-model="data.description">
                        </el-input>
                    </div>
                    <div>
                        <div>
                            <span class="dialog-hover">Price</span>
                        </div>
                        <el-input v-model="data.price" placeholder="Please enter"></el-input>
                    </div>
                    <div>
                        <div>
                            <span class="dialog-hover">Discount</span>
                        </div>
                        <el-input v-model="data.discount" placeholder="Please enter"></el-input>
                    </div>
                    <div style="margin: 10px 0;">
                        <div>
                            <span class="dialog-hover">Hotel List</span>
                        </div>
                        <el-select v-model="data.hotelId" placeholder="Please select">
                            <el-option v-for="hotel in hotels" :key="hotel.id" :label="hotel.name" :value="hotel.id">
                            </el-option>
                        </el-select>
                    </div>
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
                    @click="cancel()">Cancel</el-button>
            </span>
        </el-dialog>

        <el-drawer :append-to-body="true" title="Room Management" width="35%" :visible.sync="drawer"
            :direction="direction">
            <BedManage :roomId="roomId" />
        </el-drawer>
    </el-row>
</template>

<script>
import BedManage from '@/views/user/Bed'
export default {
    components: { BedManage },
    data() {
        return {
            cover: '',
            roomId: null,
            data: {},
            filterText: '',
            currentPage: 1,
            pageSize: 10,
            totalItems: 0,
            dialogOperation: false, // 开关
            isOperation: false, // 开关-标识新增或修改
            tableData: [],
            selectedRows: [],
            hotelRoomQueryDto: {}, // 搜索条件
            hotels: [],
            searchTime: [],
            drawer: false,
            direction: 'rtl', // right to left
        };
    },
    created() {
        this.fetchFreshData();
        this.fetchUsers();
    },
    methods: {
        // 房间管理
        roomManage(hotelRoom) {
            this.roomId = hotelRoom.id;
            this.drawer = true;
        },
        // 封面上传回调
        handleCoverSuccess(res, file) {
            this.$notify({
                duration: 1500,
                title: '头像上传',
                message: res.code === 200 ? '上传成功' : '上传失败',
                type: res.code === 200 ? 'success' : 'error'
            });
            // 上传成功则更新封面
            if (res.code === 200) {
                this.cover = res.data;
            }
        },
        // 获取酒店信息
        fetchUsers() {
            this.$axios.post('/hotel/queryHotelSupplier', {}).then(res => {
                if (res.data.code === 200) {
                    this.hotels = res.data.data;
                }
            }).catch(error => {
                console.error("查询酒店信息异常：", error);
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
                title: 'Delete Hotel Room Data',
                text: `This action cannot be undone. Do you want to continue?`,
                icon: 'warning',
            });
            if (confirmed) {
                try {
                    let ids = this.selectedRows.map(entity => entity.id);
                    const response = await this.$axios.post(`/hotelRoom/batchDelete`, ids);
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
                    console.error(`酒店房间信息删除异常：`, e);
                }
            }
        },
        // 修改信息
        async updateOperation() {
            try {
                this.data.cover = this.cover;
                const response = await this.$axios.put('/hotelRoom/update', this.data);
                this.clearFormData();
                this.$swal.fire({
                    title: 'Hotel room Information Modification',
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
                this.data.cover = this.cover;
                const response = await this.$axios.post('/hotelRoom/save', this.data);
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
                    ...this.hotelRoomQueryDto
                };
                const response = await this.$axios.post('/hotelRoom/querySupplierRoom', params);
                const { data } = response;
                this.tableData = data.data;
                this.totalItems = data.total;
            } catch (error) {
                console.error('查询酒店房间信息异常:', error);
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