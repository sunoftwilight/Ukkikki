export interface selectModeStoreType {
  selectMode: boolean;
  setSelectMode: () => void;
}

export interface selectListStoreType {
  selectList: number[];
  selectListForPk: string[];
  setSelectList: (imgId: number, isSelect: boolean) => void;  
  setSelectListForPk: (imgPk: string, isSelect: boolean) => void;  
}

export interface PrefixStoreType {
  prefix: string;
  setPrefix: (txt: string) => void; 
}

export interface CurrentDirType {
  currentDirId: string;
  currentDirName: string;
  parentDirId: string;
  parentDirName: string;
  setCurrentDirId: (txt: string) => void; 
  setCurrentDirName: (txt: string) => void; 
  setParentDirId: (txt: string) => void; 
  setParentDirName: (txt: string) => void; 
}

export interface UpdateAlbumStoreType {
  needUpdate: boolean;
  setNeedUpdate: () => void;
}