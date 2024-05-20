export interface HeaderStoreType {
  alarmOpen : boolean;
  menuOpen : boolean;
  setAlarmOpen: () => void;
  setMenuOpen: () => void;
}

export interface AlbumEditStoreType {
  isEdit : boolean;
  setIsEdit: () => void;
}

export interface AlbumDoneStoreType {
  isDone : boolean;
  setIsDone: () => void;
}