# micronaut-java-groovy-comparison

This project attempts to test  similar code in a micronaut project built upon groovy and java.

```
mn --version

 Micronaut Version: 1.0.1
| JVM Version: 1.8.0_171
```


Two identical projects:



```
mn create-app test-java

| Generating Java project...
| Application created at /home/xxx/micro-projects/micronaut-java-groovy-comparison/test-java

mn create-app -l groovy test-groovy

| Generating Groovy project...
| Application created at /home/xxx/micro-projects/micronaut-java-groovy-comparison/test-groovy

```


Run Redis locally:

````
sudo apt-get install docker docker.io
sudo docker run -d --name redis  redis:latest

docker run -d --name redis -v localpath/redis.conf:/etc/redis/redis.conf  redis:latest
```

```
./gradlew groovy-item:run test-groovy:run test-java:run --parallel 

```