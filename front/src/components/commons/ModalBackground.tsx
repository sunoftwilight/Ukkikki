import React from "react";
import { motion, AnimatePresence } from "framer-motion"

const ModalBackground: React.FC = () => {

  return (
    <AnimatePresence>
      <motion.div
        className="z-10 fixed w-screen h-screen bg-white/5 shadow-inner backdrop-blur-sm"
        initial={{ opacity: 0 }}
        animate={{ opacity: 1 }}
        exit={{ opacity: 0 }}
      />
        {/* <div className="z-10 fixed w-screen h-screen bg-white/5 shadow-inner backdrop-blur-sm" />
      </motion.div> */}
    </AnimatePresence>

  )
};

export default ModalBackground;