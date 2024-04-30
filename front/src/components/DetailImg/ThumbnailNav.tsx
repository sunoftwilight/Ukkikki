import React from "react";
import { Link } from "react-router-dom";
import { useStore } from "zustand";
import { DetailImgStore } from "../../stores/DetailImgStore";

const albumList = {
  folder: ['우리의 믿음', '우리의 사랑'],
  thumbnailImg: [
    { pk: 1, url: 'https://wimg.mk.co.kr/news/cms/202309/30/news-p.v1.20230930.e202dea5b30244fdb88348392c31289a_P1.jpeg'},
    { pk: 2, url: 'https://upload.wikimedia.org/wikipedia/commons/thumb/8/83/20231002_Jang_Won-young_%28%EC%9E%A5%EC%9B%90%EC%98%81%29.jpg/800px-20231002_Jang_Won-young_%28%EC%9E%A5%EC%9B%90%EC%98%81%29.jpg'},
    { pk: 3, url: 'https://menu.moneys.co.kr/moneyweek/thumb/2023/04/23/06/2023042314003776303_1.jpg/dims/optimize/'},
    { pk: 4, url: 'https://i2n.news1.kr/system/photos/2024/3/5/6515159/article.jpg'},
    { pk: 5, url: 'https://isplus.com/data/isp/image/2024/01/24/isp20240124000305.800x.0.jpg'},
    { pk: 6, url: 'https://spnimage.edaily.co.kr/images/Photo/files/NP/S/2022/07/PS22072100041.jpg'},
    { pk: 7, url: 'https://news.koreadaily.com/data/photo/2024/02/27/202402272228779833_65dde79e7e1e2.jpg'},
    { pk: 8, url: 'https://newsimg.sedaily.com/2023/10/24/29W32A3E5F_1.jpg'},
    { pk: 9, url: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTchzVJLijrIb17bF3kX_HVZDjoAV_3dbhqBfkR8GmqNw&s'},
    { pk: 10, url: 'https://menu.moneys.co.kr/moneyweek/thumb/2023/04/23/06/2023042314003776303_1.jpg/dims/optimize/'},
    { pk: 11, url: 'https://image.xportsnews.com/contents/images/upload/article/2023/0428/mb_1682654309210504.jpg'},
    { pk: 12, url: 'https://image.ajunews.com/content/image/2024/01/17/20240117121410404713.jpg'},
    { pk: 13, url: 'https://img.tvreportcdn.de/cms-content/uploads/2024/04/03/b1b81480-3587-44b1-a34e-1f1a48e4c2e4.jpg'},
    { pk: 14, url: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ_G_7wiIC2Kj1NGySNbaMra1HnCmTopjdgTTvvpOewHw&s'},
    { pk: 15, url: 'https://cdn.newsfreezone.co.kr/news/photo/202211/426751_402142_353.jpg'},
    { pk: 16, url: 'https://wimg.mk.co.kr/news/cms/202309/30/news-p.v1.20230930.e202dea5b30244fdb88348392c31289a_P1.jpeg'},
    { pk: 17, url: 'https://upload.wikimedia.org/wikipedia/commons/thumb/8/83/20231002_Jang_Won-young_%28%EC%9E%A5%EC%9B%90%EC%98%81%29.jpg/800px-20231002_Jang_Won-young_%28%EC%9E%A5%EC%9B%90%EC%98%81%29.jpg'},
    { pk: 18, url: 'https://menu.moneys.co.kr/moneyweek/thumb/2023/04/23/06/2023042314003776303_1.jpg/dims/optimize/'},
    { pk: 19, url: 'https://i2n.news1.kr/system/photos/2024/3/5/6515159/article.jpg'},
    { pk: 20, url: 'https://isplus.com/data/isp/image/2024/01/24/isp20240124000305.800x.0.jpg'},
    { pk: 21, url: 'https://spnimage.edaily.co.kr/images/Photo/files/NP/S/2022/07/PS22072100041.jpg'},
    { pk: 22, url: 'https://news.koreadaily.com/data/photo/2024/02/27/202402272228779833_65dde79e7e1e2.jpg'},
    { pk: 23, url: 'https://newsimg.sedaily.com/2023/10/24/29W32A3E5F_1.jpg'},
    { pk: 24, url: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTchzVJLijrIb17bF3kX_HVZDjoAV_3dbhqBfkR8GmqNw&s'},
    { pk: 25, url: 'https://menu.moneys.co.kr/moneyweek/thumb/2023/04/23/06/2023042314003776303_1.jpg/dims/optimize/'},
    { pk: 26, url: 'https://image.xportsnews.com/contents/images/upload/article/2023/0428/mb_1682654309210504.jpg'},
    { pk: 27, url: 'https://image.ajunews.com/content/image/2024/01/17/20240117121410404713.jpg'},
    { pk: 28, url: 'https://img.tvreportcdn.de/cms-content/uploads/2024/04/03/b1b81480-3587-44b1-a34e-1f1a48e4c2e4.jpg'},
    { pk: 29, url: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ_G_7wiIC2Kj1NGySNbaMra1HnCmTopjdgTTvvpOewHw&s'},
    { pk: 30, url: 'https://cdn.newsfreezone.co.kr/news/photo/202211/426751_402142_353.jpg'},
    { pk: 31, url: 'https://wimg.mk.co.kr/news/cms/202309/30/news-p.v1.20230930.e202dea5b30244fdb88348392c31289a_P1.jpeg'},
    { pk: 32, url: 'https://upload.wikimedia.org/wikipedia/commons/thumb/8/83/20231002_Jang_Won-young_%28%EC%9E%A5%EC%9B%90%EC%98%81%29.jpg/800px-20231002_Jang_Won-young_%28%EC%9E%A5%EC%9B%90%EC%98%81%29.jpg'},
    { pk: 33, url: 'https://menu.moneys.co.kr/moneyweek/thumb/2023/04/23/06/2023042314003776303_1.jpg/dims/optimize/'},
    { pk: 34, url: 'https://i2n.news1.kr/system/photos/2024/3/5/6515159/article.jpg'},
    { pk: 35, url: 'https://isplus.com/data/isp/image/2024/01/24/isp20240124000305.800x.0.jpg'},
    { pk: 36, url: 'https://spnimage.edaily.co.kr/images/Photo/files/NP/S/2022/07/PS22072100041.jpg'},
    { pk: 37, url: 'https://news.koreadaily.com/data/photo/2024/02/27/202402272228779833_65dde79e7e1e2.jpg'},
    { pk: 38, url: 'https://newsimg.sedaily.com/2023/10/24/29W32A3E5F_1.jpg'},
    { pk: 39, url: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTchzVJLijrIb17bF3kX_HVZDjoAV_3dbhqBfkR8GmqNw&s'},
    { pk: 40, url: 'https://menu.moneys.co.kr/moneyweek/thumb/2023/04/23/06/2023042314003776303_1.jpg/dims/optimize/'},
    { pk: 41, url: 'https://image.xportsnews.com/contents/images/upload/article/2023/0428/mb_1682654309210504.jpg'},
    { pk: 42, url: 'https://image.ajunews.com/content/image/2024/01/17/20240117121410404713.jpg'},
    { pk: 43, url: 'https://img.tvreportcdn.de/cms-content/uploads/2024/04/03/b1b81480-3587-44b1-a34e-1f1a48e4c2e4.jpg'},
    { pk: 44, url: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ_G_7wiIC2Kj1NGySNbaMra1HnCmTopjdgTTvvpOewHw&s'},
    { pk: 45, url: 'https://cdn.newsfreezone.co.kr/news/photo/202211/426751_402142_353.jpg'},
  ]
}

const ThumbnailNav: React.FC = () => {
  const { setCurrentImg } = useStore(DetailImgStore)

  return (
    <div className="w-full h-11 flex fixed bottom-0 overflow-x-scroll scrollbar-hide">
      { albumList.thumbnailImg.map((item, idx) => (
        <Link to={`/album/${item.pk}`} key={idx} onClick={() => setCurrentImg(item.pk, item.url)}>
          <img 
            src={item.url}
            className="min-w-11 h-full border-r-[1px] border-white object-cover"
          />
        </Link>
      ))}
    </div>
  )
}; 

export default ThumbnailNav;