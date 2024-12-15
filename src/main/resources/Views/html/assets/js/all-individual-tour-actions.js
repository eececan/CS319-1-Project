function handleIndividualTourApproval(individualTourId) {
    console.log("Approving individual tour with ID:", individualTourId);
    fetch(`/api/individual-tours/${individualTourId}/advisor/approve`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        }
    })
        .then((response) => {
            if (response.ok) {
                alert(`Individual Tour ID ${individualTourId} approved successfully!`);
                location.reload(); // Reload the page to reflect the updated status
            } else {
                return response.text().then((message) => {
                    throw new Error(message);
                });
            }
        })
        .catch((error) => {
            console.error("Error approving individual tour:", error);
            alert(`Error: ${error.message}`);
        });
}

function handleIndividualTourRejection(individualTourId) {
    console.log("Rejecting individual tour with ID:", individualTourId);
    fetch(`/api/individual-tours/${individualTourId}/advisor/reject`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        }
    })
        .then((response) => {
            if (response.ok) {
                alert(`Individual Tour ID ${individualTourId} rejected successfully!`);
                location.reload(); // Reload the page to reflect the updated status
            } else {
                return response.text().then((message) => {
                    throw new Error(message);
                });
            }
        })
        .catch((error) => {
            console.error("Error rejecting individual tour:", error);
            alert(`Error: ${error.message}`);
        });
}