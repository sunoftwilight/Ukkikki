export interface selectModeStoreType {
  selectMode: boolean;
  setSelectMode: () => void;
}

export interface selectListStoreType {
  selectList: number[];
  setSelectList: (imgId: number, isSelect: boolean) => void;  
}

export interface PrefixStoreType {
  prefix: string;
  setPrefix: (txt: string) => void; 
}