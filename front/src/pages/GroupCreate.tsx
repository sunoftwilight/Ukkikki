import React, { useRef, useState } from "react";
import InsertInfo from "../components/GroupCreate/InsertInfo";
import InsertPassword from "../components/GroupCreate/InsertPassword";
import CreateDone from "../components/GroupCreate/CreateDone";
import { CreatePartyData, CreateDoneProps } from "../types/Group";

const GroupCreate: React.FC = () => {
  const emptyFile: File = new File([], '');

  const [isState, setIsState] = useState<String>('info')
  const doneData = useRef<CreateDoneProps>({partyPk: null, partyName: '', inviteCode: ''})
  const createData = useRef<CreatePartyData>({partyName: '', partyPass: '', simplePass: '', partyProfile: emptyFile})

  const insertDataChange = (type:string, data:CreatePartyData) => {
    setIsState(type);
    createData.current = data;
  }

  const doneDataChange = (type:string, data:CreateDoneProps) => {
    setIsState(type)
    doneData.current = data;
  }

  return (
    <div className="w-full h-full p-4">
      {isState === 'info' && (
        <InsertInfo
          onNextBtnClick={(type, data) => {insertDataChange(type, data)}}
          createData={createData.current}
        />
      )}
      {isState === 'pass' && (
        <InsertPassword
          onBackBtnClick={(type, data) => {insertDataChange(type, data)}}
          onNextBtnClick={(type, data) => {doneDataChange(type, data)}}
          createData={createData.current}
          doneData={doneData.current}
          />
      )}
      {isState === 'done' && doneData.current.partyPk !== null && (
        <CreateDone {...doneData.current}/>
      )}

    </div>
  )
};

export default GroupCreate;