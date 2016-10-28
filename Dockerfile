FROM java:8
EXPOSE 8080
COPY target/*.war /data/
CMD java -jar /data/*.war

