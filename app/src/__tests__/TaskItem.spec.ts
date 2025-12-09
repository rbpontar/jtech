import { describe, it, expect, beforeEach, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import { setActivePinia, createPinia } from 'pinia'
import TaskItem from '../components/TaskItem.vue'
import { useTodosStore } from '../stores/todos'

let pinia: ReturnType<typeof createPinia>

beforeEach(() => {
  pinia = createPinia()
  setActivePinia(pinia)
  localStorage.clear()
})

describe('TaskItem', () => {
  it('toggles done state when checkbox clicked', () => {
    const s = useTodosStore()
    const l = s.createList('T')
    const t = s.addTask(l.id, 'Task1')

    const wrapper = mount(TaskItem, {
      props: { task: t, listId: l.id },
      global: { plugins: [pinia] },
    })

    const checkbox = wrapper.find('input[type="checkbox"]')
    expect(checkbox.exists()).toBe(true)
    checkbox.setChecked()
    expect(s.lists[0]!.tasks[0].done).toBe(true)
  })

  it('deletes task when confirmed', () => {
    const s = useTodosStore()
    const l = s.createList('Del')
    const t = s.addTask(l.id, 'ToDelete')

    // mock confirm
    const confirmSpy = vi.spyOn(window, 'confirm').mockImplementation(() => true)

    const wrapper = mount(TaskItem, {
      props: { task: t, listId: l.id },
      global: { plugins: [pinia] },
    })

    const delBtn = wrapper.findAll('button').filter((b) => b.text().toLowerCase().includes('delete'))[0]
    expect(delBtn).toBeTruthy()
    delBtn.trigger('click')
    expect(s.lists[0]!.tasks.length).toBe(0)
    confirmSpy.mockRestore()
  })
})
