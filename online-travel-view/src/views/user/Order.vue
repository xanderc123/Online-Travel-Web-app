<template>
    <el-row style="background-color: #FFFFFF;padding: 5px 30px;border-radius: 5px;">
        <el-row>
            <h1 style="margin-inline: 20px;">Ticket Order Details</h1>
            <el-row style="padding: 10px;margin: 0 5px;">
                <el-row>
                    <el-select @change="fetchFreshData" size="small" v-model="attractionTicketOrderQueryDto.payStatus"
                        style="margin-left: 5px;" placeholder="Payment Status">
                        <el-option v-for="item in statusList" :key="item.value" :label="item.label" :value="item.value">
                        </el-option>
                    </el-select>
                    <el-date-picker size="small" style="width: 220px;margin-left: 5px;" v-model="searchTime"
                        type="daterange" range-separator="to" start-placeholder="Start Date" end-placeholder="End Date">
                    </el-date-picker>
                </el-row>
            </el-row>
            <el-row style="margin: 0 15px;border-top: 1px solid rgb(245,245,245);">
                <el-table :data="tableData" style="width: 100%">
                    <el-table-column prop="attractionId" sortable width="120" label="Attraction ID"></el-table-column>
                    <el-table-column prop="attractionName" label="Attraction Name"></el-table-column>
                    <el-table-column prop="contactPerson" width="120" label="Contact Person"></el-table-column>
                    <el-table-column prop="contactPhone" width="120" label="Contact Phone"></el-table-column>
                    <el-table-column prop="buyNumber" sortable width="120" label="Ticket Count"></el-table-column>
                    <el-table-column prop="amount" sortable width="120" label="Amount"></el-table-column>
                    <el-table-column prop="payTime" sortable width="168" label="Payment Time"></el-table-column>
                    <el-table-column prop="createTime" sortable width="168" label="Creation Time"></el-table-column>
                    <el-table-column prop="useStatus" width="98" label="Payment Status">
                        <template slot-scope="scope">
                            <i v-if="!scope.row.payStatus" style="margin-right: 5px;" class="el-icon-warning"></i>
                            <i v-else style="margin-right: 5px;color: rgb(253, 199, 50);" class="el-icon-success"></i>
                            <el-tooltip v-if="!scope.row.payStatus" class="item" effect="dark"
                                content="Not paid, cannot reorder" placement="bottom-end">
                                <span style="text-decoration: underline;text-decoration-style: dashed;">Not Paid</span>
                            </el-tooltip>
                            <span v-else>Paid</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="Actions" width="110">
                        <template slot-scope="scope">
                            <span class="text-button" v-if="!scope.row.payStatus"
                                @click="handlePay(scope.row)">Pay</span>
                            <span class="text-button" @click="handleDelete(scope.row)">Delete</span>
                        </template>
                    </el-table-column>
                </el-table>
                <el-pagination style="margin:10px 0;" @size-change="handleSizeChange"
                    @current-change="handleCurrentChange" :current-page="currentPage" :page-sizes="[10, 20]"
                    :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper"
                    :total="totalItems"></el-pagination>
            </el-row>
            <el-row>
                <LineChart height="420px" tag="Consumption Records ($)" @on-selected="selected" :values="values"
                    :date="dates" />
            </el-row>
            <el-card shadow="hover" style="margin-top: 20px; border-color: #67C23A;">
                <div slot="header">
                    <span style="font-weight: bold;">📊 Financial Statistics (Powered by Stored Procedure)</span>
                </div>
                <el-row type="flex" align="middle">
                    <el-date-picker v-model="procDateRange" type="daterange" range-separator="to"
                        start-placeholder="Start Date" end-placeholder="End Date" value-format="yyyy-MM-dd HH:mm:ss"
                        size="small">
                    </el-date-picker>
                    <el-button type="success" size="small" style="margin-left: 10px;" @click="handleCalcRevenue">
                        Call Stored Procedure
                    </el-button>
                    <span style="margin-left: 20px; font-size: 16px;">
                        Total Revenue: <span style="color: red; font-weight: bold; font-size: 20px;">{{ revenueResult
                            }}</span> Dollar
                    </span>
                </el-row>
            </el-card>
            <el-row>
                <HotelOrderInfo />
            </el-row>
        </el-row>
    </el-row>
</template>
<script>
import LineChart from "@/components/LineChart"
import HotelOrderInfo from "@/views/user/HotelOrderInfo"
export default {
    components: { LineChart, HotelOrderInfo },
    data() {
        return {
            filterText: '',
            currentPage: 1,
            pageSize: 10,
            totalItems: 0,
            queryDays: 365,
            tableData: [],
            selectedRows: [],
            attractionTicketOrderQueryDto: {}, // 搜索条件
            searchTime: [],
            values: [],
            dates: [],
            statusList: [
                { value: null, label: 'All' },
                { value: 0, label: 'Unpaid' },
                { value: 1, label: 'Paid' }
            ],
            procDateRange: [], // 存储过程用的时间
            revenueResult: 0   // 结果
        };
    },
    created() {
        this.fetchFreshData();
        // 数据太少，默认查365天
        this.selected(this.queryDays);
    },
    methods: {
        // 请求后端的金额成交数据
        selected(time) {
            this.$axios.get(`/attractionTicketOrder/daysQueryUser/${time}`).then(response => {
                const { data } = response;
                if (data.code === 200) {
                    this.values = data.data.map(entity => entity.count);
                    this.dates = data.data.map(entity => entity.name);
                }
            })
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
                title: 'Delete Ticket Order Data',
                text: `This action cannot be undone. Do you want to continue?`,
                icon: 'warning',
            });
            if (confirmed) {
                try {
                    let ids = this.selectedRows.map(entity => entity.id);
                    const response = await this.$axios.post(`/attractionTicketOrder/batchDelete`, ids);
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
                    console.error(`门票订单信息删除异常：`, e);
                }
            }
        },
        // 修改信息
        async updateOperation() {
            try {
                const response = await this.$axios.put('/attractionTicketOrder/update', this.data);
                this.clearFormData();
                this.$swal.fire({
                    title: 'Ticket Order Information Modification',
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
                const response = await this.$axios.post('/attractionTicketOrder/save', this.data);
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
                    ...this.attractionTicketOrderQueryDto
                };
                const response = await this.$axios.post('/attractionTicketOrder/queryUser', params);
                const { data } = response;
                this.tableData = data.data;
                this.totalItems = data.total;
            } catch (error) {
                console.error('查询门票订单信息异常:', error);
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
        async handleCalcRevenue() {
            if (!this.procDateRange || this.procDateRange.length < 2) {
                this.$message.warning("Please select a date range");
                return;
            }
            try {
                const res = await this.$axios.post('/advanced/calculateRevenue', {
                    startDate: this.procDateRange[0],
                    endDate: this.procDateRange[1]
                });
                if (res.data.code === 200) {
                    this.revenueResult = res.data.revenue;
                    this.$message.success("Stored procedure executed successfully!");
                }
            } catch (error) {
                this.$message.error("Calculation failed");
            }
        },
        // 处理支付的方法
        handlePay(row) {
            const sceniTicketOrder = { id: row.id }
            this.$axios.post('/attractionTicketOrder/pay', sceniTicketOrder).then(res => {
                if (res.data.code === 200) {
                    this.$message.success('Pay Successfully');
                    this.fetchFreshData();
                    this.selected(this.queryDays);
                }
            }).catch(error => {
                console.error("支付错误：", error);
            })
        },
        handleDelete(row) {
            this.selectedRows.push(row);
            this.batchDelete();
        }
    },
};
</script>
<style scoped lang="scss"></style>