<template>
    <el-row style="background-color: #FFFFFF;padding: 5px 0;border-radius: 5px;">
        <el-row style="padding: 10px 5px;margin: 0 5px;">
            <el-row>
                <el-select @change="fetchFreshData" size="small" v-model="attractionQueryDto.categoryId"
                    style="margin-left: 5px;" placeholder="Category">
                    <el-option v-for="item in categories" :key="item.id" :label="item.name" :value="item.id">
                    </el-option>
                </el-select>
                <el-select @change="fetchFreshData" size="small" v-model="attractionQueryDto.status"
                    style="margin-left: 5px;" placeholder="Attraction Status">
                    <el-option v-for="item in statusList" :key="item.value" :label="item.label" :value="item.value">
                    </el-option>
                </el-select>
                <el-date-picker size="small" style="width: 220px;margin-left: 5px;" v-model="searchTime"
                    type="daterange" range-separator="To" start-placeholder="Creation Start"
                    end-placeholder="Creation End">
                </el-date-picker>
                <el-input size="small" style="width: 188px;margin-left: 5px;margin-right: 6px;"
                    v-model="attractionQueryDto.name" placeholder="Attraction Name" clearable
                    @clear="handleFilterClear">
                    <el-button slot="append" @click="handleFilter" icon="el-icon-search"></el-button>
                </el-input>
                <span style="float: right;">
                    <el-button size="small"
                        style="background-color: rgb(96, 98, 102);color: rgb(247,248,249);border: none;"
                        class="customer" type="info" @click="add()"><i class="el-icon-plus"></i>Add
                        Attraction</el-button>
                </span>
            </el-row>
        </el-row>
        <el-row style="margin: 0 15px;border-top: 1px solid rgb(245,245,245);">
            <el-table :data="tableData" style="width: 100%">
                <el-table-column prop="cover" label="Attraction Cover" width="120px">
                    <template slot-scope="scope">
                        <img :src="scope.row.cover" style="width: 88px;height: 55px;border-radius: 5px;" />
                    </template>
                </el-table-column>
                <el-table-column prop="name" label="Attraction Name"></el-table-column>
                <el-table-column prop="address" label="Location" width="160px"></el-table-column>
                <el-table-column prop="categoryName" label="Category" width="120px"></el-table-column>
                <el-table-column prop="supplierName" label="Supplier" width="120px"></el-table-column>
                <el-table-column prop="ratingScore" label="Rating" width="60px">
                    <template slot-scope="scope">
                        <span>{{ scope.row.ratingScore === null ? 0 : scope.row.ratingScore }}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="createTime" sortable width="168" label="Creation Time"></el-table-column>
                <el-table-column prop="isWord" width="108" label="Status">
                    <template slot-scope="scope">
                        <i v-if="!scope.row.status" style="margin-right: 5px;" class="el-icon-warning"></i>
                        <i v-else style="margin-right: 5px;color: rgb(253, 199, 50);" class="el-icon-success"></i>
                        <el-tooltip v-if="scope.row.status" class="item" effect="dark"
                            content="If set to unavailable, attraction services will be suspended"
                            placement="bottom-end">
                            <span style="text-decoration: underline;text-decoration-style: dashed;">Available</span>
                        </el-tooltip>
                        <span v-else>Unavailable</span>
                    </template>
                </el-table-column>
                <el-table-column label="Operations" width="180">
                    <template slot-scope="scope">
                        <span class="text-button" @click="TouristRouteHandle(scope.row)">Tour Route</span>
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
<el-dialog :show-close="false" :visible.sync="dialogOperation" width="45%">
    <div slot="title">
        <p class="dialog-title">{{ !isOperation ? 'Add New Attraction' : 'Edit Attraction Information' }}</p>
    </div>
    <div style="padding:0 20px;">
        <el-row>
            <span class="dialog-hover">Attraction Cover</span>
            <el-upload class="avatar-uploader" action="/api/online-travel-sys/v1.0/file/upload"
                :show-file-list="false" :on-success="handleCoverSuccess">
                <img v-if="cover" :src="cover" style="width: 250px;height: 150px;">
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
        </el-row>
        <el-row style="margin-bottom: 20px;">
            <span class="dialog-hover">Attraction Name</span>
            <input class="dialog-input" v-model="data.name" placeholder="Please enter" />
            <span class="dialog-hover">Attraction Address</span>
            <input class="dialog-input" v-model="data.address" placeholder="Please enter" />
            <div>
                <div>
                    <span class="dialog-hover">Attraction Details</span>
                </div>
                <!-- Original code, word misspelled... change v to c... -->
                <!-- <Editor height="300px" :reveiveContent="data.description" @on-receive="onReceive" /> -->
                <Editor height="300px" :receiveContent="data.description" @on-receive="onReceive" />
            </div>
            <div style="margin: 10px 0;">
                <span class="dialog-hover">Attraction Category</span>
                <el-select v-model="data.categoryId" placeholder="Please select category">
                    <el-option v-for="category in categories" :key="category.id" :label="category.name"
                        :value="category.id">
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
            type="info" @click="updateOperation()">Edit</el-button>
        <el-button class="customer" size="small" style="background-color: rgb(211, 241, 241);border: none;"
            @click="cannel()">Cancel</el-button>
    </span>
</el-dialog>
<el-dialog :show-close="false" :visible.sync="dialogLineOperation" width="28%">
    <div slot="title">
        <p class="dialog-title">{{ !isLineOperation ? 'Add New Attraction Route' : 'Edit Attraction Route Information' }}</p>
    </div>
    <div style="padding:0 20px;">
        <el-row>
            <span class="dialog-hover">Route Cover</span>
            <el-upload class="avatar-uploader" action="/api/online-travel-sys/v1.0/file/upload"
                :show-file-list="false" :on-success="handleCoverSuccess">
                <img v-if="cover" :src="cover" style="width: 250px;height: 150px;">
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
        </el-row>
        <el-row style="margin-bottom: 20px;">
            <div>
                <span class="dialog-hover">Route Introduction</span>
            </div>
            <el-input style="width: 100%;" type="textarea" :rows="2" placeholder="Please enter content"
                v-model="TouristRoute.description">
            </el-input>
            <div>
                <span class="dialog-hover">Priority</span>
            </div>
            <el-slider v-model="TouristRoute.level"></el-slider>
        </el-row>
            </div>
          <span slot="footer" class="dialog-footer">
    <el-button size="small" v-if="!isLineOperation"
        style="background-color: rgb(96, 98, 102);color: rgb(247,248,249);border: none;" class="customer"
        type="info" @click="addLineOperation()">Add Route</el-button>
    <el-button size="small" v-else
        style="background-color: rgb(96, 98, 102);color: rgb(247,248,249);border: none;" class="customer"
        type="info" @click="updateLineOperation()">Edit Route</el-button>
    <el-button class="customer" size="small" style="background-color: rgb(211, 241, 241);border: none;"
        @click="cannelLine()">Cancel</el-button>
</span>
</el-dialog>
<el-drawer title="Attraction Routes" width="35%" :visible.sync="drawer" :direction="direction">
    <div class="add-line">
        <span @click="addLine" style="cursor: pointer;font-size: 14px">
            <i class="el-icon-plus"></i>
            Add Route
        </span>
    </div>
    <div v-if="TouristRoutes.length === 0">
        <el-empty description="No route information available"></el-empty>
    </div>
    <div v-else>
        <div>
            <el-timeline>
                <el-timeline-item v-for="(TouristRoute, index) in TouristRoutes" :key="index"
                    :timestamp="TouristRoute.createTime" placement="top">
                    <el-card>
                        <div>
                            <img style="width: 100%;height: 200px;border-radius: 5px;" :src="TouristRoute.cover"
                                alt="Route Cover">
                        </div>
                        <p style="margin-bottom: 8px;font-size: 14px;color: rgb(97, 97, 97);">{{
                            TouristRoute.description }}
                        </p>
                        <div>
                            <el-tooltip class="item" effect="dark" content="Edit attraction route" placement="bottom">
                                <el-button @click="updateLine(TouristRoute)" type="primary" icon="el-icon-edit"
                                    circle></el-button>
                            </el-tooltip>
                            <el-tooltip class="item" effect="dark" content="Delete attraction route" placement="bottom">
                                <el-button @click="deleteLine(TouristRoute)" type="danger" icon="el-icon-delete"
                                    circle></el-button>
                            </el-tooltip>
                        </div>
                            </el-card>
                        </el-timeline-item>
                    </el-timeline>
                </div>
            </div>
        </el-drawer>
    </el-row>
</template>

<script>
import Editor from "@/components/Editor"
export default {
    components: { Editor },
    data() {
        return {
            TouristRoute: {},
            data: {},
            filterText: '',
            cover: '',
            currentPage: 1,
            drawer: false, // 控制景点路线的抽屉开关
            direction: 'rtl', // right to left
            pageSize: 10,
            totalItems: 0,
            dialogLineOperation: false, // 景点路线弹窗确认
            isLineOperation: false, // 景点路线操作时显示文本开关
            dialogOperation: false, // 开关
            isOperation: false, // 开关-标识新增或修改
            tableData: [],
            selectedRows: [],
            attractionQueryDto: {}, // 搜索条件
            categories: [],
            searchTime: [],
            TouristRoutes: [],
            users: [],
            isAuditList: [{ value: null, label: 'All' }, { value: 0, label: 'Not Audited' }, { value: 1, label: 'Audited' }],
            statusList: [{ value: null, label: 'All' }, { value: 0, label: 'Unavailable' }, { value: 1, label: 'Available' }],
        };
    },
    created() {
        this.fetchFreshData();
        this.fetchUsers();
        this.fetchCategories();
    },
    methods: {
        onReceive(content) {
            console.log("编辑景点信息回调：" + JSON.stringify(content));
            this.data.description = content;
        },
        // 修改景点信息
        updateLine(TouristRoute) {
            this.dialogLineOperation = true;
            this.isLineOperation = true;
            this.cover = TouristRoute.cover;
            this.TouristRoute = TouristRoute;
        },
            // Delete attraction information
        deleteLine(TouristRoute) {
            this.$confirm('This operation will delete this attraction route. Continue?', 'Operation Prompt', {
                confirmButtonText: 'Confirm',
                cancelButtonText: 'Cancel',
                type: 'warning'
            }).then(() => {
                let ids = [TouristRoute.id];
                this.$axios.post(`/TouristRoute/batchDelete`, ids).then(res => {
                    if (res.data.code === 200) {
                        this.$message.success('Attraction route deleted successfully');
                        this.fetchAttractionLines(this.data.id);
                    }
                })
            }).catch(() => {
                console.log("Operation cancelled");
            });
        },
        cannelLine() {
            this.TouristRoute = {};
            this.dialogLineOperation = false;
            this.isLineOperation = false;
            this.cover = '';
        },
        addLine() {
            this.dialogLineOperation = true;
        },
        TouristRouteHandle(attraction) {
            this.data = attraction;
            this.TouristRoute.attractionId = attraction.id;
            this.fetchAttractionLines(attraction.id);
        },
        // Request attraction route information
        fetchAttractionLines(attractionId) {
            const TouristRouteQueryDto = { attractionId: attractionId }
            this.$axios.post('/touristRoute/query', TouristRouteQueryDto).then(res => {
                if (res.data.code === 200) {
                    this.TouristRoutes = res.data.data;
                    this.drawer = true;
                }
            }).catch(error => {
                this.$message.error('Error querying attraction route information');
                console.error("Error querying attraction route information:", error);
            });
        },
        // Cover image upload
        handleCoverSuccess(res, file) {
            this.$notify({
                duration: 1500,
                title: 'Cover Upload',
                message: res.code === 200 ? 'Upload successful' : 'Upload failed',
                type: res.code === 200 ? 'success' : 'error'
            });
            // Update cover if upload successful
            if (res.code === 200) {
                this.cover = res.data;
            }
        },
        // Get attraction categories
        fetchCategories() {
            this.$axios.post('/attractionCategory/query', {}).then(res => {
                if (res.data.code === 200) {
                    this.categories = res.data.data;
                }
            }).catch(error => {
                console.error("Error querying attraction category information:", error);
            });
        },
        // 获取供应商信息
        fetchUsers() {
            this.$axios.post('/supplier/query', {}).then(res => {
                if (res.data.code === 200) {
                    this.users = res.data.data;
                }
            }).catch(error => {
                console.error("查询供应商信息异常：", error);
            });
        },
        // 置位
        cannel() {
            this.data = {};
            this.cover = '';
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
               title: 'Delete Attraction Data',
                text: `This action cannot be undone. Do you want to continue?`,
                icon: 'warning',
            });
            if (confirmed) {
                try {
                    let ids = this.selectedRows.map(entity => entity.id);
                    const response = await this.$axios.post(`/attraction/batchDelete`, ids);
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
                    console.error(`景点信息删除异常：`, e);
                }
            }
        },
        // 修改路线信息
        async updateLineOperation() {
            try {
                this.TouristRoute.cover = this.cover;
                const response = await this.$axios.put('/TouristRoute/update', this.TouristRoute);
                this.$swal.fire({
                    title: 'TouristRoute Information Modification',
                    text: response.data.msg,
                    icon: response.data.code === 200 ? 'success' : 'error',
                    showConfirmButton: false,
                    timer: 1000,
                });
                if (response.data.code === 200) {
                    this.fetchAttractionLines(this.data.id);
                    this.cannelLine();
                }
            } catch (error) {
                console.error('提交表单时出错:', error);
                this.$message.error('Submission failed. Please try again later.！');
            }
        },
        // 修改信息
        async updateOperation() {
            try {
                this.data.cover = this.cover;
                const response = await this.$axios.put('/attraction/update', this.data);
                this.clearFormData();
                this.$swal.fire({
                    title: 'Attraction information modification',
                    text: response.data.msg,
                    icon: response.data.code === 200 ? 'success' : 'error',
                    showConfirmButton: false,
                    timer: 1000,
                });
                if (response.data.code === 200) {
                    this.cannel();
                    this.fetchFreshData(this.data);
                }
            } catch (error) {
                console.error('提交表单时出错:', error);
                this.$message.error('Submission failed. Please try again later.！');
            }
        },
        // 信息新增
        async addLineOperation() {
            try {
                this.TouristRoute.attractionId = this.data.id;
                this.TouristRoute.cover = this.cover;
                const response = await this.$axios.post('/TouristRoute/save', this.TouristRoute);
                this.$message[response.data.code === 200 ? 'success' : 'error'](response.data.msg);
                if (response.data.code === 200) {
                    this.cannelLine();
                    this.fetchAttractionLines(this.data.id);
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
                const response = await this.$axios.post('/attraction/save', this.data);
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
                    ...this.attractionQueryDto
                };
                const response = await this.$axios.post('/attraction/querySupplierAttraction', params);
                const { data } = response;
                this.tableData = data.data;
                this.totalItems = data.total;
            } catch (error) {
                console.error('查询景点信息异常:', error);
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
            console.log("编辑的景点信息：" + JSON.stringify(row));
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
<style scoped lang="scss">
.add-line {
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: rgb(64, 158, 255);
    padding: 10px 20px;
    margin: 10px 20px;
    color: rgb(245, 245, 245);
    border-radius: 5px;
}
</style>