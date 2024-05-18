import { create } from 'zustand'
import { persist } from 'zustand/middleware'
import { selectModeStoreType, selectListStoreType, PrefixStoreType, CurrentDirType, UpdateAlbumStoreType, ImgGroupStoreType } from '../types/StoreType/AlbumStoreType'

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
    selectListForPk: [],
    
    setSelectList: (imgId: number, isSelect: boolean) => set((state) => ({
      selectList: imgId === -1 ? [] : isSelect ? state.selectList.filter(id => id !== imgId) : [...state.selectList, imgId]
    })),
    setSelectListForPk: (imgPk: string, isSelect: boolean) => set((state) => ({
      selectListForPk: imgPk === '-1' ? [] : isSelect ? state.selectListForPk.filter(pk => pk !== imgPk) : [...state.selectListForPk, imgPk]
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

export const currentDirStore = create(
  persist<CurrentDirType>((set) => ({
    currentDirId: '',
    currentDirName: '',
    parentDirId: '',
    parentDirName: '',

    setCurrentDirId: (txt: string) => set(() => ({ currentDirId: txt })),
    setCurrentDirName: (txt: string) => set(() => ({ currentDirName: txt })),
    setParentDirId: (txt: string) => set(() => ({ parentDirId: txt })),
    setParentDirName: (txt: string) => set(() => ({ parentDirName: txt }))
  }),
  { name: 'ALBUM_STORE' }
))

export const updateAlbumStore = create(
  persist<UpdateAlbumStoreType>((set) => ({
    needUpdate: false,

    setNeedUpdate: () => set((state) => ({needUpdate: !state.needUpdate})),
  }),
  { name: 'ALBUM_STORE' }
))

export const imgGroupStore = create(
  persist<ImgGroupStoreType>((set) => ({
    type: 0,
    groupName: '',
    setType: (newType:number) => (set(() => ({type: newType}))),
    setGroupName : (newName:string) => (set(() => ({groupName: newName})))
  }),
  { name: 'IMGGROUP_STORE'}
))