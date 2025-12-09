import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '../stores/user'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { requiresAuth: false },
  },
  {
    path: '/',
    name: 'Dashboard',
    component: () => import('../views/Dashboard.vue'),
    meta: { requiresAuth: true },
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

router.beforeEach((to) => {
  const userStore = useUserStore()
  const loggedIn = !!userStore.user && !!userStore.token

  if (to.meta.requiresAuth && !loggedIn) {
    return { name: 'Login' }
  }
  if (to.name === 'Login' && loggedIn) {
    return { name: 'Dashboard' }
  }
})

export default router
