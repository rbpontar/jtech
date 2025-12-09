import { defineStore } from "pinia";
import { api } from "../services/api";

export type User = {
  // id: string;
  name: string | undefined;
  email: string;
};

type State = {
  user: User | null;
  token: string | null;
};

const STORAGE_KEY = "user_v1";

export const useUserStore = defineStore("user", {
  state: (): State => ({
    user: null,
    token: null,
  }),
  actions: {
    init() {
      try {
        const raw = localStorage.getItem(STORAGE_KEY);
        if (raw) {
          const parsed = JSON.parse(raw);
          this.user = parsed.user ?? null;
          this.token = parsed.token ?? null;
        }
      } catch {
        this.user = null;
        this.token = null;
      }
    },
    persist() {
      const payload = {
        user: this.user,
        token: this.token,
      };
      localStorage.setItem(STORAGE_KEY, JSON.stringify(payload));
    },
    clearPersist() {
      localStorage.removeItem(STORAGE_KEY);
    },

    async login(payload: { email: string; password: string; name?: string }) {
      if (!payload.email || !payload.password) {
        throw new Error("Email e senha são obrigatórios");
      }

      try {
        const response = await api.auth.login({
          email: payload.email,
          password: payload.password,
        });

        this.user = {
          name: response.name ?? payload.email.split("@")[0],
          email: payload.email,
        };

        this.token = response.accessToken;
        this.persist();
        return true;
      } catch (error) {
        const message =
          error instanceof Error ? error.message : "Falha ao entrar";
        throw new Error(message);
      }
    },
    async register(payload: { email: string; password: string; name: string }) {
      if (!payload.email || !payload.password) {
        throw new Error("Email e senha são obrigatórios");
      }

      try {
        const response = await api.auth.register({
          email: payload.email,
          password: payload.password,
          name: payload.name,
        });
        console.log("Register response:", response);

        this.user = {
          name: payload.email.split("@")[0],
          email: payload.email,
        };

        // this.token = response.accessToken;
        // this.persist();
        return true;
      } catch (error) {
        const message =
          error instanceof Error ? error.message : "Falha ao entrar";
        throw new Error(message);
      }
    },
    async logout() {
      try {
        // await api.auth.logout();
      } catch {
        this.user = null;
        this.token = null;
      }

      this.user = null;
      this.token = null;
      this.clearPersist();
    },
  },
});
