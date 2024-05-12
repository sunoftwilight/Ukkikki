import React, { useState, useEffect, useRef } from "react";
import ChattingRoom from "../components/Chatting/ChattingRoom";
import logo from '../../icons/512.png'
import { ChatItemType } from "../types/ChatType";

const Chatting: React.FC = () => {
  const [chat, setChat] = useState('')
  const chatInput = useRef<HTMLInputElement>(null)
  const [messages, setMessages] = useState<ChatItemType[]>([]);
  const webSocket = useRef<WebSocket | null>(null);

  useEffect(() => {
    webSocket.current = new WebSocket('wss://k10d202.p.ssafy.io/api/ws');

    webSocket.current.onopen = () => {
      console.log('WebSocket 연결!');    
    };

    webSocket.current.onclose = (err: any) => {
      console.error(err);
    }

    webSocket.current.onerror = (err: any) => {
      console.error(err);
    }

    webSocket.current.onmessage = (event: MessageEvent) => {   
      setMessages((prev) => [...prev, event.data]);
    };

    return () => {
      webSocket.current?.close();
    };
  }, []);

  const sendMessage = (msg: string) => {
    if (webSocket.current!.readyState === WebSocket.OPEN) {
      webSocket.current!.send(msg);
    }
  }
  
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