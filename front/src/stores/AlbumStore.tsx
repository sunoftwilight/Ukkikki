import { create } from 'zustand'
import { persist } from 'zustand/middleware'
import { selectModeType, selectListType } from '../types/StoreType'

export const selectModeStore = create(
  persist<selectModeType>((set) => ({
    selectMode: false,

    setSelectMode: () => set((state) => ({selectMode: !state.selectMode})),
  }),
  { name: 'ALBUM_STORE' }
))

export const selectStore = create(
  persist<selectListType>((set) => ({
    selectList: [],
    
    setSelectList: (imgId: number, isSelect: boolean) => set((state) => ({
      selectList: isSelect ? state.selectList.filter(id => id !== imgId) : [...state.selectList, imgId]
    }))
  }),
  { name: 'ALBUM_STORE' }
))