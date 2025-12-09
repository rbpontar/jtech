<script setup lang="ts">
import { ref, computed } from 'vue'
import { useTodosStore } from '../stores/todos'

const todos = useTodosStore()
const newListName = ref('')
const error = ref<string | null>(null)

const lists = computed(() => todos.lists)
const activeId = computed(() => todos.activeListId)

function selectList(id: string) {
  todos.setActiveList(id)
}

async function createList() {
  error.value = null
  try {
    await todos.createList(newListName.value)
    newListName.value = ''
  } catch (e: any) {
    error.value = e?.message ?? 'Falha ao criar lista'
  }
}

function rename(listId: string) {
  const current = todos.lists.find((l) => l.id === listId)
  if (!current) return
  const name = window.prompt('Novo nome da lista', current.name)
  if (!name) return
  try {
    todos.renameList(listId, name)
  } catch (e: any) {
    alert(e?.message ?? 'Falha ao renomear')
  }
}

function remove(listId: string) {
  const list = todos.lists.find((l) => l.id === listId)
  if (!list) return
  if (list.taskCount > 0) {
    const ok = window.confirm(
      'Esta lista tem tarefas. Deletar mesmo assim? Isso removerá todas as tarefas.',
    )
    if (!ok) return
    try {
      todos.deleteList(listId)
    } catch (e: any) {
      alert(e?.message ?? 'Falha ao deletar')
    }
  } else {
    const ok = window.confirm('Deletar essa lista vazia?')
    if (!ok) return
    try {
      todos.deleteList(listId)
    } catch (e: any) {
      alert(e?.message ?? 'Falha ao deletar')
    }
  }
}
</script>

<template>
  <aside style="width: 300px; min-width: 220px">
    <div style="margin-bottom: 12px; display: flex; flex-direction: column; gap: 8px">
      <div style="display: flex; gap: 8px">
        <input v-model="newListName" placeholder="Nome da nova lista" />
        <button class="btn primary" @click.prevent="createList">Adicionar</button>
      </div>
      <div v-if="error" class="error">{{ error }}</div>
    </div>

    <div style="display: flex; flex-direction: column; gap: 8px">
      <div v-if="lists.length === 0" class="panel">Nenhuma lista ainda. Crie uma.</div>
      <div
        v-for="list in lists"
        :key="list.id"
        :style="{
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'space-between',
          padding: '8px',
          borderRadius: '8px',
          background:
            list.id === activeId
              ? 'linear-gradient(90deg,var(--glass), rgba(255,255,255,0.02))'
              : 'transparent',
        }"
      >
        <button
          style="
            background: transparent;
            border: none;
            color: inherit;
            text-align: left;
            padding: 0;
            flex: 1;
            cursor: pointer;
          "
          @click="selectList(list.id)"
        >
          <div style="font-weight: 600">{{ list.id + ' - ' + list.name }}</div>
          <div style="font-size: 12px; color: var(--muted)">{{ list.taskCount }} tarefa(s)</div>
        </button>
        <div style="display: flex; gap: 6px; margin-left: 8px">
          <button class="btn secondary" @click="rename(list.id)">Renomear</button>
          <button
            class="btn"
            style="
              background: transparent;
              color: var(--danger);
              border: 1px solid rgba(255, 255, 255, 0.03);
            "
            @click="remove(list.id)"
          >
            Deletar
          </button>
        </div>
      </div>
    </div>
  </aside>
</template>

<style scoped>
input {
  padding: 8px;
  border-radius: 8px;
  background: var(--glass);
  border: 1px solid rgba(255, 255, 255, 0.03);
  color: inherit;
}
</style>
