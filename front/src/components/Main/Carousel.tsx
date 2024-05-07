import React from "react";
import { Swiper, SwiperSlide } from "swiper/react";
import { Pagination, Autoplay } from "swiper/modules";
import 'swiper/css'
import 'swiper/css/pagination'
import carousel1 from '@/assets/Main/Carousel/carousel1.png'
import carousel2 from '@/assets/Main/Carousel/carousel2.png'
import carousel3 from '@/assets/Main/Carousel/carousel3.png'

const Carousel: React.FC = () => {
  return (
    <Swiper 
      className="w-full z-0"
      spaceBetween={50}
      pagination={{clickable: true}}
      modules={[Pagination, Autoplay]}
      slidesPerView={1}
      loop={true}
      autoplay={{
        delay: 2500,
        disableOnInteraction: false,
      }}
    >
      <SwiperSlide><img src={carousel1} alt="carousel1" className="w-full" /></SwiperSlide>
      <SwiperSlide><img src={carousel2} alt="carousel2" className="w-full" /></SwiperSlide>
      <SwiperSlide><img src={carousel3} alt="carousel3" className="w-full" /></SwiperSlide>
    </Swiper>
  )
};

export default Carousel;