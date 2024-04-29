import { create } from 'zustand'
import { persist } from 'zustand/middleware'
import { inviteType, memberListType } from '../types/StoreType'

export const inviteStore = create(
  persist<inviteType>((set) => ({
    inviteOpen: false,

    setInviteOpen: () => set((state) => ({inviteOpen: !state.inviteOpen}))
  }),
  { name: 'MODAL_STORE'}
))

export const memberStore = create(
  persist<memberListType>((set) => ({
    memberOpen: false,

    setMemberOpen: () => set((state) => ({memberOpen: !state.memberOpen}))
  }),
  { name: 'MODAL_STORE'}
))
