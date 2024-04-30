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
import singleShot from "@/assets/Camera/singleShot.png";
import multiShot from "@/assets/Camera/multiShot.png";
import photo from "@/assets/Camera/photo.png";
import video from "@/assets/Camera/video.png"

const Cam: React.FC = () => {
  const [selectedTimer, setSelectedTimer] = useState<string>(timerNone);
  const [selectedScale, setSelectedScale] = useState<string>('3:4');
  const [openTimerList, setOpenTimerList] = useState<boolean>(false);
  const [openScaleList, setOpenScaleList] = useState<boolean>(false);
  const [openOptList, setOpenOptList] = useState<boolean>(true);
  const [selectedQuan, setSelectedQuan] = useState<boolean>(true);
  const [selectedPV, setSelectedPV] = useState<boolean>(true);
  const [selectedCamera, setSelectedCamera] = useState<string | null>(null);
  const [cameras, setCameras] = useState<string[]>([]);
  const [isRecording, setIsRecording] = useState<boolean>(false);

  const videoRef = useRef<HTMLVideoElement>(null);
  const canvasRef = useRef<HTMLCanvasElement>(null);
  const chunksRef = useRef<Blob[]>([]);
  const mediaRecorderRef = useRef<MediaRecorder | null>(null);

  const qualities: Record<string, {width:number, height:number}> = {
    '3:4': { width: 4000, height: 3000 },
    '9:16': { width: 4000, height: 2252 },
    '1:1': { width: 2992, height: 2992 },
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

  const startRecording = (stream: MediaStream) => {
    mediaRecorderRef.current = new MediaRecorder(stream);

    mediaRecorderRef.current.ondataavailable = (event) => {
      chunksRef.current.push(event.data);
    };

    mediaRecorderRef.current.onstop = () => {
      const videoBlob = new Blob(chunksRef.current, { type: 'video/mp4' });
      // 녹화된 비디오를 다운로드하거나 서버에 업로드하는 등의 작업을 수행할 수 있습니다.
      // 예시: window.URL.createObjectURL(videoBlob)
    };

    mediaRecorderRef.current.start();
  };

  const stopRecording = () => {
    if (mediaRecorderRef.current && mediaRecorderRef.current.state !== 'inactive') {
      mediaRecorderRef.current.stop();
    }
  };

  const capturePhoto = () => {
    // Check if video stream is available
    if (videoRef.current && videoRef.current.srcObject) {
      const canvas = document.createElement('canvas');
      const video = videoRef.current;
      canvas.width = video.videoWidth;
      canvas.height = video.videoHeight;

      const context = canvas.getContext('2d');
      if (context) {
        context.drawImage(video, 0, 0, canvas.width, canvas.height);
        // Convert canvas to base64 or blob and save as needed
        canvas.toBlob(async blob => {
          if (blob) {
            const formData = new FormData();
            formData.append('image', blob, 'd.jpg');
            console.log(formData)
          }
        })
        
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
      console.log()
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
    switchCamera(cameras[0], selectedScale);
  }, [cameras])

  useEffect(() => {
    if(selectedCamera && selectedScale) switchCamera(selectedCamera, selectedScale);
  }, [selectedCamera, selectedScale])

  // async function getMaxVideoResolution(): Promise<{ maxWidth: number; maxHeight: number }> {
  //   try {
  //     // 미디어 장치 권한을 요청하고 비디오 트랙에 접근합니다.
  //     const stream = await navigator.mediaDevices.getUserMedia({ video: true });
  //     const track = stream.getVideoTracks()[0];
  
  //     // 비디오 트랙의 기능과 제약 조건을 가져옵니다.
  //     const capabilities = track.getCapabilities();
  
  //     // 최대 너비와 높이를 가져옵니다.
  //     const maxWidth = capabilities.width?.max || 0;
  //     const maxHeight = capabilities.height?.max || 0;
  
  //     // 스트림을 해제합니다.
  //     stream.getTracks().forEach(track => track.stop());
  
  //     return { maxWidth, maxHeight };
  //   } catch (error) {
  //     console.error('Error getting max video resolution:', error);
  //     return { maxWidth: 0, maxHeight: 0 };
  //   }
  // }
  
  // // 최대 해상도를 가져와서 출력합니다.
  // getMaxVideoResolution().then(({ maxWidth, maxHeight }) => {
  //   console.log(`Max video resolution: ${maxWidth}x${maxHeight}`);
  // });

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
              <img src={selectedQuan ? singleShot : multiShot} onClick={changeQuan} />
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
          </div>
        )}
      </div>

      <div className=''>
        <video ref={videoRef} autoPlay playsInline muted style={{ width: '100%' }} />
        
      </div>

      <div className='min-h-40 max-h-40 min-w-full max-w-full fixed bottom-0 flex justify-evenly items-center bg-black/50'>
        <div className='w-12 h-12 bg-point-gray rounded-full flex justify-center items-center'>
          <img src={!selectedPV ? photo : video} className='object-cover w-6 h-6' onClick={changePV}/>
        </div>
        <div className='w-20 h-20 bg-white rounded-full flex items-center justify-center' onClick={testLogic}>
          <div className={!selectedPV ? !isRecording ? 'w-10 h-10 bg-red rounded-full' : 'w-8 h-8 bg-red rounded-sm' : ''}>
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
