import { create } from 'zustand'
import { persist } from 'zustand/middleware'
import { AlarmListStoreType, AlarmOccuredStoreType } from '../types/StoreType/AlarmStoreType'
import { AlarmItemType } from '../types/AlarmType'

export const AlarmListStore = create(
  persist<AlarmListStoreType>((set) => ({
    alarmList: [],

    setAlarmList: (alarmItem: AlarmItemType[] | string) => set((state) => {
      if (typeof alarmItem === 'string') {
        return { alarmList: [] }
      } else {
        return { alarmList: [...state.alarmList, ...alarmItem] }
      }
    })
  }),
  { name: 'ALARM_STORE' }
))

export const AlarmOccuredStore = create(
  persist<AlarmOccuredStoreType>((set) => ({
    isAlarmOccured: false,

    setIsAlarmOccured: (exist: boolean) => set(() => ({isAlarmOccured: exist})),
  }),
  { name: 'ALARM_STORE' }
))
