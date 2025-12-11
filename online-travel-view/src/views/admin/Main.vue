<template>
    <div style="box-sizing: border-box;overflow-y: hidden;overflow-x: hidden;">
        <el-row>
            <el-col :span="16">
                <div>
                    <div>
                        <el-row style="margin-block: 20px;">
                            <el-col :span="4" v-for="(staticItem, index) in staticData" :key="index">
                                <div class="static-item">
                                    <el-statistic group-separator="," :precision="0" :value="staticItem.count"
                                        :title="staticItem.name"></el-statistic>
                                </div>
                            </el-col>
                        </el-row>
                    </div>
                    <div>
                        <div style="padding: 20px 60px;box-sizing: border-box;">
                            <LineChart height="280px" tag="Ticket Sales Revenue" @on-selected="ticketSelected"
                                :values="ticketMoneyValues" :date="ticketMoneyDates" />
                        </div>
                        <div style="padding: 20px 60px;box-sizing: border-box;">
                            <LineChart height="280px" tag="Hotel Sales Revenue" @on-selected="hotelOrderSelected"
                                :values="hotelRoomMoneyValues" :date="hotelRoomMoneyDates" />
                        </div>

                    </div>
                </div>
            </el-col>
            <el-col :span="8">
                <div class="buttom">
                    <p>Latest Strategy</p>
                    <div class="strategy-item" v-for="(strategy, index) in attractionStrategies" :key="index">
                        <div class="left">
                            <img :src="strategy.cover" :alt="strategy.title" srcset="">
                        </div>
                        <div class="right">
                            <div class="user">
                                <img :src="strategy.userAvatar" alt="" srcset="">
                                <span>{{ strategy.userName }}</span>
                            </div>
                            <div class="title" @click="readStrategy(strategy)">{{ strategy.title }}</div>
                            <div class="info">
                                <span>{{ strategy.attractionName }}</span>
                                <span>{{ timeOut(strategy.createTime) }}</span>
                            </div>
                        </div>
                    </div>
                </div>
            </el-col>
        </el-row>
    </div>
</template>
<script>
import LineChart from "@/components/LineChart"
import { timeAgo } from "@/utils/data"
export default {
    components: { LineChart },
    data() {
        return {
            staticData: [],
            attractionStrategies: [],
            ticketMoneyValues: [],
            ticketMoneyDates: [],
            hotelRoomMoneyValues: [],
            hotelRoomMoneyDates: [],
            defaultQueryDays: 365, // 由于数据太少，直接查一年来测试
        }
    },
    created() {
        this.fentchStaticCount();
        this.fentchStrategy();
        this.ticketSelected(this.defaultQueryDays);
        this.hotelOrderSelected(this.defaultQueryDays);
    },
    methods: {
        timeOut(time) {
            return timeAgo(time);
        },
        // 查询指定时间内的门票销售额记录
        hotelOrderSelected(day) {
            this.$axios.get(`/hotelOrderInfo/daysQueryMoney/${day}`).then(res => {
                if (res.data.code === 200) {
                    // 只取出里面的金额，自成一个数组
                    this.hotelRoomMoneyValues = res.data.data.map(money => money.count);
                    // 只取出里面的日期，自成一个数组
                    this.hotelRoomMoneyDates = res.data.data.map(money => money.name);
                }
            });
        },
        // 查询指定时间内的门票销售额记录
        ticketSelected(day) {
            this.$axios.get(`/attractionTicketOrder/daysQueryMoney/${day}`).then(res => {
                if (res.data.code === 200) {
                    // 只取出里面的金额，自成一个数组
                    this.ticketMoneyValues = res.data.data.map(money => money.count);
                    // 只取出里面的日期，自成一个数组
                    this.ticketMoneyDates = res.data.data.map(money => money.name);
                }
            });
        },
        readStrategy(strategy) {
            sessionStorage.setItem('strategyId', strategy.id);
            this.$router.push('/seeStrategyDetail');
        },
        // 查询最新发布的攻略数据
        fentchStrategy() {
            const strategyQuery = {
                current: 1,
                size: 5
            }
            this.$axios.post('/attractionStrategy/queryList', strategyQuery).then(res => {
                if (res.data.code === 200) {
                    this.attractionStrategies = res.data.data;
                }
            });
        },
        // 查询静态数据
        fentchStaticCount() {
            this.$axios.get('/views/staticControls').then(res => {
                if (res.data.code === 200) {
                    this.staticData = res.data.data;
                }
            });
        },
    },
};
</script>
<style scoped lang="scss">
.buttom {
    padding: 5px 20px;
    box-sizing: border-box;

    .strategy-item {
        display: flex;
        justify-content: left;
        padding: 10px 0;
        cursor: pointer;

        .left {
            padding: 5px;
            box-sizing: border-box;

            img {
                width: 138px;
                height: 80px;
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
                width: 100px;
                font-weight: 800;
                padding-bottom: 6px;
                white-space: nowrap;
                /* 防止文本换行 */
                overflow: hidden;
                /* 隐藏超出容器的文本 */
                text-overflow: ellipsis;
                /* 使用省略号表示被截断的文本 */
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
}
</style>