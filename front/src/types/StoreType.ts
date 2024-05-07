export interface headerType {
  alarmOpen : boolean;
  menuOpen : boolean;
  setAlarmOpen: () => void;
  setMenuOpen: () => void;
}

export interface albumEditType {
  isEdit : boolean;
  setIsEdit: () => void;
}

export interface albumDoneType {
  isDone : boolean;
  setIsDone: () => void;
}

export interface inviteType {
  inviteOpen : boolean;
  setInviteOpen: () => void;
}

export interface memberListType {
  memberOpen : boolean;
  setMemberOpen: () => void;
}

export interface detailImgType {
  currentImg: number
  currentUrl: string
  setCurrentImg: (pk: number, url: string) => void;
}

export interface selectModeType {
  selectMode: boolean;
  setSelectMode: () => void;
}

export interface folderModalType {
  folderOpen: boolean;
  setFolderOpen: () => void;
}

export interface optionType {
  startPageOpen : boolean;
  logoutOpen : boolean;
  setStartPageOpen: () => void;
  setLogoutOpen: () => void;
}