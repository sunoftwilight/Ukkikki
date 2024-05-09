import { create } from 'zustand'
import { persist } from 'zustand/middleware'
import { DetailImgStoreType } from '../types/StoreType/DetailImgStoreType'

export const DetailImgStore = create(
  persist<DetailImgStoreType>((set) => ({
    currentImg: '',
    currentUrl: '',

    setCurrentImg: (imgPk: string, imgUrl: string) => set(() => ({
        currentImg: imgPk,
        currentUrl: imgUrl
      }))
  }),
  { name: 'MODAL_STORE'}
))

