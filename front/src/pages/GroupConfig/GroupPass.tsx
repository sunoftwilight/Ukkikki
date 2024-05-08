import React, { useState } from "react";
import ChangePassword from "../../components/GroupConfig/ChangePassword";
import CurrentPassword from "../../components/GroupConfig/CurrentPassword";

const GroupPasswordConfig: React.FC = () => {
  const [isCurrentPass, setIsCurrentPass] = useState<boolean>(false)

  return (
		<div className="w-full h-full p-4">
      {!isCurrentPass && (
        <CurrentPassword />
      )}
      {isCurrentPass  && (
        <ChangePassword />
      )}

    </div>
  )
};

export default GroupPasswordConfig;