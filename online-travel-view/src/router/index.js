import Vue from "vue";
import VueRouter from "vue-router";
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import { getToken } from "@/utils/storage.js";
import echarts from 'echarts';

Vue.prototype.$echarts = echarts;
Vue.use(ElementUI);
Vue.use(VueRouter);

const routes = [
  {
    path: "*",
    redirect: "/login"
  },
  {
    path: "/login",
    component: () => import(`@/views/login/Login.vue`)
  },
  {
    path: "/register",
    component: () => import(`@/views/register/Register.vue`)
  },
  {
    path: "/publishStrategy",
    name: '发布攻略',
    component: () => import(`@/views/user/PublishStrategy.vue`),
    meta: { requireAuth: true },

  },
  {
    path: "/seeStrategyDetail",
    name: '攻略详情',
    icon: 'el-icon-camera-solid',
    component: () => import(`@/views/admin/StrategyDetail.vue`),
    meta: { requireAuth: true },
  },
  {
    path: "/editStrategy",
    name: '攻略修改',
    component: () => import(`@/views/user/EditStrategy.vue`),
    meta: { requireAuth: true },
  },
  {
    path: "/admin",
    component: () => import(`@/views/admin/Home.vue`),
    meta: {
      requireAuth: true,
    },
    children: [
      {
        path: "/adminLayout",
        name: 'HomePage',
        icon: 'el-icon-pie-chart',
        component: () => import(`@/views/admin/Main.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/userManage",
        name: 'UserManagement',
        icon: 'el-icon-user',
        component: () => import(`@/views/admin/UserManage.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/attractionCategoryManage",
        name: 'Category management',
        icon: 'el-icon-menu',
        component: () => import(`@/views/admin/AttractionCategoryManage.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/supplierManage",
        name: 'Supplier management',
        icon: 'el-icon-s-shop',
        component: () => import(`@/views/admin/SupplierManage.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/attractionManage",
        name: 'Attraction management',
        icon: 'el-icon-camera-solid',
        component: () => import(`@/views/admin/AttractionManage.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/attractionTicketManage",
        name: 'Ticket management',
        icon: 'el-icon-s-finance',
        component: () => import(`@/views/admin/AttractionTicketManage.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/attractionTicketOrderManage",
        name: 'Attraction Order',
        icon: 'el-icon-receiving',
        component: () => import(`@/views/admin/AttractionTicketOrderManage.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/attractionStrategyManage",
        name: 'Strategy Management',
        icon: 'el-icon-document-copy',
        component: () => import(`@/views/admin/AttractionStrategyManage.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/hotelManage",
        name: 'Hotel Management',
        icon: 'el-icon-school',
        component: () => import(`@/views/admin/HotelManage.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/hotelOrderInfoManage",
        name: 'Hotel Order',
        icon: 'el-icon-document-checked',
        component: () => import(`@/views/admin/HotelOrderInfoManage.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/hotelRoomManage",
        name: 'Hotel Room Management',
        icon: 'el-icon-box',
        component: () => import(`@/views/admin/HotelRoomManage.vue`),
        meta: { requireAuth: true },
      },

      //       {
      //   path: "/touristRouteManage",
      //   name: 'Route management',
      //   icon: 'el-icon-camera-solid',
      //   component: () => import(`@/views/admin/TouristRouteManage.vue`),
      //   meta: { requireAuth: true },
      // },
      {
        path: "/evaluationsManage",
        name: 'Comment Management',
        icon: 'el-icon-chat-dot-round',
        component: () => import(`@/views/admin/EvaluationsManage.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/noticeManage",
        name: 'Announcements',
        icon: 'el-icon-house',
        component: () => import(`@/views/admin/NoticeManage.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/supplierReport",
        name: 'Performance Report', // This will be displayed in the sidebar
        icon: 'el-icon-s-data', // Find an icon resembling a report
        component: () => import(`@/views/admin/SupplierReport.vue`), // Corresponds to the Vue file we just created
        meta: { requireAuth: true },
      }
    ]
  },
  {
    path: "/user",
    component: () => import(`@/views/user/Main.vue`),
    meta: {
      requireAuth: true,
    },
    children: [
      {
        path: "/attraction",
        name: 'attraction',
        component: () => import(`@/views/user/Attraction.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/attractionDetail",
        name: 'Attraction Detail',
        component: () => import(`@/views/user/AttractionDetail.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/order",
        name: 'My Order',
        component: () => import(`@/views/user/Order.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/strategy",
        name: 'strategy',
        component: () => import(`@/views/user/Strategy.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/strategyDetail",
        name: 'Strategy Details',
        component: () => import(`@/views/user/StrategyDetail.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/hotel",
        name: 'hotel',
        component: () => import(`@/views/user/Hotel.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/hotelDetail",
        name: 'Hotel Detail',
        component: () => import(`@/views/user/HotelDetail.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/hotelOrder",
        name: 'hotel order',
        component: () => import(`@/views/user/HotelOrder.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/ticketOrder",
        name: 'ticket order',
        component: () => import(`@/views/user/TicketOrder.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/notice",
        name: 'annoucement',
        component: () => import(`@/views/user/Notice.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/noticeDetail",
        name: '公告详情',
        component: () => import(`@/views/user/NoticeDetail.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/attractionSupplier",
        name: 'Supplier Attractions',
        component: () => import(`@/views/user/AttractionSupplier.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/hotelSupplier",
        name: 'Supplier Hotels',
        component: () => import(`@/views/user/HotelSupplier.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/ticket",
        name: 'Attraction Tickets',
        component: () => import(`@/views/user/Ticket.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/hotelRoom",
        name: 'Hotel Rooms',
        component: () => import(`@/views/user/HotelRoom.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/service",
        name: 'service center',
        component: () => import(`@/views/user/Service.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/self",
        name: 'personal center',
        component: () => import(`@/views/user/Self.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/resetPwd",
        name: 'reset password',
        component: () => import(`@/views/user/ResetPwd.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/contentCenter",
        name: '内容中心',
        component: () => import(`@/views/user/ContentCenter.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/save",
        name: '我的收藏',
        component: () => import(`@/views/user/Save.vue`),
        meta: { requireAuth: true },
      },
    ]
  }
];
const router = new VueRouter({
  routes,
  mode: 'history'
});
router.beforeEach((to, from, next) => {
  // 放行登录页和注册页
  if (to.path === '/login' || to.path === '/register') {
    return next();
  }

  // 检查需要认证的路由
  if (to.matched.some(record => record.meta.requireAuth)) {
    const token = getToken();

    // 未登录情况处理
    if (!token) {
      return next({
        path: '/login',
        query: { redirect: to.fullPath } // 记录目标路由
      });
    }

    // 已登录时的权限检查
    try {
      const role = parseInt(sessionStorage.getItem('role'));

      // 管理员路径检查
      if (to.matched[0].path === '/admin' && role !== 1) {
        clearToken();
        return next("/login"); //返回登录页
      }

      // 用户路径检查
      if (to.matched[0].path === '/user' && role !== 2) {
        clearToken();
        return next("/login"); //返回登录页
      }

      return next();
    } catch (error) {
      console.error('权限检查失败:', error);
      return next('/login');
    }
  }

  // 普通页面直接放行
  next();
});
export default router;
