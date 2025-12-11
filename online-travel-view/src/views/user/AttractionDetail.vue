<template>
    <div class="container">
        <div class="item">
            <div class="name">{{ attraction.name }}</div>
            <img :src="attraction.cover" alt="" srcset="">
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
            <div style="margin-top: 20px;">
                <span class="save-btn" @click="saveOperation" v-if="!saveBtnFlag"><i class="el-icon-star-off"></i>Add to
                    Favorites</span>
                <span class="cancel-save-btn" @click="saveOperation" v-else><i class="el-icon-star-on"></i>Remove from
                    Favorites</span>
            </div>
        </div>
        <div>
            <el-tabs v-model="activeName" @tab-click="handleClick">
                <el-tab-pane label="Attraction Details" name="first">
                    <div v-if="attraction.description === null">
                        <el-empty description="No introduction available for this attraction"></el-empty>
                    </div>
                    <div v-else v-html="attraction.description"></div>
                </el-tab-pane>
                <el-tab-pane label="Comments" name="second">
                    <Evaluations contentType="SCENIC" :contentId="attraction.id" />
                </el-tab-pane>
                <el-tab-pane label="Attraction Routes" name="five">
                    <div v-if="touristRouteList.length === 0">
                        <el-empty description="No routes have been collected yet"></el-empty>
                    </div>
                    <div v-else>
                        <el-carousel height="450px">
                            <el-carousel-item v-for="(item, index) in touristRouteList" :key="index">
                                <img :src="item.cover" style="border-radius: 5px;height: 100%;width: 100%;" alt=""
                                    srcset="">
                                <h3 style="padding: 20px;font-size: 12px;color: aliceblue;position: fixed;bottom: 0;">{{
                                    item.description }}</h3>
                            </el-carousel-item>
                        </el-carousel>
                    </div>
                </el-tab-pane>
                <el-tab-pane label="Attraction Ratings" name="third">
                    <div>
                        <div class="rating-info">
                            <div class="total-value">
                                <div v-if="ratingFlag">
                                    <el-result icon="success" title="Rating" subTitle="">
                                        <template slot="extra">
                                            <span>My Rating&nbsp;{{ myScore }} points</span>
                                        </template>
                                    </el-result>
                                </div>
                                <div v-else>
                                    <div>Click here to rate</div>
                                    <el-rate @change="ratingChange" v-model="attractionRating.score" show-text>
                                    </el-rate>
                                </div>
                            </div>
                            <div class="total-value">
                                <el-rate v-model="attraction.ratingScore" disabled show-score text-color="#ff9900"
                                    score-template="Average {value} points">
                                </el-rate>
                            </div>
                        </div>
                        <div class="rating-item" v-for="(rating, index) in attractionRatingList" :key="index">
                            <div>
                                <div class="info">
                                    <el-avatar :size="30" :src="rating.userAvatar"></el-avatar>
                                    <span>{{ rating.userName }}</span>
                                </div>
                                <div class="score">
                                    <el-rate v-model="rating.score" disabled show-score text-color="#ff9900"
                                        score-template="{value} points">
                                    </el-rate>
                                </div>
                                <div class="time">
                                    <span>Rated at&nbsp;{{ rating.createTime }}</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </el-tab-pane>
                <el-tab-pane label="Ticket List" name="four">
                    <div v-if="attractionTicketList.length === 0">
                        <el-empty description="No tickets available for sale"></el-empty>
                    </div>
                    <div v-else>
                        <div class="ticket-item" v-for="(ticket, index) in attractionTicketList" :key="index">
                            <div class="detail">
                                {{ ticket.description }}
                            </div>
                            <div class="info">
                                <span class="price">￥{{ ticket.price }}</span>
                                <span>Quantity {{ ticket.number }} tickets</span>
                                <span class="discount">{{ ticket.discount === null ? 'No discount' : ticket.discount +'off'}}</span>
                            </div>
                            <div class="operation">
                                <span>Issued {{ timeAgo(ticket) }}</span>
                                <el-button size="mini" round @click="buyTicketDialog(ticket)">Buy Ticket</el-button>
                            </div>
                        </div>
                    </div>
                </el-tab-pane>
            </el-tabs>
        </div>
        <el-dialog :show-close="false" :visible.sync="dialogTicketOperation" width="25%">
            <div slot="title">
                <p class="dialog-title">Online Ticket Purchase</p>
            </div>
            <div style="padding:0 20px;">
                <el-row>
                    <div class="ticket-item">
                        <p>*Ticket Information</p>
                        <div class="detail">
                            {{ ticket.description }}
                        </div>
                        <div class="info">
                            <span class="price">￥{{ ticket.price }}</span>
                            <span>Quantity {{ ticket.number }} tickets</span>
                            <span class="discount">{{ ticket.discount === null ? 'No discount' : ticket.discount + 'off'
                                }}</span>
                        </div>
                        <div class="operation">
                            <span>Issued {{ timeAgo(ticket) }}</span>
                        </div>
                    </div>
                    <div>
                        <div>
                            <span class="dialog-hover">Contact Person</span>
                            <input class="dialog-input" v-model="data.contactPerson" placeholder="Enter" />
                        </div>
                        <div>
                            <span class="dialog-hover">Contact Phone</span>
                            <input class="dialog-input" v-model="data.contactNum" placeholder="Enter" />
                        </div>
                        <div>
                            <p>*Purchase Quantity</p>
                            <el-input-number v-model="data.buyNumber" :min="1" :max="ticket.number"
                                label="description"></el-input-number>
                        </div>
                        <div style="margin-block: 20px;" class="ticket-item">
                            <div class="info">
                                <span>Selected {{ data.buyNumber }} tickets in total, Amount<span class="price">{{
                                        moneyCount(data)
                                        }} dollar</span></span>
                            </div>
                        </div>
                    </div>
                </el-row>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button size="small" style="background-color: rgb(96, 98, 102);color: rgb(247,248,249);border: none;"
                    class="customer" type="info" @click="addOperation()">Confirm Purchase</el-button>
                <el-button class="customer" size="small" style="background-color: rgb(211, 241, 241);border: none;"
                    @click="cancel()">Cancel</el-button>
            </span>
        </el-dialog>
    </div>
</template>
<script>
import { timeAgo } from "@/utils/data"
import Evaluations from "@/components/Evaluations"
export default {
    components: { Evaluations },
    name: "AttractionDetail",
    data() {
        return {
            data: { buyNumber: 1 }, // 存储购票相关的信息
            attraction: {},
            ticket: {},
            attractionRating: {},
            myScore: 0,
            attractionRatingList: [],
            attractionTicketList: [],
            touristRouteList: [],
            dialogTicketOperation: false,
            activeName: 'first',
            ratingFlag: false,
            saveBtnFlag: false,
        }
    },
    created() {
        this.loadInfo();
    },
    methods: {
        // 查询景点路线
        queryLine(id) {
            this.$axios.post('/touristRoute/query', { attractionId: id }).then(res => {
                if (res.data.code === 200) {
                    this.touristRouteList = res.data.data;
                }
            })
        },
        // 购票
        addOperation() {
            this.data.ticketId = this.ticket.id;
            this.$axios.post('/attractionTicketOrder/save', this.data).then(res => {
                if (res.data.code === 200) { // 下单成功
                    this.$message.success(res.data.msg);
                    this.dialogTicketOperation = false;
                    // 跳到对应的门票订单页
                    console.log("Order placed. Redirecting to order page");
                } else {
                    this.$message.error(res.data.msg);
                }
            })
        },
        // 统计金额
        moneyCount(data) {
            // 可能没有折扣
            const discountTotal = this.ticket.discount === null ? 1 : (this.ticket.discount / 10);
            const total = data.buyNumber * this.ticket.price * discountTotal;
            // 使用 Math.ceil 取整，然后保留两位小数
            return Math.ceil(total * 100) / 100;
        },
        // 取消购票
        cancel() {
            // 关闭购票弹窗
            this.dialogTicketOperation = false;
            this.data = {};
        },
        buyTicketDialog(ticket) {
            this.ticket = ticket;
            this.dialogTicketOperation = true;
        },
        // 收藏操作
        saveOperation() {
            this.$axios.post(`/attraction/saveOperation/${this.attraction.id}`).then(res => {
                if (res.data.code === 200) {
                    this.$message.success(res.data.msg);
                    this.saveStatus();
                }
            })
        },
        // 浏览指标的处理
        viewOperation() {
            this.$axios.post(`/attraction/viewOperation/${this.attraction.id}`).then(res => {
                if (res.data.code === 200) {
                    console.log("浏览指标已经记录");
                }
            })
        },
        // 查询用户是否已经收藏了这个景点
        saveStatus() {
            this.$axios.post(`/attraction/saveStatus/${this.attraction.id}`).then(res => {
                if (res.data.code === 200) {
                    this.saveBtnFlag = Boolean(res.data.data);
                }
            })
        },
        // 评分
        ratingChange(score) {
            this.$axios.post('/attractionRating/save', { attractionId: this.attraction.id, score }).then(res => {
                if (res.data.code === 200) {
                    this.$message.success(res.data.msg);
                } else {
                    this.$message(res.data.msg);
                }
                this.queryRatingStatus();
                this.fetchSceniRating(this.attraction.id);
            })
        },
        // 开始的时候，先查询是否已经评分，如果没有评分，显示评分的组件
        queryRatingStatus() {
            this.$axios.post('/attractionRating/queryUser', { attractionId: this.attraction.id }).then(res => {
                if (res.data.code === 200) {
                    this.ratingFlag = res.data.data.length !== 0;
                    // 如果评过分，存储对应的分数，做显示
                    if (this.ratingFlag) {
                        this.myScore = res.data.data[0].score;
                    }
                }
            })
        },
        handleClick(tab, event) {
            if (tab.index === '3') {
                this.fetchSceniRating(this.attraction.id);
                this.queryRatingStatus();
            } else if (tab.index === '4') {
                this.fetchAttractionTicket(this.attraction.id);
            } else if (tab.index === '2') {
                this.queryLine(this.attraction.id);
            }
        },
        // 记载景区下面的门票信息
        fetchAttractionTicket(attractionId) {
            this.$axios.post('/attractionTicket/query', { attractionId: attractionId }).then(res => {
                if (res.data.code === 200) {
                    this.attractionTicketList = res.data.data;
                }
            })
        },
        // 查询景点下面的评分信息
        fetchSceniRating(attractionId) {
            this.$axios.post('/attractionRating/query', { attractionId: attractionId }).then(res => {
                if (res.data.code === 200) {
                    this.attractionRatingList = res.data.data;
                }
            })
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
        loadInfo() {
            const jsonAttractionInfo = sessionStorage.getItem('attractionInfo');
            this.attraction = JSON.parse(jsonAttractionInfo);
            this.viewOperation();
            this.saveStatus();
        },
    }
};
</script>
<style scoped lang="scss">
.container {
    width: 900px;
    margin: 0 auto;

    .save-btn,
    .cancel-save-btn {
        display: inline-block;
        font-size: 12px;
        padding: 6px 10px;
        border-radius: 5px;
    }

    .save-btn {
        background-color: rgb(51, 51, 51);
        color: rgb(201, 187, 187);
    }

    .save-btn:hover {
        background-color: rgb(31, 31, 31);
    }

    .cancel-save-btn:hover {
        background-color: rgb(235, 138, 138);
    }

    .cancel-save-btn {
        background-color: rgb(237, 157, 157);
        color: rgb(250, 250, 250);
    }

    .rating-info {
        min-height: 200px;
        display: flex;
        justify-content: space-around;
        background-color: rgb(246, 246, 246);

        .total-value {
            display: flex;
            justify-content: center;
            align-items: center;
        }
    }

    .rating-item {
        margin-block: 20px;

        .info {
            display: flex;
            justify-content: left;
            align-items: center;
            gap: 5px;

            span {
                font-size: 14px;
            }
        }

        .score,
        .time {
            padding: 6px 30px;
        }

        .time {
            font-size: 12px;
        }

    }

    .ticket-item {
        background-color: rgb(246, 246, 246);
        border-radius: 5px;
        padding: 10px;
        box-sizing: border-box;
        margin-bottom: 2px;

        .detail {
            font-size: 20px;
            font-weight: 800;
            margin-block: 4px;
        }

        .info {
            font-size: 12px;
            margin-block: 10px;
            display: flex;
            justify-content: left;
            align-items: center;
            gap: 8px;
            color: rgb(83, 82, 82);

            .price {
                font-size: 18px;
                font-weight: 800;
                color: rgb(236, 118, 54);
            }
        }

        .operation {
            display: flex;
            justify-content: left;
            align-items: center;
            gap: 8px;

            span {
                font-size: 12px;
            }
        }
    }

    .item {
        margin-block: 20px;
        padding: 30px 10px;
        box-sizing: border-box;
        cursor: pointer;
        background-color: rgb(225, 245, 245);
        border-radius: 5px;

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
            width: 300px;
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

        .name {
            font-size: 32px;
            font-weight: 800;
            margin-bottom: 20px;
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
