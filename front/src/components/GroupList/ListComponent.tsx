import React, { useEffect, useState } from "react";
import unFavorStar from "@/assets/GroupList/unFavoriteStar.png";
import favorStar from "@/assets/GroupList/favoriteStar.png";
import { getPartyList, getPartyThumb } from "../../api/party";
import { changeUploadGroup } from "../../api/user";
import { PartyListData } from "../../types/GroupType";
import { userStore } from "../../stores/UserStore";
import { useStore } from "zustand";
import { useNavigate } from "react-router-dom";

const ListComponent: React.FC = () => {
	const [partyList, setPartList] = useState<PartyListData[]>([]);
	const user = useStore(userStore);
	const keys = user.groupKey;
	const navi = useNavigate();
	useEffect(() => {
		getList();
	}, []);

	const getList = async () => {
		await getPartyList(
			(res) => {
				res.data.data.forEach((item) => {
					checkStateAndImg(item)
				});
				setPartList(res.data.data);
			},
			(err) => {
				console.log(err);
			},
		);
	};

	const changeUpload = async (id: number) => {
		if (id === user.uploadGroupId) return;

		await changeUploadGroup(id, 
			() => {
				user.setUploadGroupId(id)
			},
			(err) => {
				console.error(err)
			}
		)
	}

  const [blobUrl, setBlobURl] = useState('')

	const checkStateAndImg = async (data: PartyListData) => {
		const key = keys[data.id]
		if (key === "expired") {
			data.partyProfile = ''
			data.expired = true;
		}
		else {
			data.expired = false
			const opt = {
				"x-amz-server-side-encryption-customer-key": key,
			};
			await getPartyThumb(
				data.partyProfile,
				opt,
        (res) => {
          const blob = new Blob([res.data], {type: 'image/png'})
          setBlobURl(URL.createObjectURL(blob))
        },
				(err) => { console.log(err); },
			);
		}
	}

	const clickHandler = (id: number, state: boolean) => {
		if (state) {
			navi(`/group/${id}/attend`);
		}
		else {
			navi(`/group/${id}/main`);
		}
	}

	return (
		<div className="flex flex-col gap-2">
			{partyList.map((item) => (
				<div
					key={item.id}
					className="relative w-100 h-20 rounded-2xl flex items-center border border-disabled-gray">
					<div className="h-full w-4/5 flex items-center" onClick={() => clickHandler(item.id, item.expired)}>
						<img
							src={blobUrl}
							className="w-14 h-14 rounded-full ms-4 me-5"
						/>
						<p className="text-xl font-pre-R">{item.partyName}</p>
					</div>
					<div className="h-full w-1/5 flex items-center justify-center">
						{!item.expired && (
							<img
								src={item.id === user.uploadGroupId ? favorStar : unFavorStar}
								className="w-6 h-6 right-5"
								onClick={() => {
									changeUpload(item.id)
								}}
							/>
						)}
						{item.expired && (
							<div className="w-4/5 h-4/5 flex flex-col justify-center items-center font-gtr-B text-white bg-disabled-gray rounded-xl">
								<p>재입장</p>
							</div>
						)}
					</div>
				</div>
			))}
		</div>
	);
};

export default ListComponent;
