<template>
    <div>
        <div>
            <div style="font-size: 32px;margin-block: 10px;font-weight: 800;">{{ strategy.title }}</div>
            <div class="user">
                <img :src="strategy.userAvatar" alt="" srcset="">
                <span>{{ strategy.userName }}</span>
            </div>
            <div class="info">
                <span>{{ strategy.attractionName }}</span>
                <span>{{ strategy.createTime }}</span>
            </div>
            <div style="font-size: 14px;" v-html="strategy.content"></div>
            <div>
                <Evaluations contentType="STRATEGY" :contentId="strategyId" />
            </div>
        </div>
    </div>
</template>
<script>
import Evaluations from "@/components/Evaluations"
export default {
    components: { Evaluations },
    name: "StrategyDetail",
    data() {
        return {
            strategy: {},
            strategyId: null
        }
    },
    created() {
        const strategyId = sessionStorage.getItem('strategyId');
        this.strategyId = Number(strategyId);
        this.fetchStrategy(strategyId);
    },
    methods: {
        // 通过攻略ID查询攻略信息
        fetchStrategy(strategyId) {
            this.$axios.post('/attractionStrategy/query', { id: strategyId }).then(res => {
                if (res.data.code === 200) {
                    this.strategy = res.data.data[0];
                }
            });
        }
    }
};
</script>
<style scoped lang="scss">
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

.info {
    font-size: 12px;
    display: flex;
    justify-content: left;
    margin-block: 20px;
    gap: 10px;

    span:first-child {
        display: inline-block;
        padding: 1px 3px;
        border-radius: 2px;
        background-color: rgb(237, 243, 249);
        color: rgb(136, 115, 233);
    }
}
</style>
