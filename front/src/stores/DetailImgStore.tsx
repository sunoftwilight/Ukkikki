import { create } from 'zustand'
import { persist } from 'zustand/middleware'
import { DetailImgStoreType } from '../types/StoreType/DetailImgStoreType'

export const DetailImgStore = create(
  persist<DetailImgStoreType>((set) => ({
    currentId: -1,
    currentImg: '',
    currentUrl: '',

    setCurrentImg: (imgId: number, imgpk: string, imgUrl: string) => set(() => ({
        currentId: imgId,
        currentImg: imgpk,
        currentUrl: imgUrl
      }))
  }),
  { name: 'MODAL_STORE'}
))

