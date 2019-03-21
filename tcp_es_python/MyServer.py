#!/usr/bin/python3           # This is server.py file
import socket
from datetime import datetime
from elasticsearch import Elasticsearch

es = Elasticsearch()

# create a socket object
serversocket = socket.socket(
	        socket.AF_INET, socket.SOCK_STREAM)

# get local machine name
host = socket.gethostname()

port = 4000

# bind to the port
serversocket.bind((host, port))

# queue up to 5 requests
serversocket.listen(5)

key = ""
while key != "Over":

    key = ""

    # establish a connection
    clientsocket,addr = serversocket.accept()

    print("Got a connection from %s" % str(addr))

    doc = {}

    while key != "done" and key != "Over":

        key = clientsocket.recv(1024).decode('ascii')
        if key != "done" and key != "Over":
            value = clientsocket.recv(1024).decode('ascii')
            if key == "query":
                res = es.search(index="games", body={"query": {"match": { "languages": value}}})
                print("Got %d Hits:" % res['hits']['total'])
                for hit in res['hits']['hits']:
                    print(hit["_source"])
            else:
                doc[key] = value

    if doc != {}:
        res = es.index(index="games", doc_type="NONE", body=doc)
        print(res['result'])

    msg = 'Thank you for connecting'+ "\r\n"
    clientsocket.send(msg.encode('ascii'))

    clientsocket.close()


print("Closing port", port)

serversocket.close()
