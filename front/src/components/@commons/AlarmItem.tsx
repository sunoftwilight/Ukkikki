import React, { useEffect, useState } from "react";
import { AlarmItemProps } from "../../types/AlarmType";

const AlarmItem: React.FC<AlarmItemProps> = ({ alarmItem }) => {

  useEffect(() => {

  }, [])

	return (
    <div className={`flex gap-3 ${alarmItem.read ? 'bg-gray' : 'bg-white'}`}>
      <img
        src={alarmItem.groupImg}
        className="rounded-full w-12 h-12"
      />
      <div className="flex flex-col gap-2">
        <div className="font-gtr-B text-xs">
          {alarmItem.groupName}
        </div>
        <div>
          <div className="text-xs font-pre-R text-black">
            <span className="font-pre-SB">{alarmItem.member}</span>
          </div>
          <div className="font-pre-R text-xs text-black">
            {alarmItem.content}
          </div>
        </div>
      </div>
    </div>
  )
};

export default AlarmItem;
