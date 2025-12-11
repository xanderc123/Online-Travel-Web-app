<template>
    <el-row style="background-color: #FFFFFF;padding: 5px 0;border-radius: 5px;">
        <el-row style="padding: 10px;margin: 0 5px;">
            <el-row>
                <span>
                    <el-button size="small"
                        style="background-color: rgb(96, 98, 102);color: rgb(247,248,249);border: none;"
                        class="customer" type="info" @click="add()"><i class="el-icon-plus"></i>Add Attraction
                        Ticket</el-button>
                </span>
            </el-row>
        </el-row>
        <el-row style="margin: 0 15px;border-top: 1px solid rgb(245,245,245);">
            <el-table :data="tableData" style="width: 100%">
                <el-table-column prop="attractionId" sortable width="120" label="Attraction ID"></el-table-column>
                <el-table-column prop="attractionName" label="Associated Attraction"></el-table-column>
                <el-table-column prop="detail" width="200" label="Ticket Description"></el-table-column>
                <el-table-column prop="number" width="100" label="Quantity"></el-table-column>
                <el-table-column prop="price" width="100" label="Price"></el-table-column>
                <el-table-column prop="discount" width="100" label="Discount">
                    <template slot-scope="scope">
                        <span>{{ scope.row.discount === null ? 'No Discount' : scope.row.discount + '% Off' }}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="createTime" sortable width="168" label="Creation Time"></el-table-column>
                <el-table-column prop="ticketStatus" width="98" label="Availability">
                    <template slot-scope="scope">
                        <i v-if="!scope.row.ticketStatus" style="margin-right: 5px;" class="el-icon-warning"></i>
                        <i v-else style="margin-right: 5px;color: rgb(253, 199, 50);" class="el-icon-success"></i>
                        <el-tooltip v-if="scope.row.ticketStatus" class="item" effect="dark"
                            content="Tickets are available for user purchase only when enabled" placement="bottom-end">
                            <span style="text-decoration: underline;text-decoration-style: dashed;">Available</span>
                        </el-tooltip>
                        <span v-else>Unavailable</span>
                    </template>
                </el-table-column>
                <el-table-column label="Operations" width="110">
                    <template slot-scope="scope">
                        <span class="text-button" @click="handleEdit(scope.row)">Edit</span>
                        <span class="text-button" @click="handleDelete(scope.row)">Delete</span>
                    </template>
                </el-table-column>
            </el-table>
        </el-row>
        <!-- Operation Panel -->
        <el-dialog :show-close="false" :visible.sync="dialogOperation" width="32%">
            <div slot="title">
                <p class="dialog-title">{{ !isOperation ? 'Add Attraction Ticket' : 'Edit Attraction Ticket Information'
                    }}</p>
            </div>
            <div style="padding:0 20px;">
                <el-row style="margin-bottom: 20px;">
                    <span class="dialog-hover">Ticket Quantity</span>
                    <input class="dialog-input" v-model="data.number" placeholder="Please enter" />
                    <span class="dialog-hover">Price</span>
                    <input class="dialog-input" v-model="data.price" placeholder="Please enter" />
                    <span class="dialog-hover">Discount</span>
                    <input class="dialog-input" v-model="data.discount" placeholder="Please enter" />
                    <div>
                        <div>
                            <span class="dialog-hover">Description</span>
                        </div>
                        <el-input style="width: 100%;" type="textarea" :rows="2" placeholder="Please enter content"
                            v-model="data.detail">
                        </el-input>
                    </div>
                    <div style="margin: 10px 0;">
                        <span class="dialog-hover">Attraction</span>
                        <el-select v-model="data.attractionId" placeholder="Select Attraction">
                            <el-option v-for="attraction in attractions" :key="attraction.id" :label="attraction.name"
                                :value="attraction.id">
                            </el-option>
                        </el-select>
                    </div>
                    <div>
                        <span class="dialog-hover">Availability Status</span>
                        <el-switch v-model="data.ticketStatus" active-text="Available" inactive-text="Unavailable">
                        </el-switch>
                    </div>
                </el-row>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button size="small" v-if="!isOperation"
                    style="background-color: rgb(96, 98, 102);color: rgb(247,248,249);border: none;" class="customer"
                    type="info" @click="addOperation()">Add</el-button>
                <el-button size="small" v-else
                    style="background-color: rgb(96, 98, 102);color: rgb(247,248,249);border: none;" class="customer"
                    type="info" @click="updateOperation()">Update</el-button>
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
            data: {},
            filterText: '',
            totalItems: 0,
            dialogOperation: false, // 开关
            isOperation: false, // 开关-标识新增或修改
            tableData: [],
            selectedRows: [],
            attractionTicketQueryDto: {}, // 搜索条件
            attractions: [],
            searchTime: [],
            statusList: [{ value: null, label: 'All' }, { value: 0, label: 'Unavailable' }, { value: 1, label: 'Available' }],
        };
    },
    created() {
        this.fetchFreshData();
        this.fetchAttractions();
    },
    methods: {
        // 获取名下的景点信息
        fetchAttractions() {
            this.$axios.post('/attraction/querySupplierAttraction', {}).then(res => {
                if (res.data.code === 200) {
                    this.attractions = res.data.data;
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
                title: 'Delete Scenic Spot Ticket Data',
                text: 'This action cannot be undone. Do you want to continue?',
                icon: 'warning',
            });
            if (confirmed) {
                try {
                    let ids = this.selectedRows.map(entity => entity.id);
                    const response = await this.$axios.post(`/attractionTicket/batchDelete`, ids);
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
                    console.error(`景区门票信息删除异常：`, e);
                }
            }
        },
        // 修改信息
        async updateOperation() {
            try {
                const response = await this.$axios.put('/attractionTicket/update', this.data);
                this.clearFormData();
                this.$swal.fire({
                    title: 'Attraction ticket information modification',
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
                const response = await this.$axios.post('/attractionTicket/save', this.data);
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
                const response = await this.$axios.post('/attractionTicket/queryAttractionTicket');
                const { data } = response;
                this.tableData = data.data;
            } catch (error) {
                console.error('查询景区门票信息异常:', error);
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