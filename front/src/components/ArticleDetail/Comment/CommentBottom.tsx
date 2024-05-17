import React from "react";
import { CommentBottomInterface } from "../../../types/Comment/CommentInterface";

import CommentInput from "./CommentInput";


const CommentBottom: React.FC<CommentBottomInterface> = ({
	idx,
	comment,
	isModify,
	commentModify,
	switchIsModify,
}) => {
	return (
		<>
			{!isModify ? (
                <div className="flex">
                    {comment.tag.map((tag) => (
                        <div
                        className="flex items-center justify-center mr-1 rounded-lg px-1 text-xs bg-blue-200 w-auto text-nowrap"
                        >
                        {tag.userName}
                        </div>
                    ))}
				    <div className="font-pre-L text-black text-sm">{comment.content}</div>
                </div>
			) : (
                <CommentInput key={idx} comment={comment} commentModify={commentModify} switchIsModify={switchIsModify}/>
			)}
		</>
	);
};

export default CommentBottom;
