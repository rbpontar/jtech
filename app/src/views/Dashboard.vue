<script setup lang="ts">
import { computed, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { useTodosStore } from '../stores/todos'
import ListSidebar from '../components/ListSidebar.vue'
import TaskItem from '../components/TaskItem.vue'

const router = useRouter()
const userStore = useUserStore()
const todos = useTodosStore()

const user = computed(() => userStore.user)
const activeList = computed(() => todos.lists.find((l) => l.id === todos.activeListId) ?? null)

const newTaskTitle = ref('')
const newTaskDesc = ref('')
const taskError = ref<string | null>(null)

function logout() {
  router.push({ name: 'Login' })
  userStore.logout()
}

function addTask() {
  taskError.value = null
  if (!todos.activeListId) {
    taskError.value = 'Selecione ou crie uma lista primeiro.'
    return
  }
  try {
    todos.addTask(todos.activeListId, newTaskTitle.value, newTaskDesc.value)
    newTaskTitle.value = ''
    newTaskDesc.value = ''
  } catch (e: any) {
    taskError.value = e?.message ?? 'Falha ao adicionar tarefa'
  }
}
</script>

<template>
  <div class="page">
    <div class="container" style="display: flex; gap: 18px; align-items: flex-start">
      <ListSidebar />

      <div style="flex: 1">
        <div class="header" style="margin-bottom: 12px">
          <div>
            <h2 style="margin: 0">
              {{ activeList ? activeList.name : 'Nenhuma lista selecionada' }}
            </h2>
            <div class="footer-note">Gerencie tarefas da lista selecionada</div>
          </div>

          <div style="display: flex; align-items: center; gap: 12px">
            <div v-if="user" class="user-chip">
              <div
                style="
                  width: 36px;
                  height: 36px;
                  border-radius: 50%;
                  background: linear-gradient(90deg, var(--accent), #60f0d6);
                  display: flex;
                  align-items: center;
                  justify-content: center;
                  color: #002b20;
                  font-weight: 700;
                "
              >
                {{ user?.name?.[0]?.toUpperCase() }}
              </div>
              <div style="font-size: 14px">{{ user?.name }}</div>
            </div>
            <button class="btn secondary" @click="logout">Sair</button>
          </div>
        </div>

        <div class="panel">
          <div style="margin-bottom: 12px">
            <h3 style="margin: 0 0 8px 0">Adicionar Tarefa</h3>
            <div style="display: flex; gap: 8px; flex-wrap: wrap">
              <input
                v-model="newTaskTitle"
                placeholder="Título da tarefa"
                style="flex: 1; min-width: 220px"
              />
              <input
                v-model="newTaskDesc"
                placeholder="Descrição (opcional)"
                style="flex: 2; min-width: 200px"
              />
              <button class="btn primary" @click.prevent="addTask">Adicionar</button>
            </div>
            <div v-if="taskError" class="error">{{ taskError }}</div>
          </div>

          <div>
            <div v-if="!activeList" class="footer-note">
              Selecione ou crie uma lista para ver tarefas.
            </div>
            <div v-else>
              <div v-if="activeList.taskCount === 0" class="footer-note">Nenhuma tarefa ainda.</div>
              <div v-for="task in activeList.tasks" :key="task.id">
                <TaskItem :task="task" :listId="activeList.id" />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
