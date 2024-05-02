import React, { useState } from "react";
import InsertInfo from "../components/GroupCreate/InsertInfo";
import InsertPassword from "../components/GroupCreate/InsertPassword";

const GroupCreate: React.FC = () => {
  const [insertData, setInsertData] = useState<String>('info')
  const insertDataChange = () => {
    setInsertData('pass')
  }
  return (
    <div className="w-full h-full p-4">
      {insertData === 'info' && (
        <InsertInfo onNextBtnClick={insertDataChange}/>
      )}
      {insertData === 'pass' && (
        <InsertPassword />
      )}
    </div>
  )
};

export default GroupCreate;