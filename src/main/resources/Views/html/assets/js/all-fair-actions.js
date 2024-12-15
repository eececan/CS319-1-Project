function handleFairApproval(fairId) {
    console.log("Approving fair with ID:", fairId);
    fetch(`/api/fairs/approve/${fairId}`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        }
    })
        .then((response) => {
            if (response.ok) {
                alert(`Fair ID ${fairId} approved successfully!`);
                location.reload();
            } else {
                return response.text().then((message) => {
                    throw new Error(message);
                });
            }
        })
        .catch((error) => {
            console.error("Error approving fair:", error);
            alert(`Error: ${error.message}`);
        });
}

function handleFairRejection(fairId) {
    console.log("Rejecting fair with ID:", fairId);
    fetch(`/api/fairs/reject/${fairId}`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        }
    })
        .then((response) => {
            if (response.ok) {
                alert(`Fair ID ${fairId} rejected successfully!`);
                location.reload();
            } else {
                return response.text().then((message) => {
                    throw new Error(message);
                });
            }
        })
        .catch((error) => {
            console.error("Error rejecting fair:", error);
            alert(`Error: ${error.message}`);
        });
}