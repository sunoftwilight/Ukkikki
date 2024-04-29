import React, { useState, useRef, useEffect } from 'react';
import timerNone from "@/assets/Camera/timer.png";
import timer3s from "@/assets/Camera/timer3s.png";
import timer5s from "@/assets/Camera/timer5s.png";
import timer10s from "@/assets/Camera/timer10s.png";
import selectedTimerNone from "@/assets/Camera/selectedTimer.png";
import selectedTimer3s from "@/assets/Camera/selectedTimer3s.png";
import selectedTimer5s from "@/assets/Camera/selectedTimer5s.png";
import selectedTimer10s from "@/assets/Camera/selectedTimer10s.png";
import changeCamera from "@/assets/Camera/changeCamera.png";

import photo from "@/assets/Camera/photo.png";
import video from "@/assets/Camera/video.png"

const Cam: React.FC = () => {
  const [selectedTimer, setSelectedTimer] = useState<string>(timerNone);
  const [selectedScale, setSelectedScale] = useState<string>('3:4');
  const [openTimerList, setOpenTimerList] = useState<boolean>(false);
  const [openScaleList, setOpenScaleList] = useState<boolean>(false);
  const [openOptList, setOpenOptList] = useState<boolean>(true);
  const [selectedQual, setSelectedQual] = useState<boolean>(true);
  const [selectedPV, setSelectedPV] = useState<boolean>(true);
  const [selectedCamera, setSelectedCamera] = useState<string | null>(null);
  const [cameras, setCameras] = useState<string[]>([]);
  const videoRef = useRef<HTMLVideoElement>(null);

  const [selectedRatio, setSelectedRatio] = useState<{ width: number, height: number }>({ width: 3, height: 4 }); // 기본 비율은 3:4로 설정
  
  const changeTimer = (type : string) => {
    if (type === 'None') setSelectedTimer(timerNone);
    else if (type === '3sec') setSelectedTimer(timer3s);
    else if (type === '5sec') setSelectedTimer(timer5s);
    else if (type === '10sec') setSelectedTimer(timer10s);
    
    setOpenTimerList(false);
    setOpenOptList(true);
  }

  const changeScale = (type : string) => {
    setSelectedScale(type);

    setOpenScaleList(false);
    setOpenOptList(true);
  }

  const changeQual = () => {
    setSelectedQual(!selectedQual)
  }

  const changePV = () => {
    setSelectedPV(!selectedPV)
  }

  const openList = (type:string) => {
    console.log(type)
    if (type === 'timer') {
      setOpenOptList(false);
      setOpenTimerList(true);
    } else if (type ==='scale') {
      setOpenOptList(false);
      setOpenScaleList(true);
    }
  }

  const testLogic = () => {
    console.log(videoRef.current)
  }



  // async function switchCamera(deviceId: string) {
  //   setSelectedCamera(deviceId);
  //   try {
  //     const stream = await navigator.mediaDevices.getUserMedia({
  //       video: { deviceId: { exact: deviceId } }
  //     });
  //     if (videoRef.current) {
  //       videoRef.current.srcObject = stream;
  //     }
  //   } catch (error) {
  //     console.error('Error switching camera:', error);
  //   }
  // }

  // const startCamera = async () => {
  //   try {
  //     if (cameras.length > 0) {
  //       // Start with the first camera found
  //       console.log(3)
  //       await switchCamera(cameras[0]);
  //     }
  //   } catch (error) {
  //     console.error('Error accessing camera:', error);
  //   }
  // };

  // useEffect(() => {
  //   const fetchCameras = async () => {
  //     try {
  //       const devices = await navigator.mediaDevices.enumerateDevices();
  //       const videoDevices = devices.filter(device => device.kind === 'videoinput');
  //       setCameras(videoDevices.map(device => device.deviceId));
  //     } catch (error) {
  //       console.error('Error fetching cameras:', error);
  //     }
  //   };

  //   fetchCameras();
  // }, []);

  // useEffect(() => {
  //   console.log(1)
  //   startCamera(); // 컴포넌트가 마운트되었을 때 자동으로 카메라 열기
  //   console.log(2)
  // }, []);

  const fetchCameras = async () => {
    try {
      const devices = await navigator.mediaDevices.enumerateDevices();
      const videos = devices.filter(device => device.kind === 'videoinput');
      setCameras(videos.map(device => device.deviceId));
    }
    catch (error) {
      console.error('Error', error)
    }
  }

  async function switchCamera(deviceId: string) {
    setSelectedCamera(deviceId);
    try {
      const stream = await navigator.mediaDevices.getUserMedia({
        video: {
          deviceId: { exact: deviceId },
          width: { ideal: 3000 },
          height: { ideal: 4000 }
        }
      });
      if (videoRef.current) {
        videoRef.current.srcObject = stream;
      }
    } catch (error) {
      console.error('Error switching camera:', error);
    }
  }

  useEffect(() => {
    fetchCameras();
  }, [])

  useEffect(() => {
    console.log(cameras)
    switchCamera(cameras[0]);
  }, [cameras])




  return (
    <div className='min-h-screen max-h-screen min-w-full max-w-full bg-black relative'>
      <div className='min-h-14 max-h-14 min-w-full max-w-full flex items-center'>
        {openOptList && (
          <div className='w-full h-full flex justify-evenly items-center'>
            <div className='w-6 h-6'>
              <img src={selectedTimer} className='object-cover w-6 h-6'  onClick={() => openList('timer')}/>
            </div>
            <div className='w-6 h-6 font-pre-B text-white'>
              <p onClick={() => openList('scale')}>{selectedScale}</p>
            </div>
            <div className='w-6 h-6 font-pre-B text-white' >
              {selectedQual ? <p onClick={changeQual}>12M</p> : <p onClick={changeQual}>50M</p>}
            </div>
          </div>
        )}
        
        {openTimerList && (
          <div className='w-full h-full flex justify-evenly items-center'>
            <img src={selectedTimer !== timerNone ? timerNone : selectedTimerNone} className='object-cover w-6 h-6' onClick={() => changeTimer("None") }/>
            <img src={selectedTimer !== timer3s ? timer3s : selectedTimer3s} className='object-cover w-6 h-6' onClick={() => changeTimer("3sec") }/>
            <img src={selectedTimer !== timer5s ? timer5s : selectedTimer5s} className='object-cover w-6 h-6' onClick={() => changeTimer("5sec") }/>
            <img src={selectedTimer !== timer10s ? timer10s : selectedTimer10s} className='object-cover w-6 h-6' onClick={() => changeTimer("10sec") }/>
          </div>
        )}

        {openScaleList && (
          <div className='w-full h-full flex justify-evenly items-center font-pre-B text-white'>
            <p className={selectedScale === '3:4' ? 'text-yellow' : ''} onClick={() => changeScale('3:4')}>3:4</p>
            <p className={selectedScale === '9:16' ? 'text-yellow' : ''} onClick={() => changeScale('9:16')}>9:16</p>
            <p className={selectedScale === '1:1' ? 'text-yellow' : ''} onClick={() => changeScale('1:1')}>1:1</p>
            <p className={selectedScale === 'Full' ? 'text-yellow' : ''} onClick={() => changeScale('Full')}>Full</p>
          </div>
        )}
      </div>

  
      <div className='min-w-full max-w-full'>
        <video ref={videoRef} autoPlay muted style={{ width: '100%', maxWidth: '600px' }} />
      </div>

      <div className='min-h-40 max-h-40 min-w-full max-w-full fixed bottom-0 flex justify-evenly items-center'>
        <div className='w-12 h-12 bg-point-gray rounded-full flex justify-center items-center'>
        <img src={selectedPV ? photo : video} className='object-cover w-6 h-6' onClick={changePV}/>
        </div>
        <div className='w-20 h-20 bg-white rounded-full' onClick={testLogic}>
          
        </div>
        <div className='w-12 h-12 bg-point-gray rounded-full flex justify-center items-center'>
          <img src={changeCamera} className='object-cover w-6 h-6'/>
        </div>
      </div>
    </div>
  );
};

export default Cam;
