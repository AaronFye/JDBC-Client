<!DOCTYPE html">
<html lang="en">
<head>
    <title>A4</title>
    <meta charset="utf-8" />
    <style type="text/css">
        body{background-color: grey; color:black;}
    </style>
</head>
<body>
    <%-- start scriptlet; --%>
        
    <center>
    <h1>Welcome to the Fall 2018 Project4 Enterprise System</h1>
    <h1>A Remote Database Management System</h1>
    <hr>
    <p>You are connected to the Project4 Enterprise System database.</p>
    <p>Please ener any valid SQL query or update statement.</p>
    <p>If no query/update command is provided, the Execute button will display all supplier information in the database.</p>
    <p>All execution results will appear below.</p>
    <br>
    <form action = "/Project4/A4" method = "post">
        <textarea id="statement" class="text" cols="50" rows ="15" name="statement"></textarea>
        <p>
            <input type = "submit" value = "Execute Command" />
            <input type = "reset" value = "Clear Form" />
        </p>
    </form>
    <hr>    
    <h4>Database Results:</h4>
    <p id=table>[]</p>
    </center>
</body>
    
</html>