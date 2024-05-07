import React, { useState } from "react";
import { ModalProps } from "../../types/Modal";
import warn from "@/assets/Modal/warn.png";
import done from "@/assets/Modal/done.png";
import ModalBackground from "./ModalBackground";
import { motion, AnimatePresence } from "framer-motion"

const OneBtnModal: React.FC<ModalProps> = ({ modalItems, onSubmitBtnClick, onCancelBtnClick }) => {
	const modalType = modalItems.modalType;

	const containClass: string = "flex justify-center items-center w-full";
	const contentClass: string = "flex font-pre-R text-base w-full items-center text-black";

	const contentHandler = () => {
		switch (modalType) {
			case "txtOnly":
				return (
					<div className={containClass}>
						<div className={`${contentClass} justify-center h-[70px]`}>
							{modalItems.content}
						</div>
					</div>
				);

			case "warn":
				return (
					<div className={`${containClass} gap-5`}>
						<img src={warn} />
						<div className={`${contentClass} h-[70px]`}>
							{modalItems.content}
						</div>
					</div>
				);

			case "done":
				return (
					<div className={`${containClass} flex-col gap-2`}>
						<img src={done} />
						<div className={`${contentClass} justify-center`}>
							{modalItems.content}
						</div>
					</div>
				);

			case "input":
				return (
					<div className={`${containClass} flex-col gap-2`}>
						<div className="font-pre-B text-black text-xl w-full">
							{modalItems.title}
						</div>
						<input
							autoComplete="false"
							maxLength={10}
							className={`${contentClass} bg-soft-gray px-3 py-2 rounded-xl h-[40px] outline-none`}
						/>
					</div>
				);

			case "ing":
				return (
					<div className={`${containClass} flex-col gap-4 pt-1`}>
						{/* loading GIF */}
						<svg
							xmlns="http://www.w3.org/2000/svg"
							width={66}
							height={66}
							viewBox="0 0 24 24"
						>
							<g
								fill="none"
								stroke="#007bff"
								strokeLinecap="round"
								strokeWidth={2}
							>
								<path
									strokeDasharray={60}
									strokeDashoffset={60}
									strokeOpacity={0.3}
									d="M12 3C16.9706 3 21 7.02944 21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3Z"
								>
									<animate
										fill="freeze"
										attributeName="stroke-dashoffset"
										dur="1.3s"
										values="60;0"
									></animate>
								</path>
								<path
									strokeDasharray={15}
									strokeDashoffset={15}
									d="M12 3C16.9706 3 21 7.02944 21 12"
								>
									<animate
										fill="freeze"
										attributeName="stroke-dashoffset"
										dur="0.3s"
										values="15;0"
									></animate>
									<animateTransform
										attributeName="transform"
										dur="1.5s"
										repeatCount="indefinite"
										type="rotate"
										values="0 12 12;360 12 12"
									></animateTransform>
								</path>
							</g>
						</svg>

						<div className={`${contentClass} justify-center`}>
							{modalItems.content}
						</div>
					</div>
				);
		}
	};

	const btnClass: string =
		"h-[35px] font-gtr-B text-lg text-white rounded-[15px]";
	const btnHandler = () => {
		switch (modalItems.btn) {
			case 0:
				return;

			case 1:
				return (
					<div className="flex w-full justify-center">
						<button className={`${btnClass} w-[130px] bg-main-blue`}>
							확인
						</button>
					</div>
				);

			case 2:
				return (
					<div className="flex gap-4 w-full justify-end">
						<button
							className={`${btnClass} w-[70px] bg-disabled-gray`}
							onClick={() => onCancelBtnClick()}>
							취소
						</button>
						<button
							className={`${btnClass} w-[70px] ${modalType === "warn" ? "bg-red" : "bg-main-blue"}`}
							onClick={() => onSubmitBtnClick()}
						>
							확인
						</button>
					</div>
				);
		}
	};

  const isOpen = useState<boolean>(false)
	return (
		<div className="fixed top-0 left-0 w-full h-full">
			<AnimatePresence>
			{isOpen && <ModalBackground/> }
				<motion.div 
					key={'modal'}
					className="flex justify-center items-center w-full h-full"
					initial={{ opacity: 0 }}
					animate={{ opacity: 1 }}
					exit={{ opacity: 0 }}
				>
					<div className="z-20 w-[300px] h-[174px] bg-white rounded-[15px] p-6 flex flex-wrap content-between">
					{contentHandler()}
					{btnHandler()}
					</div>
				</motion.div>
			</AnimatePresence>
		</div>

	);
};

export default OneBtnModal;
