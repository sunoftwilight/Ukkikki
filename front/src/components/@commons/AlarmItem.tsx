import React, { useEffect } from "react";
import { AlarmItemProps } from "../../types/AlarmType";
import { useStore } from "zustand";
import { userStore } from "../../stores/UserStore";
import { getPartyThumb } from "../../api/party";

const AlarmItem: React.FC<AlarmItemProps> = ({ alarmItem }) => {
	const { groupKey } = useStore(userStore);

  useEffect(() => {
    const opt = {
			"x-amz-server-side-encryption-customer-key": groupKey[Number(alarmItem.partyId)],
		};
		getPartyThumb(
			alarmItem.partyUrl,
			opt,
			() => {},
			(err) => { console.log(err); },
		);
  }, [])

	return (
    <div className={`flex gap-2 w-full ${alarmItem.read ? 'bg-gray' : 'bg-white'}`}>
      <img src={alarmItem.partyUrl} className="rounded-full w-12 h-12 border-[0.1px] border-point-gray object-cover" />
      <div className="flex w-[calc(100%-56px)] flex-col gap-2">
        <div className="font-gtr-B text-xs">
          {alarmItem.partyName}
        </div>
        <div>
          <div className="text-xs font-pre-R text-black">
            {/* <span className="font-pre-SB">{alarmItem.member}</span> 님께서 댓글을 작성했습니다. */}
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
