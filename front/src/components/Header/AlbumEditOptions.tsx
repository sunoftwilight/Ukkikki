import React, { useEffect, useRef, useState } from "react";
import upload from "@/assets/Header/AlbumEditOptions/upload.png";
import addFolder from "@/assets/Header/AlbumEditOptions/addFolder.png";
import edit from "@/assets/Header/AlbumEditOptions/edit.png";
import trash from "@/assets/Header/AlbumEditOptions/trash.png";
import moveFolder from "@/assets/Header/AlbumEditOptions/moveFolder.png";
import { albumEditStore } from "../../stores/HeaderStateStore";
import { AnimatePresence } from "framer-motion";
import Modal from "../@commons/Modal";
import { useStore } from "zustand";
import { currentDirStore, prefixStore, updateAlbumStore } from "../../stores/AlbumStore";
import { createDirectory, delDirectory, editDirectory } from "../../api/directory";
import { userStore } from "../../stores/UserStore";
import { useParams } from "react-router-dom";
import { folderStore } from "../../stores/ModalStore";
import { getPartyDetail } from "../../api/party";
import { uploadFile } from "../../api/file";

const AlbumEditOptions: React.FC = () => {
  const optionStyle = "flex rounded-[10px] w-full h-[30px] items-center px-3 gap-3 font-pre-R text-black text-sm bg-white/70"
  const { setIsEdit } = albumEditStore()
  const { prefix } = useStore(prefixStore)
  const { currentDirId, currentDirName, parentDirId, parentDirName, setCurrentDirId, setCurrentDirName } = useStore(currentDirStore)
  const { setNeedUpdate } = useStore(updateAlbumStore)
  const [isOkOpen, setIsOkOpen] = useState(false)
  const [isNamingOpen, setIsNamingOpen] = useState(false)
  const [isEditNameOpen, setIsEditNameOpen] = useState(false)
  const { groupKey } = useStore(userStore);
  const { groupPk } = useParams();
  const { setFolderOpen } = useStore(folderStore)
	const refInputRef = useRef(null)

  // 요청에 맞는 모달창 열기
  const openHandler = (mode: string) => {
    if (mode === 'delete') {
      setIsOkOpen(true)
    } else if (mode === 'add Folder') {
      setIsNamingOpen(true)
    } else if (mode === 'edit Name') {
      setIsEditNameOpen(true)
    } else if (mode === 'move dir') {
      setFolderOpen('move dir')
    }
  }

  // 현재 폴더 삭제 로직
  const deleteFolderHandler = async () => {
    await delDirectory(
      currentDirId,
      {data: {sseKey: groupKey[Number(groupPk)]}},
      () => {
        setCurrentDirId(parentDirId)
        setCurrentDirName(parentDirName)
        doneHandler()
      },
      (err) => {
        console.error(err)
        alert('오류가 발생했습니다. 다시 시도해주세요.')
      }
    )
  }

  // 폴더 생성 로직
  const createFolderHandler = async () => {
    await createDirectory(
      { parentDirId: currentDirId, dirName: prefix },
      () => {
        setCurrentDirId(currentDirId)
        setCurrentDirName(currentDirName)
        doneHandler()
      },
      (err) => {
        console.error(err)
        alert('오류가 발생했습니다. 다시 시도해주세요.')
      }      
    )
  }

  const editFolderNameHandler = async () => {
    await editDirectory(
      currentDirId,
      prefix,
      () => {
        setCurrentDirId(parentDirId)
        setCurrentDirName(parentDirName)
        doneHandler()
      },
      (err) => {
        console.error(err)
        alert('오류가 발생했습니다. 다시 시도해주세요.')
      }
    )
  }
  
  const [rootDir, setRootDir] = useState<string>('')

  useEffect(() => {
    getPartyDetailHandler()
  }, [groupPk])

  const getPartyDetailHandler = async () => {
    await getPartyDetail(
      Number(groupPk),
      (res) => {
        setRootDir(res.data.data.rootDirId)
      },
      (err) => { console.error(err) }
    )
  }

  const key = new Blob(
    [JSON.stringify({
      'key': groupKey[Number(groupPk)],
      'partyId': groupPk,
      'rootDirId': rootDir,
      'targetDirId': currentDirId
    })],
    {type: 'application/json'}
  ) 
  
  const uploadHandler = (e: any) => {
    const formData = new FormData();
    const filesArray = Array.from(e.target.files); // FileList 객체를 배열로 변환

    // 원하는 파일 형식만 필터링
    const filteredFiles = filesArray.filter((file: any) => 
      file.type === "image/jpg" || file.type === "image/jpeg" || file.type === "image/png"
    );

    // 필터링 후에 남은 파일이 없으면 형식 경고
    if (filteredFiles.length === 0) {
      alert('jpg, jpeg, png 파일만 업로드할 수 있습니다.')
      return
    }

    // 필터링된 파일들을 FormData에 추가
    filteredFiles.forEach((file: any) => {
      formData.append('files', file);
    });

    formData.append('key', key)

    uploadFile(
      formData,
      () => { doneHandler() },
      (err) => { 
        console.error(err) 
        alert('오류가 발생했습니다. 다시 시도해주세요.')
      }
    )
  }

  // 모달 요청 완료 후 로직
  const doneHandler = () => {
    setNeedUpdate()
    setIsOkOpen(false)
    setIsNamingOpen(false)
    setIsEditNameOpen(false)
    setIsEdit()
  }


  return (
    <AnimatePresence>
      { isOkOpen && 
        <Modal 
        key='isOkOpen'
        modalItems={{ title: '현재 폴더와 사진을 모두 삭제합니다', content: '삭제된 사진은 휴지통에 2주간 보관되며 기간 내 복구 가능합니다', modalType: 'warn', btn: 2 }}
        onSubmitBtnClick={() => deleteFolderHandler()}
        onCancelBtnClick={() => setIsOkOpen(false)}
        />
      }
      { isNamingOpen && 
        <Modal 
        key='isNamingOpen'
        modalItems={{ title: '폴더명 설정', content: '', modalType: 'input', btn: 2 }}
        onSubmitBtnClick={() => createFolderHandler()}
        onCancelBtnClick={() => setIsNamingOpen(false)}
        />
      }
      { isEditNameOpen && 
        <Modal 
        key='isEditNameOpen'
        modalItems={{ title: '폴더명 수정', content: currentDirName, modalType: 'input', btn: 2 }}
        onSubmitBtnClick={() => editFolderNameHandler()}
        onCancelBtnClick={() => setIsEditNameOpen(false)}
        />
      }
      <div className="flex flex-col px-2 py-[10px] gap-[5px] fixed top-14 right-4 w-40 h-[200px] bg-zinc bg-opacity-30 rounded-xl shadow-inner backdrop-blur-[50px]">
        <label className={`${optionStyle}`} htmlFor='files'>
          <img src={upload} className="w-4" />
          업로드
        </label>
        <input 
          multiple
          type="file" name='file' id='files' 
          style={{display: "none"}}
          ref={refInputRef}
          onChange={(e) => uploadHandler(e)}
        />

        <div className={`${optionStyle}`} onClick={() => openHandler('add Folder')}>
          <img src={addFolder} className="w-4" />
          하위 폴더 생성
        </div>

        <div className={`${optionStyle}`} onClick={() => openHandler('edit Name')}>
          <img src={edit} className="w-4" />
          현재 폴더명 수정
        </div>

        <div className={`${optionStyle}`} onClick={() => openHandler('move dir')}>
          <img src={moveFolder} className="w-4" />
          현재 폴더 이동
        </div>

        <div className={`${optionStyle}`} onClick={() => openHandler('delete')}>
          <img src={trash} className="w-4" />
          현재 폴더 삭제
        </div>
      </div>
    </AnimatePresence>
  )
};

export default AlbumEditOptions;