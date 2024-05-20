import { create } from 'zustand'
import { persist } from 'zustand/middleware'
import { LastStoreType, LoadingStoreType } from '../types/StoreType/ChatStoreType'
// import { FocusStoreType } from '../types/StoreType/ChatStoreType'

export const loadingStore = create(
  persist<LoadingStoreType>((set) => ({
    isLoading: true,

    setIsLoading: (Is: boolean) => set(() => ({ isLoading: Is }))
  }),
  { name: 'CHAT_STORE' }
))

export const lastStore = create(
  persist<LastStoreType>((set) => ({
    isLast: false,

    setIsLast: (Is: boolean) => set(() => ({ isLast: Is }))
  }),
  { name: 'CHAT_STORE' }
))

// export const focusStore = create(
//   persist<FocusStoreType>((set) => ({
//     isFocus: false,

//     setIsFocus: () => set((state) => ({isFocus: !state.isFocus})),
//   }),
//   { name: 'CHAT_STORE' }
// ))