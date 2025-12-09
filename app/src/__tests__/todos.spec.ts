import { describe, it, expect, beforeEach } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'
import { useTodosStore } from '../stores/todos'

describe('useTodosStore', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
    localStorage.clear()
  })

  it('creates lists, prevents duplicates and renames', () => {
    const s = useTodosStore()
    const l1 = s.createList('Work')
    expect(s.lists.length).toBe(1)
    expect(l1.name).toBe('Work')

    // duplicate name should throw
    expect(() => s.createList('work')).toThrow()

    const renamed = s.renameList(l1.id, 'Office')
    expect(renamed.name).toBe('Office')
  })

  it('adds tasks per list and prevents duplicate tasks', () => {
    const s = useTodosStore()
    const l = s.createList('Personal')
    const t1 = s.addTask(l.id, 'Buy milk', '2 liters')
    expect(l.tasks.length).toBe(1)
    expect(t1.title).toBe('Buy milk')

    // duplicate task title
    expect(() => s.addTask(l.id, 'buy milk')).toThrow()

    const edited = s.editTask(l.id, t1.id, { title: 'Buy eggs', done: true })
    expect(edited.title).toBe('Buy eggs')
    expect(edited.done).toBe(true)

    // toggle
    const toggled = s.toggleTaskDone(l.id, t1.id)
    expect(typeof toggled).toBe('boolean')

    // delete
    s.deleteTask(l.id, t1.id)
    expect(l.tasks.length).toBe(0)
  })

  it('delete list with tasks requires force', () => {
    const s = useTodosStore()
    const l = s.createList('Temp')
    s.addTask(l.id, 'A')
    // deleting without force should throw
    expect(() => s.deleteList(l.id, false)).toThrow()
    // with force should succeed
    expect(s.deleteList(l.id, true)).toBe(true)
  })

  it('persists and restores', () => {
    const s = useTodosStore()
    const l = s.createList('Persist')
    s.addTask(l.id, 'P1')
    // create a fresh store to init from storage
    const s2 = useTodosStore()
    s2.init()
    expect(s2.lists.length).toBeGreaterThan(0)
    expect(s2.lists[0]!.tasks.length).toBeGreaterThan(0)
  })
})
