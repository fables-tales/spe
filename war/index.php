<!DOCTYPE html> 

<html lang="en"> 

<head> 
    
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"> 
	<meta name="keywords" content="grapheme"> 
	<meta name="description" content="Grapheme does things &tm;"> 
	<title>Grapheme - it does things(tm)</title> 
	<link rel="stylesheet" href="css/main.css" type="text/css" media="screen" />
	<link rel="shortcut icon" href="favicon.ico" />
	<script type="text/javascript" language="javascript">
         document.domain = document.domain;
    </script>
    <?php
      echo "<script src=\"http://$_SERVER[SERVER_NAME]:1337/static/Orbited.js\"></script>";
    ?>
    <script>
	    Orbited.settings.port = 1337;
        TCPSocket = Orbited.TCPSocket
    </script>
    <script type="text/javascript" language="javascript" src="js/websocket.js"></script>
	<script type="text/javascript" language="javascript" src="grapheme/grapheme.nocache.js"></script>

</head> 

<body> 

<div id="minwidth"></div>

	<div="container">

		<a href="#"><img id="logo" src="images/logo.png" alt="Grapheme"></a>

		<div id="title">
			<h2>test graph</h2>
			<h3>1 user viewing</h3>
		</div>

		<div id="nav">
		<ul>
			<li><a href="#">file</a></li>
			<li><a href="#">edit</a></li>
			<li><a href="#">algorithms</a></li>
			<li><a href="#">history</a></li>
		</ul>
		</div>

			<div id="body">
				
				<div id="toolbox">
				</div><!--end toolbar-->
				
				<div id="canvas">
            	<canvas width="800" height="800" style="position: absolute; z-index: -10;"></canvas>
            	
            	<div id="chatbar" class="fixed-position">
					<div id="chatbarframe">
						<div id="chatbarcontent">
							<!--this is the bottom bar, need a button which brings up the following div:-->
							<div id="chat">
							</div>
						</div>
					</div>
				</div>
            	
			</div><!--end body-->
		
	</div><!--end container-->

</body>
</html>
