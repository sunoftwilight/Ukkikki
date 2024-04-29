import React, { useState, useRef, useEffect } from 'react';
import timerNone from "@/assets/Camera/timer.png";
import timer3s from "@/assets/Camera/timer3s.png";
import timer5s from "@/assets/Camera/timer5s.png";
import timer10s from "@/assets/Camera/timer10s.png";
import selectedTimerNone from "@/assets/Camera/selectedTimer.png";
import selectedTimer3s from "@/assets/Camera/selectedTimer3s.png";
import selectedTimer5s from "@/assets/Camera/selectedTimer5s.png";
import selectedTimer10s from "@/assets/Camera/selectedTimer10s.png";
import changeView from "@/assets/Camera/changeCamera.png";

import photo from "@/assets/Camera/photo.png";
import video from "@/assets/Camera/video.png"

const Cam: React.FC = () => {
  const [selectedTimer, setSelectedTimer] = useState<string>(timerNone);
  const [selectedScale, setSelectedScale] = useState<string>('3:4');
  const [openTimerList, setOpenTimerList] = useState<boolean>(false);
  const [openScaleList, setOpenScaleList] = useState<boolean>(false);
  const [openOptList, setOpenOptList] = useState<boolean>(true);
  const [selectedQual, setSelectedQual] = useState<string>('12M');
  const [selectedPV, setSelectedPV] = useState<boolean>(true);
  const [selectedCamera, setSelectedCamera] = useState<string | null>(null);
  const [cameras, setCameras] = useState<string[]>([]);
  const [isRecording, setIsRecording] = useState<boolean>(false);

  const videoRef = useRef<HTMLVideoElement>(null);
  const canvasRef = useRef<HTMLCanvasElement>(null);
  const mediaRecorderRef = useRef<MediaRecorder | null>(null);

  const qualities: Record<string, Record<string, {width:number, height:number}>> = {
    '12M': {
      '3:4': { width: 4000, height: 3000 },
      '9:16': { width: 4000, height: 2252 },
      '1:1': { width: 2992, height: 2992 },
      'full': { width: 4000, height: 1848 }
    },
    '50M': {
      '3:4': { width: 8160, height: 6120 },
      '9:16': { width: 8160, height: 4592 },
      '1:1': { width: 6112, height: 6112 },
      'full': { width: 8160, height: 3768 }
    }
  }

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
    if (selectedQual === '12M') setSelectedQual('50M');
    else setSelectedQual('12M');
    
  }

  const changePV = () => {
    setSelectedPV(!selectedPV)
  }

  const changeCamera = () => {
    if (selectedCamera === cameras[0]) {
      setSelectedCamera(cameras[3]);
    } else {
      setSelectedCamera(cameras[0]);
    }
  }

  const openList = (type:string) => {
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

  async function switchCamera(deviceId: string, qual: string, scale: string) {
    setSelectedCamera(deviceId);

    try {
      const width = qualities[qual][scale].width;
      const height = qualities[qual][scale].height;

      const constaints: MediaStreamConstraints = {
        video: {
          deviceId: {exact: deviceId},
          width: width,
          height: height
        }
      }
      const stream = await navigator.mediaDevices.getUserMedia(constaints);
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
    switchCamera(cameras[2], selectedQual, selectedScale);
  }, [cameras])

  useEffect(() => {
    if(selectedCamera && selectedQual && selectedScale) switchCamera(selectedCamera, selectedQual, selectedScale);
  }, [selectedCamera, selectedQual, selectedScale])



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
              <p onClick={changeQual}>{selectedQual}</p>
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

      <div className=''>
        <video ref={videoRef} autoPlay muted style={{ width: '100%' }} />
      </div>

      <div className='min-h-40 max-h-40 min-w-full max-w-full fixed bottom-0 flex justify-evenly items-center bg-black/50'>
        <div className='w-12 h-12 bg-point-gray rounded-full flex justify-center items-center'>
          <img src={!selectedPV ? photo : video} className='object-cover w-6 h-6' onClick={changePV}/>
        </div>
        <div className='w-20 h-20 bg-white rounded-full flex items-center justify-center' onClick={testLogic}>
          <div className={!selectedPV ? 'w-12 h-12 bg-red rounded-full' : ''}>
          </div>
        </div>
        <div className='w-12 h-12 bg-point-gray rounded-full flex justify-center items-center'>
          <img src={changeView} className='object-cover w-6 h-6' onClick={changeCamera}/>
        </div>
      </div>
    </div>
  );
};

export default Cam;
