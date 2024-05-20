import { create } from 'zustand';
import { persist } from 'zustand/middleware';
import { MemberInfoStoreType } from '../types/StoreType/MemberInfoStoreType';
import { UserGrantData } from '../types/GroupType';

export const memberInfoStore = create(
  persist<MemberInfoStoreType>((set) => ({
    data:null,
    setData: (newData: UserGrantData|null) => set(() => ({data: newData})),
  }),
  { name: 'MEMBER_INFO_STORE'}
))

