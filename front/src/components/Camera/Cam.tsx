import React, { useState, useRef, useEffect } from 'react';
import { upLoadPhoto } from '../../api/camera';
// Image
import timerNone from "@/assets/Camera/timer.png";
import timer3s from "@/assets/Camera/timer3s.png";
import timer5s from "@/assets/Camera/timer5s.png";
import timer10s from "@/assets/Camera/timer10s.png";
import selectedTimerNone from "@/assets/Camera/selectedTimer.png";
import selectedTimer3s from "@/assets/Camera/selectedTimer3s.png";
import selectedTimer5s from "@/assets/Camera/selectedTimer5s.png";
import selectedTimer10s from "@/assets/Camera/selectedTimer10s.png";
import changeView from "@/assets/Camera/changeCamera.png";
import singleShot from "@/assets/Camera/singleShot.png";
import multiShot from "@/assets/Camera/multiShot.png";
import photo from "@/assets/Camera/photo.png";
// import video from "@/assets/Camera/video.png"
import { useStore } from 'zustand';
import { userStore } from '../../stores/UserStore';


const Cam: React.FC = () => {
  const [selectedTimerImg, setselectedTimerImg] = useState<string>(timerNone);
  const [selectedTimer, setSelectedTimer] = useState<number>(0);
  const [selectedScale, setSelectedScale] = useState<string>('3:4');
  const [openTimerList, setOpenTimerList] = useState<boolean>(false);
  const [openScaleList, setOpenScaleList] = useState<boolean>(false);
  const [openOptList, setOpenOptList] = useState<boolean>(true);
  const [selectedQuan, setSelectedQuan] = useState<boolean>(true);
  const [selectedPV, setSelectedPV] = useState<boolean>(true);
  const [selectedCamera, setSelectedCamera] = useState<string | null>(null);
  const [cameras, setCameras] = useState<string[]>([]);
  const user = useStore(userStore);

  const videoRef = useRef<HTMLVideoElement>(null);

  const qualities: Record<string, {width:number, height:number}> = {
    '3:4': { width: 3600, height: 2700 },
    '9:16': { width: 4000, height: 2252 },
    '1:1': { width: 2992, height: 2992 },
  }

  const changeTimer = (type : string) => {
    if (type === 'None') {
      setselectedTimerImg(timerNone);
      setSelectedTimer(0);
    }
    else if (type === '3sec') {
      setselectedTimerImg(timer3s);
      setSelectedTimer(3000);
    }
    else if (type === '5sec') {
      setselectedTimerImg(timer5s);
      setSelectedTimer(5000);
    }
    else if (type === '10sec') {
      setselectedTimerImg(timer10s);
      setSelectedTimer(10000);
    }
    
    setOpenTimerList(false);
    setOpenOptList(true);
  }

  const changeScale = (type : string) => {
    setSelectedScale(type);

    setOpenScaleList(false);
    setOpenOptList(true);
  };

  const changeQuan = () => {
    setSelectedQuan(!selectedQuan);
  };

  const changePV = () => {
    setSelectedPV(!selectedPV)
  };

  const changeCamera = () => {
    if (selectedCamera === cameras[0]) {
      setSelectedCamera(cameras[3]);
    } else {
      setSelectedCamera(cameras[0]);
    }
  };

  const openList = (type:string) => {
    if (type === 'timer') {
      setOpenOptList(false);
      setOpenTimerList(true);
    } else if (type ==='scale') {
      setOpenOptList(false);
      setOpenScaleList(true);
    }
  };

  const testLogic = () => {
    capturePhoto();
  }

  const capturePhoto = () => {
    // Check if video stream is available
    if (videoRef.current && videoRef.current.srcObject) {
      const canvas = document.createElement('canvas');
      const video = videoRef.current;
      canvas.width = video.videoWidth;
      canvas.height = video.videoHeight;

      const context = canvas.getContext('2d');
      if (context) {
        setTimeout(() => {
          context.drawImage(video, 0, 0, canvas.width, canvas.height);

          canvas.toBlob(async blob => {
            if (blob && user.uploadGroupId) {

              const formData = new FormData();
  
              const key = new Blob([JSON.stringify({key: user.groupKey[user.uploadGroupId], partyId:user.uploadGroupId})], {type: 'application/json',});
  
              const file = new File([blob], 'image.jpeg', {type: 'image/jpeg'});
              formData.append('key', key);
              formData.append('files', file);
  
              upLoadPhoto(formData,
                () => {}, 
                ()=> {}
              )
            }
          }, 'image/jpeg', 1)
        }, selectedTimer)
      }
    }
  };

  const fetchCameras = async () => {
    try {
      const devices = await navigator.mediaDevices.enumerateDevices();
      const videos = devices.filter(device => device.kind === 'videoinput');
      setCameras(videos.map(device => device.deviceId));
    }
    catch (error) {
      console.error('Error', error)
    }
  };

  async function switchCamera(deviceId: string, scale: string) {
    setSelectedCamera(deviceId);

    try {

      if (videoRef.current && videoRef.current.srcObject) {
        const oldStream = videoRef.current.srcObject as MediaStream;
        const tracks = oldStream.getTracks();
        tracks.forEach(track => track.stop());
      }

      const width = qualities[scale].width;
      const height = qualities[scale].height;

      const constaints: MediaStreamConstraints = {
        video: {
          deviceId: {exact: deviceId},
          width: {ideal : width},
          height: {ideal : height}
        },
        audio: true
      }
      const stream = await navigator.mediaDevices.getUserMedia(constaints);
      if (videoRef.current) {
        videoRef.current.srcObject = stream;
      }
    } catch (error) {
      console.error('Error switching camera:', error);
    }
  };

  useEffect(() => {
    fetchCameras();
  }, [])

  useEffect(() => {
    switchCamera(cameras[3], selectedScale);
  }, [cameras])

  useEffect(() => {
    if(selectedCamera && selectedScale) switchCamera(selectedCamera, selectedScale);
  }, [selectedCamera, selectedScale])

  return (
    <div className='min-h-screen max-h-screen min-w-full max-w-full bg-black relative'>
      <div className='min-h-14 max-h-14 min-w-full max-w-full flex items-center'>
        {openOptList && (
          <div className='w-full h-full flex justify-evenly items-center'>
            <div className='w-6 h-6'>
              <img src={selectedTimerImg} className='object-cover w-6 h-6'  onClick={() => openList('timer')}/>
            </div>
            <div className='w-6 h-6 font-pre-B text-white'>
              <p onClick={() => openList('scale')}>{selectedScale}</p>
            </div>
            <div className='w-6 h-6 font-pre-B text-white' >
              <img src={selectedQuan ? singleShot : multiShot} onClick={changeQuan} />
            </div>
          </div>
        )}
        
        {openTimerList && (
          <div className='w-full h-full flex justify-evenly items-center'>
            <img src={selectedTimerImg !== timerNone ? timerNone : selectedTimerNone} className='object-cover w-6 h-6' onClick={() => changeTimer("None") }/>
            <img src={selectedTimerImg !== timer3s ? timer3s : selectedTimer3s} className='object-cover w-6 h-6' onClick={() => changeTimer("3sec") }/>
            <img src={selectedTimerImg !== timer5s ? timer5s : selectedTimer5s} className='object-cover w-6 h-6' onClick={() => changeTimer("5sec") }/>
            <img src={selectedTimerImg !== timer10s ? timer10s : selectedTimer10s} className='object-cover w-6 h-6' onClick={() => changeTimer("10sec") }/>
          </div>
        )}

        {openScaleList && (
          <div className='w-full h-full flex justify-evenly items-center font-pre-B text-white'>
            <p className={selectedScale === '3:4' ? 'text-yellow' : ''} onClick={() => changeScale('3:4')}>3:4</p>
            <p className={selectedScale === '9:16' ? 'text-yellow' : ''} onClick={() => changeScale('9:16')}>9:16</p>
            <p className={selectedScale === '1:1' ? 'text-yellow' : ''} onClick={() => changeScale('1:1')}>1:1</p>
          </div>
        )}
      </div>

      <div className=''>
        <video ref={videoRef} autoPlay playsInline muted style={{ width: '100%' }} />
      </div>

      <div className='min-h-40 max-h-40 min-w-full max-w-full fixed bottom-0 flex justify-evenly items-center bg-black/50'>
        <div className='w-12 h-12 bg-point-gray rounded-full flex justify-center items-center'>
          <img src={!selectedPV ? photo : photo} className='object-cover w-6 h-6' onClick={changePV}/>
        </div>
        <div className='w-20 h-20 bg-white rounded-full flex items-center justify-center' onClick={testLogic}>
        </div>
        <div className='w-12 h-12 bg-point-gray rounded-full flex justify-center items-center'>
          <img src={changeView} className='object-cover w-6 h-6' onClick={changeCamera}/>
        </div>
      </div>
    </div>
  );
};

export default Cam;
