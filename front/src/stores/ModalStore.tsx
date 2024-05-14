import { create } from 'zustand'
import { persist } from 'zustand/middleware'
import { InviteStoreType, MemberStoreType, FolderStoreType } from '../types/StoreType/ModalStoreType'

export const inviteStore = create(
  persist<InviteStoreType>((set) => ({
    inviteOpen: false,
    inviteCode: '',

    setInviteOpen: () => set((state) => ({inviteOpen: !state.inviteOpen})),
    setInviteCode: (code:string) => set(() => ({inviteCode: code}))
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
    folderMode: '',

    setFolderOpen: (mode: string) => set((state) => ({
      folderOpen: !state.folderOpen,
      folderMode: mode
    }))
  }),
  { name: 'MODAL_STORE'}
))
