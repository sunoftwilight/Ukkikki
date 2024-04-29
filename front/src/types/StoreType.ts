export interface headerType {
  alarmOpen : boolean;
  menuOpen : boolean;
  setAlarmOpen: () => void;
  setMenuOpen: () => void;
}

export interface inviteType {
  inviteOpen : boolean;
  setInviteOpen: () => void;
}

export interface memberListType {
  memberOpen : boolean;
  setMemberOpen: () => void;
}