import React, { useEffect, useState } from "react";
import { AlarmItemProps } from "../../types/AlarmType";
import { getPartyDetail } from "../../api/group";
import { downloadFile } from "../../api/file";

const AlarmItem: React.FC<AlarmItemProps> = ({ alarmItem }) => {
  const [partyName, setPartyName] = useState('')
  const [partyThumb, setPartyThumb] = useState('')

  useEffect(() => {
    getPartyDetail(
      alarmItem.partyId,
      (res) => {
        // console.log(res.data)
        setPartyName(res.data.data.partyName)

        // downloadFile(
        //   'jA4k9zv1/nk9xF5fkLWpaCwMs0X1JN3vd6soWbk1LVI=',
        //   {
        //     fileId : res.data.data.thumbnail,
        //     prefix: ''
        //   },
        //   (res) => { 
        //     const url = window.URL.createObjectURL(new Blob([res.data]))
        //     setPartyThumb(url)
        //   },
        //   (err) => { console.error(err) },
        // )
      },
      (err) => {
        console.error(err)
      }
    )
  }, [])


	return (
    <div className={`flex gap-2 w-full ${alarmItem.read ? 'bg-gray' : 'bg-white'}`}>
      <img src={partyThumb} className="rounded-full w-12 h-12" />
      <div className="flex w-[calc(100%-56px)] flex-col gap-2">
        <div className="font-gtr-B text-xs">
          {partyName}
        </div>
        <div>
          <div className="text-xs font-pre-R text-black">
            <span className="font-pre-SB">{alarmItem.member}</span> 님께서 댓글을 작성했습니다.
          </div>
          <div className="font-pre-R text-xs text-black">
            {alarmItem.content}
          </div>
        </div>
      </div>
    </div>
  )
};

export default AlarmItem;
