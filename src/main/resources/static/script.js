document.getElementById("uploadForm").addEventListener("submit", async function(event) {
    event.preventDefault();

    const fileInput = document.getElementById("fileInput");
    if (fileInput.files.length === 0) {
        alert("Bitte wählen Sie eine PDF-Datei aus.");
        return;
    }

    const formData = new FormData();
    formData.append("file", fileInput.files[0]);

    try {
        const response = await fetch("/api/pdf/convert", {
            method: "POST",
            body: formData
        });

        if (!response.ok) {
            const errorText = await response.text();
            console.error("Server-Fehler:", errorText);
            throw new Error("Fehler beim Hochladen der Datei: " + response.status + " - " + errorText);
        }

        const blob = await response.blob();
        const url = URL.createObjectURL(blob);
        const downloadLink = document.createElement("a");

        downloadLink.href = url;
        downloadLink.download = "converted.json";  // Setzt den Namen der Datei
        downloadLink.style.display = "none";
        document.body.appendChild(downloadLink);
        downloadLink.click();
        document.body.removeChild(downloadLink);

        URL.revokeObjectURL(url);

    } catch (error) {
        console.error("Fehler:", error);
        alert("Ein Fehler ist aufgetreten: " + error.message);
    }
});