import React, { useState, useEffect, useRef } from "react";
import ChattingRoom from "../components/Chatting/ChattingRoom";
import logo from '../../icons/512.png'
import { ChatItemType } from "../types/ChatType";
import SockJS from 'sockjs-client';
import StompJs from '@stomp/stompjs';

// 웹소켓 참고자료
// https://velog.io/@caecus/Project-Hobbyt-WebSocket-%EA%B3%BC-stomp-%EC%9D%B4%EC%9A%A9%ED%95%98%EC%97%AC-%EC%95%8C%EB%A6%BC-%EC%8B%A4%EC%8B%9C%EA%B0%84-%EC%B1%84%ED%8C%85-%EA%B5%AC%ED%98%84%ED%95%98%EA%B8%B0

const Chatting: React.FC = () => {
  const [chat, setChat] = useState('')
  const chatInput = useRef<HTMLInputElement>(null)
  const [messages, setMessages] = useState<ChatItemType[]>([]);
  const webSocket = useRef<WebSocket | null>(null);

  
  useEffect(() => {
    // 웹 소켓 연결
    // const webSocket = new WebSocket("wss://k10d202.p.ssafy.io/api/ws");
    const webSocket = new WebSocket('ws://localhost:5000/api/ws');
    webSocket.onopen = function () {
      console.log("웹소켓 연결 성공");
    };

    webSocket.onclose = (error) => {
      console.log(error);
    }
    webSocket.onerror = (error) => {
      console.log(error);
    }
    webSocket.onmessage = (event: MessageEvent) => {   
      setMessages((prev) => [...prev, event.data]);
    };
  
    return () => {
      webSocket?.close();
    }
    // const client = new StompJs.Client({
    //   brokerURL: 'wss://k10d202.p.ssafy.io/api/ws',
    //   beforeConnect: () => {
    //     console.log('before Connect')
    //   },
    //   connectHeaders: {
    //     authorization: "Bearer eyJhbGciOiJIUzI1NiJ9.eyJjYXRlZ29yeSI6ImFjY2VzcyIsImlkIjoxLCJ1c2VybmFtZSI6IuyEseq3nCIsInByb3ZpZGVySWQiOiJrYWthbyAzNDU4Njg5NDM3IiwiaWF0IjoxNzE1MjM1ODk5LCJleHAiOjE3MTYwOTk4OTl9.mdm4F9ymRYeyAKJcds4sl1_j_g-5oRfSMkQZJBcNVHk"
    //   },
    //   debug(str) {
    //     console.log('debug', str)
    //   },
    //   reconnectDelay: 5000,
    //   heartbeatIncoming: 4000,
    //   heartbeatOutgoing: 4000
    // })
  
    // client.onConnect = function (frame) {
    //   console.log('connect')
    //   console.log(frame)
    // }
  
    // client.onStompError = function (frame) {
    //   console.log(`Broker reported error`, frame.headers.message);
    //     console.log(`Additional details:${frame.body}`);
    // }

    // client.activate()
  }, []);

  // const sendMessage = (msg: string) => {
    // if (webSocket.current!.readyState === WebSocket.OPEN) {
    //   webSocket.current!.send(msg);
    // }
  // }
  
  const messageHandler = () => {
    // 작성 길이가 1 미만이라면 input 태그로 포커싱
    if (chat.length < 1) {
      chatInput.current!.focus();
      return;
    }

    
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