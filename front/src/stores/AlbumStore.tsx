import { create } from 'zustand'
import { persist } from 'zustand/middleware'
import { selectModeStoreType, selectListStoreType, PrefixStoreType } from '../types/StoreType/AlbumStoreType'

export const selectModeStore = create(
  persist<selectModeStoreType>((set) => ({
    selectMode: false,

    setSelectMode: () => set((state) => ({selectMode: !state.selectMode})),
  }),
  { name: 'ALBUM_STORE' }
))

export const selectStore = create<selectListStoreType>(
  (set) => ({
    selectList: [],
    
    setSelectList: (imgId: number, isSelect: boolean) => set((state) => ({
      selectList: isSelect ? state.selectList.filter(id => id !== imgId) : [...state.selectList, imgId]
    }))
  })
)

export const prefixStore = create(
  persist<PrefixStoreType>((set) => ({
    prefix: '',

    setPrefix: (txt: string) => set(() => ({ prefix: txt }))
  }),
  { name: 'ALBUM_STORE' }
))