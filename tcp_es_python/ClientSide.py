# An example script to connect to Google using socket
# programming in Python
import socket # for socket
import sys

try:
    # create a socket object
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    print("Socket successfully created")
except socket.error as err:
    print("socket creation failed with error %s" %(err))

# default port for socket
port = 4000

try:
    # get local machine name
    host_ip = socket.gethostname()
except socket.gaierror:

    # this means could not resolve the host
    print("there was an error resolving the host")
    sys.exit()

# connecting to the server
s.connect((host_ip, port))
to_send = ""

while to_send != "done" and to_send != "Over":

    to_send = input()

    s.send(to_send.encode('ascii'))


# Receive no more than 1024 bytes
msg = s.recv(1024)


s.close()
print (msg.decode('ascii'))
