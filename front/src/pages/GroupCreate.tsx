import React, { useState } from "react";
import InsertInfo from "../components/GroupCreate/InsertInfo";
import InsertPassword from "../components/GroupCreate/InsertPassword";
import CreateDone from "../components/GroupCreate/CreateDone";

const GroupCreate: React.FC = () => {
  const [isState, setIsState] = useState<String>('info')
  const [doneData, setDoneData] = useState<[number | null, string, string]>([null, '', ''])
  const insertDataChange = (type:string) => {
    setIsState(type)
  }
  const doneDataChange = (id:number, name:string, code:string) => {
    setIsState('done')
    setDoneData([id, name, code])
  }
  return (
    <div className="w-full h-full p-4">
      {isState === 'info' && (
        <InsertInfo onNextBtnClick={() => insertDataChange('pass')}/>
      )}
      {isState === 'pass' && (
        <InsertPassword onBackBtnClick={() => insertDataChange('info')} onNextBtnClick={(id, name, code) => {doneDataChange(id, name, code)}}/>
      )}
      {isState === 'done' && doneData[0] !== null && (
        <CreateDone id={doneData[0]} name={doneData[1]} code={doneData[2]} />
      )}

    </div>
  )
};

export default GroupCreate;