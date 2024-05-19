import React, { useEffect, useState } from "react";
import {
	CommentItemInterface,
	TagInterface,
} from "../../types/Comment/CommentInterface";
import * as C from "../../api/Article/comment";
import { useParams } from "react-router-dom";

import bb from "@/assets/ArticleDetail/bb.png"

// 컴포넌트
import CommentTop from "./Comment/CommentTop";
import CommentBottom from "./Comment/CommentBottom";
import ReplyItem from "./ReplyItem";

const CommentItem: React.FC<CommentItemInterface> = ({
	idx,
	comment,
	getCommentList,
}) => {
	// 수정 여부
	const [isModify, setIsModify] = useState(false);
	// 게시판 번호
	const { feedPk } = useParams();

	const [reply,setReply] = useState(comment.reply);

	useEffect(() => {
		createReplyCancel();
	},[comment.reply])

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

	// 수정 여부
	const switchIsModify = () => {
		setIsModify(!isModify);
	};

	// 댓글 수정
	const commentModify = async (inputValue: string, tagList: TagInterface[]) => {

		// 자식 컴포넌트에서 값을 받아오면
		const params = {
			content: inputValue,
			tagList: tagList,
		};

		// api 전송
		await C.modifyComment(
			Number(feedPk),
			idx,
			params,
			() => {
				// 댓글 조회
				getCommentList();
			},
			(err) => {
				console.log(err);
			},
		);
	};

	// 대댓글 추가
	const createReply = () => {

		// 하나만 생성하기 위해 조건을 걸어주었따.
		if(reply.length === 0 || (reply.length > 0 && reply[reply.length - 1].userId !== -1)) {

			setReply([...reply,{
				userId : -1,
				content : "",
				userName : "",
				profileUrl : "",
				createdDate : "",
				isDelete : false,
				tag : [],
			}])
		}

	}

	const createReplyCancel = () => {
		setReply(comment.reply);
	}

	return (
		<div className="flex flex-col">
			{!comment.isDelete ? (
				<div className="w-full py-2 px-5 flex gap-3 bg-white">
					<img src={comment.profileUrl} className="w-9 h-9 rounded-full" />
					<div className="flex flex-col gap-2 w-full">
						<CommentTop
							key={idx}
							comment={comment}
							isModify={isModify}
							commentDelete={commentDelete}
							switchIsModify={switchIsModify}
							createReply={createReply}
						/>
						<CommentBottom
							key={comment.userId}
							idx={idx}
							comment={comment}
							isModify={isModify}
							commentModify={commentModify}
							switchIsModify={switchIsModify}
						/>
					</div>
				</div>
			) : (
				<div className="w-full py-2 px-5 flex gap-3 bg-white">
					<img src={bb} className="w-9 h-9 rounded-full" />
					<div className="flex flex-col gap-2 w-full font-pre-L text-sm text-point-gray justify-center">
						삭제된 댓글입니다.
					</div>
				</div>
			)}

			<div className="w-full">
				{reply.map((item, replyIdx) => (
					<ReplyItem key={replyIdx} commentIdx={idx} replyIdx={replyIdx} reply={item} createReplyCancel={createReplyCancel} getCommentList={getCommentList}/>
				))}
			</div>
		</div>
	);
};

export default CommentItem;
