import axios from "axios";

const BASE = (import.meta.env.VITE_API_URI || "http://localhost:8080").replace(/\/+$/,"");
const http = axios.create({ baseURL: BASE });

export default http;