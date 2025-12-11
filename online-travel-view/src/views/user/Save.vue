<template>
    <div>
        <el-row v-if="attractionList.length === 0">
            <el-empty description="No attraction information available"></el-empty>
        </el-row>
        <el-row v-else class="attraction-item">
            <el-col :span="4" @click.native="attractionClick(attraction)" v-for="(attraction, index) in attractionList" :key="index">
                <div class="item">
                    <img :src="attraction.cover" alt="" srcset="">
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
    </div>
</template>
<script>

import { timeAgo } from "@/utils/data"
export default {
    name: "Save",
    data() {
        return {
            attractionList: []
        }
    },
    created() {
        this.fetchSaveAttraction();
    },
    methods: {
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
        // 景点的点击事件
        attractionClick(attraction) {
            sessionStorage.setItem('attractionInfo', JSON.stringify(attraction));
            // 跳转至景点详情页
            this.$router.push('/attractionDetail');
        },
        // 查询用户收藏的景点信息
        fetchSaveAttraction() {
            this.$axios.get('/attraction/querySave').then(res => {
                if (res.data.code === 200) {
                    this.attractionList = res.data.data;
                }
            });
        },
    }
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

        .name {
            font-size: 20px;
            font-weight: 800;
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
