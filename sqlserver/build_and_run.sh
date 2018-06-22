docker build -t sqlserver .
docker run -it --rm -p 1433:1433 --name sqlserver sqlserver

