<template>
    <div class="container">
        <div class="item">
            <div class="name">{{ hotel.name }}</div>
            <div class="info">
                <span class="time">{{ timeAgo(hotel) }}</span>
                <span>{{ hotel.supplierName }}</span>
            </div>
            <img :src="hotel.cover" alt="" srcset="">
            <div class="extra-info">
                <span class="address">
                    <i class="el-icon-location"></i>
                    {{ hotel.address }}
                </span>
            </div>
        </div>
        <div>
            <el-tabs v-model="activeName" @tab-click="handleClick">
                <el-tab-pane label="Comments" name="first">
                    <Evaluations contentType="HOTEL" :contentId="hotel.id" />
                </el-tab-pane>
                <el-tab-pane label="Rooms" name="second">
                    <div v-if="rooms.length === 0">
                        <el-empty description="No rooms available for rent"></el-empty>
                    </div>
                    <div v-else>
                        <div class="room-item" v-for="(room, index) in rooms" :key="index">
                            <div class="name">{{ room.name }}</div>
                            <div class="bottom">
                                <div>
                                    <img :src="room.cover" alt="">
                                </div>
                                <div>
                                    <div style="margin-bottom: 6px;">
                                        <span class="price">
                                            ¥{{ room.price }}
                                        </span>
                                        <span class="discount">
                                            {{ room.discount === null ? 'No discount' : room.discount + ' off' }}
                                        </span>
                                    </div>
                                    <div class="description">
                                        {{ room.description }}
                                    </div>
                                    <div style="margin-top: 6px;">
                                        <span class="order-room" @click="orderRoom(room)">Book Room</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </el-tab-pane>
            </el-tabs>
        </div>
        <el-dialog :show-close="false" :visible.sync="dialogRoomOperation" width="25%">
            <div slot="title">
                <p class="dialog-title">Room Reservation</p>
            </div>
            <div style="padding:0 20px;">
                <el-row>
                    <div class="bed-item">
                        <p>Bed Information</p>
                        <div class="item" v-for="(bed, index) in hotelRoomBeds" :key="index">
                            <div>
                                <span><i class="el-icon-table-lamp"></i>{{ bed.number }}</span>
                            </div>
                        </div>
                    </div>
                </el-row>
                <el-row>
                    <div>
                        <span class="dialog-hover">Contact Person</span>
                        <input class="dialog-input" v-model="data.contactPerson" placeholder="Enter" />
                    </div>
                    <div>
                        <span class="dialog-hover">Contact Phone</span>
                        <input class="dialog-input" v-model="data.contactNum" placeholder="Enter" />
                    </div>
                </el-row>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button size="small" style="background-color: rgb(96, 98, 102);color: rgb(247,248,249);border: none;"
                    class="customer" type="info" @click="addOperation()">Confirm Order</el-button>
                <el-button class="customer" size="small" style="background-color: rgb(211, 241, 241);border: none;"
                    @click="cannel()">Cancel</el-button>
            </span>
        </el-dialog>
    </div>
</template>
<script>
import { timeAgo } from "@/utils/data"
import Evaluations from "@/components/Evaluations"
export default {
    components: { Evaluations },
    name: "HotelDetail",
    data() {
        return {
            data: {},
            hotel: {}, // 酒店的信息
            rooms: [], // 酒店房间的信息
            hotelRoomBeds: [], // 房间下的床位信息
            activeName: 'first',
            dialogRoomOperation: false
        }
    },
    created() {
        this.loadInfo();
    },
    methods: {
        cannel() {
            this.dialogRoomOperation = false;
            this.data = {};
        },
        orderRoom(room) {
            this.$axios.post('/hotelRoomBed/query', { roomId: room.id }).then(res => {
                if (res.data.code === 200) {
                    this.hotelRoomBeds = res.data.data;
                    this.data.roomId = room.id;
                    this.dialogRoomOperation = true;
                }
            }).catch(error => {
                this.$message.error(error);
            })
        },
        addOperation() {
            this.$axios.post('/hotelOrderInfo/save', this.data).then(res => {
                if (res.data.code === 200) {
                    this.$message.success('Room reservation successful');
                    this.dialogRoomOperation = false;
                    this.data = {};
                } else {
                    this.$message.info(res.data.msg);
                }
            }).catch(error => {
                this.$message.error(error);
            })
        },
        timeAgo(hotel) {
            return timeAgo(hotel.createTime);
        },
        // Tab页切换时回调方法
        handleClick(tab, event) {
            if (tab.index === '1') {
                this.fetchRoomInfo();
            }
        },
        // 记载房间的信息
        fetchRoomInfo() {
            this.$axios.post('/hotelRoom/query', { hotelId: this.hotel.id }).then(res => {
                if (res.data.code === 200) {
                    this.rooms = res.data.data;
                }
            }).catch(error => {
                console.log(error);
            })
        },
        loadInfo() {
            const jsonHotelInfo = sessionStorage.getItem('hotelInfo');
            this.hotel = JSON.parse(jsonHotelInfo);
        },
    }
};
</script>
<style scoped lang="scss">
.container {
    width: 800px;
    margin: 0 auto;

    .bed-item {
        background-color: rgb(246, 246, 246);
        padding: 16px;
        box-sizing: border-box;
        margin-bottom: 4px;
        border-radius: 5px;

        .item {
            padding-block: 10px;
            font-size: 20px;
        }
    }

    .room-item {
        background-color: rgb(246, 246, 246);
        padding: 16px;
        box-sizing: border-box;
        margin-bottom: 4px;
        border-radius: 5px;

        .name {
            font-size: 24px;
            margin-block: 10px;
            font-weight: 800;
        }

        .bottom {
            display: flex;
            justify-content: flex-start;
            gap: 8px;

            img {
                width: 220px;
                border-radius: 5px;

            }

            .price {
                font-size: 24px;
                font-weight: 800;
                color: rgb(227, 152, 99);
            }

            .order-room {
                display: inline-block;
                padding: 4px 10px;
                background-color: rgb(75, 105, 207);
                border-radius: 2px;
                font-size: 12px;
                color: aliceblue;
                cursor: pointer;
            }

            .order-room:hover {
                background-color: rgb(29, 64, 179);
            }

            .discount {
                font-size: 14px;
            }

            .description {
                font-size: 12px;
                color: rgb(139, 138, 138);
            }
        }
    }

    .item {
        background-color: rgb(248, 246, 246);
        padding: 16px;
        border-radius: 5px;

        .info {
            display: flex;
            justify-content: left;
            gap: 10px;
            margin-bottom: 10px;
        }

        .name {
            font-size: 36px;
            margin-block: 10px;
            font-weight: 800;
        }

        img {
            width: 100%;
            height: 400px;
            border-radius: 5px;
        }

        .extra-info {
            margin-block: 10px;

            .address {
                font-size: 14px;
                color: rgb(28, 89, 186);
            }
        }
    }
}
</style>
