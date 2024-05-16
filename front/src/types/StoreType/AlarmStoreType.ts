import { AlarmItemType } from "../AlarmType";

export interface AlarmListStoreType {
  alarmList: AlarmItemType[];
  setAlarmList: (alarmItem: AlarmItemType[] | string) => void;
}

export interface AlarmOccuredStoreType {
  isAlarmOccured: boolean;
  setIsAlarmOccured: (exist: boolean) => void;
}