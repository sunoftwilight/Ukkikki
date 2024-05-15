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
				setPartList(res.data.data);
				res.data.data.forEach((item) => {
					getImg(item.partyProfile, keys[item.id]);
				});
			},
			(err) => {
				console.log(err);
			},
		);
	};

	const getImg = async (url: string, key: string) => {
		const opt = {
			"x-amz-server-side-encryption-customer-key": key,
		};
		await getPartyThumb(
			url,
			opt,
			() => {},
			(err) => { console.log(err); },
		);
	};

	const changeUpload = async (id: number) => {
		if (id === user.uploadGroupId) return;

		await changeUploadGroup(id, 
			(res) => {
				console.log(res)
				user.setUploadGroupId(id)
			},
			(err) => {
				console.error(err)
			}
		)
	}

	return (
		<div className="flex flex-col gap-2">
			{partyList.map((item) => (
				<div
					key={item.id}
					className="relative w-100 h-20 rounded-2xl flex items-center border border-disabled-gray">
					<div className="h-full w-5/6 flex items-center" onClick={() => navi(`/group/${item.id}/main`)}>
						<img
							src={item.partyProfile}
							className="w-14 h-14 rounded-full ms-4 me-5"
						/>
						<p className="text-xl font-pre-R">{item.partyName}</p>
					</div>
					<img
						src={item.id === user.uploadGroupId ? favorStar : unFavorStar}
						className="absolute w-6 h-6 right-5"
						onClick={() => {
							changeUpload(item.id)
						}}
					/>					
				</div>
			))}
		</div>
	);
};

export default ListComponent;
