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


Run MongoDB Locally:
```
either 

apt-get install mongodb
or 
 
sudo docker run -d -v /data/db:/data/db --name mymongo mongo:3.4.4
```


Run Consul Locally:
```
sudo docker run -p 8500:8500 consul
```

To start the apps:

```
./gradlew product:run test-groovy:run test-java:run xml-streamer:run --parallel 

```

Produces something like this: 
> 8083 = product application 

> 8082 = test-groovy application
 
> 8081 = test-java application

[Youtube testing groovy/java video](https://www.youtube.com/watch?v=hLsbvfz7FWY)

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

These are the identical tests so far:

```

http://localhost:8081/orders/test

http://localhost:8082/orders/test

```

XML Parsing a string that is converted to byte

http://localhost:8081/orders/xml
```
[["start document","start element: root","characters: val","start element: root","characters: v","characters: a","characters: l"]]

```


Stream testing

http://localhost:8085/stream/test



XML Parsing test: Started on this URL:
http://localhost:8081/orders/xmltest

This sends a http call to xml-streamer - which generates a large XML file accessible via http://localhost:8085/stream/test

The next phase is to parse this xml that is visible now and is using the standard parsing to show tags
Currently going to xmltest you will see the following on the screen - which is a basic attempt to parse the xml 

```

[["start document","start element: orders","characters: \n","start element: order","start element: name","characters: a0","end element: name","start element: description","characters: a0","end element: description","start element: price","characters: 12.2200000000000006394884621840901672840118408203125","end element: price","end element: order","characters:  ","start element: order","start element: name","characters: a1","end element: name","start element: description","characters: a1","end element: description","start element: price","characters: 12.2200000000000006394884621840901672840118408203125","end element: price","end element: order","characters:  ","start element: order","start element: name","characters: a2","end element: name","start element: description","characters: a2","end element: description","start element: price","characters: 12.2200000000000006394884621840901672840118408203125","end element: price","end element: order","characters:  ","start element: order","start element: 


```

