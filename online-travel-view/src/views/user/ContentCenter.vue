<template>
    <el-row style="background-color: #FFFFFF;padding: 5px 0;border-radius: 5px;">
        <el-row style="padding: 10px 5px;margin: 0 5px;">
            <el-row>
                <el-select @change="fetchFreshData" size="small" v-model="attractionStrategyQueryDto.isAudit"
                    style="margin-left: 5px;" placeholder="Audit Status">
                    <el-option v-for="item in isAuditList" :key="item.value" :label="item.label" :value="item.value">
                    </el-option>
                </el-select>
                <el-date-picker size="small" style="width: 220px;margin-left: 5px;" v-model="searchTime"
                    type="daterange" range-separator="to" start-placeholder="Start Date" end-placeholder="End Date">
                </el-date-picker>
                <el-input size="small" style="width: 188px;margin-left: 5px;margin-right: 6px;"
                    v-model="attractionStrategyQueryDto.title" placeholder="Guide Title" clearable
                    @clear="handleFilterClear">
                    <el-button slot="append" @click="handleFilter" icon="el-icon-search"></el-button>
                </el-input>
            </el-row>
        </el-row>
        <el-row style="margin: 0 15px;border-top: 1px solid rgb(245,245,245);">
            <el-table :data="tableData" style="width: 100%">
                <el-table-column prop="cover" label="Cover Image" width="120px">
                    <template slot-scope="scope">
                        <img :src="scope.row.cover" style="width: 88px;height: 55px;border-radius: 5px;" />
                    </template>
                </el-table-column>
                <el-table-column prop="title" label="Guide Title"></el-table-column>
                <el-table-column prop="attractionId" sortable label="Attraction ID" width="90px"></el-table-column>
                <el-table-column prop="attractionName" label="Related Attraction" width="120px"></el-table-column>
                <el-table-column prop="createTime" sortable width="168" label="Created At"></el-table-column>
                <el-table-column prop="isWord" width="108" label="Audit Status">
                    <template slot-scope="scope">
                        <i v-if="!scope.row.isAudit" style="margin-right: 5px;" class="el-icon-warning"></i>
                        <i v-else style="margin-right: 5px;color: rgb(253, 199, 50);" class="el-icon-success"></i>
                        <el-tooltip v-if="!scope.row.isAudit" class="item" effect="dark"
                            content="If not audited, it will not be displayed on the user side" placement="bottom-end">
                            <span style="text-decoration: underline;text-decoration-style: dashed;">Not Audited</span>
                        </el-tooltip>
                        <span v-else>Audited</span>
                    </template>
                </el-table-column>
                <el-table-column label="Operations" width="180">
                    <template slot-scope="scope">
                        <span class="text-button" @click="handleEdit(scope.row)">Edit</span>
                        <span class="text-button" @click="viewStrategyContent(scope.row)">View Guide</span>
                        <span class="text-button" @click="handleDelete(scope.row)">Delete</span>
                    </template>
                </el-table-column>
            </el-table>
            <el-pagination style="margin:10px 0;" @size-change="handleSizeChange" @current-change="handleCurrentChange"
                :current-page="currentPage" :page-sizes="[10, 20]" :page-size="pageSize"
                layout="total, sizes, prev, pager, next, jumper" :total="totalItems"></el-pagination>
        </el-row>
        <el-drawer title="View Content" width="35%" :visible.sync="drawer" :direction="direction">
            <div style="margin: 20px;" v-html="data.content"></div>
        </el-drawer>
    </el-row>
    
</template>

<script>
export default {
    data() {
        return {
            data: {},
            filterText: '',
            cover: '',
            currentPage: 1,
            drawer: false, // 控制攻略路线的抽屉开关
            direction: 'rtl', // right to left
            pageSize: 10,
            totalItems: 0,
            tableData: [],
            selectedRows: [],
            attractionStrategyQueryDto: {}, // 搜索条件
            searchTime: [],
            isAuditList: [{ value: null, label: 'All' }, { value: 0, label: '未审核' }, { value: 1, label: '已审核' }],
        };
    },
    created() {
        this.fetchFreshData();
    },
    methods: {
        viewStrategyContent(attractionStrategy) {
            this.data = attractionStrategy;
            this.drawer = true;
        },
        // Delete strategy information
        deleteLine(touristRoute) {
            this.$confirm('This operation will delete this scenic route. Do you want to continue?', 'Operation Prompt', {
                confirmButtonText: 'Confirm',
                cancelButtonText: 'Cancel',
                type: 'warning'
            }).then(() => {
                let ids = [touristRoute.id];
                this.$axios.post('/attractionStrategyStrategy/batchDelete', ids).then(res => {
                    if (res.data.code === 200) {
                        this.$message.success('Strategy route deleted successfully');
                        this.fetchTouristRoutes(this.data.id);
                    }
                })
            }).catch(() => {
                console.log("Operation canceled");
            });
        },
        // 批量删除数据
        async batchDelete() {
            if (!this.selectedRows.length) {
                this.$message(`No data selected.`);
                return;
            }
            const confirmed = await this.$swalConfirm({
             title: 'Delete Strategy Data',
            text: `This action cannot be undone. Do you want to continue?`,
            icon: 'warning',
            });
            if (confirmed) {
                try {
                    let ids = this.selectedRows.map(entity => entity.id);
                    const response = await this.$axios.post(`/attractionStrategy/batchDelete`, ids);
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
                    console.error(`攻略信息删除异常：`, e);
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
                    ...this.attractionStrategyQueryDto
                };
                const response = await this.$axios.post('/attractionStrategy/queryUser', params);
                const { data } = response;
                this.tableData = data.data;
                this.totalItems = data.total;
            } catch (error) {
                console.error('查询攻略信息异常:', error);
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
            // 将当前对象的信息存储起来
            sessionStorage.setItem('strategyInfo', JSON.stringify(row));
            this.$router.push('/editStrategy');
        },
        handleDelete(row) {
            this.selectedRows.push(row);
            this.batchDelete();
        }
    },
};
</script>
<style scoped lang="scss"></style>