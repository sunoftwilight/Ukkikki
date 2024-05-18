import React, { useEffect } from "react";
import { AlarmItemProps } from "../../types/AlarmType";
import { useStore } from "zustand";
import { userStore } from "../../stores/UserStore";
import { getPartyThumb } from "../../api/party";
import { redirectAlarm } from "../../api/alarm";
import { useNavigate } from "react-router-dom";
import { headerStore } from "../../stores/HeaderStateStore";

const AlarmItem: React.FC<AlarmItemProps> = ({ alarmItem }) => {
	const { groupKey } = useStore(userStore);
  const { setAlarmOpen } = useStore(headerStore)


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
  }, [alarmItem])

  const alarmTypeHandler = () => {
    switch (alarmItem.alarmType) {
      case "CHECK":
        return "체크체크"

      case "PASSWORD":
        return "파티 비밀번호를 변경했습니다"

      case "COMMENT":
        return "댓글을 작성했습니다"

      case "REPLY":
        return "대댓글을 작성했습니다"

      case "CHAT":
        return "채팅방에 회원님을 언급했습니다"

      case "MEMO":
        return "사진 메모에서 회원님을 언급했습니다"

      case "MENTION":
        return "댓글에서 회원님을 언급했습니다"
    }
  }

  const navigate = useNavigate()

  const clickHandler = () => {
    redirectAlarm(
      {
        alarmId: alarmItem.alarmId,
      },
      () => {},
      (err) => { console.error(err) }
    )
    navigate(alarmItem.identifier)
    setAlarmOpen()
  }

  const timeTransHandler = (registDateTime: string | null) => {
    if (!registDateTime) return "";
    
    const commentDateTime = new Date(registDateTime);  // 댓글 작성 시간
    const currentTime = new Date();                    // 현재 시간
    const difference = currentTime.getTime() - commentDateTime.getTime();    // 두 시간의 차
    const seconds = Math.floor(difference / 1000);
    const minutes = Math.floor(seconds / 60);
    const hours = Math.floor(minutes / 60);
    const days = Math.floor(hours / 24);
    const weeks = Math.floor(days / 7);

    if (weeks >= 1) {
      // .replace()는 첫번째 문자열만 치환 => "/-/g" //로 감싸진 모든 문자열을 치환해줌 (g = global match)
      const when = registDateTime.split('T')[0].replace(/-/g, '.')
      return when;
    } else if (1 == weeks && weeks > 0) {
      return `${weeks}주 전`;
    } else if (days > 0) {
      return `${days}일 전`;
    } else if (hours > 0) {
      return `${hours}시간 전`;
    } else if (minutes > 0) {
      return `${minutes}분 전`;
    } else {
      return `${seconds}초 전`;
    }
  }

	return (
    <div onClick={() => clickHandler()} className={`flex gap-2 w-full ${alarmItem.read ? 'bg-gray' : 'bg-white'}`}>
      <img src={alarmItem.partyUrl} className="rounded-full w-12 h-12 border-[0.1px] border-point-gray object-cover" />
      <div className="flex w-[calc(100%-56px)] flex-col gap-2">
        <div className="flex justify-between w-full">
          <div className="font-gtr-B text-xs">{alarmItem.partyName}</div>
          <div className="font-gtr-L text-point-gray text-xs">{timeTransHandler(alarmItem.createDate)}</div>
        </div>

        <div>
          <div className="text-xs font-pre-R text-black">
            <span className="font-pre-SB">{alarmItem.writerNick}</span>님께서 {alarmTypeHandler()}
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
