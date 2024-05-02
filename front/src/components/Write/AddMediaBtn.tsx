import React, { useState } from "react";
import { motion, AnimatePresence } from "framer-motion"
import MediaBtnDropUp from "./MediaBtnDropUp";

const AddMediaBtn: React.FC = () => {
  const [isOpen, setIsOpen] = useState(false)

  return (
    <div className="w-full fixed bottom-0">
      <AnimatePresence>
        {isOpen && (
          <motion.div
            className="w-full fixed bottom-[60px]"
            initial={{ opacity: 0, translateY: "44px" }}
            animate={{ opacity: 1, translateY: "0px" }}
            exit={{ opacity: 0, translateY: "44px" }}
          >
            <MediaBtnDropUp />
          </motion.div>
        )}
      </AnimatePresence>
      <div 
        onClick={() => setIsOpen(!isOpen)}
        className="w-full h-[60px] font-pre-B text-white text-2xl bg-main-blue flex justify-center items-center z-10 fixed bottom-0"
      >
        미디어 첨부
      </div>
    </div>
  )
};

export default AddMediaBtn;