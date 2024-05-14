import React, { useEffect, useState } from "react";
import plus from "@/assets/Album/plus.png"
import minus from "@/assets/Album/minus.png"
import FolderChild from "./FolderChild";
import { childDirItem } from "../../types/AlbumType";
import { getChildDir } from "../../api/directory";

interface folderItemProps {
  folder : {
    pk: string;
    name: string;
    depth: number;
  }
}

const childFolderList = [
  {depth: 1, name: '나는 밑에폴더', pk: ''},
  {depth: 1, name: '나는 하위폴더', pk: ''},
]

const FolderItem: React.FC<folderItemProps> = ({folder}) => {
  const [isOpen, setIsOpen] = useState(false)
  // const [childFolderList, setChildFolderList] = useState<childDirItem[]>([])

  useEffect(() => {
    getChildDir(
      folder.pk,
      (res) => {
        console.log(res.data.data)
        // setChildFolderList(res.data.data)
      },
      (err) => { console.error(err) }
    )
  }, [])

  return (
    <div className="flex flex-col">
      <div className="w-full h-[50px] bg-white px-2 py-3 flex gap-4 items-center" style={{ paddingLeft: `${folder.depth * 30}px` }}>
        <div onClick={() => setIsOpen(!isOpen)} className="w-6 h-6 rounded-lg bg-main-blue flex justify-center items-center">
          <img src={isOpen ? minus : plus} className="w-[14px]" />
        </div>

        <div className="font-pre-M text-black text-lg">{folder.name}</div>
      </div>
      {/* {isOpen ? 
        <div className="flex flex-col">
          {childFolderList.map((item, idx) => (
            <FolderChild key={idx} folder={item} />
          ))}
        </div>
        : 
        <></>
      } */}
    </div>
  )
};

export default FolderItem;