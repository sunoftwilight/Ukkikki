import React from "react";

const groupingList = [
  'https://i.namu.wiki/i/ar31UtD4lV4VL2tAVenmjnbaXubUHt2Izru5RSCnWZQzVf9Bv-RaCmzTFOfGoYwseU48SgVbxklmixHEEjAQKw.gif',
  'https://i.namu.wiki/i/T77l6lbrWLBsJ-xxMTngpFV__1BPVdnINmOinn7F1pSygPf6LLkNlUguiy5I7gKLVDYA3tF6PNMhCO8uwe781A.gif',
  'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTy5XjxqRj1QOhd2hULVV5MU8FFWJhBbZ2Y36Qw0TSWVuc2Y1PWbqLzqQjxXTsSILJWkz0&usqp=CAU',
  'https://trboard.game.onstove.com/Data/TR/20170720/0/636361079559147105.GIF',
  'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTy5XjxqRj1QOhd2hULVV5MU8FFWJhBbZ2Y36Qw0TSWVuc2Y1PWbqLzqQjxXTsSILJWkz0&usqp=CAU',
  'https://i.namu.wiki/i/ar31UtD4lV4VL2tAVenmjnbaXubUHt2Izru5RSCnWZQzVf9Bv-RaCmzTFOfGoYwseU48SgVbxklmixHEEjAQKw.gif',
  'https://i.namu.wiki/i/T77l6lbrWLBsJ-xxMTngpFV__1BPVdnINmOinn7F1pSygPf6LLkNlUguiy5I7gKLVDYA3tF6PNMhCO8uwe781A.gif',
  'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTy5XjxqRj1QOhd2hULVV5MU8FFWJhBbZ2Y36Qw0TSWVuc2Y1PWbqLzqQjxXTsSILJWkz0&usqp=CAU',
  'https://trboard.game.onstove.com/Data/TR/20170720/0/636361079559147105.GIF',
  'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTy5XjxqRj1QOhd2hULVV5MU8FFWJhBbZ2Y36Qw0TSWVuc2Y1PWbqLzqQjxXTsSILJWkz0&usqp=CAU',
]
const ImageGrouping: React.FC = () => {
  return (
    <div className="w-full h-[72px] flex rounded-xl bg-soft-gray items-center ps-[10px] overflow-x-auto scrollbar-hide">
      <div style={{ minWidth: `${groupingList.length * 72}px` }} className="flex gap-5">
        { groupingList.map((item, idx) => (
          <div key={idx} className="w-[52px] h-[52px] rounded-full border-disabled-gray border-[1px] flex justify-center items-center">
            <img src={item} className="w-12 h-12 object-cover rounded-full" />
          </div>
        ))}
      </div>
    </div>
  )
};

export default ImageGrouping;