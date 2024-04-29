import React from "react";
import close from '@/assets/Hamburger/close.png'
import ModalBackground from "./ModalBackground";
import headerStore from "../../stores/headerStore";
import { Link } from "react-router-dom";

const alarmDummy = [
  {
    groupImg: 'https://i.namu.wiki/i/VMIHkLm6DcUT4d9-vN4yFw7Yfitr8luT_U2YwJsugGodCQ01ooGH_kHX0D6sJ3HDS1YHfvy9B81al8rKCxqKYw.webp',
    groupName: '폼폼푸린은귀여워',
    member: '훈지훈',
    content: '이 사진 왜캐 잘나옴? 사기 ㄴ'
  },
  {
    groupImg: 'https://i.namu.wiki/i/VMIHkLm6DcUT4d9-vN4yFw7Yfitr8luT_U2YwJsugGodCQ01ooGH_kHX0D6sJ3HDS1YHfvy9B81al8rKCxqKYw.webp',
    groupName: '폼폼푸린은귀여워',
    member: '성규규성',
    content: '오 이거 쩐다 레전드'
  },
  {
    groupImg: 'https://i.namu.wiki/i/77QbC6SyC4wLmwtoCMzxvC-8OgmQ191Ve79xM2r5xMBb5-sqpqZem9lVMxNz9FffLio1RuGSDla-4gISPO4jAQ.webp',
    groupName: '시나모롤도귀여워',
    member: '용수용수선생',
    content: 'ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ형님'
  },
  {
    groupImg: 'https://i.namu.wiki/i/77QbC6SyC4wLmwtoCMzxvC-8OgmQ191Ve79xM2r5xMBb5-sqpqZem9lVMxNz9FffLio1RuGSDla-4gISPO4jAQ.webp',
    groupName: '시나모롤도귀여워',
    member: '상수시치',
    content: '에반데 ;;;;;;'
  },
]

const Hamburger: React.FC = () => {
  const menuList = [
    { name: '카메라', router: '/camera'},
    { name: '마이 앨범', router: '/mypage'},
    { name: '참여 중인 그룹', router: '/grouplist'},
    { name: '설정', router: '/setting'}
  ]

  const { alarmOpen, setAlarmOpen } = headerStore()
  const { menuOpen, setMenuOpen } = headerStore()

  const closeHandler = () => {
    if (alarmOpen) {
      setAlarmOpen()
    } else if (menuOpen) {
      setMenuOpen()
    }
  }

  return (
    <>
    { (alarmOpen || menuOpen) && 
      <div className="fixed top-0 start-0 h-screen w-screen flex justify-end">
          <ModalBackground />
        <div className="h-full w-72 z-20 bg-white p-4 flex flex-col gap-y-7">
          {/* 햄버거 제목 & 닫기 버튼 */}
          <div className="flex justify-between items-center">
            <div className="font-gtr-B text-3xl text-black">
              {menuOpen ? '메뉴' : '알림함'}
            </div>
            <img src={close} onClick={() => closeHandler()} className="font-gtr-R text-black text-xl" />
          </div>

          {/* 메뉴바 */}
            { menuOpen &&
              <div>
                {menuList.map((menuItem, idx) => (
                  <Link 
                    onClick={() => closeHandler()}
                    to={menuItem.router} 
                    key={idx} 
                    className="w-full h-12 flex items-center px-1"
                  >
                    {menuItem.name}
                  </Link>
                ))}
              </div>
            }

          {/* 알림함 */}
            { alarmOpen &&
              alarmDummy.map((alarmItem, idx) => (
                <div key={idx} className="flex gap-3">
                  <img src={alarmItem.groupImg} className="rounded-full w-12 h-12" />
                  <div className="flex flex-col gap-2">
                    <div className="font-gtr-B text-xs">{alarmItem.groupName}</div>
                    <div>
                      <div className="text-xs font-pre-R text-black">
                        <span className="font-pre-SB">{alarmItem.member}</span>님께서 댓글을 작성하였습니다.
                      </div>
                      <div className="font-pre-R text-xs text-black">{alarmItem.content}</div>
                    </div>
                  </div>
                </div>
              ))
            }
        </div>
      </div>
    }
    </>
  )
};

export default Hamburger;