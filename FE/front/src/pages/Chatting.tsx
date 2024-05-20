import React, { useState, useEffect, useRef } from "react";
import ChattingRoom from "../components/Chatting/ChattingRoom";
import logo from '../../icons/512.png'
import { ChatItemType } from "../types/ChatType";
import { Client } from '@stomp/stompjs';
import { useParams } from "react-router-dom";
import { getMsg } from "../api/chat";
import { userStore } from "../stores/UserStore";
import { useStore } from "zustand";
import { getPartyThumb } from "../api/party";
import { lastStore, loadingStore } from "../stores/ChatStore";

const Chatting: React.FC = () => {
  const chatInput = useRef<HTMLInputElement>(null)
  const [chat, setChat] = useState('')
  const [messages, setMessages] = useState<ChatItemType[]>([]);
  const [client, setClient] = useState<Client | null>(null)

  const { groupPk } = useParams();
  const { groupKey } = useStore(userStore)

  useEffect(() => {
    const stored = localStorage.getItem('USER_STORE');

    if (stored) {
      const obj = JSON.parse(stored);

      if (obj.state.accessToken !== '') {
        const token = obj.state.accessToken

        // WebSocket 연결 설정
        const connectionOptions = {
          brokerURL: 'wss://k10d202.p.ssafy.io/api/ws',
          // brokerURL: 'ws://localhost:5000/api/ws',
          connectHeaders: {
            Authorization: token
          }, // 연결 시 헤더 설정

          onConnect: () => {
            newClient.subscribe(
              `/sub/chats/party/${groupPk}`,
              message => {
                const parsedMessage = JSON.parse(message.body);
                setMessages(prev => [...prev, parsedMessage]);
              },
            );
          },

          onDisconnect: () => {}
        };
        
        // 웹소켓 연결
        const newClient = new Client();

        newClient.configure(connectionOptions);
    
        // 웹소켓 세션 활성화
        newClient.activate();
        setClient(newClient);
    
        return () => {
          newClient.deactivate();
        };
      }
    }
  }, [groupPk]);

  const sendMessage = (message: string) => {
    if (client !== null) {
      const newMessage = {
        content: message,
        password: groupKey[Number(groupPk)]
      };
      
      const stored = localStorage.getItem('USER_STORE');
      const jsonMessage = JSON.stringify(newMessage);

      if (stored) {
        const obj = JSON.parse(stored);
  
        if (obj.state.accessToken !== '') {
          const token = obj.state.accessToken
          client.publish({ destination: `/pub/message/${groupPk}`, body: jsonMessage, headers: { Authorization: token} });
          // setIsFocus()
        }} else {
          console.error('웹소켓 연결 비활성화');
        }
      }
  };
  
  const messageHandler = () => {
    // 작성 길이가 1 미만이라면 input 태그로 포커싱
    if (chat.length < 1) {
      chatInput.current!.focus();
      return;
    }

    sendMessage(chat)
    setChat('')
  }

  const enterHandler = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === "Enter") {
      messageHandler();
    }
  }

  useEffect(() => {
    getImgHandler()
  }, [])

  const getImgHandler =  () => {
    messages.forEach((item) => {
      if (item.profileType === 'S3') {
        toS3Handler(item.profileUrl)
      }
    })
  }

  const toS3Handler = async (url: string) => {
    const opt = {
      "x-amz-server-side-encryption-customer-key": groupKey[Number(groupPk)],
    }; 

    await getPartyThumb(
      url,
      opt,
      () => {},
      (err) => { console.error(err) }
    )
  }

  // 무한 스크롤
  const [page, setPage] = useState<number>(0)
  const { isLoading, setIsLoading } = useStore(loadingStore)
  const { isLast, setIsLast } = useStore(lastStore)

  // observer 컴포넌트 만나면 발생하는 콜백 함수 -> loading중 표시
  const handleObserver = (entries: IntersectionObserverEntry[]) => {
    const target = entries[0];
    if (target.isIntersecting && !isLoading && !isLast) {
      setIsLoading(true)
    }
  };
  
  // threshold : Intersection Observer의 옵션, 0 ~ 1 (0: 일 때는 교차점이 한 번만 발생해도 실행, 1은 모든 영역이 교차해야 콜백 함수가 실행)
  const observer = new IntersectionObserver(handleObserver, { threshold: 0 });

  useEffect(() => {
    // 최상단 요소를 관찰 대상으로 지정함
    const observerTarget = document.getElementById("observer");
    // 관찰 시작
    if (observerTarget) {
      observer.observe(observerTarget);
    }
  }, [])

  // 로딩중이면 페이지 상승 + api 요청
  // useEffect가 isLoading의 상태 변화를 계속 추적하며 api 쏘므로
  // setTimeout을 통해 api 요청 한번만 갈 수 있도록 수정
  useEffect(() => {
    if (isLast === true) return
    if (isLoading) {
      setPage((page) => page + 1);
      setTimeout(() => {
        fetchDataHandler();
      }, 10)
    }
  }, [isLoading])

  useEffect(() => {
    setPage(0)
    setIsLoading(true)
    setIsLast(false)
  }, [])

  // 데이터 추가 및 loading상태 변경
  const fetchDataHandler = async () => {
    await getMsg(
      Number(groupPk),
      { password: groupKey[Number(groupPk)] },
      { page: page, size: 100 },
      (res) => {
        setMessages([...res.data.simpleChatDtos, ...messages])
        if (res.data.next === false) {
          setIsLast(true)
        }
      },
      (err) => { console.error(err) }
    )
    setIsLoading(false)
  }

  return (
    <>
      <div className="px-4 pt-[10px] w-full h-[calc(100%-48px)] justify-center items-center">
        <div className="fixed w-[calc(100%-32px)] h-full flex justify-center items-center">
          <img src={logo} className="w-48" />
        </div>
        <div className="flex flex-col w-full h-full overflow-scroll scrollbar-hide z-10">
          <ChattingRoom msgList={messages} />
        </div>
      </div>

      <div className="px-4 py-[6px] w-full fixed bottom-0 h-12 flex justify-center items-center gap-[6px] bg-white">
        <input autoFocus
          ref={chatInput}
          value={chat}
          onChange={(e) => setChat(e.target.value)}
          onKeyDown={(e) => enterHandler(e)}
          autoComplete="off"
          placeholder="메세지를 작성해보세요!"
          className="py-2 px-3 font-pre-M text-base rounded-xl border-[0.4px] border-solid border-point-gray text-black w-[calc(100%-50px)] h-full outline-none" 
        />
        <button onClick={() => messageHandler()} className="bg-main-blue text-white font-pre-M text-base w-12 h-full rounded-xl">전송</button>
      </div>
    </>
  )
};

export default Chatting;