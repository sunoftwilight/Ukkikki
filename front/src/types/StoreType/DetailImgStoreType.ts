export interface DetailImgStoreType {
  currentId: number;
  currentImg: string;
  currentUrl: string;
  setCurrentImg: (id: number, pk: string, url: string) => void;
}
