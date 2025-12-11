<template>
    <div>
        <div class="top">
            <div class="top-left">
                <Logo sysName="Voyagea" />
            </div>
            <div class="top-right">
                <ul>
                    <li @click="route('/attraction')">Attractions</li>
                    <li @click="route('/strategy')">Travel Guides</li>
                    <li @click="route('/hotel')">Hotels</li>
                    <li @click="route('/order')">Orders</li>
                    <li>
                        <el-dropdown type="success" size="mini" :hide-on-click="false">
                            <span class="el-dropdown-link">
                                Order Details<i class="el-icon-arrow-down el-icon--right"></i>
                            </span>
                            <el-dropdown-menu slot="dropdown">
                                <el-dropdown-item @click.native="route('/hotelOrder')">Hotel Orders</el-dropdown-item>
                                <el-dropdown-item @click.native="route('/ticketOrder')">Ticket
                                    Bookings</el-dropdown-item>
                            </el-dropdown-menu>
                        </el-dropdown>
                    </li>
                    <li @click="route('/notice')">Notices</li>
                </ul>
                <el-dropdown type="success" size="mini" :hide-on-click="false">
                    <span class="el-dropdown-link" style="cursor: pointer;">
                        Content Center<i class="el-icon-arrow-down el-icon--right"></i>
                    </span>
                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item @click.native="route('/publishStrategy')">Publish Guide</el-dropdown-item>
                        <el-dropdown-item @click.native="route('/contentCenter')">Content Center</el-dropdown-item>
                        <el-dropdown-item @click.native="route('/save')">My Favorites</el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
                <el-button type="primary" size="mini" @click="route('/service')" round>Service Center</el-button>
                <el-dropdown type="success" size="mini" class="user-dropdown">
                    <span class="el-dropdown-link" style="display: flex; align-items: center;cursor: pointer;">
                        <el-avatar :size="30" :src="userInfo.userAvatar" style="margin-top: 0;"></el-avatar>
                        <span class="user-name" style="margin-left: 5px;font-size: 14px;">{{ userInfo.userName }}</span>
                        <i class="el-icon-arrow-down el-icon--right" style="margin-left: 5px;"></i>
                    </span>
                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item @click.native="route('/self')">Profile</el-dropdown-item>
                        <el-dropdown-item @click.native="route('/resetPwd')">Change Password</el-dropdown-item>
                        <el-dropdown-item @click.native="loginOut">Logout</el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
            </div>
        </div>

        <div class="router-view">
            <div class="item">
                <router-view></router-view>
            </div>
        </div>
        <div
            style="margin: 20px 0; padding: 20px; border: 2px solid #67C23A; border-radius: 8px; background-color: #f0f9eb;">
            <h3 style="color: #67C23A; margin-bottom: 15px;">🎯 VIP Status Upgrade</h3>
            <p style="color: #606266; line-height: 1.6; margin-bottom: 20px;">
                Check if you qualify for VIP membership. When your total spending reaches <strong>1,000 dollar</strong>,
                the system will automatically upgrade you to VIP status and unlock exclusive benefits.
            </p>
            <div style="text-align: center;">
                <el-button type="success" @click="handleVipUpgrade" :loading="vipLoading"
                    style="padding: 12px 24px; font-size: 16px;">
                    <i class="el-icon-medal"></i> Check VIP Eligibility
                </el-button>
            </div>
            <div v-if="vipMessage" style="margin-top: 20px; padding: 15px; border-radius: 6px; 
                background-color: #fdf6ec; border: 1px solid #f5dab1; color: #e6a23c;">
                <i class="el-icon-info" style="margin-right: 8px;"></i>
                <span>{{ vipMessage }}</span>
            </div>
        </div>
    </div>
</template>
<script>
import Logo from "@/components/Logo"
export default {
    components: { Logo },
    name: "User",
    data() {
        return {
            key: '',
            defaultPath: '/attraction',
            userInfo: {},
            vipLoading: false,
            vipMessage: ''
        }
    },
    created() {
        this.auth();
        // 默认加载景点页
        this.route(this.defaultPath);
    },
    methods: {
        // 路由跳转
        route(path) {
            if (this.$route.path !== path) {
                this.$router.push(path);
            };
        },
        // 退出登录
        async loginOut() {
            const confirmed = await this.$swalConfirm({
                title: 'Logout',
                text: `You need to login again after logout?`,
                icon: 'warning',
            });
            if (confirmed) {
                sessionStorage.setItem('token', null);
                this.$router.push('/');
            }
        },
        // Token 检验
        async auth() {
            const { data } = await this.$axios.get('/user/auth');
            if (data.code !== 200) { // Token校验异常
                this.$router.push('/');
            } else {
                sessionStorage.setItem('userInfo', JSON.stringify(data.data));
                this.userInfo = data.data;
            }
        },
        async handleVipUpgrade() {
            this.vipLoading = true;
            this.vipMessage = '';
            try {
                // 假设你的用户信息存在 sessionStorage 里，且 key 为 'userInfo'
                // 这里的逻辑参考了你 Main.vue 里的写法
                const userStr = sessionStorage.getItem('userInfo');
                if (!userStr) {
                    this.$message.error("Login first!");
                    this.vipLoading = false;
                    return;
                }
                const user = JSON.parse(userStr);

                // 发送请求到后端
                // 注意：这里的 URL 要和你 Controller 里的对应 (/advanced/checkVip)
                // 如果你的 axios 配置了 baseURL (比如 /api/online-travel-sys/v1.0)，记得路径要拼对
                // 假设你的 axios 已经配置好了前缀，这里直接写相对路径或者全路径
                const res = await this.$axios.post('/advanced/checkVip', { userId: user.id });
                if (res.data.code === 200) {
                    this.vipMessage = res.data.data.message;
                    if (res.data.data.success) {
                        this.$message.success("VIP upgrade successful!");
                    } else {
                        this.$message.warning("Check completed, no upgrade needed.");
                    }
                } else {
                    this.$message.error(res.data.msg || "Request failed");
                }
            } catch (error) {
                console.error(error);
                this.$message.error("System error, please check the console");
            } finally {
                this.vipLoading = false;
            }
        }
    }
};
</script>
<style scoped lang="scss">
.top {
    height: 65px;
    padding: 0 100px;
    box-sizing: border-box;
    display: flex;
    justify-content: space-between;
    align-items: center;
    /* 垂直居中 */
    border-bottom: 1px solid rgb(231, 231, 231);

    .top-left {
        flex-shrink: 0;
        /* 防止被压缩 */
    }

    .top-right {
        display: flex;
        align-items: center;
        gap: 20px;
        /* 统一间距 */

        /* 导航菜单样式 */
        ul {
            display: flex;
            /* 使用flex布局 */
            list-style: none;
            margin: 0;
            padding: 0;
            gap: 15px;
            /* 菜单项间距 */

            li {
                cursor: pointer;
                padding: 8px 12px;
                color: rgb(111, 53, 71);
                font-size: 14px;
                font-weight: 500;
                white-space: nowrap;
                /* 防止换行 */

                &:hover {
                    color: rgb(128, 144, 100);
                    background-color: rgba(128, 144, 100, 0.1);
                    border-radius: 4px;
                }
            }
        }

        /* 右侧图标按钮容器 */
        .right-actions {
            display: flex;
            align-items: center;
            gap: 15px;
            flex-shrink: 0;
            /* 防止压缩 */
        }

        /* 用户下拉菜单特殊处理 */
        .user-dropdown {
            margin-left: 10px;
        }
    }
}

/* 调整Element UI组件样式 */
.el-dropdown-link {
    display: flex;
    align-items: center;
    cursor: pointer;

    .user-name {
        margin-left: 8px;
        font-size: 14px;
    }
}

/* 响应式调整 */
@media (max-width: 1200px) {
    .top {
        padding: 0 50px;

        .top-right {
            gap: 10px;

            ul {
                gap: 10px;

                li {
                    padding: 6px 8px;
                    font-size: 13px;
                }
            }
        }
    }
}
</style>
