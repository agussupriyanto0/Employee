#/---Maksudnya menggunakan versi 21 java
FROM eclipse-temurin:21-jdk

#/---- Membuat folder di dalam /app
WORKDIR /app

#/---- Mencopy Jar
COPY target/AgusSupriyanto-Programmer-0.0.1-SNAPSHOT.jar app.jar

#/---- Port
EXPOSE 8080


#/--- Saat container start : langsung jalankan java -jar  app.jar
ENTRYPOINT ["java","-jar","app.jar"]

