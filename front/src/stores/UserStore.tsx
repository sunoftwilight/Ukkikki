import { create } from 'zustand'
import { persist } from 'zustand/middleware'
import { UserStoreType } from '../types/StoreType/UserStoreType';

export const userStore = create(
  persist<UserStoreType>((set) => ({
    isLogin: false,
    isInsertPass: false,
    isCheckPass: false,

    accessToken: '',

    userId: '',
    userName: '',
    userProfile: '',

    groupKey: [],
    uploadGroupId: null,
    simplePass: '',

    setIsLogin: (newData: boolean) => set(() => ({isLogin: newData})),
    setIsInsert: (newData: boolean) => set(() => ({isInsertPass: newData})),
    setIsCheck: (newData: boolean) => set(() => ({isCheckPass: newData})),

    setAccessToken: (newData: string) => set(() => ({accessToken: newData})),

    setUserId: (newData: string) => set(() => ({userId: newData})),
    setUserName: (newData: string) => set(() => ({userName: newData})),
    setUserProfile: (newData: string) => set(() => ({userProfile: newData})),

    setGroupKey: (newData: string[]) => set(() => ({groupKey: newData})),
    setUploadGroupId: (newData: number|null) => set(() => ({uploadGroupId: newData})),
    setSimplePass: (newData: string) => set(() => ({simplePass: newData})),
  }),
  { name: 'USER_STORE'}
))

