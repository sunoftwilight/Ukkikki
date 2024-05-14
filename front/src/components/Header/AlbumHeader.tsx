import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { motion, AnimatePresence } from "framer-motion";
import back from "@/assets/Header/back.png";
import timeOut from "@/assets/Header/timeOut.png";
import albumOption from "@/assets/Header/albumOption.png";
import selectOption from "@/assets/Header/selectOptn.png";
import AlbumEditOptions from "./AlbumEditOptions";
import AlbumSelectOptions from "./AlbumSelectOptions";
import { selectModeStore, selectStore } from "../../stores/AlbumStore";
import { albumDoneStore, albumEditStore } from "../../stores/HeaderStateStore";
import TrashOptions from "./TrashOptions";
import { useStore } from "zustand";
import { currentGroupStore } from "../../stores/GroupStore";

const AlbumHeader: React.FC = () => {
	const btnStyle = "w-14 h-8 rounded-[10px] font-pre-SB text-white flex justify-center items-center";

	const location = useLocation();
	const [isTrash, setIsTrash] = useState(false);

	useEffect(() => {
		if (location.pathname.startsWith("/trash")) {
			setIsTrash(true);
		}
	}, []);

	const { selectMode, setSelectMode } = selectModeStore();
	const { isEdit, setIsEdit } = albumEditStore();
	const { isDone, setIsDone } = albumDoneStore();
	const { setSelectList } = useStore(selectStore)
  const { currentGroup } = useStore(currentGroupStore);

	const navigate = useNavigate();
	const goBackHandler = () => {
    navigate(`/group/${currentGroup}`);
	};

	const cancelHandler = () => {
		setSelectList(-1, false)
		setSelectMode()
	}

	const isGuest = false;

	return (
		<div className="flex justify-between items-center px-4 w-full h-14 bg-white">
			{!selectMode ? (
				<>
					<img src={back} onClick={() => goBackHandler()} />

					<div className="flex gap-6 items-center">
						{isGuest && (
							<div className="flex items-center gap-1">
								<img src={timeOut} />
								<div className="font-pre-R text-black">13 : 28</div>
							</div>
						)}

						{isTrash ? (
							<></>
						) : (
							<>
								<img
									src={albumOption}
									className="w-6 h-6"
									onClick={() => setIsEdit()}
								/>
								<AnimatePresence>
									{isEdit && (
										<motion.div
											className="fixed z-10"
											initial={{ opacity: 0 }}
											animate={{ opacity: 1 }}
											exit={{ opacity: 0 }}
										>
											<AlbumEditOptions />
										</motion.div>
									)}
								</AnimatePresence>
							</>
						)}

						<button
							onClick={() => setSelectMode()}
							className="rounded-[10px] font-pre-SB text-white text-lg justify-center flex items-center bg-main-blue w-14 h-8"
						>
							선택
						</button>
					</div>
				</>
			) : (
				<>
					<button
						onClick={() => cancelHandler()}
						className={`${btnStyle} text-lg bg-disabled-gray`}
					>
						취소
					</button>
					<div className="flex items-center gap-4">
						<img
							src={selectOption}
							className="w-6 h-6"
							onClick={() => setIsDone()}
						/>
						<AnimatePresence>
							{isDone && (
								<motion.div
									className="fixed z-10"
									initial={{ opacity: 0 }}
									animate={{ opacity: 1 }}
									exit={{ opacity: 0 }}
								>
                  { isTrash ?
                    <TrashOptions />
                    :
                    <AlbumSelectOptions />
                  }
								</motion.div>
							)}
						</AnimatePresence>
						<button className={`${btnStyle} text-sm bg-main-blue`}>
							전체선택
						</button>
					</div>
				</>
			)}
		</div>
	);
};

export default AlbumHeader;
