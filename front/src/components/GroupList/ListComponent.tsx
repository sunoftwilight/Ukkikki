import React, { useEffect, useState } from "react";
import unFavorStar from "@/assets/GroupList/unFavoriteStar.png";
import favorStar from "@/assets/GroupList/favoriteStar.png";
import { Link } from "react-router-dom";
import { getPartyList, getPartyThumb } from "../../api/party";
import { PartyListData } from "../../types/GroupType";
import { userStore } from "../../stores/UserStore";
import { useStore } from "zustand";

const ListComponent: React.FC = () => {
	const [partyList, setPartList] = useState<PartyListData[]>([]);
	const user = useStore(userStore);
	const keys = user.groupKey;

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
			() => {
				// console.log(res);
			},
			(err) => {
				console.log(err);
			},
		);
	};

	return (
		<div className="flex flex-col gap-2">
			{partyList.map((item) => (
				<Link
					to={`/group/${item.id}`}
					key={item.id}
					className="relative w-100 h-20 rounded-2xl flex items-center border border-disabled-gray"
				>
					<img
						src={item.partyProfile}
						className="w-14 h-14 rounded-full ms-4 me-5"
					/>
					<p className="text-xl font-pre-R">{item.partyName}</p>
					<img
						src={item.id === user.uploadGroupId ? favorStar : unFavorStar}
						className="absolute w-6 h-6 right-5"
					/>
				</Link>
			))}
		</div>
	);
};

export default ListComponent;
