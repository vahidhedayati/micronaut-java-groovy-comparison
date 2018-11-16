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

```
sudo apt-get install docker docker.io


sudo docker run -d --name redis  redis:latest

## with a config file :
# sudo docker run -d --name redis -v localpath/redis.conf:/etc/redis/redis.conf  redis:latest

```

To start the apps:

```
./gradlew product:run test-groovy:run test-java:run --parallel 

```

Produces something like this: 
> 8083 = product application 

> 8082 = test-groovy application
 
> 8081 = test-java application


Some URLS:
```
http://localhost:8083/product/find/a1

{"name":"a1","description":"a1","count":2000,"price":13.49}

```

```
http://localhost:8083/product/

[{"name":"c3","description":"c3","count":2000,"price":15.49},
{"name":"a1","description":"a1","count":2000,"price":13.49},
{"name":"d4","description":"d4","count":2000,"price":16.490000000000002},
{"name":"b2","description":"b2","count":2000,"price":14.49}]
```


TODO
---

These appear to be broken at this stage :
```

http://localhost:8081/orders/test

http://localhost:8082/orders/test

```
