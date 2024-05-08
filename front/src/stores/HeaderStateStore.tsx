import { create } from 'zustand'
import { persist } from 'zustand/middleware'
import { AlbumEditStoreType, AlbumDoneStoreType, HeaderStoreType } from '../types/StoreType/HeaderStateStoreType';

export const headerStore = create(
  persist<HeaderStoreType>((set) => ({
    alarmOpen: false,
    menuOpen : false,

    setAlarmOpen: () => set((state) => ({alarmOpen: !state.alarmOpen})),
    setMenuOpen: () => set((state) => ({menuOpen: !state.menuOpen}))
  }),
  { name: 'HEADER_STORE'}
))

export const albumEditStore = create(
  persist<AlbumEditStoreType>((set) => ({
    isEdit: false,

    setIsEdit: () => set((state) => ({isEdit: !state.isEdit}))
  }),
  { name: 'HEADER_STORE'}
))

export const albumDoneStore = create(
  persist<AlbumDoneStoreType>((set) => ({
    isDone: false,

    setIsDone: () => set((state) => ({isDone: !state.isDone}))
  }),
  { name: 'HEADER_STORE'}
))