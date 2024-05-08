import { create } from 'zustand'
import { persist } from 'zustand/middleware'
import { DetailImgStoreType } from '../types/StoreType/DetailImgStoreType'

export const DetailImgStore = create(
  persist<DetailImgStoreType>((set) => ({
    currentImg: -1,
    currentUrl: '',

    setCurrentImg: (imgPk: number, imgUrl: string) => set(() => ({
        currentImg: imgPk,
        currentUrl: imgUrl
      }))
  }),
  { name: 'MODAL_STORE'}
))

