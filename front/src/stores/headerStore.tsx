import { create } from 'zustand'
import { persist } from 'zustand/middleware'
import headerType from '../types/storeTypes/HeaderType';

const headerStore = create(
  persist<headerType>((set) => ({
    alarmOpen: false,
    menuOpen : false,

    setAlarmOpen: () => set((state) => ({alarmOpen: !state.alarmOpen})),
    setMenuOpen: () => set((state) => ({menuOpen: !state.menuOpen}))
  }),
  { name: 'HEADER_STORE'}
))

export default headerStore;