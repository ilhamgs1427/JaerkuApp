<?php
// Sesuaikan dengan informasi database Anda
$servername = "localhost";
$username = "id21707505_semester5";
$password = "Mujair_Asik5";
$dbname = "id21707505_jaerku";

// Buat koneksi ke database
$conn = new mysqli($servername, $username, $password, $dbname);

// Periksa koneksi
if ($conn->connect_error) {                                      
    die("Koneksi ke database gagal: " . $conn->connect_error);
}

// Ambil nilai turbidityValue, tdsValue, dan pHValue dari permintaan GET
$turbidityValue = isset($_GET['nilaiNTU']) ? $_GET['nilaiNTU'] :0.0;
$tdsValue = isset($_GET['nilaiTDS']) ? $_GET['nilaiTDS'] : 0.0;
$pHValue = isset($_GET['nilaiPH']) ? $_GET['nilaiPH'] : 0.0; 
$fuzzyValue = $_GET['nilaiFuzzy'];    



// Masukkan data ke dalam tabel sensor_value
$sql = "INSERT INTO sensor_value (TDS_Value, Turbidity_Value, pH_Value,fuzzy) VALUES ('$tdsValue', '$turbidityValue', '$pHValue','$fuzzyValue')";

if ($conn->query($sql) === TRUE) {
    echo "Data berhasil disimpan ke database";

} else {
    echo "Error: " . $sql . "<br>" . $conn->error;
}

// Tutup koneksi database
$conn->close();
?>
