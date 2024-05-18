import React from "react";
import back from "@/assets/Header/back.png";
import timeOut from "@/assets/Header/timeOut.png";
import alarm from "@/assets/Header/alarm.png";
import noAlarm from "@/assets/Header/noAlarm.png";
import hamburger from "@/assets/Header/hamburger.png";
import { headerStore } from "../../stores/HeaderStateStore";
import { useLocation, useNavigate } from "react-router-dom";
import { useStore } from "zustand";
import { currentGroupStore } from "../../stores/GroupStore";
import { AlarmOccuredStore } from "../../stores/AlarmStore";

const BackHeader: React.FC = () => {
	const navigate = useNavigate();
  const location = useLocation();
  const { currentGroup } = useStore(currentGroupStore);
  const { isAlarmOccured } = useStore(AlarmOccuredStore)
	

	const goBackHandler = () => {
    if (location.pathname.startsWith('/album/detail/')) {
      navigate(`/album/${currentGroup}`)
      return
    }
		if (location.pathname.startsWith('/group') && location.pathname.includes('/main')) {
      navigate(`/group/list`)
      return
    }
		if (location.pathname.startsWith('/feed') && !location.pathname.includes('/detail')) {
      navigate(`/group/${currentGroup}/main`)
      return
    }
		navigate(-1);
	};

	const isGuest = false;

	const { setAlarmOpen } = useStore(headerStore);
	const { setMenuOpen } = useStore(headerStore);

	return (
		<div className="flex justify-between items-center px-4 w-full h-14 bg-white">
			<img src={back} onClick={() => goBackHandler()} />

			<div className="flex gap-6">
				{isGuest ? (
					<div className="flex items-center gap-1">
						<img src={timeOut} />
						<div className="font-pre-R text-black">13 : 28</div>
					</div>
				) : isAlarmOccured ? (
					<img src={alarm} onClick={() => setAlarmOpen()} />
				) : (
					<img src={noAlarm} onClick={() => setAlarmOpen()} />
				)}

				<img src={hamburger} onClick={() => setMenuOpen()} />
			</div>
		</div>
	);
};

export default BackHeader;
