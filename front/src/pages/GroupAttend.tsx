import React, { useEffect, useRef, useState } from "react";
import InfoIcon from "@/assets/GroupAttend/info_icon.png";
import { userStore } from "../stores/UserStore";
import { useStore } from "zustand";
import { useNavigate, useParams } from "react-router-dom";
import { enterPartyMember, enterPartyGuest, checkPartyPass } from "../api/party";
import { guestStore } from "../stores/GuestStore";

const GroupAttend:React.FC = () => {
  const user = useStore(userStore);
  const guest = useStore(guestStore);
  const navi = useNavigate();
  const{ groupPk } = useParams();
  const [password, setPassword] = useState<string>("");
  const inputsRefs = useRef<(HTMLInputElement | null)[]>(Array(6).fill(null));

  useEffect(() => {
    if(!guest.isGuest && !user.isLogin) navi(`/group/${groupPk}/attend/login`)
  }, [])

  const checkPass = async () => {
    await checkPartyPass(Number(groupPk), password, user.simplePass,
      (res) => {
        console.log(res.data.data)
        if(guest.isGuest) {

          const sse = res.data.data.sseKey
          const pk = res.data.data.partyId
          const key:Record<number,string> = {}
          key[pk] = sse
          guest.setPartyPk(pk)
          user.setGroupKey(key)

          attendGuest();
        }
        else if(user.isLogin) {
		      const currentKeys = user.groupKey;
		      currentKeys[res.data.data.partyId] = res.data.data.sseKey;
          user.setGroupKey(currentKeys);
          attentMember();
        }
      },
      (err) => {
        console.log(err)
        alert('비밀번호에 오류가 있습니다.')
      }
    )
  }

  const attentMember = async () => {
    await enterPartyMember(Number(groupPk),
      () => {
        navi(`/group/${groupPk}`)
      },
      (err) => {
        console.error(err)
      }
    )
  }

  const attendGuest = async () => {
    await enterPartyGuest(Number(groupPk),
      (res) => {
        user.setAccessToken('Bearer ' + res.data.data.token);
        navi(`/group/${groupPk}`)
      },
      (err) => {
        console.error(err)
      }
    )
  }

	const handleInputChange = (
		index: number,
		event: React.ChangeEvent<HTMLInputElement>,
	) => {
		const value = event.target.value;
		if (index < 6) {
			setPassword(
				(prevPassword) =>
					prevPassword.substring(0, index) +
					value +
					prevPassword.substring(index + 1),
			);
		}
		if (value.length === 1 && index < 11 && inputsRefs.current[index + 1]) {
			inputsRefs.current[index + 1]?.focus();
		} else if (
			value.length === 0 &&
			index > 0 &&
			inputsRefs.current[index - 1]
		) {
			inputsRefs.current[index - 1]?.focus();
		}
	};


  return (
    <div className="w-full h-full flex flex-col bg-white p-4 font-pre-SB text-lg gap-3 items-center">
      <p>그룹 비밀번호</p>
      <div className="w-80 h-14 flex justify-between items-center">
        {Array(6)
          .fill(null)
          .map((_, index) => (
            <input
              key={index}
              type="password"
              maxLength={1}
              className="h-14 w-12 rounded-lg bg-gray text-center outline-none"
              ref={(el) => (inputsRefs.current[index] = el)}
              onChange={(event) => handleInputChange(index, event)}
            />
        ))}
      </div>
      <div
        className={(password.length === 6 ? "bg-main-blue" : "bg-disabled-gray") + " my-5 w-80 h-[45px] font-gtr-B text-base text-white rounded-lg flex justify-center items-center"}
        onClick={()=> checkPass()}>
        <p>다음</p>
      </div>
      <div className="w-80 h-[180px] bg-gray flex flex-col p-2 rounded-xl text-sm font-pre-M">
        <div className="m-2 flex items-center">
          <img src={InfoIcon} className="w-8 h-8 me-3"/>
          <div>
            <p>서비스 보안에 의해 게스트 로그인은</p>
            <p>일부 기능에 대한 이용이 제한됩니다.</p>
          </div>
        </div>
        <div className="mx-5 flex flex-col gap-2">
          <li>
            15 분간 서비스를 이용할 수 있습니다.
          </li>
          <li>
            갱신 버튼을 통해 이용 시간을 <br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;연장할 수 있습니다.
          </li>
          <li>
            사진을 다운로드만 할 수 있습니다.
          </li>
        </div>
      </div>
    </div>
  )
}

export default GroupAttend;

