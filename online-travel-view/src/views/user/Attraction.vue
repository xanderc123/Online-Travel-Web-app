<template>
    <el-row style="background-color: #FFFFFF;padding: 5px 0;border-radius: 5px;">
        <el-row style="padding: 10px 5px;margin: 0 5px;box-sizing: border-box;">
            <el-row>
                <span style="display: inline-block;font-size: 26px;font-weight: 800;padding-left: 15px;">Attraction
                    Information</span>
                <span style="float: right;">
                    <el-select @change="fetchFreshData" size="small" v-model="attractionQueryDto.categoryId"
                        style="margin-left: 5px;" placeholder="Category">
                        <el-option v-for="item in categories" :key="item.id" :label="item.name" :value="item.id">
                        </el-option>
                    </el-select>
                    <el-date-picker size="small" style="width: 220px;margin-left: 5px;" v-model="searchTime"
                        type="daterange" range-separator="to" start-placeholder="Start Date" end-placeholder="End Date">
                    </el-date-picker>
                    <el-input size="small" style="width: 188px;margin-left: 5px;margin-right: 6px;"
                        v-model="attractionQueryDto.name" placeholder="Attraction Name" clearable
                        @clear="handleFilterClear">
                        <el-button slot="append" @click="handleFilter" icon="el-icon-search"></el-button>
                    </el-input>
                </span>
            </el-row>
        </el-row>
        <el-row style="margin: 0 15px;border-top: 1px solid rgb(245,245,245);">
            <el-row v-if="tableData.length === 0">
                <el-empty description="No attraction information available"></el-empty>
            </el-row>
            <el-row v-else class="attraction-item">
                <el-col :span="6" @click.native="attractionClick(attraction)" v-for="(attraction, index) in tableData"
                    :key="index">
                    <div class="item" style="box-sizing: border-box;">
                        <img :src="attraction.cover" style="height: 166px;" alt="" srcset="">
                        <div class="name">{{ attraction.name }}</div>
                        <div>
                            <span class="address">
                                <i class="el-icon-location"></i>
                                {{ attraction.address }}
                            </span>
                        </div>
                        <div class="info">
                            <span class="time">{{ timeAgo(attraction) }}</span>
                            <span class="save">Favorites{{ strDeal(attraction.favoriteIds) }}</span>
                            <span class="view">Views{{ strDeal(attraction.viewIds) }}</span>
                        </div>
                        <div class="extra-info">
                            <span><i class="el-icon-school"></i>{{ attraction.supplierName }}</span>
                            <span>{{ attraction.categoryName }}</span>
                        </div>
                    </div>
                </el-col>
            </el-row>
            <el-pagination style="margin:10px 0;" @size-change="handleSizeChange" @current-change="handleCurrentChange"
                :current-page="currentPage" :page-sizes="[8]" :page-size="pageSize"
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
            pageSize: 8,
            totalItems: 0,
            tableData: [],
            attractionQueryDto: {}, // 搜索条件
            categories: [],
            searchTime: [],
        };
    },
    created() {
        this.fetchFreshData();
        this.fetchCategories();
    },
    methods: {
        attractionClick(attraction) {
            sessionStorage.setItem('attractionInfo', JSON.stringify(attraction));
            // 跳转至景点详情页
            this.$router.push('/attractionDetail');
        },
        // 收藏与浏览的处理方法
        strDeal(str) {
            if (str === null) {
                return '(0)'
            }
            const favoriteIdsAry = str.split(',');
            return '(' + favoriteIdsAry.length + ')';
        },
        timeAgo(attraction) {
            return timeAgo(attraction.createTime);
        },
        // 获取景点分类
        fetchCategories() {
            this.$axios.post('/attractionCategory/query', {}).then(res => {
                if (res.data.code === 200) {
                    this.categories = res.data.data;
                    this.categories.unshift({ id: null, name: 'All' });
                    this.attractionQueryDto.categoryId = null;
                }
            }).catch(error => {
                console.error("查询景点分类信息异常：", error);
            });
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
                const response = await this.$axios.post('/attraction/query', params);
                const { data } = response;
                this.tableData = data.data;
                this.totalItems = data.total;
            } catch (error) {
                console.error('查询景点信息异常:', error);
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
        padding: 20px 10px;
        border-radius: 6px;
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
            max-height: 190px;
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