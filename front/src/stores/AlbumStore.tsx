import { create } from 'zustand'
import { persist } from 'zustand/middleware'
import { selectModeType } from '../types/StoreType'

export const selectModeStore = create(
  persist<selectModeType>((set) => ({
    selectMode: false,

    setSelectMode: () => set((state) => ({selectMode: !state.selectMode})),
  }),
  { name: 'ALBUM_STORE'}
))

