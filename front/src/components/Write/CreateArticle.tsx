import React, { useEffect, useRef, useState } from "react";
import { createArticle } from "../../api/article";
import { ArticleCreateProps } from "../../types/ArticleType";
import { useStore } from "zustand";
import { userStore } from "../../stores/UserStore";
import { useNavigate, useParams } from "react-router-dom";
import back from "@/assets/Header/back.png"
import { motion, AnimatePresence } from "framer-motion"
import ImgModal from "./ImgModal";

const CreateArticle: React.FC = () => {

  const titleRef = useRef<HTMLInputElement>(null);
  const contentRef = useRef<HTMLTextAreaElement>(null);
  const [isOpen, setIsOpen] = useState(false);
  const [addMediaList, setAddMediaList] = useState<{src:string}[]>([]);
  const [addDeviceList, setAddDeviceList] = useState<File[]>([]);
  const [addAlbumList, setAddAlbumList] = useState<{photoId: string, src: string}[]>([])
  const [isModalOpen, setIsModalOpen] = useState<boolean>(false);
  const user = useStore(userStore);
  const {groupPk} = useParams();
  const navigate = useNavigate();
  const optionStyle = "w-full h-11 bg-soft-gray px-4 font-pre-R text-black text-lg flex items-center active:bg-gray"


  const handleImageChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const files = e.target.files;
    if (!files) return;
  
    const newMediaList = Array.from(files);

    setAddDeviceList(prevMediaList => [...prevMediaList, ...newMediaList]);
	};

  // 버튼 이벤트
  const goBackHandler = () => {
    navigate(-1)
  }

  const clickCreateBtn = async () => {
    if(titleRef.current === null || contentRef.current === null) return;

    const data:ArticleCreateProps = {
      title: titleRef.current.value,
      content: contentRef.current.value,
      password: user.groupKey[Number(groupPk)],
      photoIdList: addAlbumList.map((item) => String(item.photoId))
    }
    console.log(data)

    const formData = new FormData();

		formData.append("articleCreateDto", JSON.stringify(data));

    addDeviceList.forEach((file) => { // 변경 3: URL 대신 실제 파일을 FormData에 추가
      formData.append(`multipartFiles`, file, file.name);
      console.log(file)
    });


    await createArticle(Number(groupPk), formData,
    (res) => {
      console.log(res)
      navigate(`/feed/${groupPk}`);
    },
    (err) => {
      console.error(err)
    })
  }

  useEffect(() => {
    const device = addDeviceList.map((item) => ({src: URL.createObjectURL(item)}))
    const album = addAlbumList.map(item => ({src: item.src}))
    const combined = [...device, ...album]
    setAddMediaList((prevAddMediaList) => {

      const updatedList = prevAddMediaList.filter(item => combined.includes(item));
      combined.forEach(item => {
        if (!updatedList.includes(item)) {
          updatedList.push(item);
        }
      });
      console.log(updatedList)
      return updatedList;
    });
    console.log(addMediaList)
  }, [addDeviceList, addAlbumList])


  return (
    <div className="flex flex-col w-screen h-screen relative">
      {isModalOpen && (
        <ImgModal onChoiceDoneBtn={(data) => {setAddAlbumList(data)}} onCancelBtn={() => {setIsModalOpen(false)}}/>
      )}
      <div className="flex justify-between items-center p-4 w-full h-14 bg-white">
        <img src={back} onClick={() => goBackHandler()} />
        <button onClick={() => clickCreateBtn()} className="rounded-[10px] font-pre-SB text-white text-lg justify-center flex items-center bg-main-blue w-14 h-8">작성</button>
      </div>

      <div className="flex flex-col w-full h-[calc(100%-116px)] items-center p-4 gap-3">
        <input
          ref={titleRef}
          type="text" 
          placeholder="제목을 입력하세요"
          className="w-full font-pre-SB text-black text-2xl outline-none h-12" 
        />
        <div className="flex flex-col gap-3 h-full w-full">
          <textarea
            ref={contentRef}
            placeholder="내용을 입력하고 추억을 공유해보세요"
            className="w-full h-full font-pre-R text-black text-lg outline-none"
          />
          <div className="pl-4 flex overflow-x-scroll scrollbar-hide gap-2 h-48">
            { addMediaList.map((item, idx) => (
              <img key={idx} src={item.src}
                className="w-36 h-36 object-cover"
              />
            ))}
          </div>
        </div>
      </div>

      <div className="w-full fixed bottom-0">
        <AnimatePresence>
          {isOpen && (
            <motion.div
              className="w-full fixed bottom-[60px]"
              initial={{ opacity: 0, translateY: "44px" }}
              animate={{ opacity: 1, translateY: "0px" }}
              exit={{ opacity: 0, translateY: "44px" }}
            >
              <div className="flex flex-col items-center">
                <div className={`${optionStyle} pt-4 pb-3 rounded-t-[15px]`} onClick={() => {setIsModalOpen(true)}}>
                  <p>공유 앨범에서 첨부하기</p>
                </div>
                <div className={`${optionStyle} pt-3 pb-4`}>
                  <label htmlFor="fromDevice">디바이스에서 첨부하기</label>
                  <input id="fromDevice" accept="image/*" type="file" className="hidden" onChange={handleImageChange}/>
                </div>
              </div>            
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

      
    </div>
  )
};

export default CreateArticle;