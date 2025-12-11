<template>
    <el-row style="background-color: #FFFFFF;padding: 5px 0;border-radius: 5px;">
        <el-row style="padding: 10px 5px;margin: 0 5px;">
            <el-row>
                <span style="display: inline-block;font-size: 26px;font-weight: 800;padding-left: 15px;">Travel
                    Guides</span>
                <span style="float: right;">
                    <el-input size="small" style="width: 188px;margin-left: 5px;margin-right: 6px;"
                        v-model="attractionStrategyQueryDto.title" placeholder="Guide Title" clearable
                        @clear="handleFilterClear">
                        <el-button slot="append" @click="handleFilter" icon="el-icon-search"></el-button>
                    </el-input>
                </span>
            </el-row>
        </el-row>
        <el-row style="margin: 0 15px;border-top: 1px solid rgb(245,245,245);">

            <div>
                <div class="strategy-item" v-for="(strategy, index) in tableData" :key="index">
                    <div class="left">
                        <img :src="strategy.cover" :alt="strategy.title" srcset="">
                    </div>
                    <div class="right">
                        <div class="user">
                            <img :src="strategy.userAvatar" alt="" srcset="">
                            <span>{{ strategy.userName }}</span>
                        </div>
                        <div class="title" @click="readStrategy(strategy)">{{ strategy.title }}</div>
                        <div class="detail">{{ strategy.description }}</div>
                        <div class="info">
                            <span>{{ strategy.attractionName }}</span>
                            <span>{{ strategy.createTime }}</span>
                        </div>
                    </div>
                </div>
            </div>

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
        readStrategy(strategy) {
            sessionStorage.setItem('strategyId', strategy.id);
            this.$router.push('/strategyDetail');
        },
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
                this.$axios.post(`/attractionStrategyStrategy/batchDelete`, ids).then(res => {
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
                const response = await this.$axios.post('/attractionStrategy/queryList', params);
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
<style scoped lang="scss">
.strategy-item {
    display: flex;
    justify-content: left;
    padding: 10px 0;
    cursor: pointer;

    .left {
        padding: 5px;
        box-sizing: border-box;

        img {
            width: 238px;
            height: 160px;
            border-radius: 5px;
        }
    }

    .right {
        padding: 5px;
        box-sizing: border-box;

        .user {
            display: flex;
            justify-content: left;
            align-items: center;
            margin-block: 4px;

            img {
                width: 20px;
                height: 20px;
                border-radius: 10px;
            }

            span {
                margin-left: 4px;
                font-size: 14px;
            }
        }

        .title {
            font-size: 20px;
            font-weight: 800;
            padding-bottom: 6px;
        }

        .title:hover {
            text-decoration: underline;
            text-decoration-style: dashed;
        }

        .detail {
            font-size: 14px;
            color: rgb(131, 130, 130);
        }

        .info {
            font-size: 12px;
            margin-top: 10px;
            display: flex;
            justify-content: left;
            gap: 10px;

            span:first-child {
                display: inline-block;
                padding: 1px 3px;
                border-radius: 2px;
                background-color: rgb(237, 243, 249);
                color: rgb(136, 115, 233);
            }
        }
    }

}
</style>