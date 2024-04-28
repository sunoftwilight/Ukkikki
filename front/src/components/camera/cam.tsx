import React, { useRef, useState, useEffect } from 'react';
import timer from "@/assets/camera/timer.png";
import changeCamera from "@/assets/camera/changeCamera.png";

const CameraComponent: React.FC = () => {
  // const [selectedCamera, setSelectedCamera] = useState<string | null>(null);
  // const [cameras, setCameras] = useState<string[]>([]);
  // const videoRef = useRef<HTMLVideoElement>(null);

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

  // async function switchCamera(deviceId: string) {
  //   setSelectedCamera(deviceId);
  //   try {
  //     const stream = await navigator.mediaDevices.getUserMedia({
  //       video: {           deviceId: { exact: deviceId },
  //       width: { ideal: 3072 }, // 원하는 해상도 지정
  //       height: { ideal: 4096 } // 원하는 해상도 지정
  //     }});
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
  //       await switchCamera(cameras[0]);
  //     }
  //   } catch (error) {
  //     console.error('Error accessing camera:', error);
  //   }
  // };

  // const capturePhoto = () => {
  //   // Check if video stream is available
  //   if (videoRef.current && videoRef.current.srcObject) {
  //     const canvas = document.createElement('canvas');
  //     const video = videoRef.current;
  //     canvas.width = 480;
  //     canvas.height = 640;

  //     const context = canvas.getContext('2d');
  //     if (context) {
  //       context.drawImage(video, 0, 0, canvas.width, canvas.height);
  //       // Convert canvas to base64 or blob and save as needed
  //       const imageData = canvas.toDataURL('image/jpeg');
  //       console.log('Captured image data:', imageData);
  //     }
  //   }
  // };

  return (
    <div className='min-h-screen max-h-screen min-w-full max-w-full bg-black relative'>
      <div className='min-h-14 max-h-14 min-w-full max-w-full flex justify-evenly items-center'>
        <div className='w-6 h-6'>
          <img src={timer}/>
          <div className='w-full h-full flex justify-evenly items-center'>
            <img src={timer}/>
            {/* <p>3초</p>
            <p>5초</p>
            <p>10초</p> */}
          </div>
        </div>
        <div className='w-6 h-6 font-pre-B'>
          <p>3:4</p>
          {/* <div className='w-full h-full flex justify-evenly items-center'>
            <p>3:4</p>
            <p>9:16</p>
            <p>1:1</p>
            <p>Full</p>
          </div> */}
        </div>
        <div className='w-6 h-6 font-pre-B'>
          <p>12M</p>
          <p>50M</p>
        </div>
      </div>
      <div className='min-w-full max-w-full'>

      </div>
      <div className='min-h-52 max-h-52 min-w-full max-w-full fixed bottom-0 flex justify-evenly items-center'>
        <div className='w-12 h-12 bg-point-gray rounded-full flex justify-center items-center'>
          전환
        </div>
        <div className='w-20 h-20 bg-white rounded-full'>
          
        </div>
        <div className='w-12 h-12 bg-point-gray rounded-full flex justify-center items-center'>
          <img src={changeCamera} />
        </div>
      </div>
    </div>
  );
};

export default CameraComponent;
