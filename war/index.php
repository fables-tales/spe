<!DOCTYPE html>
<html lang="en"> 
<head>
  <title>Grapheme - online, collaborative graph editing suite</title>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
  <meta name="description" content="Grapheme is an online, collaborative graph editing suite.">
  <link rel="stylesheet" href="css/main.css" type="text/css" media="screen" />
  <link rel="shortcut icon" href="favicon.ico" />
  <script type="text/javascript" language="javascript">

    if (document.images)
    {
      preload_image_object = new Image();

      image_url = new Array();
			image_url[0] = 'images/addedged.png';
			image_url[1] = 'images/addedge.png';
			image_url[2] = 'images/addvertd.png';
			image_url[3] = 'images/addvert.png';
			image_url[4] = 'images/autolayoutd.png';
			image_url[5] = 'images/autolayout.png';
			image_url[6] = 'images/chatoffline.png';
			image_url[7] = 'images/chatonline.png';
			image_url[8] = 'images/chatwriting.png';
			image_url[9] = 'images/clusterd.png';
			image_url[10] = 'images/cluster.png';
			image_url[11] = 'images/decisiond.png';
			image_url[12] = 'images/decision.png';
			image_url[13] = 'images/dijkstrad.png';
			image_url[14] = 'images/dijkstra.png';
			image_url[15] = 'images/erased.png';
			image_url[16] = 'images/erase.png';
			image_url[17] = 'images/exportd.png';
			image_url[18] = 'images/export.png';
			image_url[19] = 'images/graphoptsd.png';
			image_url[20] = 'images/graphopts.png';
			image_url[21] = 'images/helpOff.png';
			image_url[22] = 'images/helpOn.png';
			image_url[23] = 'images/ico1.png';
			image_url[24] = 'images/ico2.png';
			image_url[25] = 'images/ico3.png';
			image_url[26] = 'images/logo.png';
			image_url[27] = 'images/moved.png';
			image_url[28] = 'images/move.png';
			image_url[29] = 'images/nostyled.png';
			image_url[30] = 'images/nostyle.png';
			image_url[31] = 'images/processd.png';
			image_url[32] = 'images/process.png';
			image_url[33] = 'images/selectd.png';
			image_url[34] = 'images/select.png';
			image_url[35] = 'images/shared.png';
			image_url[36] = 'images/share.png';
			image_url[37] = 'images/stepalld.png';
			image_url[38] = 'images/stepall.png';
			image_url[39] = 'images/stepd.png';
			image_url[40] = 'images/step.png';
			image_url[41] = 'images/tabdown.png';
			image_url[42] = 'images/tab.png';
			image_url[43] = 'images/terminatord.png';
			image_url[44] = 'images/terminator.png';
			image_url[45] = 'images/toggledird.png';
			image_url[46] = 'images/toggledir.png';
			image_url[47] = 'images/toolgrad.png';
			image_url[48] = 'images/toolsep.png';
			image_url[49] = 'images/zoomd.png';
			image_url[50] = 'images/zoom.png';

			var i = 0;
			for (i = 0; i <= 50; i++) 
				preload_image_object.src = image_url[i];
    }

  </script>
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
  <ul id="nav">
    <li><a href="#">graph list</a></li>
    <li><a href="#">logout</a></li>
  </ul>
  <div id="cont">
    <div id="toolbox"></div>
    <div id="canvas">
        <div style="position:absolute;" id="gwtGL"></div>
      <!--<canvas width="2000" height="2000" style="position: absolute; z-index: -10;"></canvas>-->
    </div>
  </div>
  <div id="footer">
  	<div id="toolInfo"></div>
  	<div id="chat"></div>
  </div>
</body>
</html>
