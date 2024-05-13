import React, { useEffect, useRef, useState } from "react";
import { ModalProps } from "../../types/ModalType";
import warn from "@/assets/Modal/warn.png";
import done from "@/assets/Modal/done.png";
import ModalBackground from "./ModalBackground";
import { motion, AnimatePresence } from "framer-motion"
import { useStore } from "zustand";
import { prefixStore } from "../../stores/AlbumStore";
import LoadingGif from "./LoadingGif";

const Modal: React.FC<ModalProps> = ({ modalItems, onSubmitBtnClick, onCancelBtnClick }) => {
	const modalType = modalItems.modalType;

	const containClass: string = "flex justify-center items-center w-full";
	const contentClass: string = "flex font-pre-R text-lg w-full items-center text-black";

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
						<div className={`${contentClass} h-[70px] flex-col`}>
							{modalItems?.title}
              <div className="w-full font-pre-R text-sm text-black">{modalItems.content}</div>
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
				const prefixInput = useRef<HTMLInputElement>(null);
				const { prefix,setPrefix } = useStore(prefixStore)
				useEffect(() => {
					console.log(modalItems.content)
					setPrefix(modalItems.content)
				}, [])
				return (
					<div className={`${containClass} flex-col gap-2`}>
						<div className="font-pre-B text-black text-xl w-full">
							{modalItems.title}
						</div>
						<input
							ref={prefixInput}
							onChange={(e) => setPrefix(e.target.value)}
							autoComplete="false"
							maxLength={10}
							value={prefix}
							className={`${contentClass} bg-soft-gray px-3 py-2 rounded-xl h-[40px] outline-none`}
						/>
					</div>
				);

			case "ing":
				return (
					<div className={`${containClass} flex-col gap-4 pt-1`}>
						<LoadingGif />
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
						<button onClick={() => onSubmitBtnClick!()} className={`${btnClass} w-[130px] bg-main-blue`}>
							확인
						</button>
					</div>
				);

			case 2:
				return (
					<div className="flex gap-4 w-full justify-end">
						<button 
							className={`${btnClass} w-[70px] bg-disabled-gray`} 
							onClick={() => onCancelBtnClick!()}
						>
							취소
						</button>
						<button
							className={`${btnClass} w-[70px] ${modalType === "warn" ? "bg-red" : "bg-main-blue"}` }
							onClick={() => onSubmitBtnClick!()}
						>
							확인
						</button>
					</div>
				);
		}
	};

  const isOpen = useState<boolean>(false)
	return (
		<div className="fixed z-10 top-0 left-0 w-full h-full">
			<AnimatePresence>
				{isOpen && <ModalBackground/> }
				<motion.div 
					key='modalForm'
					className="flex justify-center items-center w-full h-full"
					initial={{ opacity: 0 }}
					animate={{ opacity: 1 }}
					exit={{ opacity: 0 }}
				>
					<div className="z-20 w-[300px] h-[174px] bg-white rounded-[15px] p-3 flex flex-wrap content-between">
						{contentHandler()}
						{btnHandler()}
					</div>
				</motion.div>
			</AnimatePresence>
		</div>
	);
};

export default Modal;
