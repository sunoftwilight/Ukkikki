import React, { useRef, useState, useEffect } from 'react';

const CameraComponent: React.FC = () => {
  const [selectedCamera, setSelectedCamera] = useState<string | null>(null);
  const [cameras, setCameras] = useState<string[]>([]);
  const videoRef = useRef<HTMLVideoElement>(null);

  useEffect(() => {
    const fetchCameras = async () => {
      try {
        const devices = await navigator.mediaDevices.enumerateDevices();
        const videoDevices = devices.filter(device => device.kind === 'videoinput');
        setCameras(videoDevices.map(device => device.deviceId));
      } catch (error) {
        console.error('Error fetching cameras:', error);
      }
    };
    
    fetchCameras();
  }, []);

  async function switchCamera(deviceId: string) {
    setSelectedCamera(deviceId);
    try {
      const stream = await navigator.mediaDevices.getUserMedia({
        video: {           deviceId: { exact: deviceId },
        width: { ideal: 3072 }, // 원하는 해상도 지정
        height: { ideal: 4096 } // 원하는 해상도 지정
      }});
      if (videoRef.current) {
        videoRef.current.srcObject = stream;
      }
    } catch (error) {
      console.error('Error switching camera:', error);
    }
  }

  const startCamera = async () => {
    try {
      if (cameras.length > 0) {
        // Start with the first camera found
        await switchCamera(cameras[0]);
      }
    } catch (error) {
      console.error('Error accessing camera:', error);
    }
  };

  const capturePhoto = () => {
    // Check if video stream is available
    if (videoRef.current && videoRef.current.srcObject) {
      const canvas = document.createElement('canvas');
      const video = videoRef.current;
      canvas.width = 480;
      canvas.height = 640;

      const context = canvas.getContext('2d');
      if (context) {
        context.drawImage(video, 0, 0, canvas.width, canvas.height);
        // Convert canvas to base64 or blob and save as needed
        const imageData = canvas.toDataURL('image/jpeg');
        console.log('Captured image data:', imageData);
      }
    }
  };

  return (
    <div className='min-h-screen max-h-screen min-w-full max-w-full bg-black relative'>
      <div className='min-h-14 max-h-14 min-w-full max-w-full bg-soft-blue'>
        1
      </div>
      <div className='min-w-full max-w-full'>

      </div>
      <div className='min-h-40 max-h-40 min-w-full max-w-full bg-soft-blue fixed bottom-0'>
        1
      </div>
    </div>
  );
};

export default CameraComponent;
