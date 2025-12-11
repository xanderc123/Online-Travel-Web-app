<template>
    <el-row style="background-color: #FFFFFF;padding: 5px 0;border-radius: 5px;">
        <el-row style="padding: 10px 5px;margin: 0 5px;">
            <el-row>
                <span style="display: inline-block;font-size: 26px;font-weight: 800;padding-left: 15px;">Hotel
                    Information</span>
                <span style="float: right;">
                    <el-date-picker size="small" style="width: 220px;margin-left: 5px;" v-model="searchTime"
                        type="daterange" range-separator="to" start-placeholder="Start Date" end-placeholder="End Date">
                    </el-date-picker>
                    <el-input size="small" style="width: 188px;margin-left: 5px;margin-right: 6px;"
                        v-model="attractionQueryDto.name" placeholder="Hotel Name" clearable @clear="handleFilterClear">
                        <el-button slot="append" @click="handleFilter" icon="el-icon-search"></el-button>
                    </el-input>
                </span>
            </el-row>
        </el-row>
        <el-row style="margin: 0 15px;border-top: 1px solid rgb(245,245,245);">
            <el-row v-if="tableData.length === 0">
                <el-empty description="No hotel information available"></el-empty>
            </el-row>
            <el-row v-else class="attraction-item">
                <el-col :span="4" @click.native="hotelClick(hotel)" v-for="(hotel, index) in tableData" :key="index">
                    <div class="item">
                        <img :src="hotel.cover" alt="" style="height: 140px;" srcset="">
                        <div class="name">{{ hotel.name }}</div>
                        <div>
                            <span class="address">
                                <i class="el-icon-location"></i>
                                {{ hotel.address }}
                            </span>
                        </div>
                        <div class="info">
                            <span class="time">{{ timeAgo(hotel) }}</span>
                            <span>Contact Phone&nbsp;{{ hotel.contactNum }}</span>
                        </div>
                        <div class="extra-info">
                            <span>{{ hotel.supplierName }}</span>
                        </div>
                    </div>
                </el-col>
            </el-row>
            <el-pagination style="margin:10px 0;" @size-change="handleSizeChange" @current-change="handleCurrentChange"
                :current-page="currentPage" :page-sizes="[10, 20]" :page-size="pageSize"
                layout="total, sizes, prev, pager, next, jumper" :total="totalItems"></el-pagination>
        </el-row>
    </el-row>
</template>

<script>
import { timeAgo } from "@/utils/data"
export default {
    data() {
        return {
            data: {},
            filterText: '',
            currentPage: 1,
            pageSize: 10,
            totalItems: 0,
            tableData: [],
            attractionQueryDto: {}, // 搜索条件
            searchTime: [],
        };
    },
    created() {
        this.fetchFreshData();
    },
    methods: {
        hotelClick(hotel) {
            sessionStorage.setItem('hotelInfo', JSON.stringify(hotel));
            // 跳转至酒店详情页
            this.$router.push('/hotelDetail');
        },
        timeAgo(attraction) {
            return timeAgo(attraction.createTime);
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
                const response = await this.$axios.post('/hotel/query', params);
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
    },
};
</script>
<style scoped lang="scss">
.attraction-item {
    .item:hover {
        background-color: rgb(248, 248, 248);
    }

    .item {
        margin-block: 20px;
        padding: 30px 10px;
        box-sizing: border-box;
        cursor: pointer;

        .extra-info {
            display: flex;
            justify-content: left;
            align-items: center;
            gap: 5px;
            font-size: 12px;

            span:last-child {
                display: inline-block;
                padding: 3px 6px;
                border-radius: 3px;
                background-color: aliceblue;
                color: rgb(84, 49, 223);
            }
        }

        img {
            width: 100%;
            min-height: 120px;
            max-height: 140px;
            border-radius: 5px;
        }

        .address {
            margin-top: 10px;
            display: inline-block;
            width: 100px;
            font-size: 12px;
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;
        }

        // .name {
        //     font-size: 20px;
        //     font-weight: 800;
        // }

        .name {
            width: 140px;
            font-size: 22px;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
        }

        .info {
            display: flex;
            justify-content: left;
            gap: 5px;
            margin-block: 6px;

            span {
                font-size: 12px;
            }
        }
    }

}
</style>