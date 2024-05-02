import React from "react";
import TextInput from "../components/Write/TextInput";
import AddMediaBtn from "../components/Write/AddMediaBtn";

const Write: React.FC = () => {
  return (
    <div className="w-full h-full">
      <TextInput />
      <AddMediaBtn />
    </div>
  )
};

export default Write;