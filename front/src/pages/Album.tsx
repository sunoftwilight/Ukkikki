import React from "react";
import folder from "@/assets/Album/folder.png"

const albumList = {
  folder: ['우리의 믿음', '우리의 사랑'],
  thumbnailImg: [
    'https://wimg.mk.co.kr/news/cms/202309/30/news-p.v1.20230930.e202dea5b30244fdb88348392c31289a_P1.jpeg',
    'https://upload.wikimedia.org/wikipedia/commons/thumb/8/83/20231002_Jang_Won-young_%28%EC%9E%A5%EC%9B%90%EC%98%81%29.jpg/800px-20231002_Jang_Won-young_%28%EC%9E%A5%EC%9B%90%EC%98%81%29.jpg',
    'https://menu.moneys.co.kr/moneyweek/thumb/2023/04/23/06/2023042314003776303_1.jpg/dims/optimize/',
    'https://i2n.news1.kr/system/photos/2024/3/5/6515159/article.jpg',
    'https://isplus.com/data/isp/image/2024/01/24/isp20240124000305.800x.0.jpg',
    'https://spnimage.edaily.co.kr/images/Photo/files/NP/S/2022/07/PS22072100041.jpg',
    'https://news.koreadaily.com/data/photo/2024/02/27/202402272228779833_65dde79e7e1e2.jpg',
    'https://newsimg.sedaily.com/2023/10/24/29W32A3E5F_1.jpg',
    'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTchzVJLijrIb17bF3kX_HVZDjoAV_3dbhqBfkR8GmqNw&s',
    'https://menu.moneys.co.kr/moneyweek/thumb/2023/04/23/06/2023042314003776303_1.jpg/dims/optimize/',
    'https://image.xportsnews.com/contents/images/upload/article/2023/0428/mb_1682654309210504.jpg',
    'https://image.ajunews.com/content/image/2024/01/17/20240117121410404713.jpg',
    'https://img.tvreportcdn.de/cms-content/uploads/2024/04/03/b1b81480-3587-44b1-a34e-1f1a48e4c2e4.jpg',
    'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ_G_7wiIC2Kj1NGySNbaMra1HnCmTopjdgTTvvpOewHw&s',
    'https://cdn.newsfreezone.co.kr/news/photo/202211/426751_402142_353.jpg',
    'https://wimg.mk.co.kr/news/cms/202309/30/news-p.v1.20230930.e202dea5b30244fdb88348392c31289a_P1.jpeg',
    'https://upload.wikimedia.org/wikipedia/commons/thumb/8/83/20231002_Jang_Won-young_%28%EC%9E%A5%EC%9B%90%EC%98%81%29.jpg/800px-20231002_Jang_Won-young_%28%EC%9E%A5%EC%9B%90%EC%98%81%29.jpg',
    'https://menu.moneys.co.kr/moneyweek/thumb/2023/04/23/06/2023042314003776303_1.jpg/dims/optimize/',
    'https://i2n.news1.kr/system/photos/2024/3/5/6515159/article.jpg',
    'https://isplus.com/data/isp/image/2024/01/24/isp20240124000305.800x.0.jpg',
    'https://spnimage.edaily.co.kr/images/Photo/files/NP/S/2022/07/PS22072100041.jpg',
    'https://news.koreadaily.com/data/photo/2024/02/27/202402272228779833_65dde79e7e1e2.jpg',
    'https://newsimg.sedaily.com/2023/10/24/29W32A3E5F_1.jpg',
    'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTchzVJLijrIb17bF3kX_HVZDjoAV_3dbhqBfkR8GmqNw&s',
    'https://menu.moneys.co.kr/moneyweek/thumb/2023/04/23/06/2023042314003776303_1.jpg/dims/optimize/',
    'https://image.xportsnews.com/contents/images/upload/article/2023/0428/mb_1682654309210504.jpg',
    'https://image.ajunews.com/content/image/2024/01/17/20240117121410404713.jpg',
    'https://img.tvreportcdn.de/cms-content/uploads/2024/04/03/b1b81480-3587-44b1-a34e-1f1a48e4c2e4.jpg',
    'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ_G_7wiIC2Kj1NGySNbaMra1HnCmTopjdgTTvvpOewHw&s',
    'https://cdn.newsfreezone.co.kr/news/photo/202211/426751_402142_353.jpg',
    'https://wimg.mk.co.kr/news/cms/202309/30/news-p.v1.20230930.e202dea5b30244fdb88348392c31289a_P1.jpeg',
    'https://upload.wikimedia.org/wikipedia/commons/thumb/8/83/20231002_Jang_Won-young_%28%EC%9E%A5%EC%9B%90%EC%98%81%29.jpg/800px-20231002_Jang_Won-young_%28%EC%9E%A5%EC%9B%90%EC%98%81%29.jpg',
    'https://menu.moneys.co.kr/moneyweek/thumb/2023/04/23/06/2023042314003776303_1.jpg/dims/optimize/',
    'https://i2n.news1.kr/system/photos/2024/3/5/6515159/article.jpg',
    'https://isplus.com/data/isp/image/2024/01/24/isp20240124000305.800x.0.jpg',
    'https://spnimage.edaily.co.kr/images/Photo/files/NP/S/2022/07/PS22072100041.jpg',
    'https://news.koreadaily.com/data/photo/2024/02/27/202402272228779833_65dde79e7e1e2.jpg',
    'https://newsimg.sedaily.com/2023/10/24/29W32A3E5F_1.jpg',
    'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTchzVJLijrIb17bF3kX_HVZDjoAV_3dbhqBfkR8GmqNw&s',
    'https://menu.moneys.co.kr/moneyweek/thumb/2023/04/23/06/2023042314003776303_1.jpg/dims/optimize/',
    'https://image.xportsnews.com/contents/images/upload/article/2023/0428/mb_1682654309210504.jpg',
    'https://image.ajunews.com/content/image/2024/01/17/20240117121410404713.jpg',
    'https://img.tvreportcdn.de/cms-content/uploads/2024/04/03/b1b81480-3587-44b1-a34e-1f1a48e4c2e4.jpg',
    'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ_G_7wiIC2Kj1NGySNbaMra1HnCmTopjdgTTvvpOewHw&s',
    'https://cdn.newsfreezone.co.kr/news/photo/202211/426751_402142_353.jpg',
  ]
}

const Album: React.FC = () => {
  return (

    <div className="grid grid-cols-3 px-4 gap-1 overflow-scroll scrollbar-hide">
      {albumList.folder.map((item, idx) => (
        <div key={idx} className="flex flex-col justify-center items-center gap-1">
          <img src={folder} className="w-[82px] h-[65px]" />
          <div className="font-pre-R text-center text-xs">{item}</div>
        </div>
      ))}
      {albumList.thumbnailImg.map((item, idx) => (
        <div key={idx} className="flex justify-center items-center">
          <img key={idx} src={item} className="w-[106px] h-[90px] object-cover rounded-lg" />
        </div>
      ))}
    </div>
  )
};

export default Album;