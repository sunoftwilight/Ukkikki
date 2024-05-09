import React, {useEffect, useRef} from "react";
import { InsertPasswordProps } from "../../types/Group";


const InsertPassword:React.FC<InsertPasswordProps> = ({onBackBtnClick, onNextBtnClick, createData, doneData}) => {

  const handleBackBtnClick = () => {
    onBackBtnClick('info', createData)
  }
  const handleNextBtnClick = () => {
    onNextBtnClick('info', doneData)
  }

  const inputsRefs = useRef<(HTMLInputElement | null)[]>(Array(6).fill(null));

  useEffect(() => {
    if (inputsRefs.current[0]) {
      inputsRefs.current[0]?.focus();
    }
  }, []);

  const handleInputChange = (index: number, event: React.ChangeEvent<HTMLInputElement>) => {
    const value = event.target.value;
    if (value.length === 1 && index < 5 && inputsRefs.current[index + 1]) {
      inputsRefs.current[index + 1]?.focus();
    } else if (value.length === 0 && index > 0 && inputsRefs.current[index - 1]) {
      inputsRefs.current[index - 1]?.focus();
    }
  };

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
                className="h-14 w-12 rounded-lg bg-gray text-center"
                ref={(el) => (inputsRefs.current[index] = el)}
                onChange={(event) => handleInputChange(index, event)}
              />
            ))}
        </div>
      </div>

      <div className="w-full mb-10">
        <p className="text-lg mb-3">그룹 비밀번호 확인</p>
        <div className="w-full h-14 flex justify-between">
          <input type="password" maxLength={1} className="h-14 w-12 rounded-lg bg-gray text-center"/>
          <input type="password" maxLength={1} className="h-14 w-12 rounded-lg bg-gray text-center"/>
          <input type="password" maxLength={1} className="h-14 w-12 rounded-lg bg-gray text-center"/>
          <input type="password" maxLength={1} className="h-14 w-12 rounded-lg bg-gray text-center"/>
          <input type="password" maxLength={1} className="h-14 w-12 rounded-lg bg-gray text-center"/>
          <input type="password" maxLength={1} className="h-14 w-12 rounded-lg bg-gray text-center"/>
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
  )
}

export default InsertPassword;

