import React, { useState, useEffect, useRef } from "react";
import { InsertPasswordProps } from "../../types/Group";
import { createParty } from "../../api/party"; 
import { userStore } from "../../stores/UserStore";
import { useStore } from "zustand";
import { PartyData } from "../../types/Group";

const InsertPassword: React.FC<InsertPasswordProps> = ({ onBackBtnClick, onNextBtnClick, createData, doneData }) => {
  const [password, setPassword] = useState<string>("");
  const [confirmPassword, setConfirmPassword] = useState<string>("");
  const inputsRefs = useRef<(HTMLInputElement | null)[]>(Array(12).fill(null));
  const user = useStore(userStore)

  useEffect(() => {
    if (inputsRefs.current[0]) {
      inputsRefs.current[0]?.focus();
    }
  }, []);

  const handleBackBtnClick = () => {
    onBackBtnClick('info', createData);
  };

  const handleNextBtnClick = () => {
    if (password === confirmPassword) {
      createData.partyPass = password;
      createRequest();
    } else {
      alert('다시 입력해주세요.')
    }
  };

  const handleInputChange = (index: number, event: React.ChangeEvent<HTMLInputElement>) => {
    const value = event.target.value;
    if (index < 6) {
      setPassword((prevPassword) => prevPassword.substring(0, index) + value + prevPassword.substring(index + 1));
    } else {
      setConfirmPassword((prevConfirmPassword) => prevConfirmPassword.substring(0, index - 6) + value + prevConfirmPassword.substring(index - 6 + 1));
    }
    if (value.length === 1 && index < 11 && inputsRefs.current[index + 1]) {
      inputsRefs.current[index + 1]?.focus();
    } else if (value.length === 0 && index > 0 && inputsRefs.current[index - 1]) {
      inputsRefs.current[index - 1]?.focus();
    }
  };

  const createRequest = async () => {
    const param = {
      password: createData.partyPass,
      partyName: createData.partyName,
      simplePassword: user.simplePass
    }

    const formData = new FormData();
    const key = new Blob([JSON.stringify(param)], {type: 'application/json',});
    const file = new File([createData.partyProfile], 'image.jpeg', {type: 'image/jpeg'});

    formData.append('createPartyDto', key);
    formData.append('photo', file);

    await createParty(formData,
      (response) => {
        const data = response.data.data;
        dataSetUp(data)
      },
      (error) => {
        console.error(error)
      }
    )
  }

  const dataSetUp = (data: PartyData) => {
    //현재 그룹키 목록
    const currentKeys = user.groupKey
    
    // 생성 완료 데이터 처리
    doneData.partyName = data.partyName;
    doneData.partyPk = data.party;
    doneData.inviteCode = data.partyLink;

    // 그룹키 갱신
    currentKeys[data.party] = data.sseKey;
    user.setGroupKey(currentKeys);

    // 생성완료 화면 이동
    onNextBtnClick('done', doneData);
  }

  return (
    <div className="flex flex-col w-full h-full font-pre-B px-4">

      <div className="w-full mb-12">
        <p className="text-3xl">비밀번호 설정</p>
      </div>

      <div className="w-full mb-10">
        <p className="text-lg mb-3">그룹 비밀번호</p>
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
        <p className="text-lg mb-3">그룹 비밀번호 확인</p>
        <div className="w-full h-14 flex justify-between">
          {Array(6)
            .fill(null)
            .map((_, index) => (
              <input
                key={index + 6}
                type="password"
                maxLength={1}
                className="h-14 w-12 rounded-lg bg-gray text-center outline-none"
                ref={(el) => (inputsRefs.current[index + 6] = el)}
                onChange={(event) => handleInputChange(index + 6, event)}
              />
            ))}
        </div>
      </div>


      <div className="w-full text-xl text-white">
        <button className="w-full h-[60px] bg-main-blue rounded-2xl" onClick={handleNextBtnClick}>
          <p>완료</p>
        </button>
        <button className="w-full h-[60px] bg-gray mt-2 rounded-2xl" onClick={handleBackBtnClick}>
          <p>이전</p>
        </button>
      </div>
    </div>
  );
};

export default InsertPassword;
