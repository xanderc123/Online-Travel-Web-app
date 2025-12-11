<template>
    <div class="register-containel">
        <div class="register-panel">
            <div class="logo">
                <Logo sysName="Having no account? sign up now"/>
            </div>
            <div class="text">
                <input v-model="act" class="act" placeholder="Sign up account" />
            </div>
            <div class="text">
                <input v-model="name" class="act" placeholder="username" />
            </div>
            <div class="text">
                <input v-model="pwd" class="pwd" type="password" placeholder="enter the password" />
            </div>
            <div class="text">
                <input v-model="pwdConfirm" class="pwd" type="password" placeholder="enter the password again" />
            </div>
            <div>
                <span class="register-btn" @click="registerFunc">register now</span>
            </div>
            <div class="tip">
                <p>having an account?<span class="no-act" @click="toDoLogin">back to login</span></p>
            </div>
        </div>
    </div>
</template>

<script>
const DELAY_TIME = 1300;
import request from "@/utils/request.js";
import md5 from 'js-md5';
import Logo from '@/components/Logo.vue';
export default {
    name: "Register",
    components: { Logo },
    data() {
        return {
            act: '', // 账号
            pwd: '', // 密码
            pwdConfirm: '', // 确认密码
            name: '' // 用户名
        }
    },
    methods: {
        // 返回登录页面
        toDoLogin() {
            this.$router.push('/login');
        },
        async registerFunc() {
            if (!this.act || !this.pwd || !this.pwdConfirm || !this.name ) {
                this.$swal.fire({
                    title: 'fill in and verify',
                    text: 'account or password cannot be empty',
                    icon: 'error',
                    showConfirmButton: false,
                    timer: DELAY_TIME,
                });
                return;
            }
            if (this.pwd !== this.pwdConfirm) {
                this.$swal.fire({
                    title: 'fill in and verify',
                    text: 'the password enter before and after cannot match',
                    icon: 'error',
                    showConfirmButton: false,
                    timer: DELAY_TIME,
                });
                return;
            }
            const hashedPwd = md5(md5(this.pwd));
            const paramDTO = { userAccount: this.act, userPwd: hashedPwd, userName: this.name };
            try {
                const { data } = await request.post(`user/register`, paramDTO);
                if (data.code !== 200) {
                    this.$swal.fire({
                        title: 'Registration failed',
                        text: data.msg,
                        icon: 'error',
                        showConfirmButton: false,
                        timer: DELAY_TIME,
                    });
                    return;
                }
                // 使用Swal通知注册成功，延迟后跳转
                this.$swal.fire({
                    title: 'Registration succeeded',
                    text: 'Back to the login page...',
                    icon: 'success',
                    showConfirmButton: false,
                    timer: DELAY_TIME,
                });
                // 根据角色延迟跳转
                setTimeout(() => {
                    this.$router.push('/login');
                }, DELAY_TIME);
            } catch (error) {
                console.error('The registration request has an error:', error);
            }
        }
    }
};
</script>

<style lang="scss" scoped>
* {
    user-select: none;
}

.register-containel {
    width: 100%;
    height: 100vh;
    display: flex;
    /* 启用Flexbox布局 */
    justify-content: center;
    /* 水平居中 */
    align-items: center;
    /* 垂直居中 */
    flex-direction: column;
    /* 如果需要垂直居中，确保子元素也是这样排列 */

    .register-panel {
        margin: 0 auto;
        width: 333px;
        height: auto;
        padding: 40px 30px 16px 30px;
        border-radius: 3px;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1), 0 1px 3px rgba(0, 0, 0, 0.06);

        .logo {
            margin: 10px 0 30px 0;
        }

        .act,
        .pwd {
            margin: 8px 0;
            height: 53px;
            line-height: 53px;
            width: 100%;
            padding: 0 8px;
            background-color: rgb(248,248,248);
            box-sizing: border-box;
            border: 1px solid rgb(248,248,248);
            border-radius: 5px;
            font-size: 18px;
            padding: 0 15px;
            margin-top: 13px;
        }

        .act:focus,
        .pwd:focus {
            outline: none;
            border: 1px solid rgb(206, 205, 205);
            transition: 1.2s;
        }

        .role {
            display: inline-block;
            color: rgb(30, 102, 147);
            font-size: 14px;
            padding-right: 10px;
        }
    }

    .register-btn {
        display: inline-block;
        text-align: center;
        border-radius: 3px;
        margin-top: 20px;
        height: 43px;
        line-height: 43px;
        width: 100%;
        background-color: rgb(155, 191, 93);
        font-size: 14px !important;
        border: none;
        color: rgb(250,250,250);
        padding: 0 !important;
        cursor: pointer;
        user-select: none;
    }

    .tip {
        margin: 20px 0;

        p {
            padding: 3px 0;
            font-size: 14px;
            margin: 0;
            color: #647897;

            i {
                margin-right: 3px;
            }

            span {
                color: #3b3c3e;
                border-radius: 2px;
                margin: 0 6px;
            }

            .no-act:hover {
                color: #568ed7;
                cursor: pointer;
            }

        }
    }

}
</style>
