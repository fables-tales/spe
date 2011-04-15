<!DOCTYPE html>
<html lang="en"> 
<head>
  <title>Grapheme - online, collaborative graph editing suite</title>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
  <meta name="description" content="Grapheme is an online, collaborative graph editing suite.">
  <link rel="stylesheet" href="css/main.css" type="text/css" media="screen" />
  <link rel="shortcut icon" href="favicon.ico" />
  <script type="text/javascript" language="javascript">
    document.domain = document.domain;
  </script>
  <?php echo "<script src=\"http://$_SERVER[SERVER_NAME]:1337/static/Orbited.js\"></script>\n"; ?>
  <script>
    Orbited.settings.port = 1337;
    TCPSocket = Orbited.TCPSocket
  </script>
  <script type="text/javascript" language="javascript" src="js/websocket.js"></script>
  <script type="text/javascript" language="javascript" src="grapheme/grapheme.nocache.js"></script>
</head>
<body>
  <h1>Grapheme</h1>
  <div id="graphInfo">
  </div>
  <!--<ul id="nav">
    <li><a href="#">file</a></li>
    <li><a href="#">edit</a></li>
    <li><a href="#">algorithms</a></li>
    <li><a href="#">history</a></li>
  </ul>-->
  <div id="cont">
    <div id="toolbox"></div>
    <div id="canvas">
      <canvas width="2000" height="2000" style="position: absolute; z-index: -10;"></canvas>
    </div>
  </div>
  <div id="footer">
  	<div id="toolInfo"></div>
  	<div id="chat"></div>
  </div>
</body>
</html>
