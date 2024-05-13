import React, { useEffect, useState } from "react";
import settings from "@/assets/GroupMain/settings.png"
import etc from "@/assets/GroupMain/etc.png"
import { memberStore } from "../../stores/ModalStore";
import { Link, useParams } from "react-router-dom";
import { getPartyDetail } from "../../api/party";
import { PartyDetailData } from "../../types/GroupType";


const GroupProfile: React.FC = () => { 
  const { setMemberOpen } = memberStore()
  const { groupPk } = useParams();
  const [groupInfo, setGroupInfo] = useState<PartyDetailData>({partyMembers: [], partyName: '', rootDirId: '', thumbnail: ''})

  useEffect(() => {
    getDetail()
  }, [])

  const getDetail = async () => {
    await getPartyDetail(Number(groupPk),
    (res) => {
      const data = res.data.data;
      console.log(data)
      setGroupInfo({
        partyMembers: data.partyMembers,
        partyName: data.partyName,
        rootDirId: data.rootDirId,
        thumbnail: data.thumbnail})
    },
    (err) => {
      console.error(err)
    })
  }

  const memberThumb = () => {
    const memberThumbs = groupInfo.partyMembers.map((data) => (
      <img key={data.memberId} src={data.profileUrl} className="rounded-full object-cover w-8 h-8 border-white border-2 -mr-2"/>
    ));
    return memberThumbs;
  }

  return (
    <div className="flex flex-col justify-center items-center gap-4">
      <div className="w-[90px] h-[90px] rounded-full border-disabled-gray border-[1px] flex justify-center items-center">
        <img src={groupInfo.thumbnail} className="w-[84px] h-[84px] rounded-full" />
      </div>

      <div className="flex items-center gap-4">
        <div className="text-black text-xl font-gtr-B">{groupInfo.partyName}</div>
        <Link to={`/group/${groupPk}/config`}>
          <img src={settings} className="w-3 h-3" />
        </Link>
      </div>

      <div className="flex" onClick={() => setMemberOpen()}>
        {memberThumb()}
        <div className="w-8 h-8 rounded-full bg-gray flex justify-center items-center border-white border-2">
          <img src={etc} className="w-[13px]" />
        </div>
      </div>
    </div>
  )
};

export default GroupProfile;