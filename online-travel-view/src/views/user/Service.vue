<template>
    <div>
        <div v-if="suppliers.length !== 0">
            <h1
                style="padding-left: 10px;border-left: 4px solid rgb(0, 122, 204);background-color: rgb(250,250,250);padding-block: 10px;">
                Basic Supplier Information</h1>
            <div class="info">
                <el-descriptions class="margin-top" title="" :column="3" :size="size">
                    <el-descriptions-item>
                        <template slot="label">
                            Supplier Name
                        </template>
                        {{ suppliers[0].name }}
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template slot="label">
                            Work Email
                        </template>
                        {{ suppliers[0].email }}
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template slot="label">
                            Office Address
                        </template>
                        {{ suppliers[0].workAddress }}
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template slot="label">
                            Product Type Provided
                        </template>
                        <el-tag size="small">{{ suppliers[0].productType }}</el-tag>
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template slot="label">
                            <i class="el-icon-office-building"></i>
                            Contact Person
                        </template>
                        {{ suppliers[0].contactPerson }}
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template slot="label">
                            <i class="el-icon-office-building"></i>
                            Contact Phone
                        </template>
                        {{ suppliers[0].contactPhone }}
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template slot="label">
                            Audit Status
                        </template>
                        <el-tag size="small">{{ suppliers[0].isAudit ? 'Audited' : 'Unaudited' }}</el-tag>
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template slot="label">
                            Supplier Status
                        </template>
                        <el-tag size="small">{{ suppliers[0].status ? 'Active' : 'Inactive' }}</el-tag>
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template slot="label">
                            Operation
                        </template>
                        <el-button class="customer"
                            style="border-radius: 20px;margin-bottom: 20px;background-color: rgb(51,51,51);"
                            type="warning" size="mini" @click="updateInfo">Edit Information</el-button>
                    </el-descriptions-item>
                </el-descriptions>
            </div>

            <div style="margin: 20px 0;" v-if="suppliers[0].isAudit && suppliers[0].status">
                <h1
                    style="padding-left: 10px;border-left: 4px solid rgb(0, 122, 204);background-color: rgb(250,250,250);padding-block: 10px;">
                    Supplier Function Navigation</h1>
                <div class="content-publish">
                    <div @click="route('/attractionSupplier')">
                        Attraction Information
                    </div>
                    <div @click="route('/hotelSupplier')">
                        Hotel Information
                    </div>
                    <div @click="route('/ticket')">
                        Attraction Ticket Management
                    </div>
                    <div @click="route('/hotelRoom')">
                        Hotel Room Management
                    </div>
                </div>
                <div class="content-publish">
                    <div @click="route('/hotelOrder')">
                        Hotel Orders
                    </div>
                    <div @click="route('/ticketOrder')">
                        Ticket Bookings
                    </div>
                </div>
            </div>
        </div>
        <div v-else style="width: 500px;margin: 0 auto;">
            <div>
                <h2 style="text-align: center;">Supplier Information Application</h2>
            </div>
            <div>
                <p>*Supplier Name</p>
                <input style="line-height: 45px;" class="dialog-input" v-model="data.name" placeholder="Please enter" />
            </div>
            <div>
                <p>*Contact Person</p>
                <input style="line-height: 45px;" class="dialog-input" v-model="data.contactPerson"
                    placeholder="Please enter" />
            </div>
            <div>
                <p>*Contact Phone</p>
                <input style="line-height: 45px;" class="dialog-input" v-model="data.contactNumber"
                    placeholder="Please enter" />
            </div>
            <div>
                <p>*Work Email</p>
                <input style="line-height: 45px;" class="dialog-input" v-model="data.email"
                    placeholder="Please enter" />
            </div>
            <div>
                <p>*Product Type</p>
                <input style="line-height: 45px;" class="dialog-input" v-model="data.productType"
                    placeholder="Please enter" />
            </div>
            <div>
                <p>*Office Address</p>
                <input style="line-height: 45px;" class="dialog-input" v-model="data.location"
                    placeholder="Please enter" />
            </div>
            <div style="margin-top: 20px;text-align: center;">
                <el-button type="primary" class="customer" size="mini" @click="postInfo" round>
                    Submit Application
                </el-button>
            </div>
        </div>
        <el-dialog :show-close="false" :visible.sync="dialogStatusOperation" width="25%">
            <div slot="title">
                <p class="dialog-title">Edit Information</p>
            </div>
            <div style="padding:0 20px;">
                <div>
                    <p>*Supplier Name</p>
                    <input class="dialog-input" v-model="data.name" placeholder="Please enter" />
                </div>
                <div>
                    <p>*Contact Person</p>
                    <input class="dialog-input" v-model="data.contactPerson" placeholder="Please enter" />
                </div>
                <div>
                    <p>*Contact Phone</p>
                    <input class="dialog-input" v-model="data.contactPhone" placeholder="Please enter" />
                </div>
                <div>
                    <p>*Work Email</p>
                    <input class="dialog-input" v-model="data.email" placeholder="Please enter" />
                </div>
                <div>
                    <p>*Product Type</p>
                    <input class="dialog-input" v-model="data.productType" placeholder="Please enter" />
                </div>
                <div>
                    <p>*Office Address</p>
                    <input class="dialog-input" v-model="data.workAddress" placeholder="Please enter" />
                </div>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button size="small" style="background-color: rgb(96, 98, 102);color: rgb(247,248,249);border: none;"
                    class="customer" type="info" @click="confirmStatus">Confirm</el-button>
                <el-button class="customer" size="small" style="background-color: rgb(241, 241, 241);border: none;"
                    @click="dialogStatusOperation = false">Cancel</el-button>
            </span>
        </el-dialog>
    </div>
</template>
<script>
export default {
    name: "Service",
    data() {
        return {
            size: 'small',
            data: {},
            suppliers: [],
            dialogStatusOperation: false
        }
    },
    created() {
        this.auth();
    },
    methods: {
        // 路由跳转
        route(path) {
            this.$router.push(path);
        },
        // Supplier information modification
        async confirmStatus() {
            const { data } = await this.$axios.put('/supplier/update', this.data);
            this.$notify({
                duration: 1000,
                title: 'Supplier Information Update',
                message: data.code === 200 ? 'Updated successfully' : data.msg,
                type: data.code === 200 ? 'success' : 'error'
            });
            this.dialogStatusOperation = false;
        },
        updateInfo() {
            this.data = this.suppliers[0];
            this.dialogStatusOperation = true;
        },
        async postInfo() {
            const { data } = await this.$axios.post('/supplier/save', this.data);
            this.$notify({
                duration: 1000,
                title: 'Application Operation',
                message: data.code === 200 ? 'Application submitted successfully. Please wait patiently' : data.msg,
                type: data.code === 200 ? 'success' : 'error'
            });
        },
        async auth() {
            const { data } = await this.$axios.post('/supplier/queryUser');
            if (data.code === 200) {
                this.suppliers = data.data;
            }
        },
    }
};
</script>
<style scoped lang="scss">
.info {
    box-shadow: 0 0 20px rgb(240, 240, 240);
    padding: 20px;
    border-radius: 5px;
}

.content-publish {
    border-radius: 5px;
    line-height: 40px;
    display: flex;
    justify-content: left;
    gap: 10px;
    padding: 20px;
    margin-block: 10px;
    box-shadow: 0 0 20px rgb(240, 240, 240);

    div {
        padding: 6px 20px;
        cursor: pointer;
        background-color: rgb(248, 248, 248);
        font-weight: bold;
        border-radius: 5px;

    }

    div:hover {
        background-color: rgb(240, 240, 240);
    }
}
</style>
