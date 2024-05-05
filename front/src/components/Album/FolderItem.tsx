import React, { useState } from "react";
import plus from "@/assets/Album/plus.png"
import minus from "@/assets/Album/minus.png"
import FolderChild from "./FolderChild";

interface folderItemProps {
  folder : {
    depth: number;
    name: string;
  }
}

const childFolderList = [
  {depth: 1, name: '나는 밑에폴더'},
  {depth: 1, name: '나는 하위폴더'},
]

const FolderItem: React.FC<folderItemProps> = ({folder}) => {
  const [isOpen, setIsOpen] = useState(false)

  return (
    <div className="flex flex-col">
      <div className="w-full h-[50px] bg-white px-2 py-3 flex gap-4 items-center">
        <div onClick={() => setIsOpen(!isOpen)} className="w-6 h-6 rounded-lg bg-main-blue flex justify-center items-center">
          <img src={isOpen ? minus : plus} className="w-[14px]" />
        </div>

        <div className="font-pre-M text-black text-lg">{folder.name}</div>
      </div>
      {isOpen ? 
        <div className="flex flex-col">
          {childFolderList.map((item, idx) => (
            <FolderChild key={idx} folder={item} />
          ))}
        </div>
        : 
        <></>
      }
    </div>
  )
};

export default FolderItem;