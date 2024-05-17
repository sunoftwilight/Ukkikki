import React from "react";
import { ReplyBottomInterface } from "../../../types/Comment/CommentInterface";

import CommentInput from "../Comment/CommentInput";


const ReplyBottom: React.FC<ReplyBottomInterface> = ({
	idx,
	reply,
	isModify,
	replyModify,
	switchIsModify,
}) => {
	idx;
	return (
		<>
			{!isModify ? (
                <div className="flex">
                    {reply.tag.map((tag) => (
                        <div
                        className="flex items-center justify-center mr-1 rounded-lg px-1 text-xs bg-blue-200 w-auto text-nowrap"
                        >
                        {tag.userName}
                        </div>
                    ))}
				    <div className="font-pre-L text-black text-sm">{reply.content}</div>
                </div>
			) : (
                <CommentInput key={reply.userId} userId={reply.userId} content={reply.content} tag={reply.tag} commentModify={replyModify} switchIsModify={switchIsModify}/>
			)}
		</>
	);
};

export default ReplyBottom;
