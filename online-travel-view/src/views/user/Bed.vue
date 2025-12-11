<template>
    <div>
        <div v-if="bedOperaion" style="padding: 20px;">
            <div>
                <span class="dialog-hover">Bed Number</span>
                <input class="dialog-input" v-model="data.number" placeholder="Enter" />
                <el-row style="margin: 20px 0;">
                    <div>
                        <span class="dialog-hover">Bed Status</span>
                    </div>
                    <el-switch active-color="#13ce66" inactive-color="#ff4949" v-model="data.status" active-text="Available"
                        inactive-text="Unavailable">
                    </el-switch>
                </el-row>
            </div>
            <div style="display: flex;justify-content: center;align-items: center;">
                <el-button size="small" v-if="!isOperation"
                    style="background-color: rgb(96, 98, 102);color: rgb(247,248,249);border: none;" class="customer"
                    type="info" @click="addOperation()">Add</el-button>
                <el-button size="small" v-else
                    style="background-color: rgb(96, 98, 102);color: rgb(247,248,249);border: none;" class="customer"
                    type="info" @click="updateOperation()">Modify</el-button>
            </div>
        </div>
        <div style="display: flex;justify-content: center;align-items: center;margin: 10px;">
            <el-button style="width: 100%;" v-if="beds.length !== 0" size="mini" type="info"
                @click="createBed">Create Bed Information</el-button>
        </div>

        <div v-if="beds.length === 0">
            <el-empty>
                <el-button type="info" @click="createBed">Create Bed Information</el-button>
            </el-empty>
        </div>
        <div v-else style="padding: 10px;">
            <div class="bed-item" v-for="(bed, index) in beds" :key="index">
                <div style="font-size: 18px;">
                    <span style="margin-right: 5px;">
                        <i class="el-icon-table-lamp"></i>
                        Bed Number:{{ bed.number }}
                    </span>
                    <span>{{ bed.status ? 'Status: Available' : 'Status: Unavailable' }}</span>
                </div>
                <div style="margin: 8px 6px;font-size: 12px;">
                    Creation Time: {{ bed.createTime }}
                </div>
                <div>
                    <el-button style="margin-right: 10px;" @click="updateBedInfo(bed)" size="mini" type="primary"
                        icon="el-icon-edit" circle></el-button>
                    <el-popconfirm title="Confirm to delete bed information?" @confirm="deleteBed(bed)">
                        <el-button slot="reference" size="mini" type="danger" icon="el-icon-delete" circle></el-button>
                    </el-popconfirm>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    props: {
        roomId: {
            type: Number,
            default: ''
        },
    },
    watch: {
        roomId(v1, v2) {
            console.log("房间ID：" ,v1);
            this.fetchBeds(v1);
        },
    },
    data() {
        return {
            data: {},
            beds: [],
            bedOperaion: false, // 控制床位新增/编辑的开关变量
            isOperation: false, // false: 新增；true： 修改
        }
    },
    created() {
        this.fetchBeds();
    },
    methods: {
        async deleteBed(bed) {
            let ids = [bed.id]
            const response = await this.$axios.post(`/hotelRoomBed/batchDelete`, ids);
            if (response.data.code === 200) {
                this.cannel();
                this.fetchBeds();
                return;
            }
        },
        createBed() {
            this.data = {};
            this.bedOperaion = true;
        },
        updateBedInfo(bed) {
            this.data = bed;
            this.bedOperaion = true;
            this.isOperation = true;
        },
        // 床位新增
        addOperation() {
            const hotelRoomBed = {
                roomId: this.roomId,
                ...this.data
            }
            this.$axios.post('/hotelRoomBed/save', hotelRoomBed).then(res => {
                if (res.data.code === 200) {
                    // 关闭操作区域
                    this.bedOperaion = false;
              this.$message.success('Bed added successfully');
                    this.fetchBeds();
                }
            }).catch(error => {
                console.error("查询床位信息异常:", error);

            })
        },
        // 床位修改
        updateOperation() {
            this.$axios.put('/hotelRoomBed/update', this.data).then(res => {
                if (res.data.code === 200) {
                    // 关闭操作区域
                    this.bedOperaion = false;
                    this.isOperation = false;
                    this.$message.success('Bed modification successful');
                    this.fetchBeds();
                }
            }).catch(error => {
                console.error("查询床位信息异常:", error);

            })
        },
        // 取消事件
        cannel() {
            this.data = {};
            this.bedOperaion = false;
            this.isOperation = false;
        },
        // 加载对应的床位信息
        fetchBeds() {
            const hotelRoomBedQueryDto = {
                roomId: this.roomId
            }
            this.$axios.post('/hotelRoomBed/query', hotelRoomBedQueryDto).then(res => {
                if (res.data.code === 200) {
                    this.beds = res.data.data;
                }
            }).catch(error => {
                console.error("查询床位信息异常:", error);

            })
        },
    },
};
</script>
<style scoped lang="scss">
.bed-item {
    background-color: rgb(248, 248, 248);
    padding: 10px;
    border-radius: 5px;
    margin-bottom: 4px;
}
</style>