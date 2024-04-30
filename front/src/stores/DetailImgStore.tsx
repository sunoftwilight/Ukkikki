import { create } from 'zustand'
import { persist } from 'zustand/middleware'
import { detailImgType } from '../types/StoreType'

export const DetailImgStore = create(
  persist<detailImgType>((set) => ({
    currentImg: -1,
    currentUrl: '',

    setCurrentImg: (imgPk: number, imgUrl: string) => set(() => ({
        currentImg: imgPk,
        currentUrl: imgUrl
      }))
  }),
  { name: 'MODAL_STORE'}
))

