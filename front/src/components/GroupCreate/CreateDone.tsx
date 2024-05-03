import React from "react";
import { inviteStore } from "../../stores/ModalStore";

interface CreateDoneProps {
    id: number,
    name: string,
    code: string
}

const CreateDone:React.FC<CreateDoneProps> = ({name}) => {
  const GroupName = name;
  //   const GroupId = id;
  //   const GroupCode = code;
  const btnStyle = "w-full h-[50px] rounded-[15px] flex justify-center items-center hover:scale-95 active:scale-95 active:bg-main-blue transition-transform duration-75"
  const { setInviteOpen } = inviteStore()
  return (
    <div className="flex flex-col w-full h-full px-4">
      <div className="font-pre-B text-2xl mt-28 mb-24">
        <p>'{GroupName}' 이</p>
        <p>생성되었습니다!</p>
      </div>
      <div className="font-pre-B text-white text-xl flex flex-col gap-4">
        <button className={btnStyle + ' bg-main-blue'}>
          <p>바로가기</p>
        </button>
        <button className={btnStyle + ' bg-soft-blue'} onClick={setInviteOpen}>
          <p>링크 공유하기</p>
        </button>
      </div>
    </div>
  )
}

export default CreateDone;

