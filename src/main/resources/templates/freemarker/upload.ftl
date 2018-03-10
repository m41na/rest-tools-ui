<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<title>${title}</title>
</head>
<body>
	<h1>${message}</h1>
	
	<form action="${action}" method="post" enctype="multipart/form-data">
		<p>Select a file : <input type="file" name="file" size="45" accept=".pdf" /></p>
     	<input type="submit" value="Upload PDF" />
	</form>
</body>
</html>