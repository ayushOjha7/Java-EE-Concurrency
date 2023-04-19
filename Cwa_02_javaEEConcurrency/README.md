# Build
mvn clean package && docker build -t Cwa_02_javaEEconcurrency/MyApplication .

# RUN

docker rm -f MyApplication || true && docker run -d -p 8080:8080 -p 4848:4848 --name MyApplication Cwa_02_javaEEconcurrency/MyApplication 