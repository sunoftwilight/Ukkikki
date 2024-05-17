import React, { useState, useEffect, useRef } from "react";
import ChattingRoom from "../components/Chatting/ChattingRoom";
import logo from '../../icons/512.png'
import { ChatItemType } from "../types/ChatType";
import { Client, StompHeaders } from '@stomp/stompjs';
import { useParams } from "react-router-dom";
import { getMsg } from "../api/chat";
import { userStore } from "../stores/UserStore";
import { useStore } from "zustand";

// 웹소켓 참고자료
// https://velog.io/@caecus/Project-Hobbyt-WebSocket-%EA%B3%BC-stomp-%EC%9D%B4%EC%9A%A9%ED%95%98%EC%97%AC-%EC%95%8C%EB%A6%BC-%EC%8B%A4%EC%8B%9C%EA%B0%84-%EC%B1%84%ED%8C%85-%EA%B5%AC%ED%98%84%ED%95%98%EA%B8%B0

const Chatting: React.FC = () => {
  const chatInput = useRef<HTMLInputElement>(null)
  const [chat, setChat] = useState('')
  const [messages, setMessages] = useState<ChatItemType[]>([]);

  const [client, setClient] = useState<Client | null>(null)

  const { groupPk } = useParams();
  const { groupKey } = useStore(userStore)

  useEffect(() => {
    getMsg(
      Number(groupPk),
      {
        page: 1,
        size: 30
      },
      (res) => {
        console.log(res.data)
      },
      (err) => { console.error(err) }
    )
  }, [])
  
  useEffect(() => {
    const stored = localStorage.getItem('USER_STORE');

    if (stored) {
      const obj = JSON.parse(stored);

      if (obj.state.accessToken !== '') {
        const token = obj.state.accessToken
        
        // 웹소켓 연결
        const newClient = new Client();
        newClient.configure({
          brokerURL: import.meta.env.VITE_CHAT_URL,
          onConnect: () => {
            const headers: StompHeaders = {
              authorization: 'Bearer ' + token,
            };

            newClient.subscribe(
              `/sub/chats/party/${groupPk}`,
              message => {
                const parsedMessage = JSON.parse(message.body);
                setMessages(prevChatLogs => [...prevChatLogs, parsedMessage.body.data]);
              },
              headers,
            );
          },
    
          onDisconnect: () => {
            console.log('웹소켓 연결 종료');
          },
        });
    
        // 웹소켓 세션 활성화
        newClient.activate();
        setClient(newClient);
    
        return () => {
          newClient.deactivate();
        };
      }
    }
  }, []);

  const sendMessage = (message: string) => {
    if (client !== null) {
      const newMessage = {
        content: message,
        password: groupKey[Number(groupPk)]
      };
      const jsonMessage = JSON.stringify(newMessage);
      client.publish({ destination: `/pub/message/${groupPk}`, body: jsonMessage });
    } else {
      console.error('웹소켓 연결 노활성화.');
    }
  };
  
  const messageHandler = () => {
    // 작성 길이가 1 미만이라면 input 태그로 포커싱
    if (chat.length < 1) {
      chatInput.current!.focus();
      return;
    }

    sendMessage(chat)
  }

  const enterHandler = (e: any) => {
    if (e.key === "Enter") {
      messageHandler();
    }
  }

  return (
    <>
      <div className="px-4 pt-[10px] w-full h-[calc(100%-48px)] justify-center items-center">
        <div className="fixed w-[calc(100%-32px)] h-full flex justify-center items-center">
          <img src={logo} className="w-48" />
        </div>
        <ChattingRoom list={messages} />
      </div>

      {/* <div className="w-full bg-white "> */}
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
      {/* </div> */}
    </>
  )
};

export default Chatting;