import { create } from 'zustand'
import { persist } from 'zustand/middleware'
import { UserStoreType } from '../types/StoreType/UserStoreType';

export const userStore = create(
  persist<UserStoreType>((set) => ({
    accessToken: '',
    userId: '',
    userName: '',
    userProfile: '',
    setAccessToken: (newData: string) => set(() => ({accessToken: newData})),
    setUserId: (newData: string) => set(() => ({userId: newData})),
    setUserName: (newData: string) => set(() => ({userName: newData})),
    setUserProfile: (newData: string) => set(() => ({userProfile: newData}))
  }),
  { name: 'USER_STORE'}
))

