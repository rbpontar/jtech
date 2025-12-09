import { describe, it, expect, beforeEach } from "vitest";
import { setActivePinia, createPinia } from "pinia";
import { useUserStore } from "../stores/user";

const STORAGE_KEY = "user_v1";

describe("useUserStore", () => {
  beforeEach(() => {
    setActivePinia(createPinia());
    localStorage.clear();
  });

  it("login stores user and token and persists", async () => {
    const store = useUserStore();
    await store.login({ email: "a@b.com", password: "p", name: "Alice" });
    expect(store.user).toBeTruthy();
    expect(store.user?.email).toBe("a@b.com");
    expect(store.token).toBeTruthy();

    const raw = localStorage.getItem(STORAGE_KEY);
    expect(raw).toBeTruthy();
    const parsed = JSON.parse(raw as string);
    expect(parsed.user.email).toBe("a@b.com");
    expect(parsed.token).toBeTruthy();
  });

  it("logout clears user and localStorage", async () => {
    const store = useUserStore();
    await store.login({ email: "x@y.com", password: "p", name: "X" });
    store.logout();
    expect(store.user).toBeNull();
    expect(store.token).toBeNull();
    expect(localStorage.getItem(STORAGE_KEY)).toBeNull();
  });

  it("init loads persisted session", () => {
    const payload = {
      user: { id: "1", name: "Bob", email: "bob@example.com" },
      token: "tkn",
    };
    localStorage.setItem(STORAGE_KEY, JSON.stringify(payload));
    const store = useUserStore();
    store.init();
    expect(store.user?.email).toBe("bob@example.com");
    expect(store.token).toBe("tkn");
  });
});
