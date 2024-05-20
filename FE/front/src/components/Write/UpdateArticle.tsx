import React, { useEffect, useRef, useState } from "react";

import { updateArticle, getArticleImg, getArticleDetail } from "../../api/article";
import { userStore } from "../../stores/UserStore";
import { useNavigate, useParams } from "react-router-dom";
import back from "@/assets/Header/back.png"


const UpdateArticle: React.FC = () => {

  const titleRef = useRef<HTMLInputElement>(null);
  const contentRef = useRef<HTMLTextAreaElement>(null);
  const [addMediaList, setAddMediaList] = useState<{src:string}[]>([]);
  const [addDeviceList] = useState<File[]>([]);
  const [addAlbumList, setAddAlbumList] = useState<{photoId: string, src: string}[]>([])
  const { groupKey } = userStore();
  const navigate = useNavigate();
  const { groupPk, feedPk } = useParams();


  useEffect(() => {
    getArticle();
  }, [])

  const getArticle = async() => {
    const gp = Number(groupPk)
    const ap = Number(feedPk)
    await getArticleDetail(gp, ap,
      (res) => {
        const data = res.data.data
        if (data.photoList.length > 0){
          data.photoList.forEach(item => {
            getImg(data.partyId, item.photoUrl)
          })
          setAddMediaList(data.photoList.map((item) => ({src: item.photoUrl})))
          setAddAlbumList(data.photoList.map((item) => ({src: item.photoUrl, photoId: String(item.photoId)})))
        }
        titleRef.current!.value = data.title
        contentRef.current!.value = data.content
      },  
      (err) => {
        console.error(err)
      }
    )
  }

  const getImg = async (pk: number, url: string) => {
		const key = groupKey[pk]
    const opt = {
      "x-amz-server-side-encryption-customer-key": key,
    };
    await getArticleImg(
      url,
      opt,
      () => {},
      (err) => { console.log(err); },
    );
	}

  // 버튼 이벤트
  const goBackHandler = () => {
    navigate(-1)
  }

  const clickUpdateBtn = async () => {
    if(titleRef.current === null || contentRef.current === null) return;

    const data = {
      title: titleRef.current.value,
      content: contentRef.current.value,
      password: groupKey[Number(groupPk)],
      articlePhotoList: [],
      addPhoto: true
    }

    const formData = new FormData();

		formData.append("articleUpdateDto", JSON.stringify(data));

    addDeviceList.forEach((file) => { // 변경 3: URL 대신 실제 파일을 FormData에 추가
      formData.append(`multipartFiles`, file, file.name);
    });

    await updateArticle(Number(groupPk), Number(feedPk), formData,
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
      return updatedList;
    });
  }, [addDeviceList, addAlbumList])


  return (
    <div className="flex flex-col w-screen h-screen relative">
      <div className="flex justify-between items-center p-4 w-full h-14 bg-white">
        <img src={back} onClick={() => goBackHandler()} />
        <button onClick={() => clickUpdateBtn()} className="rounded-[10px] font-pre-SB text-white text-lg justify-center flex items-center bg-main-blue w-14 h-8">수정</button>
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
    </div>
  )
};

export default UpdateArticle;