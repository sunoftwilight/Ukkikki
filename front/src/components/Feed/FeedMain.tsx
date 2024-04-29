import React from "react";

const articleList = [
  {
    writerName: '나는이해진이다',
    writerImg: 'https://i.namu.wiki/i/6nJq7Dza9kRKQbvw-EBUHqArvnLuKWGqeaWTT5odfIp1mJnrJNuLRe5hmxC3eXQtB0_1sQiknnDOpT0-kz1baA.webp',
    createdDate: '24.02.16',
    title: '미안한데 내 동년배들 요즘 다 우끼끼로 사진 공유한다',
    content: '내 동년배 중에 사진 공유 카톡으로 하는 놈들 아무도 없다 요즘 카톡으로 사진공유하면 같이 못논다 누가 30장씩 끊어치기하냐',
    imgThumbnail: 'https://cafe24.poxo.com/ec01/onemorebag/2OMm5gLLSI+40BfdB9P7bNSS7kRPwYFLtZrprbQv4+DseNLRn9GVDTOik0ZO0mq4ckfr2QQON8Tl9WrYezr4mw==/_/web/upload/NNEditor/20210728/copy-1627441677-E1848EE185ACE18480E185A9E18489E185B5E186B7_E1848BE185AFE186ABE18486E185A9E1848BE185A5E18487E185A2E186A8_E18491E185A9E18489E185B3E18490E185A5_A3.jpg',
  },
  {
    writerName: '나는이해진인가',
    writerImg: 'https://blog.kakaocdn.net/dn/SORL8/btr22Ks9Frm/Crse2TjNQoYJz8CQVKjSF1/img.jpg',
    createdDate: '24.02.16',
    title: '미안한데 내 동년배들 요즘 다 우끼끼로 사진 공유한다',
    content: '내 동년배 중에 사진 공유 카톡으로 하는 놈들 아무도 없다 요즘 카톡으로 사진공유하면 같이 못논다 누가 30장씩 끊어치기하냐',
    imgThumbnail: 'https://pbs.twimg.com/media/GCQ9BhvaUAAMx-s.jpg:large',
  },
  {
    writerName: '나는이해진일수도',
    writerImg: 'https://blog.kakaocdn.net/dn/c4jfxr/btr2YaGDx31/YUI9JpYZYugkq0mLErICa1/img.jpg',
    createdDate: '24.02.16',
    title: '미안한데 내 동년배들 요즘 다 우끼끼로 사진 공유한다',
    content: '내 동년배 중에 사진 공유 카톡으로 하는 놈들 아무도 없다 요즘 카톡으로 사진공유하면 같이 못논다 누가 30장씩 끊어치기하냐',
    imgThumbnail: 'https://www.kocca.kr/n_content/vol23/img/new/issue_people1/people1_3.jpg',
  },
  {
    writerName: '나는이해진일걸',
    writerImg: 'https://mblogthumb-phinf.pstatic.net/MjAyMTA4MjVfNjAg/MDAxNjI5ODQxMzc4MTg2.-_QGXsB5hH59hImBwdlDptHLGOTJzyhFKcAMWQyMjCcg.aCZ_dulUuTERV5mrnh9poxgxI1B-wbdxJPTZmXLZEZUg.JPEG.wldktjdgns/IMG_1271.JPG?type=w800',
    createdDate: '24.02.16',
    title: '미안한데 내 동년배들 요즘 다 우끼끼로 사진 공유한다',
    content: '내 동년배 중에 사진 공유 카톡으로 하는 놈들 아무도 없다 요즘 카톡으로 사진공유하면 같이 못논다 누가 30장씩 끊어치기하냐',
    imgThumbnail: 'https://www.kocca.kr/n_content/vol23/img/new/issue_people1/people1_8.jpg',
  },
  {
    writerName: '나는이해진아닌가',
    writerImg: 'https://blog.kakaocdn.net/dn/rgYkq/btra8zeW38W/hkpMT3ET3zKJHplH7RKpu1/img.jpg',
    createdDate: '24.02.16',
    title: '미안한데 내 동년배들 요즘 다 우끼끼로 사진 공유한다',
    content: '내 동년배 중에 사진 공유 카톡으로 하는 놈들 아무도 없다 요즘 카톡으로 사진공유하면 같이 못논다 누가 30장씩 끊어치기하냐',
    imgThumbnail: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQcpuM-cDTt3TJdxOrTohqyLtgkm29xX3Zt8hLOKbzxaw&s',
  },
  {
    writerName: '나는이해진아닐수도',
    writerImg: 'https://file3.instiz.net/data/file3/2022/06/29/a/d/5/ad5a7413f5c5fb6a904634aa90cad54c.jpg',
    createdDate: '24.02.16',
    title: '미안한데 내 동년배들 요즘 다 우끼끼로 사진 공유한다',
    content: '내 동년배 중에 사진 공유 카톡으로 하는 놈들 아무도 없다 요즘 카톡으로 사진공유하면 같이 못논다 누가 30장씩 끊어치기하냐',
    imgThumbnail: 'https://mblogthumb-phinf.pstatic.net/MjAyMzA2MjdfMjMx/MDAxNjg3Nzk1NjQwNzgw.Xez2z2d-6XTWhQ0e2ciSvaCM7pxDSffDD8DIswol7_og.BqBSFo7alKatwMtWUAdjNc0IIloEJ08kkC470jwY5VYg.JPEG.pyjpyj06086/IMG_4982.JPG?type=w800',
  },
  {
    writerName: '나는이해진아니다',
    writerImg: 'https://file3.instiz.net/data/file3/2022/06/29/c/2/2/c22e0370040ecb6b604b7e54931200b5.jpg',
    createdDate: '24.02.16',
    title: '미안한데 내 동년배들 요즘 다 우끼끼로 사진 공유한다',
    content: '내 동년배 중에 사진 공유 카톡으로 하는 놈들 아무도 없다 요즘 카톡으로 사진공유하면 같이 못논다 \n 누가 30장씩 끊어치기하냐 내 동년배 중에 사진 공유 카톡으로 하는 놈들 아무도 없다 요즘 카톡으로 사진공유하면 같이 못논다 누가 30장씩 끊어치기하냐 내 동년배 중에 사진 공유 카톡으로 하는 놈들 아무도 없다 요즘 카톡으로 사진공유하면 같이 못논다 누가 30장씩 끊어치기하냐',
    imgThumbnail: 'https://mblogthumb-phinf.pstatic.net/MjAyMzA2MjdfMjcx/MDAxNjg3Nzk1NjQwOTE5.nu7_IIlQ9qkEIXkhVOwQ8hEd_KN4EUIkw3ZmJvl4S-Ug.Fw7Nl_8F37X-mE76iJaYalwuuBw38xWJoIFyZCrxEagg.JPEG.pyjpyj06086/IMG_5005.JPG?type=w800',
  },
]

const FeedMain: React.FC = () => {
  return (
    <div className="flex flex-col w-full h-full overflow-scroll scrollbar-hide">
      { articleList.map((item, idx) => (        
        <div className="px-4 py-2 flex flex-col gap-2">
          <div className="w-full flex gap-3 items-center">
            <img src={item.writerImg} className="rounded-full w-12 h-12 object-cover" />
            <div className="flex flex-col gap-1">
              <div className="font-pre-SB text-black text-base">{item.writerName}</div>
              <div className="font-pre-R text-point-gray text-sm">{item.createdDate}</div>
            </div>
          </div>
          <div className="w-full p-3 flex flex-col gap-5 bg-soft-gray rounded-xl">
            <div className="flex flex-col gap-3">
              <div className="truncate font-pre-SB text-lg text-black">{item.title}</div>
              <div className="line-clamp-4 font-pre-R text-sm text-black">{item.content}</div>
            </div>

            <img src={item.imgThumbnail} className="rounded-xl h-52 object-cover" />
          </div>
        </div>
      ))}
    </div>
  )
};

export default FeedMain;