import React from "react";

const dummyUrl = 'https://i.namu.wiki/i/JZhjW3kQarQRQ8rNailLaCGZEYHIA2joCs_ttsZxAWk83erw6O8katydeDxMJZCgVjXXf484gV7rgBrfhvY4Ww.webp'

const ArticleImgDetail: React.FC = () => {
  const fromAlbum = true

  return (
    <div className="w-full h-[calc(100%-60px)] flex justify-center items-center flex-col">
      <img src={dummyUrl} className="w-full h-full object-contain" />
      { fromAlbum && 
        <div className="w-full h-[60px] bg-main-blue font-pre-SB text-white text-2xl flex items-center justify-center fixed bottom-0">
          공유 앨범으로 이동
        </div>
      }
    </div>
  )
};

export default ArticleImgDetail;