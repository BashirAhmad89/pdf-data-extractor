// Event listener for the form submission that triggers the file upload function
document.getElementById("uploadForm").addEventListener("submit", async function(event) {
    // Prevents the default form submission behavior (page reload on submit)
    event.preventDefault();

    // Retrieves the file input element from the form
    const fileInput = document.getElementById("fileInput");
    // Checks if a file has been selected. If not, shows a message and exits the function
    if (fileInput.files.length === 0) {
        alert("Please select a PDF file.");
        return;
    }

    const selectedFile = fileInput.files[0];

    // Check if the selected file is a PDF
    if (selectedFile.type !== "application/pdf") {
        alert("Please upload a valid PDF file.");
        return;
    }

    // Creates a FormData object to send the file to the server
    const formData = new FormData();
    // Appends the selected file to the FormData under the name "file"
    formData.append("file", fileInput.files[0]);

    // Extract the original filename without the .pdf extension
    const originalFileName = fileInput.files[0].name.replace(/\.pdf$/i, "");

    try {
        // Sends the file to the server by making a POST request to the "/api/pdf/convert" endpoint
        const response = await fetch("/api/pdf/convert", {
            method: "POST",       // Sets the method to POST for file uploads
            body: formData        // Sets the FormData as the request body
        });

        // Checks if the server's response was successful (status 200–299)
        if (!response.ok) {
            // If the response fails, retrieves the error text from the server
            const errorText = await response.text();
            console.error("Server error:", errorText);
            // Throws an error and exits the function
            throw new Error("File upload error: " + response.status + " - " + errorText);
        }

        // Retrieves the response as a Blob object, representing the converted JSON file
        const blob = await response.blob();
        // Creates a temporary URL to download the file
        const url = URL.createObjectURL(blob);
        const downloadLink = document.createElement("a");

        // Use the original file name with a .json extension for the download
        downloadLink.href = url;
        downloadLink.download = originalFileName + ".json";     // Sets the filename to the original PDF name with a .json extension
        downloadLink.style.display = "none";                    // Hides the link in the document

        // Adds the link to the DOM, triggers the click, then removes the link immediately
        document.body.appendChild(downloadLink);
        downloadLink.click();
        document.body.removeChild(downloadLink);

        // Releases the temporary URL to save memory
        URL.revokeObjectURL(url);

    } catch (error) {
        // Logs an error in the console and shows an alert with the error message
        console.error("Error:", error);
        alert("An error occurred: " + error.message);
    }
});