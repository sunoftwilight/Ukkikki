import { create } from 'zustand'
import { persist } from 'zustand/middleware'
import { selectTrashStoreType } from '../types/StoreType/TrashStoreType'
import { UpdateAlbumStoreType } from '../types/StoreType/AlbumStoreType'

export const selectTrashStore = create<selectTrashStoreType>(
  (set) => ({
    selectTrash: [],

    setSelectTrash: (imgPk: string, isSelect: boolean) => set((state) => ({
      selectTrash: imgPk === '-1' ? [] : isSelect ? state.selectTrash.filter(pk => pk !== imgPk) : [...state.selectTrash, imgPk]
    }))
  })
)

export const updateTrashStore = create(
  persist<UpdateAlbumStoreType>((set) => ({
    needUpdate: false,

    setNeedUpdate: () => set((state) => ({needUpdate: !state.needUpdate})),
  }),
  { name: 'ALBUM_STORE' }
))