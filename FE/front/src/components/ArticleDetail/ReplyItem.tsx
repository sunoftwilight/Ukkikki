import React, { useState } from "react";
import {
	ReplyItemInterface,
	TagInterface,
} from "../../types/Comment/CommentInterface";

import CommentInput from "./Comment/CommentInput";
import * as C from "../../api/Article/comment";
import { useParams } from "react-router-dom";

import ReplyTop from "./Reply/ReplyTop";
import ReplyBottom from "./Reply/ReplyBottom";

import bb from "@/assets/ArticleDetail/bb.png"
import CommentImg from "./CommentImg";

const ReplyItem: React.FC<ReplyItemInterface> = ({
	commentIdx,
	replyIdx,
	reply,
	createReplyCancel,
	getCommentList,
}) => {
	// 게시판 번호
	const { feedPk } = useParams();
	// 수정 여부
	const [isModify, setIsModify] = useState(false);

	// 대댓글 생성
	const replyCreate = async (inputValue: string, tagList: TagInterface[]) => {
		// 자식 컴포넌트에서 값을 받아오면
		const params = {
			content: inputValue,
			tagList: tagList,
		};

		await C.createReply(
			Number(feedPk),
			commentIdx,
			params,
			() => {
				getCommentList();
			},
			(err) => {
				console.log(err);
			},
		);
	};


	// 대댓글 수정
	const replyModify = async (inputValue : string, tagList : TagInterface[]) => {

		
		// 자식 컴포넌트에서 값을 받아오면
		const params = {
			content: inputValue,
			tagList: tagList,
		};

		await C.modifyReply(
			Number(feedPk), commentIdx, replyIdx, params,
			() => {
				// 댓글 조회
				getCommentList();
			},
			(err) => {
				console.log(err);
			}
		)
	}


	// 대댓글 삭제
	const replyDelete = async () => {
		await C.deleteReply(
			Number(feedPk), commentIdx, replyIdx,
			() => {
				getCommentList();
			},
			(err) => {
				console.log(err);
			}
		)
	}

	// 수정 여부
	const switchIsModify = () => {
		if (reply.userId === -1) {
			createReplyCancel();
		} else {
			setIsModify(!isModify)
		}
	};


	return (
		<>
			{reply.userId === -1 ? (
				<div className="w-full pl-16 pr-6">
					<CommentInput
						key={reply.userId}
						userId={reply.userId}
						content={reply.content}
						tag={reply.tag}
						commentModify={replyCreate}
						switchIsModify={switchIsModify}
					/>
				</div>
			) : (					
				<>
					{reply.isDelete ? (
						<div className="w-full py-2 px-5 flex gap-3 bg-white pl-16">
							<img src={bb} className="w-9 h-9 rounded-full" />
							<div className="flex flex-col gap-2 w-full font-pre-L text-sm text-point-gray justify-center">
								삭제된 댓글입니다.
							</div>
						</div>
					) : (
						<div className="w-full py-2 px-5 flex gap-3 bg-white pl-16">
              { reply.profileUrl.startsWith('http://k.kakaocdn.net/') ?
                <img src={reply.profileUrl} className="w-9 h-9 rounded-full" />
                :
                <CommentImg url={reply.profileUrl} />
              }
							{/* <img src={reply.profileUrl} className="w-9 h-9 rounded-full" /> */}
							<div className="flex flex-col gap-2 w-[calc(100%-40px)]">
								<ReplyTop key={replyIdx} reply={reply} commentDelete={replyDelete} switchIsModify={switchIsModify} isModify={isModify}/>
								<ReplyBottom key={reply.userId + "reply"} idx={replyIdx} isModify={isModify} reply={reply} replyModify={replyModify} switchIsModify={switchIsModify}/>
							</div>
						</div>
					)}
				</>
			)}
		</>
	);
};

export default ReplyItem;
