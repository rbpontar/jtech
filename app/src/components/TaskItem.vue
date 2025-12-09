<script setup lang="ts">
import { ref } from 'vue'
import type { Task } from '../stores/todos'
import { useTodosStore } from '../stores/todos'

const props = defineProps<{ task: Task; listId: string }>()
const todos = useTodosStore()
const editing = ref(false)
const editTitle = ref(props.task.title)
const editDesc = ref(props.task.description ?? '')

function toggle() {
  try {
    todos.toggleTaskDone(props.listId, props.task.id)
  } catch (e: any) {
    alert(e?.message ?? 'Falha')
  }
}

function remove() {
  const ok = window.confirm('Deletar essa tarefa?')
  if (!ok) return
  try {
    todos.deleteTask(props.listId, props.task.id)
  } catch (e: any) {
    alert(e?.message ?? 'Falha ao deletar')
  }
}

function save() {
  try {
    todos.editTask(props.listId, props.task.id, {
      id: props.task.id,
      title: editTitle.value,
      description: editDesc.value,
      done: props.task.done,
    })
    editing.value = false
  } catch (e: any) {
    alert(e?.message ?? 'Falha ao salvar.')
  }
}
</script>

<template>
  <div style="
      display: flex;
      align-items: flex-start;
      gap: 12px;
      padding: 10px;
      border-bottom: 1px solid rgba(255, 255, 255, 0.02);
    ">
    <input type="checkbox" :checked="task.done" @change="toggle" />
    <div style="flex: 1">
      <div v-if="!editing">
        <div :style="{ textDecoration: task.done ? 'line-through' : 'none', fontWeight: 600 }">
          {{ task.id + ' - ' + task.title }}
        </div>
        <div style="font-size: 13px; color: var(--muted)">{{ task.description }}</div>
      </div>
      <div v-else>
        <input v-model="editTitle" style="
            width: 100%;
            padding: 8px;
            margin-bottom: 6px;
            border-radius: 6px;
            /* background: var(--glass); */
            border: 1px solid rgba(255, 255, 255, 0.03);
          " />
        <textarea v-model="editDesc" rows="2" style="
            width: 100%;
            padding: 8px;
            border-radius: 6px;
            /* background: var(--glass); */
            border: 1px solid rgba(255, 255, 255, 0.03);
          "></textarea>
      </div>
    </div>

    <div style="display: flex; flex-direction: column; gap: 6px">
      <button v-if="!editing" class="btn secondary" @click.prevent="editing = true">Editar</button>
      <button v-else class="btn primary" @click.prevent="save">Salvar</button>
      <button class="btn" style="
          background: transparent;
          color: var(--danger);
          border: 1px solid rgba(255, 255, 255, 0.03);
        " @click.prevent="remove">
        Excluir
      </button>
    </div>
  </div>
</template>

<style scoped></style>
