import React, { useState } from "react";
import InsertInfo from "../components/GroupCreate/InsertInfo";
import InsertPassword from "../components/GroupCreate/InsertPassword";

const GroupCreate: React.FC = () => {
  const [insertData, setInsertData] = useState<String>('pass')
  const insertDataChange = (type:string) => {
    setInsertData(type)
  }
  return (
    <div className="w-full h-full p-4">
      {insertData === 'info' && (
        <InsertInfo onNextBtnClick={() => insertDataChange('pass')}/>
      )}
      {insertData === 'pass' && (
        <InsertPassword onBackBtnClick={() => insertDataChange('info')}/>
      )}
    </div>
  )
};

export default GroupCreate;