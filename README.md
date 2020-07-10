# haat

#####Build
````
 docker build -t haat .
````
#####Generate
````
docker save --output haat.tar haat
````
#####Load
```
docker load --input haat.tar
```

#####Run
```
docker run -v /root/project/volume/:/filestorage -p 4005:4005 -d haat:latest
```

#####Run
```
docker run --name some-postgres -e POSTGRES_PASSWORD=sajib5566 -d postgres
```

