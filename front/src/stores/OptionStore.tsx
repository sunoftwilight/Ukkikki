import { create } from 'zustand'
import { persist } from 'zustand/middleware'
import { optionType } from '../types/StoreType';

export const optionStore = create(
  persist<optionType>((set) => ({
    startPageOpen: false,
    logoutOpen : false,

    setStartPageOpen: () => set((state) => ({startPageOpen: !state.startPageOpen})),
    setLogoutOpen: () => set((state) => ({logoutOpen: !state.logoutOpen}))
  }),
  { name: 'HEADER_STORE'}
))
