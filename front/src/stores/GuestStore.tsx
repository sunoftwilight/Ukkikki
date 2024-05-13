import { create } from 'zustand'
import { persist } from 'zustand/middleware'
import { GuestStoreType } from '../types/StoreType/GuestStoreType';

export const GuestStore = create(
  persist<GuestStoreType>((set) => ({
    isGuest: false,
    isInvite: false,
    viewPartyPk: 0,

    setIsGuest: (newData: boolean) => set(() => ({isGuest: newData})),
    setIsInvite: (newData: boolean) => set(() => ({isInvite: newData})),
    setPartyPk: (newData: number) => set(() => ({viewPartyPk: newData})),
  }),
  { name: 'GUEST_STORE'}
))

