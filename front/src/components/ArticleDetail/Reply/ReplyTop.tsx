import React from "react";
import editBtn from "@/assets/ArticleDetail/edit.png";
import deleteBtn from "@/assets/ArticleDetail/delete.png";
import { ReplyTopInterface } from "../../../types/Comment/CommentInterface";

const ReplyTop: React.FC<ReplyTopInterface> = ({ reply, isModify, commentDelete, switchIsModify}) => {
	return (
		<div className="flex justify-between">
			<div className="flex gap-2">
				<div className="font-pre-SB text-black text-xs">{reply.userName}</div>
				<div className="font-pre-L text-point-gray text-[10px]">
					{reply.createdDate}
				</div>
			</div>

			{!isModify && (
				<div className="flex gap-2">
					<img src={editBtn} className="w-4" onClick={switchIsModify} />
					<img src={deleteBtn} className="w-3" onClick={commentDelete} />
				</div>
			)}
		</div>
	);
};

export default ReplyTop;
