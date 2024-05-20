export interface GuestStoreType {
  isInvite: boolean;
  isGuest: boolean;
  viewPartyPk: number;

  setIsInvite: (newBool: boolean) => void;
  setIsGuest: (newBool: boolean) => void;
  setPartyPk : (newPk: number) => void;
}