import React, { useEffect, useRef} from "react";
// import heart from "@/assets/Album/heart.png"
// import downloaded from "@/assets/Album/downloaded.png"

import { getGroupDetail} from "../../api/party";
// import { getGroupDetail, getPartyThumb } from "../../api/party";
import { useParams } from "react-router-dom";
// import { Link, useParams } from "react-router-dom";
import { imgGroupStore, updateAlbumStore } from "../../stores/AlbumStore";
// import { imgGroupStore, selectModeStore, updateAlbumStore } from "../../stores/AlbumStore";
// import { userStore } from "../../stores/UserStore";
// import SecureImg from "../Album/SecureImg";
// import { DetailImgStore } from "../../stores/DetailImgStore";
import { useStore } from "zustand";
// import SelectModeImg from "../Album/SelectModeImg";

const GroupAlbum: React.FC = () => {
  // const { setCurrentImg } = useStore(DetailImgStore)
  // const { selectMode } = useStore(selectModeStore)
  const { needUpdate } = useStore(updateAlbumStore)
  const { groupPk } = useParams();
  const { type, groupName } = imgGroupStore();
  // const [ albumList, setAlbumList ] = useState<{photoId:number, photoUrl:string, thumbnailUrl:string, fileId:string}[]>([]);
  // const {groupKey} = userStore();
  const isInitialRender = useRef(true);

  useEffect(() => {
    getImgDetail(type, groupName);
  }, []);

  // useEffect(() => {
  //   imgList.forEach((item) => {
  //     getImg(item)
  //   })
  // }, [imgList])

  // useEffect(() => {
  //   getFirstDirectoryHandler()
  //   return () => {
  //     isInitialRender.current = true;
  //   };
  // }, [])


  const getImgDetail = async (type:number, groupName:string) => {
    const key = Number(groupPk)
    await getGroupDetail(type, groupName, key,
      (res) => {
        console.log(res.data.data)
        // setAlbumList(res.data.data)
      },
      (err) => {
        console.log(err)
      })
  }

  // const getImg = async (data:{photoId:number, photoUrl:string, thumbnailUrl:string}) => {
	// 	const opt = {
	// 		"x-amz-server-side-encryption-customer-key": groupKey[Number(groupPk)],
	// 	};
	// 	await getPartyThumb(
	// 		data.thumbnailUrl,
	// 		opt,
	// 		(res) => {
  //       const blob = new Blob([res.data], {type: 'image/png'})
  //       data.thumbnailUrl = (URL.createObjectURL(blob))
  //     },
	// 		(err) => { console.log(err); },
	// 	);
	// };

  useEffect(() => {
    if (isInitialRender.current) {
      isInitialRender.current = false;
    } else {

    }
  }, [needUpdate])



  return (
    <div className="grid grid-cols-3 sm:grid-cols-5 md:grid-cols-6 lg:grid-cols-8 xl:grid-cols-10 2xl:grid-cols-12 px-4 gap-1 overflow-scroll scrollbar-hide ">
    {/* {albumList!.map((item, idx) => (
      (
        selectMode ? 
          <SelectModeImg key={idx} item={item} />
        :
          <Link 
            to={`/album/detail/${item.pk}/${groupPk}`} state={{url: item.url}}
            key={idx} onClick={() => setCurrentImg(item.photoId, item.pk, item.url)}
            className="flex justify-center items-center"
          >
            <div className="relative">
              {item.isDownload ? 
                <img src={downloaded} className="absolute bottom-0 left-0 w-6" />
                : <></>
              }
              {item.isLikes ? 
                <div className="absolute bottom-1 right-1">
                  <img className="w-4" src={heart} />
                </div>
                : <></>
              }
              <SecureImg url={item.url} />
            </div>
          </Link>
        )))}   */}
    </div>
  )
};

export default GroupAlbum;