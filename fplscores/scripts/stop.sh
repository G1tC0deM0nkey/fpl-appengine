while [ true ]
do
    if [ "$(curl -s http://localhost:80/server/shutdown?key=Cheddars)" = 'OK' ]
    then
        exit 0
    else
        echo "shutdown server"
        sleep 3s
    fi
done