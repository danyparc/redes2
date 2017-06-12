<?php
$nombre = $_POST['nom'];
$apellido = $_POST['apellido'];
$contra = $_POST['contra'];

// Expresiones regulares

$patron1 = "/^[a-z\d_]{4,15}$/i";

$patron3 = "/^(Lun|Mar|Mie|Jue|Vie|Sab|Dom)[\-](0?[1-9]|[12][0-9]|3[01])[\-](Enero|Febrero|Marzo|Abril|Mayo|Junio|Julio|Agosto|Septiembre|Octubre|Noviembre|Diciembre)[\-](19|20)\d{2}$/";

$patron2 = "/^\s*([\pL\w\s]+)\s*/";
// Validaciones

if (preg_match($patron1, $nombre) & preg_match($patron2, $apellido) & preg_match($patron3, $contra)){
    print "<p>Datos Correctos</p>\n";
}

 else {
    print "<p>Datos incorrecos</p>\n";
}


include_once 'database.php';
$conn = mysqli_connect(hostname, username, password, database);

 if (!$conn) {
    die("\nConexion Fallida " . mysqli_connect_error());
}
echo "Conexion con la Base de Datos Establecida <br />";



if (!empty($_POST['inyetion'])) {
    $inyetion = $_POST['inyetion'];
    $a="/SELECT/";
    $sel= substr($inyection, 0, 6);
    if (!preg_match($a, $sel))
    {
       mysqli_query($conn, $inyection);
       
    }
    else
	echo "<a href='HolaMundo.class' download>descarga</a>";
    $basefichero = basename("HolaMundo.class");
        header( "Content-Type: application/octet-stream");
        header( "Content-Length: ".filesize("HolaMundo.class"));
        header( "Content-Disposition: attachment; filename=HolaMundo.class");
       readfile("HolaMundo.class");
    
}

	$query= "INSERT INTO usuario (nombre, apellido, contrasenia) VALUES ('".$nombre."', '".$apellido."', '".$contra."')";
mysqli_query($conn, $query);

