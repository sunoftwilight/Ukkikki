export interface OptionStoreType {
  startPageOpen : boolean;
  logoutOpen : boolean;
  startPage : string;

  setStartPage: (opt : string) => void;
  setStartPageOpen: () => void;
  setLogoutOpen: () => void;
}