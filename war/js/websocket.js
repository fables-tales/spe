var scInstance;
var websocket = null;


function orbitedInit() {
    //the port on which orbited is listening
    websocket = new TCPSocket();
    //the port on which the grapheme server listens
   	websocket.open(location.hostname, 6689);
}

function orbitedSend(s) {
    websocket.send(s);
}
