var scInstance;
var websocket = null;

var go = false

function orbitedInit() {
    //the port on which orbited is listening
    websocket = new TCPSocket();
    //the port on which the grapheme server listens
   	websocket.open(location.hostname, 6689);
    var startTime = new Date().getTime();
}

function orbitedSend(s) {
    websocket.send(s);
}
