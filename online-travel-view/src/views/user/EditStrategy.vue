<template>
    <div style="background-color: rgb(249,249,249);">
        <div style="background-color: rgb(255,255,255);min-width: 700px;max-width: 900px;margin: 0 auto;padding: 20px 10px;">
            <div>
                <p>*Title</p>
                <input style="line-height: 45px;" class="dialog-input" v-model="data.title" placeholder="Guide Title" />
            </div>
            <div>
                <p>*Related Attraction</p>
                <el-select size="small" v-model="data.attractionId" placeholder="Select Attraction">
                    <el-option v-for="item in attractions" :key="item.id" :label="item.name" :value="item.id">
                    </el-option>
                </el-select>
            </div>
            <div style="margin-block: 10px;">
                <p>*Cover Image</p>
                <el-upload class="avatar-uploader" action="/api/online-travel-sys/v1.0/file/upload"
                    :show-file-list="false" :on-success="handleCoverSuccess">
                    <img v-if="cover" :src="cover" style="width: 200px;height: 140px;">
                    <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                </el-upload>
            </div>
            <div>
                <p>*Guide Details</p>
                <Editor :receiveContent="receiveContent" height="400px" @on-receive="onReceive" />
            </div>
            <div style="margin-top: 20px;text-align: center;">
                <el-button type="primary" class="customer" size="mini" @click="updateInfo" round>Update Now</el-button>
            </div>
        </div>
    </div>
</template>
<script>
import Editor from "@/components/Editor"
export default {
    components: { Editor },
    name: "PublishStrategy",
    data() {
        return {
            data: {},
            cover: '',
            attractions: [],
            receiveContent: '你的真知灼见，栩栩如生'
        }
    },
    created() {
        this.loadSession();
        this.fetchAttractions();
    },
    methods: {
        loadSession(){
            const jsonStrategyInfo = sessionStorage.getItem('strategyInfo');
            this.data = JSON.parse(jsonStrategyInfo);
            this.receiveContent = this.data.content;
            this.cover = this.data.cover;
            
        },
        // 攻略修改
        async updateInfo() {
            try {
                this.data.cover = this.cover;
                const { data } = await this.$axios.put('/attractionStrategy/update',this.data);
                if (data.code === 200) {
                    this.$message.success('攻略修改成功');
                    this.$router.push('/user');
                }
            } catch (error) {
                this.$message.error(error);
            }
        },
        // 富文本编辑器内容回调函数
        onReceive(content) {
            this.data.content = content;
        },
        // 查询All的景点信息
        async fetchAttractions() {
            try {
                const { data } = await this.$axios.get('/attraction/querySelectedAttraction');
                if (data.code === 200) {
                    this.attractions = data.data;
                }
            } catch (error) {
                this.$message.error(error);
            }
        },
        // 封面上传成功时回调函数
        handleCoverSuccess(res, file) {
            this.$notify({
                duration: 1500,
                title: '封面上传',
                message: res.code === 200 ? '上传成功' : '上传失败',
                type: res.code === 200 ? 'success' : 'error'
            });
            // 上传成功则更新封面
            if (res.code === 200) {
                this.cover = res.data;
            }
        },
    }
};
</script>
<style scoped lang="scss"></style>
