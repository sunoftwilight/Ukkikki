import { create } from 'zustand'
import { persist } from 'zustand/middleware'
import { OptionStoreType } from '../types/StoreType/OptionStoreType';

export const optionStore = create(
  persist<OptionStoreType>((set) => ({
    startPageOpen: false,
    logoutOpen : false,

    setStartPageOpen: () => set((state) => ({startPageOpen: !state.startPageOpen})),
    setLogoutOpen: () => set((state) => ({logoutOpen: !state.logoutOpen}))
  }),
  { name: 'HEADER_STORE'}
))
