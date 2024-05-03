import React from "react";
import unFavorStar from "@/assets/GroupList/unFavoriteStar.png";
import favorStar from "@/assets/GroupList/favoriteStar.png";
const ListComponent:React.FC = () => {
    const sampleList = [
        {src: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQylUM6q508tjNv_Aj4N5wRBhxmVNj3_rjXo5VCuUkiRQ&s', partyId: 1, partyName: '그룹이름보단그루비룸', favorite: false},
        {src: favorStar, partyId: 2, partyName: '카레라이스', favorite: true},
        {src: favorStar, partyId: 3, partyName: '롯사모(롯데샌드)', favorite: false},
        {src: favorStar, partyId: 4, partyName: '햄버거의킹은버거킹', favorite: false},
        {src: favorStar, partyId: 5, partyName: '뷰티풀개발자', favorite: false},
    ]
    return (
      <div className="flex flex-col gap-2">
        { sampleList.map((item, idx) => (
          <div key={idx} className="relative w-100 h-20 rounded-2xl flex items-center border border-disabled-gray">
            <div className="w-14 h-14 rounded-full ms-4 me-5">
              <img src={item.src} className="object-cover w-14 h-14 rounded-full"/>
            </div>
            <p className="text-xl font-pre-R">{item.partyName}</p>
            <img src={item.favorite ? favorStar : unFavorStar} className="absolute w-6 h-6 right-5"/>
          </div>
        ))}
      </div>
    )
}

export default ListComponent;


