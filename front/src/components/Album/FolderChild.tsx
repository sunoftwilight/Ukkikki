import React, { useState, useEffect } from "react";
import plus from "@/assets/Album/plus.png"
import minus from "@/assets/Album/minus.png"
import { childDirItem } from "../../types/AlbumType";
import { getChildDir } from "../../api/directory";

interface folderChildProps {
  folder : {
    pk: string;
    name: string;
  }
}

const FolderChild: React.FC<folderChildProps> = ({folder}) => {
  const [isOpen, setIsOpen] = useState(false)
  const [childFolderList, setChildFolderList] = useState<childDirItem[]>([])

  useEffect(() => {
    getChildDir(
      folder.pk,
      (res) => {
        console.log(res.data.data)
        setChildFolderList(res.data.data)
      },
      (err) => { console.error(err) }
    )
  }, [])

  return (
    <div className="w-full h-[50px] bg-white px-2 py-3 flex gap-4 items-center pl-[30px]">
      <div onClick={() => setIsOpen(!isOpen)} className="w-6 h-6 rounded-lg bg-main-blue flex justify-center items-center">
        <img src={isOpen ? minus : plus} className="w-[14px]" />
      </div>

      <div className="font-pre-M text-black text-lg">{folder.name}</div>
    </div>
  )
};

export default FolderChild;