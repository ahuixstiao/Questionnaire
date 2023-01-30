import {defineStore} from 'pinia';

export const userStore = defineStore('user', {
    state: () => ({
        users: {
            avatar: localStorage.getItem("avatar"),
            nickName: localStorage.getItem("nickName"),
        }
    }),
})