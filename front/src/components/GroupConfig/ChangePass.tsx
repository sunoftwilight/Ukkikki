import React, { useRef, useState } from "react";
import { useStore } from "zustand";
import { userStore } from "../../stores/UserStore";
import { useNavigate, useParams } from "react-router-dom";
import { changePartyPass } from "../../api/party";
import { GroupKey } from "../../types/GroupType";

const ChangePass: React.FC = () => {
  const user = useStore(userStore);
  const navi = useNavigate();
  const {groupPk} = useParams();

  const [currentPass, setCurrentPass] = useState<string>("");
  const [pass, setPass] = useState<string>("");
	const [confirmPass, setConfirmPass] = useState<string>("");
  const inputsRefs = useRef<(HTMLInputElement | null)[]>(Array(18).fill(null));

	const handleInputChange = (
		index: number,
		event: React.ChangeEvent<HTMLInputElement>,
	) => {
		const value = event.target.value;
		if (index < 6) {
			setCurrentPass(
				(prevCurrentPass) =>
					prevCurrentPass.substring(0, index) +
					value +
					prevCurrentPass.substring(index + 1),
			);
		} else if (6 <= index && index < 12) {
			setPass(
				(prevPass) =>
					prevPass.substring(0, index - 6) +
					value +
					prevPass.substring(index - 6 + 1),
			);
		} else {
      setConfirmPass(
				(prevConfirmPass) =>
					prevConfirmPass.substring(0, index - 12) +
					value +
					prevConfirmPass.substring(index - 12 + 1),
			);
    }
    
		if (value.length === 1 && index < 17 && inputsRefs.current[index + 1]) {
			inputsRefs.current[index + 1]?.focus();
		} else if (
			value.length === 0 &&
			index > 0 &&
			inputsRefs.current[index - 1]
		) {
			inputsRefs.current[index - 1]?.focus();
		}
	};

  const clickChangeBtn = async() => {
    if (pass !== confirmPass || confirmPass.length !== 6) return;
    const data = {
      beforePassword : currentPass,
      afterPassword : pass,
      simplePassword: user.simplePass,
    }
    console.log(data)
    await changePartyPass(Number(groupPk), data, 
      (res) => {
				dataSetUp(res.data.data);
        navi(-1)
      },
      (err) => {
        console.error(err)
      })
  }

	const dataSetUp = (data: GroupKey) => {
		//현재 그룹키 목록
		const currentKeys = user.groupKey;

		// 그룹키 갱신
		currentKeys[data.partyId] = data.sseKey;
		user.setGroupKey(currentKeys);
	};

  return (
    <div className="flex flex-col w-full h-full font-pre-SB px-4">
      <div className="w-full mb-10">
        <p className="text-lg">현재 비밀번호</p>
        <div className="w-full h-14 flex justify-between">
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
      </div>
      
      <div className="w-full mb-10">
        <p className="text-lg mb-3">새 비밀번호</p>
        <div className="w-full h-14 flex justify-between">
        {Array(6)
						.fill(null)
						.map((_, index) => (
							<input
								key={index}
								type="password"
								maxLength={1}
								className="h-14 w-12 rounded-lg bg-gray text-center outline-none"
								ref={(el) => (inputsRefs.current[index+6] = el)}
								onChange={(event) => handleInputChange(index + 6, event)}
							/>
						))}
        </div>
      </div>

      <div className="w-full mb-10">
        <p className="text-lg mb-3">새 비밀번호 확인</p>
        <div className="w-full h-14 flex justify-between">
        {Array(6)
						.fill(null)
						.map((_, index) => (
							<input
								key={index}
								type="password"
								maxLength={1}
								className="h-14 w-12 rounded-lg bg-gray text-center outline-none"
								ref={(el) => (inputsRefs.current[index+12] = el)}
								onChange={(event) => handleInputChange(index+12, event)}
							/>
						))}
        </div>
      </div>

      <div className="w-full text-xl text-white">
        <button
          className={(confirmPass.length === 6 ? "bg-main-blue" : "bg-disabled-gray") + " my-5 w-80 h-[45px] font-gtr-B text-base text-white rounded-lg flex justify-center items-center"}
          onClick={() => clickChangeBtn()}>
          <p>변경</p>
        </button>
      </div>
    </div>
  )
}

export default ChangePass