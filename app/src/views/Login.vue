<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { useTodosStore } from '@/stores/todos'

defineOptions({
  name: 'LoginView',
})

const router = useRouter()
const userStore = useUserStore()
const todosStore = useTodosStore()

const email = ref('')
const password = ref('')
const name = ref('')
const error = ref<string | null>(null)
const loading = ref(false)

async function submit() {
  error.value = null
  if (!email.value.trim() || !password.value.trim()) {
    error.value = 'Email e senha são obrigatórios.'
    return
  }
  loading.value = true
  try {
    await userStore.login({
      email: email.value.trim(),
      password: password.value.trim(),
      name: name.value?.trim(),
    })
    todosStore.init()
    router.push({ name: 'Dashboard' })
  } catch (e) {
    const err = e as Error
    error.value = err?.message ?? 'Falha ao entrar'
  } finally {
    loading.value = false
  }
}

async function register() {
  error.value = null
  if (!email.value.trim() || !password.value.trim()) {
    error.value = 'Email e senha são obrigatórios.'
    return
  }
  loading.value = true
  try {
    await userStore.register({
      email: email.value.trim(),
      password: password.value.trim(),
      name: name.value?.trim(),
    })
    alert('Registro bem-sucedido! Agora você pode entrar.');
  } catch (e) {
    const err = e as Error
    error.value = err?.message ?? 'Falha ao registrar'
  } finally {
    loading.value = false
  }
}

</script>

<template>
  <div class="page">
    <div class="card auth-card">
      <h2 class="title">Entrar</h2>
      <p class="subtitle">Digite suas credenciais para entrar</p>

      <form class="auth-form" @submit.prevent="submit">
        <div class="form-group">
          <label for="email">Email</label>
          <input id="email" v-model="email" type="email" placeholder="seu@email.com" />
        </div>

        <div class="form-group">
          <label for="password">Senha</label>
          <input id="password" v-model="password" type="password" placeholder="senha" />
        </div>

        <div class="form-group">
          <label for="name">Nome (opcional)</label>
          <input id="name" v-model="name" type="text" placeholder="Seu nome" />
        </div>

        <div v-if="error" class="error">{{ error }}</div>

        <div style="display: flex; gap: 12px; align-items: center; margin-top: 6px">
          <button class="btn primary" :disabled="loading">
            <span v-if="!loading">Entrar</span>
            <span v-else>Entrando...</span>
          </button>
          <button type="button" class="btn secondary" @click.prevent="
            () => {
              email = ''
              password = ''
              name = ''
            }
          ">
            Limpar
          </button>

          <button type="button" class="btn secondary" @click.prevent="register">
            Registrar
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<style scoped></style>
