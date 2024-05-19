import { create } from 'zustand'
import { persist } from 'zustand/middleware'
import { PageStoreType } from '../types/StoreType/PageStoreType'



export const pageStore = create(
  persist<PageStoreType>((set) => ({
    isComeGroup: false,

    setIsComeGroup: (newData:boolean) => set(() => ({isComeGroup: newData})),
  }),
  { name: 'PAGE_STORE' }
))