<!doctype html>
<html>
  <head>
  	<title>Sales DashBoard</title>
		<link rel="stylesheet" type="text/css" href="gxt/css/gxt-all.css" />
		<link rel="stylesheet" type="text/css" href="resources.css" />
		<style>
			* {
  				margin: 0px;
  				padding: 0px;
			}

			#loading {
  				position: absolute;
  				left: 45%;
  				top: 40%;
  				margin-left: -45px;
  				padding: 2px;
  				z-index: 20001;
  				height: auto;
  				border: 1px solid #ccc;
			}

			#loading a {
  				color: #225588;
			}

			#loading .loading-indicator {
  				background: white;
  				color: #444;
  				font: bold 13px tahoma, arial, helvetica;
  				padding: 10px;
  				margin: 0;
  				height: auto;
			}

			#loading .loading-indicator img {
  				margin-right:8px;
  				float:left;
  				vertical-align:top;
			}

			#loading-msg {
  				font: normal 10px arial, tahoma, sans-serif;
			}
		</style>
  </head>

  <body style="overflow: hidden">
		<div id="loading">
    		<div class="loading-indicator">
    			<img src="gxt/images/default/shared/large-loading.gif" width="32" height="32" style="margin-right:8px;float:left;vertical-align:top;"/>Launching Application<a href="#"></a><br /><span id="loading-msg">Loading&nbsp;app...</span>
    		</div>
		</div>
	
	<!--                                       	   -->
    <!-- This script loads your compiled module.   -->
    <!-- If you add any GWT meta tags, they must   -->
    <!-- be added before this line.                -->
    <!--                                           -->
    <script type="text/javascript" language="javascript" src="webclient/webclient.nocache.js"></script>
		<iframe src="javascript:''" id="__gwt_historyFrame" style="width:0;height:0;border:0"></iframe>
		<script type="text/javascript">
			var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
			document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
		</script>
		<script type="text/javascript">
			try{
				var pageTracker = _gat._getTracker("UA-1396058-1");  
				pageTracker._initData();
				pageTracker._trackPageview();
			} catch(err) {}
		</script>
  </body>
</html>
