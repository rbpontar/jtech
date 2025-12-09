import { defineStore } from "pinia";
import { api } from "../services/api";
import { useUserStore } from "./user";

export type Task = {
  id: string;
  title: string;
  description?: string;
  done: boolean;
  createdAt?: string;
  taskListId?: string;
};

export type List = {
  id: string;
  name: string;
  taskCount: number;
  tasks: Task[];
};

type State = {
  lists: List[];
  activeListId: string | null;
};

const STORAGE_KEY = "todos_v1";

export const useTodosStore = defineStore("todos", {
  state: (): State => ({
    lists: [],
    activeListId: null,
  }),
  actions: {
    async init() {
      try {
        try {
          const userStore = useUserStore();
          const loggedIn = !!userStore.user && !!userStore.token;
          if (loggedIn) {
            const response = await api.list.getAll();
            const listFromApi = response;
            this.lists = listFromApi;
            this.activeListId = this.lists[0]?.id ?? null;
            this.setActiveList(this.activeListId!);
          }
        } catch (e) {
          console.error("Error fetching lists from API:", e);
          this.lists = [];
          this.activeListId = null;
        }
      } catch {
        this.lists = [];
        this.activeListId = null;
      }
    },
    persist() {
      const payload = {
        lists: this.lists,
        activeListId: this.activeListId,
      };
      localStorage.setItem(STORAGE_KEY, JSON.stringify(payload));
    },
    async createList(name: string) {
      const trimmed = name?.trim();
      const response = await api.list.create({ name: trimmed });
      const newList: List = {
        id: response.id,
        name: response.name,
        taskCount: 0,
        tasks: [],
      };
      this.lists.push(newList);
      this.activeListId = newList.id;
      this.persist();
      return newList;
    },
    async renameList(id: string, newName: string) {
      const trimmed = newName?.trim();
      const response = await api.list.update(id, { name: trimmed });
      const list = this.lists.find(() => response.id === id);
      if (!list) throw new Error("Lista não encontrada");
      list.name = trimmed;
      this.persist();
      return list;
    },
    async deleteList(id: string) {
      await api.list.delete(id);
      const idx = this.lists.findIndex((l) => l.id === id);
      if (idx === -1) throw new Error("Lista n�o encontrada");
      this.lists.splice(idx, 1);
      if (this.activeListId === id) {
        this.activeListId = this.lists[0]?.id ?? null;
      }
      this.persist();

      return true;
    },
    async setActiveList(id: string) {
      if (id) {
        const tasks = await api.task.getByTaskListId(id);
        const list = this.lists.find((l) => l.id === id);
        if (list) {
          list.tasks = [];
          tasks.forEach((e) => {
            list.tasks.push(e);
          });
        }
        this.activeListId = id;
        this.persist();
      }
    },
    async addTask(listId: string, title: string, description?: string) {
      const trimmed = title?.trim();
      if (!trimmed) throw new Error("Título da tarefa é obrigatório");

      const task: Task = {
        id: "",
        title: trimmed,
        description: description?.trim() || undefined,
        done: false,
        createdAt: new Date().toISOString(),
        taskListId: listId,
      };
      const response = await api.task.create(task);
      task.id = response.id;
      const list = this.lists.find((l) => l.id === listId);
      if (!list) throw new Error("Lista não encontrada");

      list.tasks.push(task);
      list.taskCount++;
      this.persist();
      return task;
    },
    async editTask(listId: string, taskId: string, updates: Task) {
      updates.taskListId = listId;
      const updated = await api.task.update(taskId, updates);
      console.log(updated);
      const list = this.lists.find((l) => l.id === listId);
      if (!list) throw new Error("Lista não encontrada");
      const task = list.tasks.find((t) => t.id === taskId);
      if (!task) throw new Error("Tarefa não encontrada");
      if (updates.title) {
        const trimmed = updates.title.trim();
        if (!trimmed) throw new Error("Título da tarefa é obrigatório");
        if (
          list.tasks.find(
            (t) =>
              t.title.toLowerCase() === trimmed.toLowerCase() && t.id !== taskId
          )
        ) {
          throw new Error("Outra tarefa com este título já existe na lista");
        }
        task.title = trimmed;
      }
      if (typeof updates.description !== "undefined") {
        task.description = updates.description?.trim() || undefined;
      }
      if (typeof updates.done === "boolean") {
        task.done = updates.done;
      }
      this.persist();
      return task;
    },
    async deleteTask(listId: string, taskId: string) {
      await api.task.delete(taskId);
      const list = this.lists.find((l) => l.id === listId);
      if (!list) throw new Error("Lista não encontrada");
      const idx = list.tasks.findIndex((t) => t.id === taskId);
      if (idx === -1) throw new Error("Tarefa não encontrada");
      list.tasks.splice(idx, 1);
      list.taskCount--;
      this.persist();
      return true;
    },
    toggleTaskDone(listId: string, taskId: string) {
      const list = this.lists.find((l) => l.id === listId);
      if (!list) throw new Error("Lista não encontrada");
      const task = list.tasks.find((t) => t.id === taskId);
      if (!task) throw new Error("Tarefa não encontrada");
      task.done = !task.done;
      api.task.toggle(taskId, task.done);
      this.persist();
      return task.done;
    },
  },
});
