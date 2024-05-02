import React from "react";

const chatList = [
  {type: 'admit', date: '2018-06-17T22:46:17.348', profileUrl: '', name: '용달용수', msg: ''},
  {type: 'admit', date: '2018-06-17T22:46:17.348', profileUrl: '', name: '난지니야해지니야', msg: ''},
  {type: 'msg', date: '2018-06-17T22:46:17.348', profileUrl: 'https://m.segye.com/content/image/2021/07/29/20210729517145.jpg', name: '이상수현실수', msg: '이상과현실 그 사이 어디쯤'},
  {type: 'msg', date: '2018-06-17T22:46:17.348', profileUrl: 'https://mblogthumb-phinf.pstatic.net/MjAxNzAxMDZfMzQg/MDAxNDgzNjg5NDE1Nzky.UAoMiE1Ie787serClFjniWB6-7oh-ytv6hdpkc62Jacg.R65uBZRoATAKXRt96NVKz3vO7eJlciV0irAU0CFOY-cg.PNG.citykang100/%EC%A3%BC%EB%B1%83.png?type=w420', name: '박쥐훈쥐박', msg: '박마우스훈 아니고 배트훈박마우스훈 아니고 배트훈박마우스훈 아니고 배트훈박마우스훈 아니고 배트훈박마우스훈 아니고 배트훈박마우스훈 아니고 배트훈박마우스훈 아니고 배트훈박마우스훈 아니고 배트훈박마우스훈 아니고 배트훈박마우스훈 아니고 배트훈박마우스훈 아니고 배트훈박마우스훈 아니고 배트훈박마우스훈 아니고 배트훈박마우스훈 아니고 배트훈박마우스훈 아니고 배트훈박마우스훈 아니고 배트훈박마우스훈 아니고 배트훈박마우스훈 아니고 배트훈박마우스훈 아니고 배트훈박마우스훈 아니고 배트훈박마우스훈 아니고 배트훈박마우스훈 아니고 배트훈박마우스훈 아니고 배트훈박마우스훈 아니고 배트훈박마우스훈 아니고 배트훈박마우스훈 아니고 배트훈박마우스훈 아니고 배트훈박마우스훈 아니고 배트훈박마우스훈 아니고 배트훈박마우스훈 아니고 배트훈박마우스훈 아니고 배트훈박마우스훈 아니고 배트훈박마우스훈 아니고 배트훈박마우스훈 아니고 배트훈'},
  {type: 'msg', date: '2018-06-17T22:46:17.348', profileUrl: 'https://mblogthumb-phinf.pstatic.net/MjAyMDAyMjBfMTcx/MDAxNTgyMTc3NzE4MTM2.3TEgVNEr9dY-u4xYKIjvdUORI5S4JxddBEdw3NObSE8g.VArR98Hw0A6Db46aYtCHBb6WGQyWHV5SGjWsCRGl3xQg.JPEG.cutechoromi/%EC%9A%A9%EB%8B%AC%EC%B0%A8%EB%B9%84%EC%9A%A91.jpg?type=w800', name: '용달용수', msg: '이사할땐 역시! 용달용수!'},
  {type: 'msg', date: '2018-06-17T22:46:17.348', profileUrl: 'https://img.hankyung.com/photo/202301/01.32332945.1.jpg', name: '성규이름규', msg: '양성규음성규양성규말성규'},
  {type: 'admit', date: '', profileUrl: '2018-06-17T22:46:17.348', name: '병조고약주고', msg: ''},
  {type: 'msg', date: '2018-06-17T22:46:17.348', profileUrl: 'https://i.namu.wiki/i/f9-Qqr0oPVJnyPbCVqNBgOk4wGfJTwQKRS15GG7Ui3DC0_KMCw8gvMwy4voW0-wet77R-Uvz1iz3IQMBOBbQ3A.webp', name: '병조고약주고', msg: '두통엔 게보린!'},
]

const ChattingRoom: React.FC = () => {
  return (
    <div className="flex flex-col w-full h-full overflow-scroll scrollbar-hide z-10">
      { chatList.map((item, idx) => (
        item.type === 'admit' 
        ? 
        <div key={idx} className="w-full mb-[10px] rounded-[15px] py-2 bg-disabled-gray opacity-80 font-pre-R text-white text-sm flex justify-center items-center">
          {item.name} 님이 그룹에 참여했습니다.
        </div>
        : 
        <div key={idx} className="w-full mb-[10px] rounded-[15px] bg-soft-gray opacity-80 flex py-2 px-[10px] gap-[10px]">
          <img src={item.profileUrl} className="rounded-full w-[50px] h-[50px]" />
          <div className="flex flex-col gap-2">
            <div className="flex gap-2 items-center mt-[2px]">
              <div className="font-pre-SB text-black text-xs">{item.name}</div>
              <div className="font-pre-L text-point-gray text-[10px]">{item.date.split('T')[0]} &nbsp; {item.date.split('T')[1]}</div>
            </div>
            <div className="font-pre-L text-black text-sm">{item.msg}</div>
          </div>
        </div>
      ))}
    </div>
  )
};

export default ChattingRoom;