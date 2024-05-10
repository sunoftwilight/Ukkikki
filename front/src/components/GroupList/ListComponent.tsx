import React, { useEffect, useState } from "react";
import unFavorStar from "@/assets/GroupList/unFavoriteStar.png";
import favorStar from "@/assets/GroupList/favoriteStar.png";
import { Link } from "react-router-dom";
import { getPartyList } from "../../api/party";

const ListComponent:React.FC = () => {
    const [partyList, setPartList] = useState<any[]>([])

    const getList = async () => {
      await getPartyList(
        (res) => {
          console.log(res)
        },
        (err) => {
          console.log(err)
        }
      )
    }
    useEffect(() => {
      getList();
    },[])

    return (
      <div className="flex flex-col gap-2">
      { partyList.map((item, idx) => (
        <Link to={'/group'} key={idx} className="relative w-100 h-20 rounded-2xl flex items-center border border-disabled-gray">
          <img src={item.src} className="w-14 h-14 rounded-full ms-4 me-5"/>
          <p className="text-xl font-pre-R">{item.partyName}</p>
          <img src={item.favorite ? favorStar : unFavorStar} className="absolute w-6 h-6 right-5"/>
        </Link>
      ))}
    </div>
    )
}

export default ListComponent;


