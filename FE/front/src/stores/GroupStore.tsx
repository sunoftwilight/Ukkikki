import { create } from 'zustand'
import { persist } from 'zustand/middleware'
import { GroupStoreType } from '../types/StoreType/GroupStoreType'

export const currentGroupStore = create(
  persist<GroupStoreType>((set) => ({
    currentGroup: -1,

    setCurrentGroup: (partyId: number) => set(() => ({ currentGroup: partyId })),
  }),
  { name: 'GROUP_STORE' }
))