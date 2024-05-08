import { create } from 'zustand'
import { persist } from 'zustand/middleware'
import { InviteStoreType, MemberStoreType, FolderStoreType } from '../types/StoreType/ModalStoreType'

export const inviteStore = create(
  persist<InviteStoreType>((set) => ({
    inviteOpen: false,

    setInviteOpen: () => set((state) => ({inviteOpen: !state.inviteOpen}))
  }),
  { name: 'MODAL_STORE'}
))

export const memberStore = create(
  persist<MemberStoreType>((set) => ({
    memberOpen: false,

    setMemberOpen: () => set((state) => ({memberOpen: !state.memberOpen}))
  }),
  { name: 'MODAL_STORE'}
))

export const folderStore = create(
  persist<FolderStoreType>((set) => ({
    folderOpen: false,

    setFolderOpen: () => set((state) => ({folderOpen: !state.folderOpen}))
  }),
  { name: 'MODAL_STORE'}
))
