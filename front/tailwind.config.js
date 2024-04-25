/** @type {import('tailwindcss').Config} */

export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        "main-blue"     : "#007BFF",
        "soft-blue"     : "#8CC4FF",
        "soft-brown"    : "#D4A174",
        "yellow"        : "#FFCE21",
        "soft-gray"     : "#F2F2F2",
        "gray"          : "#E6E6E6",
        "disabled-gray" : "#BDBDBD",
        "point-gray"    : "#5C5C5C",
        "white"         : "#FFFFFF",
        "red"           : "#E71C1C",
        "black"         : "#2E2E2E",
        "green"         : "#52DD22",
      },
      fontFamily:{
        'gtr-B' : ['gtr-B'],
        'gtr-R' : ['gtr-R'],
        'gtr-T' : ['gtr-T'],
        'pre-Black' : ['pre-Black'],
        'pre-B' : ['pre-B'],
        'pre-EB' : ['pre-EB'],
        'pre-EL' : ['pre-EL'],
        'pre-L' : ['pre-L'],
        'pre-M' : ['pre-M'],
        'pre-R' : ['pre-R'],
        'pre-SB' : ['pre-SB'],
        'pre-T' : ['pre-T'],
      }
    },
  },
  plugins: [require("tailwind-scrollbar-hide")],
}

