<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Orders Log</title>
		<script src="http://code.jquery.com/jquery-latest.js"></script>
		<script src="jquery.logviewer.js"></script>
		<script type="text/javascript">
			jQuery(document).bind("ready", function() {
			     jQuery('#logcontent').logViewer({logUrl: 'orders.log'});
			});
		</script>
	</head>
	<body>
		<h1>Orders Log</h1>
		Live log:<br/>
		<textarea id="logcontent" WRAP="off" style="width: 90%; height: 90%;" autocomplete="off">
		
		</textarea>
	</body>
</html>