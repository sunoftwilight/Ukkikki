import React, { useState } from "react";
import { CommentItemInterface, TagInterface } from "../../types/Comment/CommentInterface";
import * as C from "../../api/Article/comment";
import { useParams } from "react-router-dom";

import TrashImg from "../../assets/Trash/trash.png";

// 컴포넌트
import CommentTop from "./Comment/CommentTop";
import CommentBottom from "./Comment/CommentBottom";


const CommentItem: React.FC<CommentItemInterface> = ({
	idx,
	comment,
	getCommentList,
}) => {
	// 수정 여부
	const [isModify, setIsModify] = useState(false);
	// 게시판 번호
	const { feedPk } = useParams();

	// 댓글 삭제
	const commentDelete = async () => {
		await C.deleteComment(
			Number(feedPk),
			idx,
			() => {
				getCommentList();
			},
			(err) => {
				console.log(err);
			},
		);
	};

	const switchIsModify = () => {
		setIsModify(!isModify);
	};

	const commentModify = async (inputValue : string, tagList : TagInterface[]) => {
		
		const params = {
			content : inputValue,
			tagList : tagList
		}

		await C.modifyComment(
			Number(feedPk),idx,params,
			() => {
				getCommentList();
			},
			(err) => {
				console.log(err);
			}
		)
	};

	return (
		<div className="flex flex-col">
			{!comment.isDelete ? (
				<div className="w-full py-2 px-5 flex gap-3 bg-white">
				<img src={comment.profileUrl} className="w-9 h-9 rounded-full" />
					<div className="flex flex-col gap-2 w-full">
						<CommentTop
							idx={idx}
							comment={comment}
							isModify={isModify}
							commentDelete={commentDelete}
							switchIsModify={switchIsModify}
						/>
						<CommentBottom idx={idx} comment={comment} isModify={isModify} commentModify={commentModify} switchIsModify={switchIsModify}/>
					</div>
				</div>
			) : (
				<div className="w-full py-2 px-5 flex gap-3 bg-white">
					<img src={TrashImg} className="w-9 h-9 rounded-full" />
					<div className="flex flex-col gap-2 w-full font-pre-L text-sm text-point-gray justify-center">
						삭제된 댓글입니다.
					</div>
				</div>
			)}

			{/* <div>
        { reply.map((item, idx) => (
          <ReplyItem key={idx} reply={item} />
        ))}
      </div> */}
		</div>
	);
};

export default CommentItem;
