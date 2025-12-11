<template>
    <el-row style="background-color: #FFFFFF;padding: 5px 0;border-radius: 5px;">
        <el-row style="padding: 10px 5px;margin:0 10px;">
            <el-row>
                <el-select @change="fetchFreshData" size="small" v-model="AttractionQueryDto.categoryId"
                    style="margin-left: 5px;" placeholder="Category">
                    <el-option v-for="item in categories" :key="item.id" :label="item.name" :value="item.id">
                    </el-option>
                </el-select>
                <el-date-picker size="small" style="width: 260px;margin-left: 5px;" v-model="searchTime"
                    type="daterange" range-separator="to" start-placeholder="Creation begin "
                    end-placeholder="Creation end">
                </el-date-picker>
                <el-input size="small" style="width: 188px;margin-left: 5px;margin-right: 6px;"
                    v-model="AttractionQueryDto.name" placeholder="attractionName" clearable @clear="handleFilterClear">
                    <el-button slot="append" @click="handleFilter" icon="el-icon-search"></el-button>
                </el-input>
                <span style="float: right;">
                    <el-button size="small"
                        style="background-color: rgb(96, 98, 102);color: rgb(247,248,249);border: none;"
                        class="customer" type="info" @click="add()"><i class="el-icon-plus"></i>add
                        attraction</el-button>
                </span>
            </el-row>
        </el-row>
        <el-row style="margin: 0 25px;border-top: 1px solid rgb(245,245,245);">
            <el-table :data="tableData" style="width: 100%">

                <el-table-column prop="cover" label="Attraction cover" width="120px">
                    <template slot-scope="scope">
                        <img :src="scope.row.cover" style="width: 88px;height: 55px;border-radius: 5px;" />
                    </template>
                </el-table-column>
                <el-table-column prop="name" label="AttractionName"></el-table-column>
                <el-table-column prop="address" label="address" width="160px"></el-table-column>
                <el-table-column prop="categoryName" label="Category" width="120px"></el-table-column>
                <el-table-column prop="supplierName" label="Supplier" width="120px"></el-table-column>
                <el-table-column prop="ratingScore" label="RatingScore" width="100px">
                    <template slot-scope="scope">
                        <span>{{ scope.row.ratingScore === null ? 0 : scope.row.ratingScore }}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="createTime" sortable width="168" label="CreateTime"></el-table-column>

                <el-table-column prop="isWord" width="108" label="Status">
                    <template slot-scope="scope">
                        <i v-if="scope.row.status" style="margin-right: 5px;" class="el-icon-success"></i>
                        <i v-else style="margin-right: 5px;color: rgb(253, 199, 50);" class="el-icon-warning"></i>
                        <el-tooltip v-if="scope.row.status" class="item" effect="dark"
                            content="unavailable status, stop attraction service." placement="bottom-end">
                            <span style="text-decoration: underline;text-decoration-style: dashed;">available</span>
                        </el-tooltip>
                        <span v-else>unavailable</span>
                    </template>
                </el-table-column>
                <el-table-column label="operate" width="180">
                    <template slot-scope="scope">
                        <span class="text-button" @click="handleTouristRoute(scope.row)">Route</span>
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
                <p class="dialog-title">{{ !isOperation ? 'Add attraction' : 'Modify attraction info' }}</p>
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
                <el-row style="margin-top: 20px;">
                    <span class="dialog-hover">attraction name</span>
                    <input class="dialog-input" v-model="data.name" placeholder="Please Enter" />
                    <span class="dialog-hover">address</span>
                    <input class="dialog-input" v-model="data.address" placeholder="Address" />

                    <div style="margin: 10px 0;">
                        <span class="dialog-hover">Attraction Category</span>
                        <el-select v-model="data.categoryId" placeholder="please select category">
                            <el-option v-for="category in categories" :key="category.id" :label="category.name"
                                :value="category.id">
                            </el-option>
                        </el-select>
                    </div>
                    <div>
                        <span class="dialog-hover">Attraction Status</span>
                        <el-switch v-model="data.status" active-text="available" inactive-text="unavailable">
                        </el-switch>
                    </div>

                    <div style="margin: 10px 0;">
                        <span class="dialog-hover">Supplier</span>
                        <el-select v-model="data.supplierId" placeholder="please select supplier">
                            <el-option v-for="supplier in users" :key="supplier.id" :label="supplier.name"
                                :value="supplier.id">
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
            
        <el-dialog :show-close="false" :visible.sync="dialogLineOperation" width="28%">
            <div slot="title">
                <p class="dialog-title">{{ !isLineOperation ? 'Add Tourist Route' : 'Modify Route Information' }}</p>
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
                        <span class="dialog-hover">Attraction description</span>
                    </div>

                    <el-input style="width: 100%;" type="textarea" :rows="2" placeholder="Please Enter"
                        v-model="touristRoute.description">
                    </el-input>
                    <div>
                        <span class="dialog-hover">Priority</span>
                    </div>

                    <el-slider v-model="touristRoute.level"></el-slider>
                </el-row>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button size="small" v-if="!isLineOperation"
                    style="background-color: rgb(96, 98, 102);color: rgb(247,248,249);border: none;" class="customer"
                    type="info" @click="addLineOperation()">Add Route</el-button>
                <el-button size="small" v-else
                    style="background-color: rgb(96, 98, 102);color: rgb(247,248,249);border: none;" class="customer"
                    type="info" @click="updateLineOperation()">Modify Route</el-button>
                <el-button class="customer" size="small" style="background-color: rgb(211, 241, 241);border: none;"
                    @click="cannelLine()">cancel</el-button>
            </span>
        </el-dialog>
            <el-drawer title="Tourist Route" width="35%" :visible.sync="drawer" :direction="direction">
                <div class="add-line">
                    <span @click="addLine" style="cursor: pointer;font-size: 14px">
                        <i class="el-icon-plus"></i>
                        Add Route
                    </span>
                </div>
                <div v-if="touristRoutes.length === 0">
                    <el-empty description="No information of Route"></el-empty>
                </div>
                <div v-else>
                    <div>
                        <el-timeline>
                            <el-timeline-item v-for="(touristRoute, index) in touristRoutes" :key="index"
                                :timestamp="touristRoute.createTime" placement="top">
                                <el-card>
                                    <div>
                                        <img style="width: 100%;height: 200px;border-radius: 5px;"
                                            :src="touristRoute.cover" alt="Route Cover">
                                    </div>
                                    <p style="margin-bottom: 8px;font-size: 14px;color: rgb(97, 97, 97);">{{
                                        touristRoute.description }}
                                    </p>
                                    <div>
                                        <el-tooltip class="item" effect="dark" content="Modify Route" placement="bottom">
                                            <el-button @click="updateLine(touristRoute)" type="primary"
                                                icon="el-icon-edit" circle></el-button>
                                        </el-tooltip>
                                        <el-tooltip class="item" effect="dark" content="Delete Route" placement="bottom">
                                            <el-button @click="deleteLine(touristRoute)" type="danger"
                                                icon="el-icon-delete" circle></el-button>
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
    export default {
        data() {
            return {
                touristRoute: {},
                data: {},
                filterText: '',
                cover: '',
                currentPage: 1,
                drawer: false, // 控制景点路线的抽屉开关
                direction: 'rtl', // right to left
                pageSize: 10,
                totalItems: 0,
                dialogLineOperation: false, // 路线操作对话框
                isLineOperation: false,     // 标识新增或修改路线
                dialogOperation: false, // 开关
                isOperation: false, // 开关-标识新增或修改
                tableData: [],
                searchTime: [],
                selectedRows: [],
                AttractionQueryDto: {}, // 搜索条件
                categories: [],
                searchTime: [],
                touristRoutes: [],
                users: [],
                isAuditList: [{ value: null, label: 'all' }, { value: 0, label: 'Pending' }, { value: 1, label: 'Approved' }],
                statusList: [{ value: null, label: 'all' }, { value: 0, label: 'unavailable' }, { value: 1, label: 'available' }],
            };
        },
        created() {
            this.fetchFreshData();
            this.fetchUsers();
            this.fetchCategories();
        },
        methods: {
                    // 修改景点信息
        updateLine(touristRoute) {
            this.dialogLineOperation = true;
            this.isLineOperation = true;
            this.cover = touristRoute.cover;
            this.touristRoute = touristRoute;
        },
    // Delete scenic spot information
    deleteLine(touristRoute) {
        this.$confirm('This operation will delete this scenic route. Continue?', 'Operation Confirmation', {
            confirmButtonText: 'Confirm',
            cancelButtonText: 'Cancel',
            type: 'warning'
        }).then(() => {
            let ids = [touristRoute.id];
            this.$axios.post(`/touristRoute/batchDelete`, ids).then(res => {
                if (res.data.code === 200) {
                    this.$message.success('Scenic route deleted successfully');
                    this.fetchTouristRoutes(this.data.id);
                }
            })
        }).catch(() => {
            console.log("Operation cancelled");
        });
        },
        cannelLine() {
            this.touristRoute = {};
            this.dialogLineOperation = false;
            this.isLineOperation = false;
            this.cover = '';
        },
        addLine() {
            this.dialogLineOperation = true;
        },
        fetchTouristRoutes(attractionId) {
                const touristRouteQueryDto = { attractionId: attractionId }
                this.$axios.post('/touristRoute/query', touristRouteQueryDto).then(res => {
                    if (res.data.code === 200) {
                        this.touristRoutes = res.data.data;
                        this.drawer = true;
                    }
                }).catch(error => {
                    this.$message.error('Error retrieving attraction route information.');
                    console.error("Error retrieving attraction route information.：", error);
                });
            },
                // 修改路线信息
        async updateLineOperation() {
            try {
                this.touristRoute.cover = this.cover;
                const response = await this.$axios.put('/touristRoute/update', this.touristRoute);
                this.$swal.fire({
                    title: 'Tourist route information modification',
                    text: response.data.msg,
                    icon: response.data.code === 200 ? 'success' : 'error',
                    showConfirmButton: false,
                    timer: 1000,
                });
                if (response.data.code === 200) {
                    this.fetchTouristRoutes(this.data.id);
                    this.cannelLine();
                }
            } catch (error) {
                console.error('提交表单时出错:', error);
                this.$message.error('Submission failed. Please try again later.！');
            }
        },
                    // 信息新增
        async addLineOperation() {
            try {
                this.touristRoute.attractionId = this.data.id;
                this.touristRoute.cover = this.cover;
                const response = await this.$axios.post('/touristRoute/save', this.touristRoute);
                this.$message[response.data.code === 200 ? 'success' : 'error'](response.data.msg);
                if (response.data.code === 200) {
                    this.cannelLine();
                    this.fetchTouristRoutes(this.data.id);
                }
            } catch (error) {
                console.error('提交表单时出错:', error);
                this.$message.error('Submission failed. Please try again later.！');
            }
        },
            handleTouristRoute(attraction) {
                this.data = attraction;
                this.touristRoute.attractionId = attraction.id;
                this.fetchTouristRoutes(attraction.id);
            },
            handleCoverSuccess(res, file) {
                this.$notify({
                    duration: 1500,
                    title: 'cover upload',
                    message: res.code === 200 ? 'Succeed' : 'Fail',
                    type: res.code === 200 ? 'success' : 'error'
                });
                // 上传成功则更新封面
                if (res.code === 200) {
                    this.cover = res.data;
                }
            },
            fetchCategories() {
                const queryParams = {
                    current: 1,
                    size: 100, // 获取足够多的分类数据
                };
                this.$axios.post('/attractionCategory/query', queryParams).then(res => {
                    if (res.data.code == 200) {
                        this.categories = res.data.data;
                    }
                }).catch(error => {
                    console.log("Query user info error", error);
                });
            },
            //get userId
            fetchUsers() {
                const queryParams = {
                    pageNum: this.currentPage || 1,      // 当前页码
                    pageSize: this.pageSize || 10,       // 每页大小

                };

                this.$axios.post('/supplier/query', queryParams).then(res => {
                    if (res.data.code == 200) {
                        this.users = res.data.data;
                    }
                }).catch(error => {
                    console.log("Query user info error", error);
                });
            },
            cancel() {
                this.data = {};
                this.cover = '',
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
                    title: ' delete attraction',
                    text: `cannot recover after deletion,continue?`,
                    icon: 'warning',
                });
                if (confirmed) {
                    try {
                        let ids = this.selectedRows.map(entity => entity.id);
                        const response = await this.$axios.post(`/attraction/batchDelete`, ids);
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
                        console.error(`attraction info delete error:`, e);
                    }
                }
            },

            // 修改信息
            async updateOperation() {

                try {
                    this.data.cover = this.cover;
                    const response = await this.$axios.put('/attraction/update', this.data);
                    this.clearFormData();
                    this.$swal.fire({
                        title: 'attraction info modification',
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
                    this.data.cover = this.cover;
                    const response = await this.$axios.post('/attraction/save', this.data);
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
                        ...this.AttractionQueryDto
                    };
                    const response = await this.$axios.post('/attraction/query', params);
                    const { data } = response;
                    this.tableData = data.data;
                    this.totalItems = data.total;
                } catch (error) {
                    console.error('Query attraction error:', error);
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