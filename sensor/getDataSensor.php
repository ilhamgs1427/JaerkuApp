<?php
// Sesuaikan dengan informasi database Anda
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "jaerku";

// Buat koneksi ke database
$conn = new mysqli($servername, $username, $password, $dbname);

// Periksa koneksi
if ($conn->connect_error) {
    die("Koneksi ke database gagal: " . $conn->connect_error);
}  

// Ambil 10 data terakhir dari tabel sensor_value
$result = $conn->query("SELECT ID, TDS_Value, Turbidity_Value, pH_Value, fuzzy, DATE_FORMAT(Tanggal, '%H.%i') AS waktu FROM sensor_value ORDER BY ID DESC LIMIT 10");


$data = array();

while ($row = $result->fetch_assoc()) {
    $fuzzyValue = $row['fuzzy'];

    // Logika penentuan kondisi
    if ($fuzzyValue == 1) {
        $kondisi = "normal";
        $keterangan = "Kondisi Air Normal dan tidak ada kerusakan dari sensor";
    } elseif ($fuzzyValue == 2) {
        $kondisi = "tidak normal";
        $keterangan = "Kondisi Air Tidak Normal";
    } else {
        $kondisi = "Terdapat Error";
        $keterangan = "Terdapat Error pada nilai fuzzy";
    }

    $data[] = array(
        'id' => $row['ID'],
        'TDS_Value' => $row['TDS_Value'],
        'Turbidity_Value' => $row['Turbidity_Value'],
        'pH_Value' => $row['pH_Value'],
        'fuzzy' => $fuzzyValue,
        'kondisi' => $kondisi,
        'keterangan' => $keterangan,
        'waktu' => $row['waktu'] 

    );
}

$response = array();

if (count($data) > 0) {
    $response['error'] = false;
    $response['message'] = "success";
    $response['data'] = $data;
} else {
    $response['error'] = true;
    $response['message'] = "failed";
    $response['data'] = "No data available";
}
header('Content-Type: application/json');
echo json_encode($response);

$conn->close();
?>
