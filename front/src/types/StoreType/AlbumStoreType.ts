export interface selectModeStoreType {
  selectMode: boolean;
  setSelectMode: () => void;
}

export interface selectListStoreType {
  selectList: string[];
  setSelectList: (imgId: string, isSelect: boolean) => void;  
}

export interface PrefixStoreType {
  prefix: string;
  setPrefix: (txt: string) => void; 
}

export interface CurrentDirType {
  currentDir: string;
  parentDir: string;
  setCurrentDir: (txt: string) => void; 
  setParentDir: (txt: string) => void; 
}