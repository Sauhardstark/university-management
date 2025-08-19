# --- build ---
FROM node:22-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
ARG VITE_API_URI=http://localhost:8080
RUN echo "VITE_API_URI=${VITE_API_URI}" > .env.production
RUN npm run build

# --- serve ---
FROM node:22-alpine 
WORKDIR /app
RUN npm i -g serve
COPY --from=build /app/dist ./dist
EXPOSE 80
CMD ["serve","-s","dist","-l","80"]
