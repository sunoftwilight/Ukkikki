import { create } from 'zustand'
import { persist } from 'zustand/middleware'
import { InviteStoreType, MemberStoreType, FolderStoreType } from '../types/StoreType/ModalStoreType'
import { MemberData } from '../types/GroupType'

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
    members: [],
    setMemberOpen: () => set((state) => ({memberOpen: !state.memberOpen})),
    setMembers: (newData: MemberData[]) => set(() => ({members: newData}))
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
