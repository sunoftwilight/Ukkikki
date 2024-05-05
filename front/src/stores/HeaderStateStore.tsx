import { create } from 'zustand'
import { persist } from 'zustand/middleware'
import { albumEditType, albumDoneType, headerType } from '../types/StoreType';

export const headerStore = create(
  persist<headerType>((set) => ({
    alarmOpen: false,
    menuOpen : false,

    setAlarmOpen: () => set((state) => ({alarmOpen: !state.alarmOpen})),
    setMenuOpen: () => set((state) => ({menuOpen: !state.menuOpen}))
  }),
  { name: 'HEADER_STORE'}
))

export const albumEditStore = create(
  persist<albumEditType>((set) => ({
    isEdit: false,

    setIsEdit: () => set((state) => ({isEdit: !state.isEdit}))
  }),
  { name: 'HEADER_STORE'}
))

export const albumDoneStore = create(
  persist<albumDoneType>((set) => ({
    isDone: false,

    setIsDone: () => set((state) => ({isDone: !state.isDone}))
  }),
  { name: 'HEADER_STORE'}
))