import React, { useState } from "react";
import ChangePass from "../../components/GroupConfig/ChangePass";

const GroupPasswordConfig: React.FC = () => {
  const [isCurrentPass] = useState<boolean>(false)

  return (
		<div className="w-full h-full p-4">
      {!isCurrentPass && (
        <ChangePass />
      )}

    </div>
  )
};

export default GroupPasswordConfig;