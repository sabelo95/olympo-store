import axios from "axios";

const axiosClient = axios.create({
  baseURL: import.meta.env.VITE_API_AUTH,
  headers: {
    "Content-Type": "application/json",
  },
});

export default axiosClient;
